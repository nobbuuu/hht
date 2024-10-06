package com.booyue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 *
 * @author Administrator
 * @date 2017/1/3
 *
 * 没有滚动的viewpager
 */
public class ListViewInterceptChildScroll extends ListView {

    public ListViewInterceptChildScroll(Context context) {
        super(context);
    }

    public ListViewInterceptChildScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
