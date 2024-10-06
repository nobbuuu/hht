package com.bigkoo.pickerview.lib;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author: wangxinhua
 * @date: 2019/9/4
 * @description :
 */
public class BooyueWheelView extends WheelView {
    public BooyueWheelView(Context context) {
        super(context);
    }

    public BooyueWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setItemVisibleCount(int itemVisibleCount){
        itemsVisible = itemVisibleCount;
    }

}
