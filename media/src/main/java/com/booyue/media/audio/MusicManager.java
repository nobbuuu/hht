package com.booyue.media.audio;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.OSUtil;
import com.booyue.media.audio.listener.IHHTMusicPlayerCallback;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description : 音频播放器管理类
 */
public class MusicManager {
    private static final String TAG = "MusicManager";

    private MusicService.MusicPlayer mMusicPlayer;

    private static MusicManager instance;

    public static MusicManager getInstance() {
        if (instance == null) {
            synchronized (MusicManager.class) {
                if (instance == null) {
                    instance = new MusicManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化音乐播放器服务
     *
     * @param application
     * @param musicPlayerCallback
     */
    private Intent serviceIntent;
    private HHTServiceConnectionImp hhtServiceConnectionImp;
    private HHTServiceConnection serviceConnection;
    private IHHTMusicPlayerCallback musicPlayerCallback;

    public void initMusicSerivce(Application application, HHTServiceConnection serviceConnection,
                                 IHHTMusicPlayerCallback musicPlayerCallback) {
        serviceIntent = new Intent(application, MusicService.class);
        this.serviceConnection = serviceConnection;
        this.musicPlayerCallback = musicPlayerCallback;
        if (OSUtil.isVersionO()) {
            try {
                application.startForegroundService(serviceIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //oppo手机你们可以尝试自己在启动服务时捕获异常，不上报，因为OPPO手机内对耗电管理的限制对他们的正常使用没有影响。
            try {
                application.startService(serviceIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hhtServiceConnectionImp = new HHTServiceConnectionImp();
        application.bindService(serviceIntent, hhtServiceConnectionImp, Context.BIND_AUTO_CREATE);
    }

    private boolean serviceBind = false;

    public class HHTServiceConnectionImp implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, android.os.IBinder service) {
            LoggerUtils.d(TAG, "onServiceConnected: ");
            if (service instanceof MusicService.MusicPlayer) {
                serviceBind = true;
                mMusicPlayer = (MusicService.MusicPlayer) service;
                if (serviceConnection != null) {
                    serviceConnection.onServiceConnected(mMusicPlayer);
                }
                mMusicPlayer.addListener(musicPlayerCallback);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LoggerUtils.d(TAG, "onServiceDisconnected: ");
            serviceBind = false;
            if (serviceConnection != null) {
                serviceConnection.onServiceDisconnected();
            }
        }
    }

    /**
     * 火火兔服务连接状态
     */
    public interface HHTServiceConnection {
        void onServiceConnected(MusicService.MusicPlayer mMusicPlayer);

        void onServiceDisconnected();
    }


    /**
     * 获取音频播放器控制对象
     *
     * @return
     */
    public MusicService.MusicPlayer getMusicPlayer() {
        return mMusicPlayer;
    }

    public void stopService(Application application) {
        LoggerUtils.d(TAG, "stopService: " + serviceIntent);
        if (serviceIntent != null) {
            LoggerUtils.d(TAG, "serviceBind: " + serviceBind);
            if (serviceBind) {
                application.unbindService(hhtServiceConnectionImp);
            }
            boolean success = application.stopService(serviceIntent);
            LoggerUtils.d(TAG, "success:" + success);
        }
    }

}
