package com.edgar.imagebrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private String urlString;
    private ArrayList<String> allFolderNames;
    private int curFolderPosition;
    private RecyclerView rvReader;
    private Button btnLastDir, btnNextDir;
    private int maxPosition = 0;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_last_dir:
                    if (curFolderPosition > 0) {
                        curFolderPosition--;
                        switchReader(allFolderNames.get(curFolderPosition));
                    }
                    break;

                case R.id.btn_next_dir:
                    if (curFolderPosition < maxPosition) {
                        curFolderPosition++;
                        switchReader(allFolderNames.get(curFolderPosition));
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

        initView();
        initData();
    }

    private void initView() {

        Toolbar mToolbar = findViewById(R.id.reader_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLastDir = findViewById(R.id.btn_last_dir);
        btnNextDir = findViewById(R.id.btn_next_dir);

        btnLastDir.setOnClickListener(mOnClickListener);
        btnNextDir.setOnClickListener(mOnClickListener);

    }

    private void initData() {

        urlString = getIntent().getStringExtra("URL_STRING");
        urlString = (urlString == null ? SelectPathActivity.ROOT_PATH : urlString);
        allFolderNames = getIntent().getStringArrayListExtra("ALL_FOLDER_NAMES");
        curFolderPosition = getIntent().getIntExtra("CUR_FOLDER_POSITION", 0);
        maxPosition = allFolderNames.size() - 1;
        switchReader(urlString);
    }

    private void switchReader(String requestPath) {

        ReaderFragment readerFragment = ReaderFragment.newInstance(requestPath);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.reader_container, readerFragment);
        transaction.commit();

    }



}
