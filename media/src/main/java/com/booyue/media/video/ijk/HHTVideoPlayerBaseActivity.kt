package com.booyue.media.video.ijk

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import com.booyue.base.listener.ScreenListener
import com.booyue.base.toast.Tips
import com.booyue.base.util.LoggerUtils
import com.booyue.media.MediaConfig
import com.booyue.media.MediaConstant
import com.booyue.media.bean.VideoBean
import com.booyue.media.video.exo.ExoPlayerControlView
import com.booyue.media.video.ijk.base.CompatHomeKeyActivity
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import java.util.*

/**
 * @author: wangxinhua
 * @date: 2018/12/22
 * @description : exo播放器页面基类
 */
abstract class HHTVideoPlayerBaseActivity<T : VideoBean> : CompatHomeKeyActivity(), HHTVideoPlayerController.OnPlayerStateChangeListener {
    companion object {
        val TAG = "HHTVideoPlayerBaseActivity"
    }

    var videoList: ArrayList<T>? = null
    var mPosition: Int = 0
    var mBean: T? = null
    var mVideoPlayer: NiceVideoPlayer? = null
    var mVideoControllView: HHTVideoPlayerController? = null
    var screenListener: ScreenListener? = null
    var localUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE)//不显示程序的标题栏
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//设置全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        //设置布局
        initContentView()
        //释放焦点
        initListener()
        initData()
    }


    abstract fun initContentView()
    override fun onConfigurationChanged(newConfig: Configuration) {

        LoggerUtils.d(TAG, "onConfigurationChanged: " + newConfig?.orientation)
//        var mConfiguration = this@HHTVideoPlayerBaseActivity.resources.configuration //获取设置的配置信息
//        var ori = mConfiguration.orientation //获取屏幕方向
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
            mVideoPlayer?.enterFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
            mVideoPlayer?.exitFullScreen()
        }
        super.onConfigurationChanged(newConfig)

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
                if (mVideoPlayer != null && mVideoPlayer?.isPlaying!!) {
                    mVideoPlayer?.pause()
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
        initPlayer()
    }

    private fun initPlayer() {
        if (mVideoPlayer == null) return
        mVideoPlayer!!.setPlayerType(NiceVideoPlayer.TYPE_IJK) // IjkPlayer or MediaPlayer
    }

    /**
     * 开始播放
     */
    fun start(name: String, path: String) {
        if (mVideoPlayer == null) {
            return
        }
        mVideoPlayer!!.releasePlayer()
        mVideoControllView!!.setTitle(name)
//        controller!!.imageView().setImageResource(com.booyue.babylisten.R.drawable.default_image)
        mVideoPlayer!!.setController(mVideoControllView)
        mVideoPlayer!!.setUp(path, null)
        mVideoPlayer!!.start()
    }

    /**
     * 播放下一首
     * @param manual 是否手动触发，
     */
    fun next(manual: Boolean) {
        LoggerUtils.d(TAG, "next(): manual $manual")
        //自动播放完了，循环播放
        if (!manual && (ExoPlayerControlView.REPEAT_MODE_ONE == ExoPlayerControlView.sRepeatMode)) {
            LoggerUtils.d(TAG, ": 单集循环")

        } else if (ExoPlayerControlView.REPEAT_MODE_RANDOM == ExoPlayerControlView.sRepeatMode) {
            mPosition = Random().nextInt(videoList!!.size)
            LoggerUtils.d(TAG, ": 随机播放 $mPosition")
        } else {
            mPosition++
            if (mPosition >= videoList!!.size) {
//                mPosition = 0
                //如果用户手动切换下一集，就进行提示，否则不提示
                if (manual) {
                    Tips.show("这是最后一集")
                }
                return
            }
        }
        requestPlay()
    }


    /**
     * 准备播放
     */
    fun requestPlay() {
        LoggerUtils.d(TAG, ": requestPlay $mPosition")
        requestPlay(object : RequestPlayCallback {
            override fun allow(url:String) {
                allowPlay(url)
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
            mPosition = videoList?.size!! - 1
        }
        requestPlay()
    }

    /**
     * 获取当前正在播放的视频
     */
    private fun getCurrentPlayVideo(): VideoBean? {
        return videoList?.get(mPosition)
    }

    /**
     * 判断是否是网络地址
     */
    fun isNetUrl(): Boolean {
        return localUrl!!.startsWith("http")
    }

    /**
     * 数据是否无效
     * @return
     */
    fun isInvalide(): Boolean {
        return videoList == null || videoList!!.size <= 0 || videoList!!.size <= mPosition
    }

    /**
     * 开始播放
     */
    fun allowPlay(url:String) {
        var name = videoList?.get(mPosition)?.name
//        localUrl = videoList?.get(mPosition)?.url
        if (isNetUrl()) {
            start(name!!, url!! + MediaConfig.ServerParams)
        } else {
            start(name!!, url!!)
        }
        onVideoPlayerStart()
    }

    /**
     * 通知用户正在播放视频，需要处理相关逻辑，例如：加入播放历史，发送正在播放广播，初始化喜爱图标
     */
    abstract fun onVideoPlayerStart()

    /**
     * 重写此方法，判断是否满足播放条件
     */
    abstract fun requestPlay(callback: RequestPlayCallback)

    interface RequestPlayCallback {
        fun allow(url:String)
        fun deny()
    }

    override fun onDestroy() {
        screenListener?.unregisterListener()
        super.onDestroy()
    }

    /**
     * 按键监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LoggerUtils.d(TAG, "onKeyDown: $keyCode")
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            var exitFullScreen = NiceVideoPlayerManager.instance().onBackPressd()
            LoggerUtils.d(TAG, "exitFullScreen: $exitFullScreen")
            if (!exitFullScreen) {
                finish()
            }else{
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onStart() {
        super.onStart()
        mVideoControllView?.setOnPlayerStateChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        mVideoControllView?.setOnPlayerStateChangeListener(null)
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
    }

//    override fun onBackPressed() {
//        var exitFullScreen = NiceVideoPlayerManager.instance().onBackPressd()
//        LoggerUtils.d(TAG, "exitFullScreen: $exitFullScreen")
//        if (exitFullScreen) return
//        super.onBackPressed()
//    }

    /**
     * 播放状态回调
     */
    override fun onPlayerIdle() {
        LoggerUtils.d(TAG, "onPlayerIdle: ")

    }

    override fun onPlayerPreparing() {
        LoggerUtils.d(TAG, "onPlayerPreparing: ")
    }

    override fun onPlayerPrepared() {
        LoggerUtils.d(TAG, "onPlayerPrepared: ")
    }

    override fun onPlayerPlaying() {
        LoggerUtils.d(TAG, "onPlayerPlaying: ")
    }

    override fun onPlayerPaused() {
        LoggerUtils.d(TAG, "onPlayerPaused: ")
    }

    override fun onPlayerBufferingStart() {
        LoggerUtils.d(TAG, "onPlayerBufferingStart: ")
    }

    override fun onPlayerBufferingStop() {
        LoggerUtils.d(TAG, "onPlayerBufferingStop: ")
    }

    override fun onPlayerError() {
        LoggerUtils.d(TAG, "onPlayerError: ")
    }

    override fun onPlayerComplete() {
        LoggerUtils.d(TAG, "onPlayerComplete: ")
        next(false)
    }


}
