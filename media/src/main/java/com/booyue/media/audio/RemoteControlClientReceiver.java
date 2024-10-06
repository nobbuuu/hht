package com.booyue.media.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.booyue.media.MediaConstant;


/**
 * @author: wangxinhua
 * @date: 2019/1/17
 * @description : 远程控制客户端接收器
 */
public class RemoteControlClientReceiver extends BroadcastReceiver {
    private static final String TAG = "RemoteControlClient";
    private static final long intervalTime = 800;
    private static long lastTime = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }
            Log.d(TAG, "KeyCode: " + event.getKeyCode());
//                   if (event.getKeyCode() != KeyEvent.KEYCODE_HEADSETHOOK
//                    && event.getKeyCode() != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
//                    && event.getAction()  != KeyEvent.ACTION_DOWN
//                    && event.getAction()  != KeyEvent.KEYCODE_MEDIA_PLAY
//                    && event.getAction()  != KeyEvent.KEYCODE_MEDIA_PAUSE)
//                return;
            //以下值根据实际收到的情况做处理！！
            switch (event.getKeyCode()) {
                //播放AND暂停
                //oppo 手机暂停播放都是执行这个广播，而且都是执行两次，所以如果第二次超过一定时间不理会
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                  Log.d(TAG, "KEYCODE_MEDIA_PLAY_PAUSE: ");
                    if((System.currentTimeMillis() - lastTime) < intervalTime){
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    if (MusicManager.getInstance().getMusicPlayer() != null) {
                        if (MusicManager.getInstance().getMusicPlayer().isPlaying()) {
                            notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_PAUSE);
                        } else {
                            notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_PLAY);
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    if((System.currentTimeMillis() - lastTime) < intervalTime){
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_PLAY);
                    Log.d(TAG, "KEYCODE_MEDIA_PLAY: ");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    if((System.currentTimeMillis() - lastTime) < intervalTime){
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_PAUSE);
                    Log.d(TAG, "KEYCODE_MEDIA_PAUSE: ");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    if((System.currentTimeMillis() - lastTime) < intervalTime){
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_NEXT);
                    Log.d(TAG, "KEYCODE_MEDIA_NEXT: ");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    if((System.currentTimeMillis() - lastTime) < intervalTime){
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    notifyMediaButtonChange(context, MediaConstant.INTENT_ACTION_MEDIA_BUTTON_PREVIOUS);
                    Log.d(TAG, "KEYCODE_MEDIA_PREVIOUS: ");
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.d(TAG, "KEYCODE_MEDIA_STOP: ");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 通知外部
     * @param context
     * @param action
     */
    private void notifyMediaButtonChange(Context context,String action){
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }
}
