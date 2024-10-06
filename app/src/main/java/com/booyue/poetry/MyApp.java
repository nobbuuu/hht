package com.booyue.poetry;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.booyue.base.app.ProjectInit;
import com.booyue.poetry.constant.UrlConstant;
import com.booyue.poetry.download.DownloadService;
import com.booyue.poetry.download.DownloadServiceManager;
import com.booyue.poetry.statistic.UmengStatisticManager;

import java.io.File;

/**
 * @author: wanglong
 * @date: 2022 11/05
 * @description :
 */
public class MyApp extends Application {
    public static Handler mHandler = new Handler();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //    private DownloadHelper downloadHelper;
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

//        MyApp.setCustomDensity(this, this);
        instance = this;
        File logFile = new File(getExternalCacheDir(), "log.txt");
        boolean isdebug = logFile.exists();
        LogUtils.dTag("axiba", "isdebug = " + isdebug);
        ProjectInit.getInstance()
                .init(this)
                .apiHost(UrlConstant.HOST_URL)
                .debug(true)
                .databaseName("PrimaryCourse.db")
                .config();
//        downloadHelper = new DownloadHelper(1);
        DownloadServiceManager.getInstance().initDownloadSerivce(MyApp.getInstance());
        UmengStatisticManager.init(this);
    }

    public static MyApp getInstance() {
        return instance;
    }

    /**
     * 获取下载句柄
     *
     * @return
     */
    public DownloadService.DownloadManager getDownloadHelper() {
        return DownloadServiceManager.getInstance().getDownloadManager();
    }


    private static float sNoncompatDensity;// 系统的Density
    private static float sNoncompatScaleDensity;// 系统的ScaledDensity

    public static void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            // 系统的Density
            sNoncompatDensity = appDisplayMetrics.density;
            // 系统的ScaledDensity
            sNoncompatScaleDensity = appDisplayMetrics.scaledDensity;
            // 监听在系统设置中切换字体
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        // 此处以360dp的设计图作为例子
        final float targetDensity = appDisplayMetrics.widthPixels / 720;
        final float targetScaledDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

}
