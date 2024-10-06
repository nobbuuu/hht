package com.booyue.base.util;

import android.util.Log;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/6/19.10:54
 *
 * 运行时环境工具（执行命令）
 */

public class RuntimeUtil {
    /**
     * 获取权限
     *
     * @param permission 权限
     * @param path 文件路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            Log.e(TAG, "chmod", e);
        }
    }
}
