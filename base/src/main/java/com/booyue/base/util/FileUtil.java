package com.booyue.base.util;

import android.os.Environment;

import java.io.File;

/**
 * @author: wangxinhua
 * @date: 2019/3/5
 * @description :
 */
public class FileUtil {
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     *检测外部存储设备的读写权限
     * @return
     */
    public static boolean[] checkExternalStorageAvailable() {
        boolean mExternalStorageReadable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageReadable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageReadable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageReadable = mExternalStorageWriteable = false;
        }
        boolean[] rwstate = { mExternalStorageReadable, mExternalStorageWriteable };
        return rwstate;
    }

    //获取文件的后缀名
    public static String getExtension(String filePath) {
        String suffix = "";
        final File file = new File(filePath);
        final String name = file.getName();
        final int idx = name.lastIndexOf('.');
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 获得扩展名
     * @return
     */
    public static String extension(String url){
        if(url != null){
            int dot = url.lastIndexOf(".");
            int slash = url.lastIndexOf("/");
            //没有扩展名
            if(dot != -1 && slash < dot ){
                return url.substring(dot + 1);
            }
        }
        return null;
    }
}
