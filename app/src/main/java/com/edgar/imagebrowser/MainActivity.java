package com.edgar.imagebrowser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.edgar.imagebrowser.DetailReader.DetailActivity;
import com.edgar.imagebrowser.MainMangaList.MangaItem;
import com.edgar.imagebrowser.MainMangaList.MangaListAdapter;
import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_PATH = 101;
    private static final int CODE_REQUEST_PERMISSION = 102;
    private static final int CODE_EXIT_PROGRAM = 103;
    private static final String TAG = "=========" + MainActivity.class.getName();

    private String workPath = Environment.getExternalStorageDirectory().getPath();
    private MangaListAdapter adapter;
    private RecyclerView rvMangaList;
    private boolean firstItemLoaded = true;
    private ArrayList<String> allFolderNames;

    private boolean hasPermission = false;
    private boolean isExit = false;

    private MangaListAdapter.OnItemClickListener mOnItemClickListener = new MangaListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(MangaItem mangaItem, int position) {
            String titleString = mangaItem.getTitleString();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("TITLE_STRING", titleString);
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

        checkPermission();
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
                if (hasPermission) {
                    Intent spIntent = new Intent(MainActivity.this, SelectPathActivity.class);
                    startActivityForResult(spIntent, CODE_REQUEST_PATH);
                } else {
                    Toast.makeText(this, "Need write storage permission",
                            Toast.LENGTH_SHORT).show();
                }
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

        new GetMangasTask().execute(workPath);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        workPath = sharedPreferences.getString("KEY_WORK_PATH", workPath);

        File workDir = new File(workPath);
        if (!workDir.isDirectory() || !workDir.exists()) return;
    }

    private void saveWorkPath() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEY_WORK_PATH", workPath);
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetMangasTask extends AsyncTask<String, Integer, Void> {

        public GetMangasTask() {
            Snackbar.make(rvMangaList, "Loading...", Snackbar.LENGTH_SHORT).show();
        }

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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Need write storage permission",
                        Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CODE_REQUEST_PERMISSION);
            }
        } else {
            hasPermission = true;
            initData();
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true;
                    initData();
                    initView();
                } else {
                    hasPermission = false;
                    Toast.makeText(this, "Permission not granted!",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                hasPermission = false;
                break;
        }
    }

    //handle double click return key to exit
    @SuppressLint("HandlerLeak")
    private Handler exitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CODE_EXIT_PROGRAM) {
                isExit = false;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Snackbar.make(rvMangaList, "One more click to exit the program",
                    Snackbar.LENGTH_SHORT).show();
            // exit after 2 seconds
            exitHandler.sendEmptyMessageDelayed(CODE_EXIT_PROGRAM, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
