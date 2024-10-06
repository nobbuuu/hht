package com.booyue.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 *
 * @author Administrator
 * @date 2018/4/4
 * 推荐页面的scrollview,主要是解决和内部recyclerview的滑动冲突
 */

public class HomeScrollView extends ScrollView{

    private int scaledTouchSlop;
    private int startX;
    private int startY;
    private int moveX;
    private int moveY;

    public HomeScrollView(Context context) {
        this(context,null);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

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
                    return true;
                    //水平方向滑动不要拦截
                }else if(Math.abs(moveY - startY) < Math.abs(moveX - startX) &&
                        Math.abs(moveX - startX) > scaledTouchSlop){
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
