package com.edgar.imagebrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.edgar.imagebrowser.MainMangaList.MangaItem;
import com.edgar.imagebrowser.MainMangaList.MangaListAdapter;
import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_PATH = 101;
    private static final String TAG = "=========" + MainActivity.class.getName();

    private String workPath = Environment.getExternalStorageDirectory().getPath();
    private MangaListAdapter adapter;
    private RecyclerView rvMangaList;
    private boolean firstItemLoaded = true;
    private ArrayList<String> allFolderNames;

    private MangaListAdapter.OnItemClickListener mOnItemClickListener = new MangaListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(MangaItem mangaItem, int position) {
            String urlString = mangaItem.getUrlString();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("URL_STRING", urlString);
            intent.putExtra("ALL_FOLDER_NAMES", allFolderNames);
            intent.putExtra("CUR_FOLDER_POSITION", position);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }

        initView();
        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mi_select_path:
                Intent spIntent = new Intent(MainActivity.this, SelectPathActivity.class);
                startActivityForResult(spIntent, CODE_REQUEST_PATH);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_PATH:
                if (resultCode == RESULT_OK && data != null) {
                    workPath = data.getStringExtra("WORK_PATH");
                    saveWorkPath();
                    adapter.removeAllItems();
                    new GetMangasTask().execute(workPath);
                }
                break;

            default:
                break;
        }
    }

    private void initView() {

        rvMangaList = findViewById(R.id.rv_manga_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvMangaList.setLayoutManager(layoutManager);
        adapter = new MangaListAdapter(this);
        adapter.setOnItemClickListener(mOnItemClickListener);
        adapter.setHasStableIds(true);
        rvMangaList.setAdapter(adapter);
        rvMangaList.setItemAnimator(null);

    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        workPath = sharedPreferences.getString("KEY_WORK_PATH", workPath);

        File workDir = new File(workPath);
        if (!workDir.isDirectory() || !workDir.exists()) return;

        new GetMangasTask().execute(workPath);
    }

    private void saveWorkPath() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEY_WORK_PATH", workPath);
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetMangasTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String urlString = strings[0];
            File workDir = new File(urlString);
            if (!workDir.isDirectory() || !workDir.exists()) {
                return null;
            } else {
                allFolderNames = MyUtils.sortFilesByDirectory(workDir);
                ArrayList<String> childFilenames = new ArrayList<>(Arrays.asList(workDir.list()));
                MyUtils.sortStringList(childFilenames);
                for (String childFilename : childFilenames) {
                    File childFile = new File(urlString + "/" + childFilename);
                    if (childFile.isDirectory() && !childFilename.startsWith(".")) {
                        MangaItem item = new MangaItem(childFile.getAbsolutePath(), childFilename);
                        adapter.addItem(item);
                        publishProgress(adapter.getItemCount() - 1);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            adapter.notifyItemInserted(values[0]);
            if (firstItemLoaded) {
                firstItemLoaded = false;
                rvMangaList.scrollToPosition(0);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            firstItemLoaded = true;
            Snackbar.make(rvMangaList, "Load completed!", Snackbar.LENGTH_SHORT).show();
        }
    }

}
