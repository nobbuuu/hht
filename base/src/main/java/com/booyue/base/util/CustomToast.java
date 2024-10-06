package com.booyue.base.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.booyue.base.R;
import com.booyue.base.app.ProjectInit;

/**
 *
 * @author Administrator
 * @date 2017/5/11
 * <p>
 * custom toast
 */
public class CustomToast {

    public static void showToast(String message, boolean showIcon) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(ProjectInit.getApplicationContext()).inflate(R.layout.layout_toast, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.tv_message);
        //为控件设置属性
        if (!TextUtils.isEmpty(message)) {
            mTextView.setText(message);
        }
        if (!showIcon) {
            mTextView.setCompoundDrawables(null, null, null, null);
        }

        //Toast的初始化
        Toast toastStart = new Toast(ProjectInit.getApplicationContext());
        //获取屏幕高度
        WindowManager wm = (WindowManager) ProjectInit.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void showToast(int message) {
        showToast(message,false);
    }
    public static void showToast(String message) {
        showToast(message,false);
    }


    public static void showToast(int message, boolean showIcon) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(ProjectInit.getApplicationContext()).inflate(R.layout.layout_toast, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.tv_message);
        //为控件设置属性
        if (message != 0) {
            mTextView.setText(message);
        }
        if (!showIcon) {
            mTextView.setCompoundDrawables(null, null, null, null);
        }

        //Toast的初始化
        Toast toastStart = new Toast(ProjectInit.getApplicationContext());
        //获取屏幕高度
        WindowManager wm = (WindowManager)ProjectInit.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void loadingDialog(int message,View view,TextView tvTips) {
        //为控件设置属性
        if (message != 0) {
            tvTips.setText(message);
        }
        //Toast的初始化
        Toast toastStart = new Toast(ProjectInit.getApplicationContext());
        //获取屏幕高度
        WindowManager wm = (WindowManager) ProjectInit.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(view);
        toastStart.show();
    }
}
