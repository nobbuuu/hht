package com.booyue.poetry.download;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.OSUtil;
import com.booyue.database.download.DownloadHelper;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description : 音频播放器管理类
 */
public class DownloadServiceManager {
    private static final String TAG = "MusicManager";

    private DownloadService.DownloadManager mDownloadManager;

    private static DownloadServiceManager instance;

    public static DownloadServiceManager getInstance() {
        if (instance == null) {
            synchronized (DownloadServiceManager.class) {
                if (instance == null) {
                    instance = new DownloadServiceManager();
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



    public void initDownloadSerivce(Application application) {
        serviceIntent = new Intent(application, DownloadService.class);
        if (OSUtil.isVersionO()) {
            try {
//                application.startForegroundService(serviceIntent);
                application.startService(serviceIntent);
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
    public void addListener(DownloadHelper.DownloadCallback downloadCallback){
        if(mDownloadManager == null)return;
        mDownloadManager.register(downloadCallback);
    }
    public void removeListener(DownloadHelper.DownloadCallback downloadCallback){
        if(mDownloadManager == null)return;
        mDownloadManager.unRegister(downloadCallback);
    }

    private boolean serviceBind = false;

    public class HHTServiceConnectionImp implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, android.os.IBinder service) {
            LoggerUtils.d(TAG, "onServiceConnected: ");
            if (service instanceof DownloadService.DownloadManager) {
                serviceBind = true;
                mDownloadManager = (DownloadService.DownloadManager) service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LoggerUtils.d(TAG, "onServiceDisconnected: ");
            serviceBind = false;
        }
    }


    /**
     * 获取音频播放器控制对象
     *
     * @return
     */
    public DownloadService.DownloadManager getDownloadManager() {
        return mDownloadManager;
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
