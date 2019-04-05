package com.edgar.imagebrowser;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class SelectPathActivity extends AppCompatActivity {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String TAG = "==========" + SelectPathActivity.class.getName();
    private String last_path = ROOT_PATH;

    private FolderListFragment.CallbackValue mCallbackValue = new FolderListFragment.CallbackValue() {
        @Override
        public void onValueCallback(String nextPath) {
            switchFragment(nextPath);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
        }
    };

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

        switchFragment(ROOT_PATH);

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

    private void switchFragment(String requestPath) {
        if (!requestPath.equals(ROOT_PATH)) {
            File file = new File(requestPath);
            last_path = file.getParentFile().getAbsolutePath();
        } else {
            last_path = ROOT_PATH;
        }
        FolderListFragment listFragment = FolderListFragment.newInstance(requestPath);
        listFragment.setCallbackValue(mCallbackValue);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.folders_fragment, listFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switchFragment(last_path);
        }
        return false;
    }
}
