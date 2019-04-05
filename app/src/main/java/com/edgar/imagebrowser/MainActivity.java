package com.edgar.imagebrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
    private static final int GET_MANGA_LIST_FAILED = 102;
    private static final int GET_MANGA_LIST_SUCCESS = 103;
    private static final int GET_MANGA_LIST_FINISHED = 104;
    private static final String TAG = "=========" + MainActivity.class.getName();
    private String workPath = Environment.getExternalStorageDirectory().getPath();
    private ArrayList<MangaItem> mangaItems = new ArrayList<>();
    private MangaListAdapter adapter;
    private RecyclerView rvMangaList;

    private Handler mHandler;

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
                new GetMangaListThread(workPath).start();
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
        rvMangaList.setAdapter(adapter);

    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        workPath = sharedPreferences.getString("KEY_WORK_PATH", workPath);

        File workDir = new File(workPath);
        if (!workDir.isDirectory() || !workDir.exists()) return;

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GET_MANGA_LIST_FAILED:
                        new GetMangaListThread(workPath).start();
                        break;

                    case GET_MANGA_LIST_SUCCESS:
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        break;

                    case GET_MANGA_LIST_FINISHED:
                        Snackbar.make(rvMangaList, "Load complete!", Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        };

        new GetMangaListThread(workPath).start();
    }

    private void saveWorkPath() {
        SharedPreferences sharedPreferences = getSharedPreferences("SETTING_WORK_PATH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEY_WORK_PATH", workPath);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class GetMangaListThread extends Thread {

        private String urlString;

        public GetMangaListThread(String urlString) {
            this.urlString = urlString;
            adapter.removeAllItems();
        }

        @Override
        public void run() {
            super.run();
            Message message;
            try {
                File workDir = new File(urlString);
                if (!workDir.isDirectory() || !workDir.exists()) {
                    message = Message.obtain();
                    message.what = GET_MANGA_LIST_FAILED;
                    mHandler.sendMessage(message);

                } else {
                    ArrayList<String> childFilenames = new ArrayList<>(Arrays.asList(workDir.list()));
                    MyUtils.sortStringList(childFilenames);
                    for (String childFilename : childFilenames) {
                        File childFile = new File(urlString + "/" + childFilename);
                        if (childFile.isDirectory() && !childFilename.startsWith(".")) {
                            MangaItem item = new MangaItem(childFile.getAbsolutePath(), childFilename);
                            adapter.addItem(item);
                            message = Message.obtain();
                            message.what = GET_MANGA_LIST_SUCCESS;
                            mHandler.sendMessage(message);
                        }
                    }
                }
                message = Message.obtain();
                message.what = GET_MANGA_LIST_FINISHED;
                mHandler.sendMessage(message);

            } catch (NullPointerException npe) {
                npe.printStackTrace();
                message = Message.obtain();
                message.what = GET_MANGA_LIST_FAILED;
                mHandler.sendMessage(message);
            }
        }
    }

}
