package com.booyue.base.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Administrator on 2016/10/13.
 *
 * 字体工具类，
 */
public class TypefaceUtils {

    private static Typeface typeFace;
    private static Typeface typeFaceU;

    public static final Typeface setTypeface(Context context) {

        if (typeFace == null) {
            typeFace = Typeface.DEFAULT;

        }
        return typeFace;
    }

    public static final Typeface setTypefaceB(Context context) {
//        if (typeFaceU == null) {
//            typeFaceU = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/pfbd.ttf");
//        }
        return Typeface.DEFAULT_BOLD;
    }
}
