package com.see.imageloaderdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Log;

import java.io.FileDescriptor;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 *
 * Created by see on 2016/8/6.
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer() {
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;//仅仅返回尺寸
        BitmapFactory.decodeResource(res, resId, options);

        //计算 inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //通过inSampleSize的设置，生成Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);


    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        //image原来的 height 和 width
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG, "origin,w=" + width + " h=" + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //计算出来insamplesize的值是2的幂，并且让高度和宽度大于reqHeight和reqWidth
            while ((halfHeight / inSampleSize) >= reqHeight && (halfHeight / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }

        }
        Log.d(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        //计算 inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //通过 inSampleSize的设置生成bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);

    }
}
