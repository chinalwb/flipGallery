package com.chinalwb.flipgallerylib;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

public class Utils {
    public static final float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static Bitmap getBitmap(Resources res, int resId, int targetWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        float sourceWidth = options.outWidth;
        int sampleSize = 1;
        while (sourceWidth / targetWidth > 1) {
            sampleSize *= 2;
            sourceWidth /= 2;
        }

        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = targetWidth * sampleSize;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        return bitmap;
    }
}
