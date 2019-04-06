package com.edgar.imagebrowser.DetailReader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.edgar.imagebrowser.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "=========" + DetailActivity.class.getName();
    private String urlString;
    private String titleString;
    private ArrayList<String> allFolderNames;
    private int curFolderPosition;
    private int maxPosition = 0;
    private MenuItem menuItem;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_last_dir:
                    if (curFolderPosition > 0) {
                        curFolderPosition--;
                        switchReader(allFolderNames.get(curFolderPosition));
                        String indexString = String.valueOf(curFolderPosition + 1) + "/"
                                + String.valueOf(maxPosition + 1);
                        menuItem.setTitle(indexString);
                    }
                    break;

                case R.id.btn_next_dir:
                    if (curFolderPosition < maxPosition) {
                        curFolderPosition++;
                        switchReader(allFolderNames.get(curFolderPosition));
                        String indexString = String.valueOf(curFolderPosition + 1) + "/"
                                + String.valueOf(maxPosition + 1);
                        menuItem.setTitle(indexString);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initData();
        initView();

    }

    private void initView() {

        Toolbar mToolbar = findViewById(R.id.reader_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
//        mToolbar.setTitle(titleString);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnLastDir = findViewById(R.id.btn_last_dir);
        Button btnNextDir = findViewById(R.id.btn_next_dir);

        btnLastDir.setOnClickListener(mOnClickListener);
        btnNextDir.setOnClickListener(mOnClickListener);
        switchReader(urlString);

    }

    private void initData() {

        allFolderNames = getIntent().getStringArrayListExtra("ALL_FOLDER_NAMES");
        curFolderPosition = getIntent().getIntExtra("CUR_FOLDER_POSITION", 0);
        titleString = getIntent().getStringExtra("TITLE_STRING");
        urlString = allFolderNames.get(curFolderPosition);
        maxPosition = allFolderNames.size() - 1;
    }

    private void switchReader(String requestPath) {

        ReaderFragment readerFragment = ReaderFragment.newInstance(requestPath);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.reader_container, readerFragment);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu, menu);
        menuItem = menu.getItem(0);
        String indexString = String.valueOf(curFolderPosition + 1) + "/"
                + String.valueOf(maxPosition + 1);
        menuItem.setTitle(indexString);
        return super.onCreateOptionsMenu(menu);
    }
}
