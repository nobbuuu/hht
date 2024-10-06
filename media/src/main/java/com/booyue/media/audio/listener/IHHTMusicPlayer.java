package com.booyue.media.audio.listener;

/**
 * @author: wangxinhua
 * @date: 2019/4/26
 * @description : 播放
 */
public interface IHHTMusicPlayer {
    //播放
    void play(String path, String tag);
    //暂停
    void pause();
    //恢复
    void resume(String path);//恢复播放
    //拖动到某一位置播放
    void seek(int position);
    //停止
    void stop(boolean cancelNotication);
    //从某个位置开始播放
    void playFrom(String path, int position, String tag);
    //播放或恢复
    void playOrResume(String path, String tag);
    //判断是否正在播放
    boolean isPlaying();
    //是否是当前正在播放音频的地址
    boolean isPlayingPath(String path);
    //是否是当前暂停音频的地址
    boolean isPausedPath(String path);
    //设置监听
    void addListener(IHHTMusicPlayerCallback callback);
    //移除监听
    void removeListener(IHHTMusicPlayerCallback callback);

    //开始定时
    void startTiming(long time);
    //取消定时
    void cancelTiming();
    //查询定时剩余时间
    long querTimeRemaining();
    //查询是否定时开启中
    boolean isTiming();

    int getPlayerProgress();
    int getCurrentTime();
    int getDurationTime();

}
