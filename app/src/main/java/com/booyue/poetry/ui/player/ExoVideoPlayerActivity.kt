package com.booyue.poetry.ui.player

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.KeyEvent
import com.booyue.base.toast.Tips
import com.booyue.base.util.LoggerUtils
import com.booyue.base.util.NetWorkUtils
import com.booyue.media.MediaConstant
import com.booyue.media.bean.VideoBean
import com.booyue.media.video.exo.ExoPlayerBaseActivity
import com.booyue.media.video.exo.ExoPlayerControlView
import com.booyue.media.video.ijk.HHTVideoPlayerBaseActivity
import com.booyue.poetry.R
import kotlinx.android.synthetic.main.activity_video_player_exo.*

/**
 * @author: wangxinhua
 * @date: 2020/6/17 14:14
 * @description :
 */
class ExoVideoPlayerActivity: ExoPlayerBaseActivity<VideoBean>() {
    companion object {
        /**
         * 启动当前页面
         */
        fun launch(context: Activity, mVideoList: ArrayList<VideoBean>, position: Int, requestCode: Int = 0) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(MediaConstant.INTENT_KEY_LIST, mVideoList as ArrayList<out Parcelable>)
            bundle.putInt(MediaConstant.INTENT_KEY_POSITION, position)
            if (mVideoList.size <= position) {
                return
            }
            val intent = Intent(context, ExoVideoPlayerActivity::class.java)
            intent.putExtras(bundle)
            if (requestCode > 0) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
            context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
        }
        /**
         * 启动当前页面
         */
        fun launchV(context: Activity, mVideoList: ArrayList<VideoBean>, position: Int) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(MediaConstant.INTENT_KEY_LIST, mVideoList as ArrayList<out Parcelable>)
            bundle.putInt(MediaConstant.INTENT_KEY_POSITION, position)
            if (mVideoList.size <= position) {
                return
            }
            val intent = Intent(context, ExoVideoPlayerActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
        }
    }

    override fun showLoadingDialog(s: String) {

    }

    override fun hideLoadingDialog() {
    }

    override fun onPrepared() {
    }

    override fun requestPlay(callback: RequestPlayCallback) {
        mBean = videoList?.get(mPosition)
        if (mBean == null) {
            return
        }
//        localUrl = MyApp.getMyAppInstance().downloadDao.getLocalPathById(LikeManager.LikeType.TYPE_VIDEO, mBean!!.id.toString() + "")
        localUrl = mBean!!.localUrl
        if (TextUtils.isEmpty(localUrl)) {
            localUrl = mBean!!.url
        }
//        localUrl = "http://resource.alilo.com.cn/res/7,016d663ba2c68b.mp4"
        LoggerUtils.d(HHTVideoPlayerBaseActivity.TAG, "url: $localUrl")
        //播放地址错误
        if (TextUtils.isEmpty(localUrl)) {
            Tips.show("地址错误")
            return
        }
//        localUrl = "https://resource.alilo.com.cn/res/7,016d663ba2c68b.mp4"
        //网络地址
        if (isNetUrl()) {
            //没有网络提示
            if (!NetWorkUtils.isNetWorkAvailable(this)) {
                Tips.show(R.string.error_network)
            } else {
                callback.allow()
            }
            //本地资源
        } else {
            callback.allow()
        }
    }

    override fun setView() {
        setContentView(R.layout.activity_video_player_exo)
    }

    override fun initView() {
    }

    override fun initData() {
        super.initData()
        initPlayer(playerView,object : ExoPlayerControlView.OnViewClickListener{
            override fun onBackButtonClick() {
                exit()
            }

            override fun onPrevClick() {
            }

            override fun onNextClick() {
            }

            override fun onNumberButtonClick() {
            }
        })
    }

}