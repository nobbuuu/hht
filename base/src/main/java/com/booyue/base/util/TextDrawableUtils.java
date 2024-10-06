package com.booyue.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * @author: wangxinhua
 * @date: 2019/12/20 9:23
 * @description :
 */
public class TextDrawableUtils {

    public static void setLeftDrawable(Context context,TextView tv,int drawableId){
        Drawable drawable = context.getResources().getDrawable(drawableId);
        int height = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();
        drawable.setBounds(0,0,width,height);
        tv.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
    }

    public static void setTopDrawable(Context context,TextView tv,int drawableId){
        Drawable drawable = context.getResources().getDrawable(drawableId);
        int height = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();
        drawable.setBounds(0,0,width,height);
        tv.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
    }

    public static void setRightDrawable(Context context,TextView tv,int drawableId){
        Drawable drawable = context.getResources().getDrawable(drawableId);
        int height = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();
        drawable.setBounds(0,0,width,height);
        tv.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
    }

    public static void setBottomDrawable(Context context,TextView tv,int drawableId){
        Drawable drawable = context.getResources().getDrawable(drawableId);
        int height = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();
        drawable.setBounds(0,0,width,height);
        tv.setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable);
    }
    public static void removeAllDrawable(TextView tv){
        tv.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
    }



}
