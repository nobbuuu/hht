package com.booyue.base.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.booyue.base.app.ProjectInit;

import java.io.File;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2019/3/6
 * @description :
 */
public class PackageUtil {
    private static final String TAG = "PackageUtil";
    public static int getVersioncode(){
        PackageManager packageManager = ProjectInit.getApplicationContext().getPackageManager();
        String packageName = ProjectInit.getApplicationContext().getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
     public static String getVersionName(){
        PackageManager packageManager = ProjectInit.getApplicationContext().getPackageManager();
        String packageName = ProjectInit.getApplicationContext().getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    public static boolean checkAppInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 检测APP是否已安装
     *
     * @param context     上下文
     * @param packageName app包名
     * @return true 已安装 false 未安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 安装app
     *
     * @param context
     * @param file    apk路径
     */
    public static void installApp(Context context, File file) {
        LoggerUtils.d(TAG, "installApp: " + file.getAbsolutePath());
        // 代码安装
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        // intent.setDataAndType(Uri.parse("file://"+fileName),
//        // "application/vnd.android.package-archive");
//        intent.setDataAndType(Uri.fromFile(path), "application/vnd.android.package-archive");
//        context.startActivity(intent);

        if (FileUtil.getExtension(file.getPath()).equals("apk")) {
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(install);
        }
    }

    /**
     * 卸载 apk
     *
     * @param context
     * @param pkg     包名
     */
    public static void uninstallApp(Context context, String pkg) {
//        context.startActivity(new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + pkg)));
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:" + pkg));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 启动安装好的app
     *
     * @param context
     * @param packageName
     */
    public static boolean launchApp(Context context, String packageName,Uri uri) {
//        String activityName = null;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageManager pm = context.getApplicationContext().getPackageManager();
        Intent mainIntent = pm.getLaunchIntentForPackage(packageName);
        if (mainIntent != null) {
            mainIntent.setData(uri);
            context.startActivity(mainIntent);
            return true;
        }
        return false;
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        /**modify by : 2017/11/13 10:20  非常重要，否则有点应用无法启动*/
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK);
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.PERMISSION_GRANTED);
//        if(resolveInfos != null) {
//            for (ResolveInfo reInfo : resolveInfos) {
//                if (packageName.equals(reInfo.activityInfo.packageName)) {
//                    activityName = reInfo.activityInfo.name; //
//                    break;
//                }
//            }
//        }
//        if(activityName != null){
//            mainIntent.setComponent(new ComponentName(packageName, activityName));
//            LoggerUtils.d(TAG + "packageName = " + packageName + ",LauncherActivityName =  " + activityName);
//        }

    }

    /**
     * 获取apk的包名
     * @param context
     * @param filePath apk文件全路径
     * @return
     */
    public static String getPackageName(Context context, String filePath) {
        String packageName = null;
        try {
            PackageManager pm = context.getPackageManager();
            Log.e("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                packageName = info.packageName;
            }
        } catch (Exception e) {

        }
        return packageName;
    }
    /**
     * 判断app是否处于前台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
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
     * 获取当前app进程名
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
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取app的图标
     *
     * @param context
     * @return
     */
    public static int getAppIconResId(Context context) {
        int icon = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                icon = info.applicationInfo.icon;
            }
            return icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /***
     * 获取指定app的版本名
     * @param context
     * @param packageName
     */
    public static String getAppVersionName(Context context, String packageName) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                versionName = packageInfo.versionName;
            }
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /***
     * 获取指定app的版本号
     * @param context
     * @param packageName
     */
    public static int getAppVersionCode(Context context, String packageName) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                versionCode = packageInfo.versionCode;
            }
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取app的信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static LocalApplicationInfo getAppInfo(Context context, String packageName) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (packageInfo == null) {
                return null;
            }
            LocalApplicationInfo localApplicationInfo = new LocalApplicationInfo();
            localApplicationInfo.packageName = packageInfo.packageName;
            localApplicationInfo.versionCode = packageInfo.versionCode;
            localApplicationInfo.versionName = packageInfo.versionName;
            localApplicationInfo.appName = (String) packageInfo.applicationInfo.loadLabel(pm);
            localApplicationInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
            return localApplicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
//            if((packageInfo.applicationInfo.flags&LocalApplicationInfo.FLAG_SYSTEM)==0){
//                appList.add(tmpInfo);//如果非系统应用，则添加至appList
//            }
        }
        return null;
    }

    /**
     * 启动web浏览器
     * @param context
     * @param url
     */
    public static void launchWebBrowser(Context context,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * appinfo实体类
     */
    public static class LocalApplicationInfo {
        public String appName;
        public String packageName;
        public int versionCode;
        public String versionName;
        public Drawable appIcon;
    }









}
