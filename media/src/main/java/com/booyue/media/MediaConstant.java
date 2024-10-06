package com.booyue.media;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description :
 */
public class MediaConstant {
    /**
     * 连接蓝牙之后，播放火火兔歌曲，按设备上的上一曲，下一曲，播放，暂停没有响应
     * 所以使用{@link com.booyue.media.audio.RemoteControlClientReceiver}监听媒体按钮的触发
     */
    public static final String INTENT_ACTION_MEDIA_BUTTON_PLAY = "intent_action_media_button_play";
    public static final String INTENT_ACTION_MEDIA_BUTTON_PAUSE = "intent_action_media_button_pause";
    public static final String INTENT_ACTION_MEDIA_BUTTON_PREVIOUS = "intent_action_media_button_previous";
    public static final String INTENT_ACTION_MEDIA_BUTTON_NEXT = "intent_action_media_button_next";


    /**
     * service向activity发送的广播
     */
    //TAG change通知之前的播放暂停
    public static final String ACTION_PLAY_TAG_CHANGE = "com.booyue.action_play_change_tag";
    //更新进度条
    public static final String ACTION_UPDATE_SEEKBAR_PROGRESS = "com.booyue.action_update_seekbar_progress";

    public static final String ACTION_PLAY_MUSIC_FINISH = "com.booyue.action_play_music_finsh";
    //更新当前页面
    public static final String ACTION_PLAY_MUSIC_CHANGE = "com.booyue.action_play_music_change";

    /**
     * 处于正在播放的动作，此时需要通知其他播放器暂停
     */
    //音频播放器播放儿歌，故事
    public static final String ACTION_AUDIO_PLAYER_PLAYING = "com.booyue.action_audio_player_playing";
    //播放器播放视频
    public static final String ACTION_VIDEO_PLAYER_PLAYING = "com.booyue.action_video_player_playing";
    //录音播放器
    public static final String ACTION_RECORD_PLAYER_PLAYING = "com.booyue.action_record_player_playing";
    //蓝牙故事机播放器
    public static final String ACTION_BLUETOOTH_PLAYER_PLAYING = "com.booyue.action_bluetooth_player_playing";
    //变声对讲播放器
    public static final String ACTION_WHINE_PLAYER_PLAYING = "com.booyue.action_whine_player_playing";
    //intent传递参数的key
    public static final String INTENT_KEY_LIST = "list";
    public static final String INTENT_KEY_POSITION = "position";
    public static final String INTENT_KEY_IS_AUTO_PLAY = "iaAutoPlay";
}
