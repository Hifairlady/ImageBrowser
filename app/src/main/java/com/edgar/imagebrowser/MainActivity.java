package com.edgar.imagebrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_PATH = 101;
    private static final String TAG = "=========" + MainActivity.class.getName();
    private String workPath = Environment.getExternalStorageDirectory().getPath();
    private ArrayList<MangaItem> mangaItems = new ArrayList<>();
    private MangaListAdapter adapter;
    private SwipeRefreshLayout srlMangaList;

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
                }
                saveWorkPath();
                break;

            default:
                break;
        }
    }

    private void initView() {

        RecyclerView rvMangaList = findViewById(R.id.rv_manga_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvMangaList.setLayoutManager(layoutManager);
        adapter = new MangaListAdapter(this, mangaItems);
        rvMangaList.setAdapter(adapter);

        srlMangaList = findViewById(R.id.srl_manga_list);
        srlMangaList.setColorSchemeColors(0xFFFEEAE6);
        srlMangaList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                srlMangaList.setRefreshing(false);
            }
        });
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        workPath = sharedPreferences.getString("KEY_WORK_PATH", workPath);

        File workDir = new File(workPath);
        if (!workDir.isDirectory() || !workDir.exists()) return;

        File[] childFiles = workDir.listFiles();
        ArrayList<String> childFilenames = MyUtils.sortFilesByDirectory(childFiles);
        if (childFilenames.isEmpty()) return;

        for (String childFilename : childFilenames) {
            File childFile = new File(childFilename);
            if (childFile.isDirectory() && !childFilename.startsWith(".")) {
                MangaItem item = new MangaItem(childFile.getAbsolutePath(), childFilename);
                mangaItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();

    }

    private void saveWorkPath() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEY_WORK_PATH", workPath);
        editor.apply();
    }
}
