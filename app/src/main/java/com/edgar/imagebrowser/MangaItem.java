package com.edgar.imagebrowser;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MangaItem {

    private static final String TAG = "========" + MangaItem.class.getName();
    private String coverName, titleString, urlString;

    public MangaItem(String url, String titleString) {
        this.titleString = titleString;
        this.urlString = url;
        Log.d(TAG, "MangaItem: " + url);
        setCoverPathFromUrl(url);
    }

    public String getTitleString() {
        return titleString;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getCoverName() {
        return coverName;
    }

    private void setCoverPathFromUrl(String url) {
        File file = new File(url);
        if (!file.exists() || !file.isDirectory()) {
            this.coverName = null;
        } else {
            ArrayList<String> filenames = MyUtils.filterImageFilenames(file);
            this.coverName = (filenames.size() == 0 ? null : filenames.get(0));
        }
    }
}