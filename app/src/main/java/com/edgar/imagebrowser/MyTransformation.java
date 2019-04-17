package com.edgar.imagebrowser;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class MyTransformation extends BitmapTransformation {

    private static final String TAG = "===============" + MyTransformation.class.getName();

    private static final String ID = "com.edgar.imagebrowser.MyTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    private int targetWidth;

    public MyTransformation(int width) {
        this.targetWidth = width;
    }

    @Override
    public Bitmap transform(BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
//        Log.d(TAG, "transform: out " + outWidth + " " + outHeight);
        Log.d(TAG, "transform: bitmap " + toTransform.getWidth() + " " + toTransform.getHeight());
        if (toTransform.getWidth() == outWidth && toTransform.getHeight() == outHeight) {
            return toTransform;
        }

        if (toTransform.getWidth() == 0) {
            return toTransform;
        }

        double aspectRatio = (double) toTransform.getHeight() / (double) toTransform.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);

        if (targetHeight != 0 && targetWidth != 0) {
            Log.d(TAG, "transform: target " + targetWidth + " " + targetHeight);
            return Bitmap.createScaledBitmap(toTransform, targetWidth, targetHeight, false);
        } else {
            return toTransform;
        }
//        return toTransform;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
