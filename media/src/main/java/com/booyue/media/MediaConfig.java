package com.booyue.media;

import android.app.Application;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description :
 */
public class MediaConfig {
    //服务器需要统计资源播放需要传入的参数，由主app赋值
    public static String ServerParams = "";
    //设置全局变量
    public static Application application;

    public static boolean isAllowPlayWith4G = false;
}
