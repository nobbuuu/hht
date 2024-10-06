package com.booyue.media.video.ijk;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.booyue.base.util.LoggerUtils;
import com.booyue.media.R;
import com.xiao.nicevideoplayer.ChangeClarityDialog;
import com.xiao.nicevideoplayer.INiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceUtil;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;

/**
 * Created by XiaoJianjun on 2017/6/21.
 * 仿腾讯视频热点列表页播放器控制器.
 */
public abstract class HHTVideoPlayerController
        extends NiceVideoPlayerController
        implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        ChangeClarityDialog.OnClarityChangedListener {
    private static final String TAG = "HHTVideoPlayerControlle";
    private Context mContext;
    private ImageView mImage;

    private LinearLayout mTop;
    private ImageView mBack;
    private TextView tvTopSpace;
    private TextView mTitle;
    private LinearLayout mContainerButton;

//    private LinearLayout mBatteryTime;
//    private ImageView mBattery;
//    private TextView mTime;

    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private ImageView mRestartPauseNormal;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private ImageView mFullScreen;

//    private TextView mLength;

    private LinearLayout mLoading;
    private TextView mLoadText;

//    private LinearLayout mChangePositon;
//    private TextView mChangePositionCurrent;
//    private ProgressBar mChangePositionProgress;
//
//    private LinearLayout mChangeBrightness;
//    private ProgressBar mChangeBrightnessProgress;
//
//    private LinearLayout mChangeVolume;
//    private ProgressBar mChangeVolumeProgress;

    private LinearLayout mError;
    private TextView mRetry;

//    private LinearLayout mCompleted;
//    private TextView mReplay;
//    private TextView mShare;

    private boolean topBottomVisible;
    private CountDownTimer mDismissTopBottomCountDownTimer;

//    private List<Clarity> clarities;
//    private int defaultClarityIndex;

//    private ChangeClarityDialog mClarityDialog;

//    private boolean hasRegisterBatteryReceiver; // 是否已经注册了电池广播

    public HHTVideoPlayerController(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.video_control_view, this, true);

        mImage = (ImageView) findViewById(R.id.image);

        mTop = (LinearLayout) findViewById(R.id.rl_container_top);
        mBack = (ImageView) findViewById(R.id.back);
        tvTopSpace = findViewById(R.id.tvTopSpace);
        mTitle = (TextView) findViewById(R.id.title);
        mContainerButton = (LinearLayout) findViewById(R.id.ll_button);

        mBottom = findViewById(R.id.ll_container_bottom);
        mRestartPause = findViewById(R.id.restart_or_pause);
        mRestartPauseNormal = findViewById(R.id.restart_or_pause_normal);
        mPosition = findViewById(R.id.position);
        mDuration = findViewById(R.id.duration);
        mSeek = findViewById(R.id.seek);
        mFullScreen = findViewById(R.id.full_screen);

        mLoading = findViewById(R.id.loading);
        mLoadText = findViewById(R.id.load_text);


        mError = findViewById(R.id.error);
        mRetry = findViewById(R.id.retry);


        mBack.setOnClickListener(this);
        mRestartPause.setOnClickListener(this);
        mRestartPauseNormal.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
        initCustomView();
        initModeNormalView();
    }
    public abstract void initCustomView();

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public ImageView imageView() {
        return mImage;
    }

    @Override
    public void setImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    @Override
    public void setLenght(long length) {
//        mLength.setText(NiceUtil.formatTime(length));
    }

    @Override
    public void setNiceVideoPlayer(INiceVideoPlayer niceVideoPlayer) {
        super.setNiceVideoPlayer(niceVideoPlayer);
        // 给播放器配置视频链接地址
//        if (clarities != null && clarities.size() > 1) {
//            mNiceVideoPlayer.setUp(clarities.get(defaultClarityIndex).videoUrl, null);
//        }
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case NiceVideoPlayer.STATE_IDLE:
                LoggerUtils.d(TAG, ": ");
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerIdle();
                }
                break;
            case NiceVideoPlayer.STATE_PREPARING:
                mImage.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mLoadText.setText("正在加载...");
                mError.setVisibility(View.GONE);

                mTop.setVisibility(View.GONE);
                mRestartPauseNormal.setVisibility(View.GONE);
                mBottom.setVisibility(View.GONE);
//                mLength.setVisibility(View.GONE);
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerPreparing();
                }
                break;
            case NiceVideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerPrepared();
                }
                break;
            case NiceVideoPlayer.STATE_PLAYING:
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_media_video_pause);
                mRestartPauseNormal.setImageResource(R.drawable.ic_media_video_pause);
                startDismissTopBottomTimer();
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerPlaying();
                }
                break;
            case NiceVideoPlayer.STATE_PAUSED:
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_media_video_play);
                mRestartPauseNormal.setImageResource(R.drawable.ic_media_video_play);
                cancelDismissTopBottomTimer();
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerPaused();
                }
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PLAYING:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_media_video_pause);
                mRestartPauseNormal.setImageResource(R.drawable.ic_media_video_pause);
                mLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerBufferingStop();
                }
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PAUSED:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_media_video_play);
                mRestartPauseNormal.setImageResource(R.drawable.ic_media_video_play);
                mLoadText.setText("正在缓冲...");
                cancelDismissTopBottomTimer();
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerBufferingStart();
                }
                break;
            case NiceVideoPlayer.STATE_ERROR:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mTop.setVisibility(View.VISIBLE);
                mError.setVisibility(View.VISIBLE);
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerError();
                }
                break;
            case NiceVideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                setTopBottomVisible(true);
                mImage.setVisibility(View.VISIBLE);
                if(mOnPlayerStateChangeListener != null){
                    mOnPlayerStateChangeListener.onPlayerComplete();
                }
                break;
        }
    }

    @Override
    protected void onPlayModeChanged(int playMode) {
        switch (playMode) {
            case NiceVideoPlayer.MODE_NORMAL:
//                mBack.setVisibility(View.GONE);
                mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
                mFullScreen.setVisibility(View.VISIBLE);

                initModeNormalView();

                break;
            case NiceVideoPlayer.MODE_FULL_SCREEN:
                mBack.setVisibility(View.VISIBLE);
//                mFullScreen.setVisibility(View.GONE);
                mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_shrink);
                initModeFullScreenView();

                break;
            case NiceVideoPlayer.MODE_TINY_WINDOW:
                mBack.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void initModeNormalView(){
        LoggerUtils.d(TAG, "initModeNormalView: ");
        mTitle.setVisibility(View.GONE);
        tvTopSpace.setVisibility(View.GONE);
        mRestartPause.setVisibility(View.GONE);
        mFullScreen.setVisibility(View.VISIBLE);
        mRestartPauseNormal.setVisibility(View.VISIBLE);

    }
    public void initModeFullScreenView(){
        LoggerUtils.d(TAG, "initModeFullScreenView: ");
        mTitle.setVisibility(View.VISIBLE);
        tvTopSpace.setVisibility(View.GONE);
        mFullScreen.setVisibility(View.GONE);
        mRestartPause.setVisibility(View.VISIBLE);
        mRestartPauseNormal.setVisibility(View.GONE);
    }
    @Override
    protected void reset() {
        topBottomVisible = false;
        cancelUpdateProgressTimer();
        cancelDismissTopBottomTimer();
        mSeek.setProgress(0);
        mSeek.setSecondaryProgress(0);

        mImage.setVisibility(View.VISIBLE);

        mBottom.setVisibility(View.GONE);
        mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
//        mLength.setVisibility(View.VISIBLE);

        mTop.setVisibility(View.VISIBLE);
//        mBack.setVisibility(View.GONE);

        mLoading.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);

    }

    /**
     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
     * UI相关的逻辑都尽量到{@link #onPlayStateChanged}和{@link #onPlayModeChanged}中处理.
     */
    @Override
    public void onClick(View v) {
        if (v == mBack) {
            if (mNiceVideoPlayer.isFullScreen()) {
                mNiceVideoPlayer.exitFullScreen();
            } else if (mNiceVideoPlayer.isTinyWindow()) {
                mNiceVideoPlayer.exitTinyWindow();
            } else {
               onFinish();
            }
        } else if (v == mRestartPause || v == mRestartPauseNormal) {
            if (mNiceVideoPlayer.isPlaying() || mNiceVideoPlayer.isBufferingPlaying()) {
                mNiceVideoPlayer.pause();
            } else if (mNiceVideoPlayer.isPaused() || mNiceVideoPlayer.isBufferingPaused()) {
                mNiceVideoPlayer.restart();
            }
        } else if (v == mFullScreen) {
            if (mNiceVideoPlayer.isNormal() || mNiceVideoPlayer.isTinyWindow()) {
                mNiceVideoPlayer.enterFullScreen();
            } else if (mNiceVideoPlayer.isFullScreen()) {
                mNiceVideoPlayer.exitFullScreen();
            }
        }else if (v == mRetry) {
            mNiceVideoPlayer.restart();
        }else if (v == this) {
            if (mNiceVideoPlayer.isPlaying()
                    || mNiceVideoPlayer.isPaused()
                    || mNiceVideoPlayer.isBufferingPlaying()
                    || mNiceVideoPlayer.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible);
            }
        }
        onCustomViewClick(v);

    }
    public abstract void onFinish();
    public abstract void onCustomViewClick(View v);

    @Override
    public void onClarityChanged(int clarityIndex) {
        // 根据切换后的清晰度索引值，设置对应的视频链接地址，并从当前播放位置接着播放
//        Clarity clarity = clarities.get(clarityIndex);
//        long currentPosition = mNiceVideoPlayer.getCurrentPosition();
//        mNiceVideoPlayer.releasePlayer();
//        mNiceVideoPlayer.setUp(clarity.videoUrl, null);
//        mNiceVideoPlayer.start(currentPosition);
    }

    @Override
    public void onClarityNotChanged() {
        // 清晰度没有变化，对话框消失后，需要重新显示出top、bottom
        setTopBottomVisible(true);
    }

    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private void setTopBottomVisible(boolean visible) {
        mTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        topBottomVisible = visible;
        if(!mNiceVideoPlayer.isFullScreen()){
            mRestartPauseNormal.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        if (visible) {
            if (!mNiceVideoPlayer.isPaused() && !mNiceVideoPlayer.isBufferingPaused()) {
                startDismissTopBottomTimer();
            }
        } else {
            cancelDismissTopBottomTimer();
        }
    }

    /**
     * 开启top、bottom自动消失的timer
     */
    private void startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer();
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    setTopBottomVisible(false);
                }
            };
        }
        mDismissTopBottomCountDownTimer.start();
    }

    /**
     * 取消top、bottom自动消失的timer
     */
    private void cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer.cancel();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mNiceVideoPlayer.isBufferingPaused() || mNiceVideoPlayer.isPaused()) {
            mNiceVideoPlayer.restart();
        }
        long position = (long) (mNiceVideoPlayer.getDuration() * seekBar.getProgress() / 100f);
        mNiceVideoPlayer.seekTo(position);
        startDismissTopBottomTimer();
    }

    @Override
    protected void updateProgress() {
        long position = mNiceVideoPlayer.getCurrentPosition();
        long duration = mNiceVideoPlayer.getDuration();
        int bufferPercentage = mNiceVideoPlayer.getBufferPercentage();
        mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        mSeek.setProgress(progress);
        mPosition.setText(NiceUtil.formatTime(position));
        mDuration.setText(NiceUtil.formatTime(duration));
        // 更新时间
//        mTime.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date()));
    }

    @Override
    protected void showChangePosition(long duration, int newPositionProgress) {

    }

    @Override
    protected void hideChangePosition() {

    }

    @Override
    protected void showChangeVolume(int newVolumeProgress) {

    }

    @Override
    protected void hideChangeVolume() {

    }

    @Override
    protected void showChangeBrightness(int newBrightnessProgress) {

    }

    @Override
    protected void hideChangeBrightness() {

    }

    /**
     * {@link #onPlayStateChanged(int)}
     */
    public interface OnPlayerStateChangeListener{
        void onPlayerIdle();
        void onPlayerPreparing();
        void onPlayerPrepared();
        void onPlayerPlaying();
        void onPlayerPaused();
        void onPlayerBufferingStart();
        void onPlayerBufferingStop();
        void onPlayerError();
        void onPlayerComplete();
    }

    private OnPlayerStateChangeListener mOnPlayerStateChangeListener;
    public void setOnPlayerStateChangeListener(OnPlayerStateChangeListener onPlayerStateChangeListener){
        mOnPlayerStateChangeListener = onPlayerStateChangeListener;
    }

}
