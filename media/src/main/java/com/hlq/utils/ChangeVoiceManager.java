package com.hlq.utils;

import android.content.Context;

import androidx.annotation.IntDef;
import androidx.annotation.Keep;

import org.fmod.FMOD;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wangxinhua
 * @date: 2019/5/9
 * @description : 变声对讲管理
 *
 *   1、init()
 *   2、play()
 */
public class ChangeVoiceManager {
    private ExecutorService fixedThreadPool;

    /**
     * 初始化变声器
     * @param context
     */
    public void init(Context context){
        FMOD.init(context);
        fixedThreadPool = Executors.newFixedThreadPool(1);
    }

    class PlayRunnable implements Runnable {
        private String path;
        private int type;
        private PlayRunnable(String path, int type){
            this.path = path;
            this.type = type;
        }
        @Override
        public void run() {
            ChangeUtils.change(path, type);
        }
    }
    @Keep
    @IntDef({ChangeUtils.normal,ChangeUtils.luoli,ChangeUtils.dashu,ChangeUtils.jingsong,ChangeUtils.gaoguai,ChangeUtils.kongling})
    public @interface VoiceType{

    }


    /**
     * 播放变声
     * @param path 音频路径
     * @param type 音频类型
     */
    public void play(String path,@VoiceType int type){
//        playRunnable = new PlayRunnable(path,type);
        fixedThreadPool.execute(new PlayRunnable(path,type));
    }
}
