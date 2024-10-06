package com.booyue.base.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.booyue.base.R;
import com.booyue.base.util.WindowsUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: wangxinhua
 * @date: 2019/11/28 13:48
 * @description :
 */
public class BaseDialog {
    public static final String TAG = "BaseDialog";

    /**
     * 检测是否满足可以创建对话框
     *
     * @param context
     * @return true 不能创建 false 可以创建
     */
    public static boolean isInvalid(Context context) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                return true;
            }
        }
        return false;
    }



    /**
     * 显示对话框
     *
     * @param activity 对话框在依附的activity
     * @param view     对话框膨胀视图
     * @return 对话框对象
     */
    public static AlertDialog showDialog(Activity activity, View view, boolean alignBottom) {
        final AlertDialog dialog = createDialog(activity);
        if (dialog == null) {
            return null;
        }
        dialog.show();
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(false);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setContentView(view);
        if (alignBottom) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = 0;
            params.y = WindowsUtils.getDisplayHeight(activity) / 2;
            window.setAttributes(params);
        }
        return dialog;
    }
     public static AlertDialog showDialog(Activity activity, View view, boolean alignBottom,boolean CanceledOnTouchOutside) {
        final AlertDialog dialog = createDialog(activity);
        if (dialog == null) {
            return null;
        }
        dialog.show();
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(CanceledOnTouchOutside);
        dialog.setCancelable(CanceledOnTouchOutside);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setContentView(view);
        if (alignBottom) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = 0;
            params.y = WindowsUtils.getDisplayHeight(activity) / 2;
            window.setAttributes(params);
        }else {
            WindowManager.LayoutParams lp = window.getAttributes();
            DisplayMetrics d = activity.getResources().getDisplayMetrics();//获取屏幕尺寸
            lp.gravity = Gravity.CENTER;  //中央居中
            window.setAttributes(lp);
        }
        return dialog;
    }


    public static AlertDialog showLandscapeDialog(Activity activity, View view, boolean alignRight) {
        final AlertDialog dialog = createDialog(activity);
        if (dialog == null) {
            return null;
        }
        dialog.show();
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable(0));//设置window背景
        window.setWindowAnimations(R.style.dialogWindowAnim);
        //核心代码 解决了无法去除遮罩问题
        dialog.getWindow().setDimAmount(0f);
        window.setContentView(view);

        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = activity.getResources().getDisplayMetrics();//获取屏幕尺寸
        lp.gravity = Gravity.BOTTOM;  //中央居中
        lp.y = 40;
        lp.width = (int) (d.widthPixels * 0.5); //宽度为屏幕60%
        window.setAttributes(lp);
        if (alignRight) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.x = WindowsUtils.getDisplayWidth(activity) / 2;
            params.y = 0;
            window.setAttributes(params);
        }

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 5000);
        return dialog;
    }

    /**
     * 创建对话框
     *
     * @param context
     * @return
     */
    public static AlertDialog createDialog(Context context) {
        if (isInvalid(context)) {
            return null;
        }
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        return dialog;
    }

    /**
     * 取消对话框
     */
    public static void dismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            Activity activity = dialog.getOwnerActivity();
            if (!isInvalid(activity)) {
                dialog.dismiss();
            }
        }
    }


}
