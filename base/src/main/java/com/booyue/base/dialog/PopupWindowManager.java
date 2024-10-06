package com.booyue.base.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.booyue.base.compat.Solve7PopupWindow;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 11:25
 * @description :
 */
public class PopupWindowManager {

    @SuppressLint("ResourceAsColor")
    public static PopupWindow show(final Activity context, View popupView, View locationView,int res, final PopupWindow.OnDismissListener onDismissListener) {
//        if (popupWindow == null) {
        Solve7PopupWindow popupWindow = new Solve7PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        }
        // 设置PopupWindow的背景
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),res);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                //移除对话框一样的背景颜色
//                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                context.getWindow().setAttributes(lp);
                if(onDismissListener != null){
                    onDismissListener.onDismiss();
                }
            }
        });
        //显示像对话框一样的背景颜色
//        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        context.getWindow().setAttributes(lp);

//        popupWindow.setAnimationStyle(R.style.animTranslate);
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
//            popupWindow.showAtLocation(locationView, Gravity.NO_GRAVITY, 0, 0);
            // 以触发弹出窗的view为基准，出现在view的正下方，弹出的pop_view左上角正对view的左下角  偏移量默认为0,0
            popupWindow.showAsDropDown(locationView);
        }
        return popupWindow;
    }
}
