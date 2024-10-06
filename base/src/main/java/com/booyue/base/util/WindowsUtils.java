package com.booyue.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class WindowsUtils {

    /**
     * 标题栏隐藏
     * 在Activity.setCurrentView()之前调用此方法
     */
    public static void HideTitle(Context cxt) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 隐藏状态栏（全屏）
     * 在Activity.setCurrentView()之前调用此方法
     */
    private void HideStatusBar(Context cxt) {
        // 隐藏标题
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 定义全屏参数
            int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            // 获得窗口对象
            Window myWindow = activity.getWindow();
            // 设置 Flag 标识
            myWindow.setFlags(flag, flag);
        }
    }

    /**
     * 销毁dialog
     */
    public static void delayFinishActivity(final Activity a, int delayTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                a.finish();
            }
        }, delayTime);

    }

    /**
     * 获取屏幕尺寸
     *
     * @param ctx
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static Point screenSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    /**
     * 获取频幕的宽度
     */
    public static int getDisplayWidth(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）=720
        return width;
    }

    /**
     * 获取频幕的高度
     */
    public static int getDisplayHeight(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;  // 屏幕高度（像素）=1280
        return height;
    }

    /**
     * 获取屏幕的密度
     */
    public static float getDisplayDensity(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）=2.0
        return density;
    }

    /**
     * 获取屏幕的密度Dpi
     */
    public static int getDisplayDensityDpi(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）=320
        return densityDpi;
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
     * 修复软键盘遮住对话框的bug
     */
    public static void softInputAdjustPan(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    /**
     * 获取状态栏的go高度
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏的高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 隐藏输入法
     *
     * @param activity
     * @return
     */
    public static boolean hideInputMethod(Activity activity) {

        InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        return mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected boolean useThemestatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置

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
                    /* View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*/ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(context.getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }
    }


}
