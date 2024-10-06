//package com.booyue.media.demo;
//
//import android.app.Application;
//import android.content.Context;
//import android.support.multidex.MultiDex;
//
//import com.booyue.base.app.ProjectInit;
//
///**
// * @author: wangxinhua
// * @date: 2019/11/12
// * @description :
// */
//public class ApplicationDemo extends Application {
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        ProjectInit.getInstance().init(this).debug(true).config();
//    }
//}
