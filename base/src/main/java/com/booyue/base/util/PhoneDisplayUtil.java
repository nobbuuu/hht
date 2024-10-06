package com.booyue.base.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2018/6/19.11:06
 *
 * 手机显示工具类
 */

public class PhoneDisplayUtil {
    public static float sDensity ; //手机密度 0.75 1 1.5 2 3 4
    public static int   sDensityDpi ; //每寸手机密度 120 160 240 320 480 640
    public static int   sHeightPixels ;//手机屏幕高度
    public static int   sWidthPixels;//手机屏幕宽度
    public static float sScaleDensity; //字体的密度 sp与px之间的转换关系
    public static boolean isInit = false;//是否已经初始化了

    public static void init(Context context) {
        if (!isInit) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            sDensity = displayMetrics.density;
            sDensityDpi = displayMetrics.densityDpi;
            sHeightPixels = displayMetrics.heightPixels;
            sWidthPixels = displayMetrics.widthPixels;
            sScaleDensity = displayMetrics.scaledDensity;
        }
        isInit = true;
    }

    /**
     * 获取通知栏高�?
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context){
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            int navHeight = context.getResources().getDimensionPixelSize(resourceId);
            return navHeight;
        }else {
            return 0;
        }
    }

    /**
     * 获取标题栏高
     * @param context
     * @return
     */
    public static int getTitleBarHeight(Activity context) {
        int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight(context);
    }

    /**
     * 隐藏键盘
     * @param activity activity
     */
    public static void hideInputMethod(Activity activity) {
        hideInputMethod(activity, activity.getCurrentFocus());
    }

    /**
     * 隐藏键盘
     * @param context context
     * @param view The currently focused view
     */
    public static void hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * 编辑框点击之后才弹出软键盘
     */
    public static void setSoftInputMode(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }


    /**
     * 显示输入键盘
     * @param context context
     * @param view The currently focused view, which would like to receive soft keyboard input
     */
    public static void showInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 软键盘的弹出导致整个view上移
     */
    public static void setsoftInputAdjustPan(Activity activity) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * 设置状态栏字体和图标颜色为灰色
     * @param context
     */
    public static void setStatusBar(Activity context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
//            View decorView = context.getWindow().getDecorView();
//            int option = /*View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | */View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            //根据上面设置是否对状态栏单独设置颜色
////            if (useThemestatusBarColor) {
//////                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorTheme));
////            } else {
////                context.getWindow().setStatusBarColor(Color.TRANSPARENT);
////            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
//            WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            context.getWindow().getDecorView().setSystemUiVisibility(
                   /* View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | */View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}
