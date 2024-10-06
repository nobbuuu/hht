package com.booyue.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.booyue.base.app.ProjectInit;

/**
 * @author: wangxinhua
 * @date: 2020/6/29 16:30
 * @description :
 */
public class SharePreUtil {
    public static SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private static SharePreUtil instance;
    public static SharePreUtil getInstance(){
        if(instance == null){
            synchronized (SharePreUtil.class){
                if(instance == null){
                    instance = new SharePreUtil(ProjectInit.getApplicationContext(),"setting");
                }
            }
        }
        return instance;
    }

    public SharePreUtil(Context context, String filename) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static final String CUSTOM_DOWNLOAD_PATH = "custom_download_path";
    //保存自定义的下载路径
    public void setCustomDownloadPath(String path){
        editor.putString(CUSTOM_DOWNLOAD_PATH,path);
        editor.commit();
    }
    //获取用户定义的下载路径
    public String getCustomDownloadPath(){
        return sp.getString(CUSTOM_DOWNLOAD_PATH,"");
    }

}
