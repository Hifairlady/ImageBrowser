package com.edgar.imagebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_PATH = 101;
    private String workPath = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }

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
                Toast.makeText(this, workPath, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
