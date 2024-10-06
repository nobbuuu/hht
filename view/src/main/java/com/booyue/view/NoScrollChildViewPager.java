package com.booyue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.viewpager.widget.ViewPager;

/**
 *
 * @author Administrator
 * @date 2017/1/3
 *
 * 没有滚动的viewpager
 */
public class NoScrollChildViewPager extends ViewPager {
    private static final String TAG = "NoScrollChildViewPager";

    private int scaledTouchSlop;
    private int startX;
    private int startY;
    private int moveX;
    private int moveY;
    public NoScrollChildViewPager(Context context) {
        this(context,null);
    }

    public NoScrollChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getRawX();
                moveY = (int) ev.getRawY();
                //垂直方向滑动父控件拦截
                if(Math.abs(moveY - startY) > Math.abs(moveX - startX) &&
                        Math.abs(moveY - startY) > scaledTouchSlop){
                    return false;
                    //水平方向滑动不要拦截
                }else if(Math.abs(moveY - startY) < Math.abs(moveX - startX) &&
                        Math.abs(moveX - startX) > scaledTouchSlop){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
