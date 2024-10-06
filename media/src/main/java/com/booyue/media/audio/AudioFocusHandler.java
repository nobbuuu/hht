package com.booyue.media.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.booyue.media.MediaConfig;

/**
 * Created by Administrator on 2018/3/5.16:46
 * 音频焦点操作类，主要用于音频焦点管理
 */

public class AudioFocusHandler {
    public static final String TAG = "AudioFocusHandler";
    private AudioFocusChange mAudioFocusChange = null;
    private static AudioFocusHandler playHandler;

    public static AudioFocusHandler getInstance() {
        if (playHandler == null) {
            synchronized (AudioFocusHandler.class){
                if(playHandler == null){
                    playHandler = new AudioFocusHandler();
                }
            }
        }
        return playHandler;
    }

    private AudioFocusHandler() {
        mAudioFocusChange = new AudioFocusChange();
    }


    public boolean requestAudioFocus(Context context) {
        AudioManager am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int requestResult = am.requestAudioFocus(
                mAudioFocusChange,
                AudioManager.STREAM_MUSIC,
                //获取焦点的用途
                AudioManager.AUDIOFOCUS_GAIN);
        boolean isGranted = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == requestResult;
        Log.d(TAG, "requestAudioFocus isGranted : " + isGranted);
        if(isGranted){
            RemoteControlClient.registerMediaButtonReceiver(context);
        }
        return true;
    }

    /**
     * 释放焦点
     * @return
     */
    public boolean abandonAudioFocus(Context context) {
        if (mAudioFocusChange == null) {
            return false;
        }
        AudioManager am = (AudioManager)context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        boolean isGranded = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.abandonAudioFocus(mAudioFocusChange);
        Log.d(TAG, "abandonAudioFocus: isGranded " + isGranded);
        if(isGranded){
            RemoteControlClient.unregisterMediaButtonReceiver();
        }
        return isGranded;
    }

    /**
     * 音频焦点变化监听
     */
    class AudioFocusChange implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            //获取焦点1
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.d(TAG, "获取焦点1: ");
                //获取短暂焦点2
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) {
                Log.d(TAG, "获取焦点2: ");
                //失去焦点-1
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                Log.d(TAG, "失去焦点1: ");
                RemoteControlClient.registerMediaButtonReceiver(MediaConfig.application);
                MusicService.MusicPlayer musicPlayer = MusicManager.getInstance().getMusicPlayer();
                if(musicPlayer != null && musicPlayer.isPlaying()){
                    musicPlayer.pause();
                }
                //失去短暂焦点-2
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                Log.d(TAG, "失去焦点2: ");
                MusicService.MusicPlayer musicPlayer = MusicManager.getInstance().getMusicPlayer();
                if(musicPlayer != null && musicPlayer.isPlaying()){
                    musicPlayer.pause();
                }
            }
        }
    }
}
