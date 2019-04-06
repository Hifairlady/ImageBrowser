package com.edgar.imagebrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edgar.imagebrowser.SelectPath.SelectPathActivity;

public class ReaderActivity extends AppCompatActivity {

    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

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

        urlString = getIntent().getStringExtra("URL_STRING");
        urlString = (urlString == null ? SelectPathActivity.ROOT_PATH : urlString);
    }


}
