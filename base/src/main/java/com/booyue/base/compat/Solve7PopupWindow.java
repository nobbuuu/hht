package com.booyue.base.compat;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2017/7/13.
 *
 * 解决7.0设置showAsDropDown（）无效问题
 */
public class Solve7PopupWindow extends PopupWindow{

    public Solve7PopupWindow(View mMenuView, int matchParent, int matchParent1,boolean focusable) {
        super(mMenuView, matchParent,matchParent1,focusable);
    }


    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            //获取popupwindow的高度（屏幕的高度 - 锚点view的bottom的位置）
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }


}
