package com.booyue.media.video.exo

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.booyue.base.util.LoggerUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

/**
 * @author: wangxinhua
 * @date: 2018/12/20
 * @description :
 */
class ExoPlayer private constructor() {
    private object Holder {
        val INSTANCE = ExoPlayer()
    }

    companion object {
        val TAG = "ExoPlayer"
        val instance by lazy {
            Holder.INSTANCE
        }
        val TOGGLE_MODE_ALL = 0
        val TOGGLE_MODE_ONE = 1
        val TOGGLE_MODE_SHUFFLE = 2
        var TOGGLE_MODE = TOGGLE_MODE_ALL
    }

    private var context: Context? = null
    private var exoPlayer: com.google.android.exoplayer2.ExoPlayer? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var mPlayWhenReady: Boolean = true
    private var playerView: ExoPlayerView? = null
    private var mPrepared: Boolean = false
    fun initializePlayer(context: Context) {
        this.context = context
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context,
                DefaultRenderersFactory(context, EXTENSION_RENDERER_MODE_ON), DefaultTrackSelector(), DefaultLoadControl())
        //create a default trackselector
//        var bindWidthMeter = DefaultBandwidthMeter()
//        var videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

    }

    private var isBuffering = false
    private var isEnd = true//因为无法区分首次播放和恢复播放，所以需要判断是否经过了
    fun setPlayerParameter(playerView: ExoPlayerView) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
            throw Exception("sdcard not mount")
        }
        this.playerView = playerView
        if (playerView.player == null) {
            playerView.player = exoPlayer
            exoPlayer!!.playWhenReady = mPlayWhenReady
            exoPlayer?.addListener(object : Player.EventListener {
                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                    Log.d(TAG, "onTimelineChanged: ")
                    mEventListener?.onTimelineChanged(timeline, manifest, reason)
                }

                //                int STATE_IDLE = 1;
//                int STATE_BUFFERING = 2;
//                int STATE_READY = 3;
//                int STATE_ENDED = 4;
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    Log.d(TAG, "onPlayerStateChanged: playWhenReady = $playWhenReady ,playbackState = $playbackState")
//                    if (playbackState != Player.STATE_READY) {
//                           mPrepared = false
//                    }
                    when (playbackState) {
                        Player.STATE_ENDED -> mOnVideoCompleteListener?.onVideoComplete()
                        Player.STATE_BUFFERING -> {
                            if (!isBuffering) {
                                onVideoBufferingListenerListener?.onVideoBuffering(true)
                            }
                            isBuffering = true
                        }
                        Player.STATE_READY -> {
                            isBuffering = false
                            onVideoBufferingListenerListener?.onVideoBuffering(false)
                        }
                    }
                    if (playWhenReady) {
                        if (playbackState == Player.STATE_ENDED) {
                            mPlayerStateListener?.complete()
                            isEnd = true
                        } else if (playbackState == Player.STATE_READY) {
                            if (isEnd) {
                                mPlayerStateListener?.play()
                                isEnd = false
                            } else {
                                mPlayerStateListener?.resume()
                            }
                        }
                    } else {
                        if (playbackState == Player.STATE_READY) {
                            mPlayerStateListener?.pause()
                        }
                    }
                    mEventListener?.onPlayerStateChanged(playWhenReady, playbackState)
                }

                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                    Log.d(TAG, "onTracksChanged: ")
                    mEventListener?.onTracksChanged(trackGroups, trackSelections)
                }

                override fun onLoadingChanged(isLoading: Boolean) {
                    Log.d(TAG, "onLoadingChanged: isLoading = $isLoading")
                    //控制播放过程中出现加载对话框
//                    if(mPrepared){
//                        return
//                    }
//                    onVideoLoadingListenerListener?.onVideoLoading(isLoading)

                    mEventListener?.onLoadingChanged(isLoading)
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    Log.d(TAG, "onRepeatModeChanged: repeatMode = $repeatMode")
                    mEventListener?.onRepeatModeChanged(repeatMode)
                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    Log.d(TAG, "onShuffleModeEnabledChanged: shuffleModeEnabled = $shuffleModeEnabled")
                    mEventListener?.onShuffleModeEnabledChanged(shuffleModeEnabled)
                }

                override fun onPlayerError(error: ExoPlaybackException?) {
                    Log.d(TAG, "onPlayerError: error = ${error?.message}")
                    mEventListener?.onPlayerError(error)
                }

                override fun onPositionDiscontinuity(reason: Int) {
                    Log.d(TAG, "onPositionDiscontinuity: reason = $reason")
                    mEventListener?.onPositionDiscontinuity(reason)
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                    Log.d(TAG, "onPlaybackParametersChanged: playbackParameters = $playbackParameters")
                    mEventListener?.onPlaybackParametersChanged(playbackParameters)
                }

                override fun onSeekProcessed() {
                    Log.d(TAG, "onSeekProcessed: ")
                    mEventListener?.onSeekProcessed()
                }
            })
        }
    }


    /**
     * 准备工作
     * 设置数据源
     */
    fun prepare(name: String?, url: String) {
        LoggerUtils.d(TAG, "allowPlay : $url")
        var uri = Uri.parse(url)
//        var mediaSource: MediaSource? = null
        var mediaSource = if (url.startsWith("http")) {
            buildHttpMediaSource(uri)
        } else {
            buildMediaSource(uri)
        }
        exoPlayer?.prepare(mediaSource)
//        var lastIndex = url.lastIndexOf("/")
        setVideoSimpleName(name ?: "")
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, "ExoPlayerDemo")).createMediaSource(uri)
    }

    private fun buildHttpMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("ExoPlayerDemo")).createMediaSource(uri)
    }

    /**
     * 暂停
     */
    fun pause() {
        if (exoPlayer != null) {
            exoPlayer?.playWhenReady = false
        }
    }

    /**
     * 播放
     */
    fun play() {
        if (exoPlayer != null) {
            exoPlayer?.playWhenReady = true
        }
    }

    fun isPlaying(): Boolean {
        if (exoPlayer != null) {
            return exoPlayer?.playWhenReady == true
        }
        return false
    }


    /**
     * 停止播放
     */
    fun stop() {
        if (exoPlayer != null) {
            exoPlayer?.stop(true)
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        if (exoPlayer != null) {
            exoPlayer!!.release()
            exoPlayer = null
            playerView = null
        }
    }


    interface OnVideoCompleteListener {
        fun onVideoComplete()
    }

    private var mOnVideoCompleteListener: OnVideoCompleteListener? = null

    fun setOnVideoCompleteListener(onVideoCompleteListener: OnVideoCompleteListener) {
        this.mOnVideoCompleteListener = onVideoCompleteListener
    }

    fun setVideoSimpleName(name: String) {
        playerView?.setVideoSimpleName(name)
    }

    fun setExoPlayerUIListner(exoPlayerUIListener: ExoPlayerControlView.OnViewClickListener) {
        playerView?.setExoPlayerUIListner(exoPlayerUIListener)
    }

    fun setRewind() {
        playerView?.setRewind()
    }

    fun setFastForward() {
        playerView?.setFastForward()
    }


    private var mEventListener: Player.EventListener? = null

    fun setEventListener(eventListener: Player.EventListener) {
        mEventListener = eventListener
    }

    private var onVideoBufferingListenerListener: OnVideoBufferingListener? = null

    interface OnVideoBufferingListener {
        fun onVideoBuffering(isBuffering: Boolean)
    }

    fun setBufferingListener(onVideoBufferingListenerListener: OnVideoBufferingListener) {
        this.onVideoBufferingListenerListener = onVideoBufferingListenerListener
    }

    private var onVideoLoadingListenerListener: OnVideoLoadingListener? = null

    interface OnVideoLoadingListener {
        fun onVideoLoading(isLoading: Boolean)
    }

    fun setLoadingListener(onVideoLoadingListenerListener: OnVideoLoadingListener) {
        this.onVideoLoadingListenerListener = onVideoLoadingListenerListener
    }

    interface PlayerStateListener {
        fun play()
        fun pause()
        fun resume()
        fun complete()
    }

    private var mPlayerStateListener: PlayerStateListener? = null

    fun setPlayerStateListener(mPlayerStateListener: PlayerStateListener?) {
        this.mPlayerStateListener = mPlayerStateListener
    }


}