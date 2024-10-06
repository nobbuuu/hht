package com.booyue.media.audio.listener;

import com.booyue.media.audio.MusicService;

/**
 * @author: wangxinhua
 * @date: 2019/4/26
 * @description :
 */
public interface IHHTMusicPlayerCallback {
    /**
     * 音乐开始播放
     * @param path 播放路径
     * @param duration 音乐时长
     */
//    SocketActionConstant.PlayAction.ACTION_PLAY_MUSIC_START
    void onMusicPlayerPreparedWithStart(String tag, String path, int duration);

    /**
     * 如果是删除通知栏播放器，此时无需更新通知栏状态
     * @param mtag
     * @param updateNotification
     */
    void onMusicPlayerPaused(String mtag,boolean updateNotification);
    void onMusicPlayerPlaying(String mTag);
    //网络不好的情况，显示正在缓冲中对话框，结束之后继续播放
    void onMusicPlayerBufferingStart(String tag);
    void onMusicPlayerBufferingEnd(String tag);

    void onMusicPlayerComplete(String tag, String path, int duration);
    void onMusicPlayerError(int what, int extra);
    void onMusicPlayerProgress(String tag, String path, int currentTime, int durationTime);
    //缓冲进入，更新缓存进度条
    void onMusicPlayerBuffingProgress(String mTag, int percent, String path);
    //准备中、调用prepare方法之后调用
    void onMusicPlayerPreparing();

    void onMusicPlayerTAGChange(String tag);
    //创建通知回调，让主app创建
    void notifyCreateNotification(MusicService musicService,boolean cancel);
    void notifyDeleteNotification(MusicService musicService);
}
