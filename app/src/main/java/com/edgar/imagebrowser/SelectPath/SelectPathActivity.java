package com.edgar.imagebrowser.SelectPath;

import android.content.Intent;
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

import com.edgar.imagebrowser.R;

import java.io.File;
import java.util.HashMap;

public class SelectPathActivity extends AppCompatActivity {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String TAG = "==========" + SelectPathActivity.class.getName();
    private String last_path = ROOT_PATH;
    private String finalPath = ROOT_PATH;
    private HashMap<String, Integer> positionMap = new HashMap<>();

    private FolderListFragment.CallbackValue mCallbackValue = new FolderListFragment.CallbackValue() {
        @Override
        public void onValueCallback(String nextPath, String curPath, int position) {
            switchFragment(nextPath);
            positionMap.put(curPath, position);
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
                setResult(RESULT_CANCELED);
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
                Intent intent = new Intent();
                intent.putExtra("WORK_PATH", finalPath);
                setResult(RESULT_OK, intent);
                finish();
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
            last_path = "";
        }
        finalPath = requestPath;
        int firstPosition = positionMap.getOrDefault(requestPath, 0);
        FolderListFragment listFragment = FolderListFragment.newInstance(requestPath, firstPosition);
        listFragment.setCallbackValue(mCallbackValue);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.folders_fragment, listFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !last_path.equals("")) {
            switchFragment(last_path);
        }
        return false;
    }
}
