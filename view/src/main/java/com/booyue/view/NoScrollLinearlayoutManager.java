package com.booyue.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author: wangxinhua
 * @date: 2019/9/4
 * @description :
 */
public class NoScrollLinearlayoutManager extends LinearLayoutManager {
    public NoScrollLinearlayoutManager(Context context) {
        super(context);
    }

    public NoScrollLinearlayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollLinearlayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean scrollEnable = true;

    public void setScrollEnabled(boolean scrollEnable){
        this.scrollEnable = scrollEnable;
    }


    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally() && scrollEnable;
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }
}
