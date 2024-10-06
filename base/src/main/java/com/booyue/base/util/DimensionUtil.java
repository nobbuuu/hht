package com.booyue.base.util;

import android.content.Context;

/**
 * Created by Administrator on 2018/6/19.11:04
 */

public class DimensionUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param pxValue

     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        if(!PhoneDisplayUtil.isInit){
            PhoneDisplayUtil.init(context);
        }
        return (int) (pxValue / PhoneDisplayUtil.sDensity + 0.5f);
    }
    /**
     * 将dip值转换为px值，保证尺寸大小不变
     * @param dipValue

     * @return
     */
    public static int dip2Px(Context context, float dipValue){
        if(!PhoneDisplayUtil.isInit){
            PhoneDisplayUtil.init(context);
        }
        return (int) (dipValue * PhoneDisplayUtil.sDensity + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属�?�scaledDensity�?
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        if(!PhoneDisplayUtil.isInit){
            PhoneDisplayUtil.init(context);
        }
        return (int) (pxValue / PhoneDisplayUtil.sScaleDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属�?�scaledDensity�?
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        if(!PhoneDisplayUtil.isInit){
            PhoneDisplayUtil.init(context);
        }
        return (int) (spValue * PhoneDisplayUtil.sScaleDensity + 0.5f);
    }

}
