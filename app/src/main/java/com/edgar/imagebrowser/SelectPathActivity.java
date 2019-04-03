package com.edgar.imagebrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SelectPathActivity extends AppCompatActivity {

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
        }
    };

    private RecyclerView rvFolderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_path);

        Toolbar mToolbar = findViewById(R.id.sp_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvFolderList = findViewById(R.id.rv_folder_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFolderList.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_path_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_confirm_select:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
