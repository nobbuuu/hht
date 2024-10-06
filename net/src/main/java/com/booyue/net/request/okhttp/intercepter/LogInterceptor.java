package com.booyue.net.request.okhttp.intercepter;


import com.booyue.base.util.LoggerUtils;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 * @author Administrator
 * @date 2018/7/6
 *
 * 日志拦截器
 */

public class LogInterceptor implements HttpLoggingInterceptor.Logger{
    private static final String TAG = "LogInterceptor";

    public static HttpLoggingInterceptor httpLoggingInterceptor(){
        /**
         *  日志显示级别
         *  {@link HttpLoggingInterceptor.Level.NONE} no logs
         *  {@link HttpLoggingInterceptor.Level.BASIC} logs request and response line
         *  {@link HttpLoggingInterceptor.Level.HEADERS} logs request and response line and their respective headers
         *  {@link HttpLoggingInterceptor.Level.BODY} logs request and response line and their respective headers and bodies (if represent)
         */
        HttpLoggingInterceptor.Level logLevel= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new LogInterceptor());
        loggingInterceptor.setLevel(logLevel);
        return loggingInterceptor;
    }

    @Override
    public void log(String message) {
        LoggerUtils.d(TAG,"Retrofit==:"+message);
    }
}
