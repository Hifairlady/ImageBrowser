package com.edgar.imagebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

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
                startActivity(spIntent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
