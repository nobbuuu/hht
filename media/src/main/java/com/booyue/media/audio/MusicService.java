package com.booyue.media.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.booyue.base.util.LoggerUtils;
import com.booyue.media.MediaConfig;
import com.booyue.media.MediaConstant;
import com.booyue.media.HHTHttpProxyCacheServer;
import com.booyue.media.audio.listener.IHHTMusicPlayer;
import com.booyue.media.audio.listener.IHHTMusicPlayerCallback;
import com.booyue.media.audio.listener.IHHTMusicPlayerTimingCallback;
import com.danikula.videocache.HttpProxyCacheServer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements OnBufferingUpdateListener,
        OnPreparedListener, OnInfoListener, OnCompletionListener, OnErrorListener {
    private static final String TAG = "MusicService";
    //当前播放文件路径
    private static String mPlayingPath = "";
    //播放歌曲
    MediaPlayer mMediaPlayer;
    //用来更新歌曲播放进度
    Thread mUpdateThread;
    //判定mediaPlayer是否是处于paused状态
    boolean mIsPaused;
    boolean mIsStop;
    //判定mediaPlayer是否是处于准备状态
    boolean mIsPreparing;
    //从指定位置开始播放
    int mPlayFromPostionOnce;
    //标记是否播放完成，如果列表只有一首歌曲或者单曲播放，播放完之后需要重新播放，但是playOrResume()对于同一个音频只有恢复
    boolean mIsComplete = false;

    public static String mTag = "";

    //标记缓冲开始和完成
    private boolean isBuffering = false;
    private MusicPlayer musicPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        LoggerUtils.d(TAG, "onBind: ");
        musicPlayer = new MusicPlayer();
        createNotification(true);
        return musicPlayer;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MusicPlayer extends Binder implements IHHTMusicPlayer {
        /**
         * 删除通知
         */
        public void deleteNotification() {
            LoggerUtils.d(TAG, "deleteNotification: ");
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.notifyDeleteNotification(MusicService.this);
                }
            }
        }

        @Override
        public void play(String path, String tag) {
            if (mTag != null && !mTag.equals(tag) && musicPlayer != null) {
                musicPlayer.onMusicPlayerTAGChange();
            }
            mTag = tag;
            MusicService.this.play(path);
        }

        @Override
        public void pause() {
            LoggerUtils.d(TAG, "pause: ");
            createNotification(false);
            if (Looper.getMainLooper() != Looper.myLooper()) {
                mHandler.post(() ->
                        MusicService.this.pause()
                );
            } else {
                MusicService.this.pause();
            }
        }

        @Override
        public void resume(String path) {
            MusicService.this.resumePlay("");
        }

        @Override
        public void seek(int position) {
            MusicService.this.seekTo(position);
        }

        @Override
        public void stop(boolean cancelNotification) {
            MusicService.this.stop(cancelNotification);
        }


        @Override
        public void playFrom(String path, int position, String tag) {
            if (mTag != null && !mTag.equals(tag)) {
                musicPlayer.onMusicPlayerTAGChange();
            }
            mTag = tag;
            MusicService.this.playFrom(path, position);
        }

        @Override
        public void playOrResume(String path, String tag) {
            createNotification(false);
            LoggerUtils.d(TAG, "playOrResume: startPath = \n" + path + ",mPlayingPath = \n" + mPlayingPath);
            //同一个音频
            if (mPlayingPath != null && mPlayingPath.equals(path)) {
                if (!isPlaying()) {
                    MusicService.this.resumePlay(path);
                }
                //重新播放
            } else {
                play(path, tag);
            }
        }

        @Override
        public boolean isPlaying() {
            boolean isPlaying = MusicService.this.isPlaying();
            LoggerUtils.d(TAG, "isPlaying: " + isPlaying);
            return isPlaying;
        }

        @Override
        public boolean isPlayingPath(String path) {
            return (mPlayingPath != null && mPlayingPath.equals(path) && isPlaying());
        }

        @Override
        public boolean isPausedPath(String path) {
            return (mPlayingPath != null && mPlayingPath.equals(path) && !isPlaying());
        }

        public String getPath() {
            return mPlayingPath;
        }

        private List<IHHTMusicPlayerCallback> callbackList = new ArrayList<>();

        @Override
        public void addListener(IHHTMusicPlayerCallback callback) {
            if (!callbackList.contains(callback) && callback != null) {
                this.callbackList.add(callback);
                LoggerUtils.d(TAG, "addListener: ");
            }
        }

        @Override
        public void removeListener(IHHTMusicPlayerCallback callback) {
            if (callbackList.contains(callback)) {
                this.callbackList.remove(callback);
            }
        }

        @Override
        public void startTiming(long time) {
            timingRemain = time;
            if (mHandler.hasMessages(MESSAGE_TIMING)) {
                mHandler.removeMessages(MESSAGE_TIMING);
            }
            mHandler.sendEmptyMessage(MESSAGE_TIMING);

        }

        @Override
        public void cancelTiming() {
            if (mHandler.hasMessages(MESSAGE_TIMING)) {
                mHandler.removeMessages(MESSAGE_TIMING);
                resetTimingState();
            }
        }

        @Override
        public long querTimeRemaining() {
            return getTimingRemain();
        }

        @Override
        public boolean isTiming() {
            return MusicService.this.isTiming();
        }

        @Override
        public int getPlayerProgress() {
            return MusicService.this.getPlayerProgress();
        }

        @Override
        public int getCurrentTime() {
            return MusicService.this.getCurrentPosition();
        }

        @Override
        public int getDurationTime() {
            return MusicService.this.getDuration();
        }


        public void onMusicPlayerPause(boolean updateNotification) {
            LoggerUtils.d(TAG, "onMusicPlayerPause: ");
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerPaused(mTag,updateNotification);
                }
            }
        }

        public void onError(int what, int extra) {
            LoggerUtils.d(TAG, "onError: ");
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerError(what, extra);
                }
            }
        }

        public void onMusicPlayerBufferingProgress(int percent, String path) {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerBuffingProgress(mTag, percent, path);
                }
            }
        }

        public void onMusicPlayerProgress(String tag, String path, int currentTime, int durationTime) {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerProgress(tag, path, currentTime, durationTime);
                }
            }
        }


        public void onMusicPlayerComplete(String path, int duration) {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerComplete(mTag, path, duration);
                }
            }
        }

        public void onMusicPlayerBufferingStart() {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerBufferingStart(mTag);
                }
            }
        }

        public void onMusicPlayerBufferingEnd() {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerBufferingEnd(mTag);
                }
            }
        }

        public void onMusicPlayerPreparing() {
            LoggerUtils.d(TAG, "onMusicPlayerPreparing: ");
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerPreparing();
                }
            }

        }

        public void onMusicPlayerPreparedWithStart(int duration, String path) {
            LoggerUtils.d(TAG, "onMusicPlayerPreparedWithStart: ");
            //通知其他播放器停止
            Intent intent = new Intent(MediaConstant.ACTION_AUDIO_PLAYER_PLAYING);
            MediaConfig.application.getApplicationContext().sendBroadcast(intent);
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerPreparedWithStart(mTag, path, duration);
                }
            }
        }

        public void onMusicPlayerPlaying() {
            //通知其他播放器停止
            LoggerUtils.d(TAG, "onMusicPlayerPlaying: ");
            Intent intent = new Intent(MediaConstant.ACTION_AUDIO_PLAYER_PLAYING);
            MediaConfig.application.getApplicationContext().sendBroadcast(intent);
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerPlaying(mTag);
                }
            }
        }

        public void onMusicPlayerTAGChange() {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.onMusicPlayerTAGChange(mTag);
                }
            }
        }

        /**
         * 定时器更新页面
         *
         * @param time
         */
        public void onTiming(long time) {
            if (callbackList.size() > 0) {
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    if (ihhtMusicPlayerCallback instanceof IHHTMusicPlayerTimingCallback) {
                        LoggerUtils.d(TAG, "onTiming: " + time);
                        ((IHHTMusicPlayerTimingCallback) ihhtMusicPlayerCallback).onTiming(time);
                    }
                }
            }
        }

        /**
         * 通知创建通知
         */
        public void notifyCreateNotification(boolean cancel) {
            LoggerUtils.d(TAG, "notifyCreateNotification: cancel" + cancel);
            if (callbackList.size() > 0) {
                LoggerUtils.d(TAG, "callbackList.size: " + callbackList.size());
                for (IHHTMusicPlayerCallback ihhtMusicPlayerCallback : callbackList) {
                    ihhtMusicPlayerCallback.notifyCreateNotification(MusicService.this, cancel);
                }
            }
        }


    }

    private int progress = 0;
    /**
     * 删除通知栏的播放器，此时进入stop，如果从任务栏进入播放页面，此时应该是播放如果实现resume,会无响应，因为stop状态不能直接resume
     * 所以需要重新play 然后seekto
     */
    private int currentPosition = 0;
    private int mDuration = 0;

    /**
     * modify by : 2020/1/17 11:28
     */

    @Override
    public void onCreate() {
        super.onCreate();
        LoggerUtils.d(TAG, "MusicSevervice onCreate ");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mUpdateThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    while (!Thread.interrupted()) {
                        if (mMediaPlayer.isPlaying()) {
                            int current = mMediaPlayer.getCurrentPosition();
                            int duration = mMediaPlayer.getDuration();
                            progress = current * 100 / duration;
                            currentPosition = current;
                            mDuration = duration;
                            if (musicPlayer != null) {
                                musicPlayer.onMusicPlayerProgress(mTag, mPlayingPath, current, duration);
                            }
                        }
                        TimeUnit.MILLISECONDS.sleep(300);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        mUpdateThread.start();
    }

    /**
     * 创建通知
     *
     * @param cancle 创建服务的时候创建通知立马取消，只用用户使用了播放才需要显示通知
     */
    public void createNotification(boolean cancle) {
        LoggerUtils.d(TAG, "createNotification: cancle " + cancle);
        //必须延迟一下，不然用户未设置监听，此时通知用户创建通知，会失败，会导致service.startforegound未调用而出现无响应的问题
        mHandler.postDelayed(() -> musicPlayer.notifyCreateNotification(cancle), 200);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LoggerUtils.d(TAG, "MusicSevervice onDestroy ");
        if (mIsPaused || isPlaying()) {
            stop(false);
        }
        mUpdateThread.interrupt();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }


    /**
     * 获取音频总时长
     *
     * @return 总时长
     */
//    public int getDuration() {
//        if (mIsPaused || isPlaying()) {
//            return mMediaPlayer.getDuration();
//        }
//        return 0;
//    }

    /**
     * 恢复播放
     */
    public void resumePlay(String path) {
        if (mMediaPlayer == null) {
            return;
        }
        LoggerUtils.d(TAG, "resumePlay: " + path);
        //正在播放
        if (mIsPaused) {
            LoggerUtils.d(TAG, "continue play " + mPlayingPath);
            mIsPaused = false;
            mMediaPlayer.start();
            if (musicPlayer != null) {
                musicPlayer.onMusicPlayerPlaying();
            }
        } else if (mIsComplete) {
            play(path);
        } else if (mIsStop) {
            play(mPlayingPath);
        }
    }

    /**
     * 判断当前是否正在播放
     *
     * @return
     */
    public Boolean isPlaying() {
        boolean isPlaying = false;
        if (mMediaPlayer != null) {
            isPlaying = mMediaPlayer.isPlaying() || mIsPreparing;
        }
        return isPlaying;
    }

    /**
     * 停止播放
     */
    public void stop(boolean cancelNotification) {
        LoggerUtils.d(TAG + "停止播放");
        if (mIsPaused || isPlaying()) {
            mMediaPlayer.stop();
        }
        mIsPaused = false;
        mIsPreparing = false;
        if(cancelNotification){
            mIsStop = true;/**modify by : 2020/1/17 11:13*/
        }
        if (musicPlayer != null) {
            musicPlayer.onMusicPlayerPause(false);
        }
    }

    /**
     * 从指定位置播放
     *
     * @param url
     * @param pos
     */
    public void playFrom(String url, int pos) {
        //正在播放
        if (url != null) {
            if (url.equals(mPlayingPath) && isPlaying()) {
                seekTo(pos);
                return;
            }
            if (pos > 0) {
                mPlayFromPostionOnce = pos;
            }
            play(url);
        }
    }

    /**
     * 判断是否播放网络资源
     *
     * @param url
     * @return
     */
    public boolean isNetUrl(String url) {
        return url.startsWith("http");
    }


    public void play(String url) {
        //正在播放
        if (url == null || mMediaPlayer == null) {
            return;
        }
        //检测是否在播放状态
        if (isPlaying() || mIsPaused) {
            stop(false);
        }
        // 播放URL指向的位置的音乐
        try {
            mPlayingPath = url;
            LoggerUtils.d(TAG, "MusicService play " + mPlayingPath);
            mIsPaused = false;
            mIsComplete = false;
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            String mediaUrl = mPlayingPath;
            if (isNetUrl(mediaUrl)) {
                HttpProxyCacheServer proxy = HHTHttpProxyCacheServer.getProxy(this);
                mediaUrl = proxy.getProxyUrl(mPlayingPath + MediaConfig.ServerParams);
            }
            mMediaPlayer.setDataSource(mediaUrl);
            if (isNetUrl(mediaUrl)) {
                mMediaPlayer.prepareAsync();
                if (musicPlayer != null) {
                    musicPlayer.onMusicPlayerPreparing();
                }
            } else {
                mMediaPlayer.prepare();
            }
            mIsPreparing = true;
            LoggerUtils.d(TAG, "allowPlay start: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拖动到指定位置播放
     *
     * @param position
     */
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isPlaying()) {
            //暂停音乐播放
            mIsPaused = true;
            //preparing状态条用 pause 会出现OnError（） -38，所以放到 prepared监听中判断实现
            if (!mIsPreparing) {
                mMediaPlayer.pause();
                if (musicPlayer != null) {
                    musicPlayer.onMusicPlayerPause(true);
                }
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (percent <= 100) {
            MusicService.this.musicPlayer.onMusicPlayerBufferingProgress(percent, mPlayingPath);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LoggerUtils.d(TAG, "prepared  :" + mIsPaused + " path:" + mPlayingPath);
        mIsPreparing = false;
        LoggerUtils.d(TAG, "allowPlay end: ");
        if (mIsPaused) {
            return;
        }
        if (mIsStop) {
            mp.seekTo(currentPosition);
            mIsStop = false;
        }
        if (mPlayFromPostionOnce > 0 && mPlayFromPostionOnce < mp.getDuration()) {
            LoggerUtils.d(TAG, "playing from :" + new SimpleDateFormat("mm:ss").format(new Date(mPlayFromPostionOnce)));
            mp.seekTo(mPlayFromPostionOnce);
        }
        mPlayFromPostionOnce = -1;
        mp.start();
        musicPlayer.onMusicPlayerPreparedWithStart(mp.getDuration(), mPlayingPath);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            if (!isBuffering) {
                MusicService.this.musicPlayer.onMusicPlayerBufferingStart();
                isBuffering = true;
            }
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (isBuffering) {
                MusicService.this.musicPlayer.onMusicPlayerBufferingEnd();
                isBuffering = false;
            }
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        int duration = mMediaPlayer.getDuration();
        //正常情况下，播完当前歌曲会播放下一首，但是如果设置了定时isPlayNext置为false因此播放停止
        mIsComplete = true;
        mIsPaused = false;
        MusicService.this.musicPlayer.onMusicPlayerComplete(mPlayingPath, duration);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LoggerUtils.e(TAG, "onError:what " + what + "extra " + extra + "path :" + mPlayingPath);
        MusicService.this.musicPlayer.onError(what, extra);
        return true;
    }

    private static int MESSAGE_TIMING = 100;
    private static int MESSAGE_INTERVAL = 1000;
    private long timingRemain = -5;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_TIMING) {
                if (timingRemain > 0) {
                    timingRemain = timingRemain - 1000;
                    MusicService.this.musicPlayer.onTiming(timingRemain);
                    sendEmptyMessageDelayed(MESSAGE_TIMING, MESSAGE_INTERVAL);
                } else {
                    if (isPlaying()) {
                        pause();
                        resetTimingState();
                    }
                }
            }
        }
    };

    /**
     * 获取定时剩余时间
     *
     * @return 定时剩余时间
     */
    private long getTimingRemain() {
        LoggerUtils.d(TAG, "getTimingRemain: " + timingRemain);
        return timingRemain;
    }

    /**
     * 判断是否定时倒计时中
     *
     * @return
     */
    public boolean isTiming() {
        return timingRemain > 0;
    }

    public void resetTimingState() {
        timingRemain = -5;
    }

    public int getPlayerProgress() {
        if(progress < 0) return 0;
        return progress;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }
    public int getDuration(){
        return mDuration;
    }

}

