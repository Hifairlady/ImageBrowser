package com.edgar.imagebrowser;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyUtils {

    public static void sortStringList(ArrayList<String> stringArrayList) {
        Collections.sort(stringArrayList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
    }

    public static ArrayList<String> sortFilesByDirectory(File[] files) {
        ArrayList<String> result = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory() && file.exists() && file.list().length != 0
                    && !file.getName().startsWith(".")) {
                result.add(file.getAbsolutePath());
            }
        }
        sortStringList(result);
        return result;
    }

    public static ArrayList<String> filterImageFilenames(File file) {
        ArrayList<String> result = new ArrayList<>();
        String[] filenames = file.list();
        for (String filename : filenames) {
            File tmp = new File(filename);
            if ((filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")
                    || filename.endsWith(".bmp") || filename.endsWith(".JPG")
                    || filename.endsWith(".JPEG") || filename.endsWith(".PNG")
                    || filename.endsWith(".BMP")) && !filename.startsWith(".") && !tmp.isDirectory()) {
                result.add(filename);
            }
        }
        sortStringList(result);
        return result;
    }

    public static float px2dp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
