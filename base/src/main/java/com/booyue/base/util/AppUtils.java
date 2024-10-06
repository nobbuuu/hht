package com.booyue.base.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import com.booyue.base.app.ProjectInit;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 * <p>
 * app工具类
 */
public class AppUtils {
    private static final String TAG = "AppUtils";

    /**
     * 获取当前app的包名
     *
     * @param context
     * @return
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
            {
                return info.processName;//返回包名
            }
        }
        return "";
    }
    public static void killProcess(){
        int pid = android.os.Process.myPid();
        Process.killProcess(pid);
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }
     /**
     * 获取版本名
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 获取系统版本
     */
    public static int getSystemVersion() {
        int version = Build.VERSION.SDK_INT;
        return version;
    }

    public static boolean isTopActivityForPlayActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.

                getSystemService(Context.ACTIVITY_SERVICE);

//        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT) {

            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(5);

            if (runningTasks != null && runningTasks.size() > 0) {

                ComponentName topActivity = runningTasks.get(0).topActivity;

                LoggerUtils.d("isTopActivityForPlayActivity<5.0", topActivity.getPackageName());

                LoggerUtils.d("isTopActivityForPlayActivity", topActivity.getClassName());


                if (topActivity.getPackageName().equals(context.getPackageName())

                        && topActivity.getClassName().equals(context.getPackageName()+".Play3Activity")) {

                    return true;

                }
            }
            return false;
//        } else {
//            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
//
//                    activityManager.getRunningAppProcesses();
//
//            if (runningAppProcessInfoList.size() == 0) return false;
//
//
//            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList) {
//
//
//                LoggerUtils.d("isTopActivityForPlayActivity>5.0", Integer.toString(runningAppProcessInfo.importance));
//
//                LoggerUtils.d("isTopActivityForPlayActivity", runningAppProcessInfo.processName);
//
//                if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.
//                        IMPORTANCE_FOREGROUND &&
//
//                        runningAppProcessInfo.processName.equals(context.getPackageName())
//                        ) {
//                    activityManager.
//
//
//                    return true;
//                }
//            }
//            return false;
//
//
//        }

    }

    private static final String LANUCHAER_PACKAGE = "com.sec.android.app.launcher";
    public static boolean isTopActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

//        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT) {

        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(5);

        if (runningTasks != null && runningTasks.size() > 0) {

            ComponentName topActivity = runningTasks.get(0).topActivity;

            LoggerUtils.d("isTopActivity<5.0", topActivity.getPackageName());

            LoggerUtils.d("isTopActivity", topActivity.getClassName());

            if(topActivity.getPackageName().equals(LANUCHAER_PACKAGE)){
                return true;
            }
        }
        return false;

//        } else {
//            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
//
//                    activityManager.getRunningAppProcesses();
//
//            if (runningAppProcessInfoList.size() == 0) return false;
//
//
//            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList) {
//
//
//                LoggerUtils.d("isTopActivityForPlayActivity>5.0", Integer.toString(runningAppProcessInfo.importance));
//
//                LoggerUtils.d("isTopActivityForPlayActivity", runningAppProcessInfo.processName);
//
//                if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.
//                        IMPORTANCE_FOREGROUND &&
//
//                        runningAppProcessInfo.processName.equals(context.getPackageName())
//                        ) {
//                    activityManager.
//
//
//                    return true;
//                }
//            }
//            return false;
//
//
//        }

    }


    /**
     * 程序是否在前台运行
     * @return
     */
    public static boolean isAppOnBackground = false;

    public static boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) ProjectInit.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ProjectInit.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前app是否处于后台
     * @return true处于后台，false 处于前台
     */
    public static boolean isAppOnBackground(){
        ActivityManager am = (ActivityManager)  ProjectInit.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        /**
         * @param 1 返回列表条目的最大数量
         */
        String packageName = ProjectInit.getApplicationContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            LoggerUtils.e(TAG, "top activity:" +
                    topActivity.getPackageName() + " context:" +
                    ProjectInit.getApplicationContext().getPackageName());
            if (!topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


}
