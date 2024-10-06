package com.booyue.base.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2018/6/19.10:57
 */

public class MemoryUtil {
    /**
     * 是否安装了sdcard
     * @return true表示有，false表示没有
     */
    public static boolean haveSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统内部可用空间大小
     *
     * @return available size
     */
    public static long getSystemAvailableSize() {
        File rootDirectory = Environment.getRootDirectory();
        StatFs statFs = new StatFs(rootDirectory.getAbsolutePath());
        long availableBlock = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return availableBlock * blockSize;
    }


    /**
     * 获取sd卡可用空间大�?
     *
     * @return available size
     */
    public static long getSDCardAvailableSize() {
        long available = 0;
        if (haveSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();
            available = availaBlock * blocSize;
        } else {
            available = -1;
        }
        return available;
    }
}
