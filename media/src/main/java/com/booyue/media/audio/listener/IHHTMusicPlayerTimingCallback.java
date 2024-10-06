package com.booyue.media.audio.listener;

import com.booyue.media.audio.MusicService;

/**
 * @author: wangxinhua
 * @date: 2019/9/30
 * @description :
 */
public abstract class IHHTMusicPlayerTimingCallback implements IHHTMusicPlayerCallback {

    @Override
    public void onMusicPlayerPreparedWithStart(String tag, String path, int duration) {

    }

    @Override
    public void onMusicPlayerPaused(String mtag,boolean updateNotification) {

    }

    @Override
    public void onMusicPlayerPlaying(String mTag) {

    }

    @Override
    public void onMusicPlayerBufferingStart(String tag) {

    }

    @Override
    public void onMusicPlayerBufferingEnd(String tag) {

    }

    @Override
    public void onMusicPlayerComplete(String tag, String path, int duration) {

    }

    @Override
    public void onMusicPlayerError(int what, int extra) {

    }

    @Override
    public void onMusicPlayerProgress(String tag, String path, int currentTime, int durationTime) {

    }

    @Override
    public void onMusicPlayerBuffingProgress(String mTag, int percent, String path) {

    }

    @Override
    public void onMusicPlayerPreparing() {

    }

    @Override
    public void onMusicPlayerTAGChange(String tag) {

    }

    @Override
    public void notifyCreateNotification(MusicService musicService, boolean cancel) {

    }

    @Override
    public void notifyDeleteNotification(MusicService musicService) {

    }

    public abstract void onTiming(long time);
}
