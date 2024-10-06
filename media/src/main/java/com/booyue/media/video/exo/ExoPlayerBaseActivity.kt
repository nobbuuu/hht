package com.booyue.media.video.exo

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.booyue.base.activity.BaseActivity
import com.booyue.base.listener.ScreenListener
import com.booyue.base.toast.Tips
import com.booyue.base.util.LoggerUtils
import com.booyue.media.MediaConfig
import com.booyue.media.MediaConstant
import com.booyue.media.bean.VideoBean
import com.google.android.exoplayer2.util.RepeatModeUtil
import kotlinx.android.synthetic.main.video_control_view.view.*
import java.util.*

/**
 * @author: wangxinhua
 * @date: 2018/12/22
 * @description : exo播放器页面基类
 */
abstract class ExoPlayerBaseActivity<T : VideoBean> : BaseActivity() {
    companion object {
        val TAG = "ExoPlayerBaseActivity"
    }

    var videoList: ArrayList<T>? = null
    var mPosition: Int = 0
    var mBean: T? = null
    var booyueExoPlayer: ExoPlayer? = null
    var screenListener: ScreenListener? = null
    var localUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE)//不显示程序的标题栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//设置全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)

    }


    override fun initListener() {
        /**
         * 设置屏幕息屏监听
         */
        screenListener = ScreenListener(this)
        screenListener?.begin(object : ScreenListener.ScreenStateListener {
            override fun onUserPresent() {
                LoggerUtils.d(TAG, "onUserPresent: ")
            }

            override fun onScreenOn() {
                LoggerUtils.d(TAG, "onScreenOn: ")
            }

            override fun onScreenOff() {
                LoggerUtils.d(TAG, "onScreenOff: ")
                if (booyueExoPlayer != null && booyueExoPlayer?.isPlaying()!!) {
                    booyueExoPlayer?.pause()
                }
            }
        })
    }

    /**
     * 重写此方法，调用父类的initData,然后调用initPlayer
     */
    override fun initData() {
        /**
         * 传递过来的数据
         */
        val intent = intent
        var bundle = intent.extras
        videoList = bundle?.getParcelableArrayList<T>(MediaConstant.INTENT_KEY_LIST)
        if (videoList == null) {
            return
        }
        var pos = bundle?.getInt(MediaConstant.INTENT_KEY_POSITION) ?: 0
        mPosition = if (pos < 0 && pos >= videoList!!.size) 0 else pos
        mBean = videoList!![mPosition]
    }

    fun initPlayer(playerView: ExoPlayerView, onViewClickListener: ExoPlayerControlView.OnViewClickListener) {
        booyueExoPlayer = ExoPlayer.instance
        booyueExoPlayer?.apply {
            initializePlayer(this@ExoPlayerBaseActivity)
            setPlayerParameter(playerView)
            playerView.setFastForwardIncrementMs(100)
            playerView.setRewindIncrementMs(100)
            playerView.setRepeatToggleModes(RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL)
            setExoPlayerUIListner(object : ExoPlayerControlView.OnViewClickListener {
                override fun onNumberButtonClick() {
                    onViewClickListener?.onNumberButtonClick()

                }

                override fun onBackButtonClick() {
                    onViewClickListener.onBackButtonClick()
                }

                override fun onPrevClick() {
                    LoggerUtils.d(TAG, "onPrevClick: ")
                    previous()
                    onViewClickListener.onPrevClick()
                }

                override fun onNextClick() {
                    LoggerUtils.d(TAG, "onNextClick: ")
                    next(true)
                    onViewClickListener.onNextClick()
                }
            })

            setOnVideoCompleteListener(object : ExoPlayer.OnVideoCompleteListener {
                override fun onVideoComplete() {
                    next(false)
                }
            })
            setBufferingListener(object : ExoPlayer.OnVideoBufferingListener {
                override fun onVideoBuffering(isBuffering: Boolean) {
                    LoggerUtils.d(TAG, "isBuffering: $isBuffering")
                    if (isBuffering) {
                        showLoadingDialog("正在缓冲中...")
                    } else {
                        hideLoadingDialog()
                    }
                }
            })
            setLoadingListener(object : ExoPlayer.OnVideoLoadingListener {
                override fun onVideoLoading(isLoading: Boolean) {
                    LoggerUtils.d(TAG, "isLoading: $isLoading")
                    if (isLoading) {
                        showLoadingDialog("正在加载中...")
                    } else {
                        hideLoadingDialog()
                    }
                }
            })
        }
        preparePlay()
    }

    abstract fun showLoadingDialog(s: String)
    abstract fun hideLoadingDialog()

    /**
     * 播放下一首
     * @param manual 是否手动触发，
     */
    fun next(manual: Boolean) {
        LoggerUtils.d(TAG, "next(): manual $manual")
        //自动播放完了，循环播放
        if (ExoPlayerControlView.REPEAT_MODE_ONE == ExoPlayerControlView.sRepeatMode && !manual) {

        } else if (ExoPlayerControlView.REPEAT_MODE_RANDOM == ExoPlayerControlView.sRepeatMode) {
            if (videoList!!.size == 1) {
                Tips.show("没有更多视频了")
                return
            }
            mPosition = Random().nextInt(videoList!!.size)
            LoggerUtils.d(TAG, ": 随机播放 $mPosition")
        } else {
            mPosition++
            if (mPosition >= videoList!!.size) {
                mPosition = videoList!!.size - 1
                //如果用户手动切换下一集，就进行提示，否则不提示
                Tips.show("没有更多视频了")
                return
            }
        }
        preparePlay()
    }

    /**
     * 准备播放
     */
    private fun preparePlay() {
        LoggerUtils.d(TAG, ": requestPlay $mPosition")
        requestPlay(object : RequestPlayCallback {
            override fun allow() {
                prepare()
            }

            override fun deny() {
                exit()
            }
        })
    }

    /**
     * 播放上一首
     */
    fun previous() {
        mPosition--
        if (mPosition < 0) {
            mPosition = 0
            Tips.show("没有更多视频了")
            return
        }
        preparePlay()
    }


    public fun finishAfterDialog() {
        Handler().postDelayed({ finish() }, 1200)
    }

    /**
     * 获取当前正在播放的视频
     */
    private fun getCurrentPlayVideo(): VideoBean? {
        return videoList?.get(mPosition)
    }

//    /**
//     * 获取当前需要播放视频的地址，用户可以重写此方法，获取本地地址
//     */
//    open fun getCurrentPlayUrl(){
//        localUrl = getCurrentPlayVideo()?.path
//    }

    /**
     * 判断是否是网络地址
     */
    fun isNetUrl(): Boolean {
        return localUrl!!.startsWith("http")
    }

    /**
     * 开始播放
     */
    fun prepare() {
        var name = videoList?.get(mPosition)?.name
        if (isNetUrl()) {
            booyueExoPlayer?.prepare(name, localUrl!! + MediaConfig.ServerParams)
        } else {
            booyueExoPlayer?.prepare(name, localUrl!!)
        }
        onPrepared()
    }

    fun prepare(bean: VideoBean) {
        val name = bean.name
        if (isNetUrl()) {
            booyueExoPlayer?.prepare(name, bean.url + MediaConfig.ServerParams)
        } else {
            booyueExoPlayer?.prepare(name, localUrl!!)
        }
        onPrepared()
    }

    abstract fun onPrepared()
    abstract fun requestPlay(callback: RequestPlayCallback)

    interface RequestPlayCallback {
        fun allow()
        fun deny()
    }


    override fun onRestart() {
        super.onRestart()
        booyueExoPlayer?.setFastForward()
    }


    override fun onResume() {
        super.onResume()
        booyueExoPlayer?.play()
    }


    override fun onPause() {
        super.onPause()
        booyueExoPlayer?.pause()
    }

    override fun onDestroy() {
        booyueExoPlayer?.release()
        screenListener?.unregisterListener()
        super.onDestroy()
    }

    /**
     * 按键监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

}
