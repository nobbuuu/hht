package com.booyue.media.audio;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

/**
 * @author: wangxinhua
 * @date: 2019/10/23
 * @description : 蓝牙远程控制
 */
public class RemoteControlClient {
    private static final String TAG = "RemoteControlClient";

    /**
     * 注册媒体按钮监听
     */
    private static AudioManager audioManager;
    private static ComponentName cn;
    private static MediaSessionCompat mediaSessionCompat;

    public static void registerMediaButtonReceiver(Context context) {
        Log.d(TAG, "registerMediaButtonReceiver: Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT);
        unregisterMediaButtonReceiver();
        //获取音频服务
//        如果 API 21+ 用以上广播注册的方式也是可以的监听的，区别在于如果有别的播放器抢占了焦点你可就抢不回来了
//        if (Build.VERSION.SDK_INT <  Build.VERSION_CODES.LOLLIPOP) {
//        //注册接收的Receiver
//         if (mediaSessionCompat == null) {
//             ComponentName cn = new ComponentName(MyApp.getContext().getPackageName(), RemoteControlClientReceiver.class.getName());
//             Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
//             intent.setClass(getContext(), RemoteControlClientReceiver.class);
//             PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 19088760, intent, 1);
//             mediaSessionCompat = new MediaSessionCompat(MyApp.getContext(), "BY_MediaPlayer", cn, pendingIntent);
//             mediaSessionCompat.setFlags(3);
//             mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
//                 @Override
//                 public void onPlay() {
//                     super.onPlay();
//                     //这里处理播放器逻辑 播放
//                     notifyMediaButtonChange(getContext(), SocketActionConstant.PlayAction.INTENT_ACTION_MEDIA_BUTTON_PLAY);
//                     LoggerUtils.d(TAG, "onPlay: ");
//                 }
//
//                 @Override
//                 public void onPause() {
//                     super.onPause();
//                     //这里处理播放器逻辑 暂停
//                     notifyMediaButtonChange(getContext(), SocketActionConstant.PlayAction.INTENT_ACTION_MEDIA_BUTTON_PAUSE);
//                     LoggerUtils.d(TAG, "onPause: ");
//                 }
//
//                 @Override
//                 public void onSkipToNext() {
//                     super.onSkipToNext();
//                     notifyMediaButtonChange(getContext(), SocketActionConstant.PlayAction.INTENT_ACTION_MEDIA_BUTTON_NEXT);
//                     LoggerUtils.d(TAG, "onSkipToNext: ");
//                 }
//
//                 @Override
//                 public void onSkipToPrevious() {
//                     super.onSkipToPrevious();
//                     //这里处理播放器逻辑 上一曲
//                     notifyMediaButtonChange(getContext(), SocketActionConstant.PlayAction.INTENT_ACTION_MEDIA_BUTTON_PREVIOUS);
//                     LoggerUtils.d(TAG, "onSkipToPrevious: ");
//                 }
//             });
//         }
//          mediaSessionCompat.setActive(true);
//        }else {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
        cn = new ComponentName(context.getApplicationContext().getPackageName(), RemoteControlClientReceiver.class.getName());
        audioManager.registerMediaButtonEventReceiver(cn);

    }

    /**
     * 注销媒体按钮监听
     */
    public static void unregisterMediaButtonReceiver() {

//            if (Build.VERSION.SDK_INT <  Build.VERSION_CODES.LOLLIPOP) {
//                //注册接收的Receiver
//                if (mediaSessionCompat != null) {
//                    mediaSessionCompat.setCallback(null);
//                    mediaSessionCompat.setActive(false);
//                    mediaSessionCompat.release();
//                    mediaSessionCompat = null;
//                }
//            } else {
        if (audioManager != null && cn != null) {
            audioManager.unregisterMediaButtonEventReceiver(cn);
        }
//            }
    }
}
