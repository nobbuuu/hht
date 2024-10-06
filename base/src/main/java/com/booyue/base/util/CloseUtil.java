package com.booyue.base.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2018/7/6.10:48
 * 关闭流通道工具类
 */

public class CloseUtil {
    //关闭流通道
    public static void close(Closeable... closeables){
        if(closeables == null) {
            return;
        }
        for (int i = 0; i < closeables.length; i++) {
            if(closeables[i] != null){
                try {
                    closeables[i].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(closeables[i] != null){
                        closeables[i] = null;
                    }
                }
            }
        }
    }
}
