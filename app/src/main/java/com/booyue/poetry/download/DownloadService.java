package com.booyue.poetry.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.booyue.base.util.LoggerUtils;
import com.booyue.database.download.DownloadHelper;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.poetry.listener.IDownloadManager;

/**
 * @author: wangxinhua
 * @date: 2020/7/18 11:15
 * @description :
 */
public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private DownloadHelper downloadHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadManager();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

//        onStartForeground();
        downloadHelper = new DownloadHelper(1);
        LoggerUtils.d(TAG, "onCreate: " + downloadHelper);
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        onStartForeground();
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void  onStartForeground(){
//        String CHANNEL_ID = "booyue_primaryCourse_001";
//        String CHANNEL_NAME = "booyue_primaryCourse";
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(channel);
//        Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
//        startForeground(1, notification);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class DownloadManager extends Binder implements IDownloadManager {
        @Override
        public void register(DownloadHelper.DownloadCallback subscriber) {
            downloadHelper.register(subscriber);
        }

        @Override
        public void unRegister(DownloadHelper.DownloadCallback subscriber) {
            downloadHelper.unRegister(subscriber);
        }

        @Override
        public Boolean insertAndStart(DownloadBean bean) {
            return downloadHelper.insertAndStart(bean);
        }

        @Override
        public Boolean isTaskManaged(long id) {
            return downloadHelper.isTaskManaged(id);
        }

        @Override
        public Boolean IsTaskRunning(long id) {
            return downloadHelper.IsTaskRunning(id);
        }

        @Override
        public void Pause(long id) {
            downloadHelper.Pause(id);
        }

        @Override
        public void Resume(long id) {
            downloadHelper.Resume(id);
        }

        @Override
        public int getTaskTotal() {
            return downloadHelper.getTaskTotal();
        }

        @Override
        public DownloadBean getTaskByPosition(int position) {
            return downloadHelper.getTaskByPosition(position);
        }

        @Override
        public DownloadBean getTaskByid(long id) {
            return downloadHelper.getTaskByid(id);
        }

        @Override
        public void removeTaskByid(long id) {
            downloadHelper.removeTaskByid(id);
        }

        @Override
        public void removeCompleteTask(long id) {
            downloadHelper.removeCompleteTask(id);
        }

        @Override
        public int getTaskPosition(int id) {
            return downloadHelper.getTaskPosition(id);
        }
    }
}
