package com.booyue.poetry.ui.player

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import com.booyue.base.toast.Tips
import com.booyue.base.util.AppUtils
import com.booyue.base.util.LoggerUtils
import com.booyue.base.util.NetWorkUtils
import com.booyue.media.MediaConstant
import com.booyue.media.bean.VideoBean
import com.booyue.media.video.ijk.HHTVideoPlayerBaseActivity
import com.booyue.media.video.ijk.HHTVideoPlayerControllerExt
import com.booyue.poetry.R


/**
 * @author: wangxinhua
 * @date: 2020/3/10 14:23
 * @description :
 */
class NiceVideoPlayerActivity : HHTVideoPlayerBaseActivity<VideoBean>() {
    override fun onVideoPlayerStart() {
    }

    override fun setView() {
    }

    override fun initView() {

    }
//
//    private var hhtSwipeRefreshLayout: HHTSwipeRecyclerView? = null
//    private var mVideoAdapter: DownloadOptionAdapter? = null

    override fun initContentView() {
        setContentView(R.layout.activity_video_player)
//        hhtSwipeRefreshLayout = findViewById(R.id.hhtSwipeRefreshLayout)
        mVideoPlayer = findViewById(R.id.hhtVideoPlayer)
        //不保存上次进入继续播放
        mVideoPlayer!!.continueFromLastPosition(false)
        mVideoControllView = HHTVideoPlayerControllerExt(this)
        if (mVideoControllView is HHTVideoPlayerControllerExt) {
            (mVideoControllView as HHTVideoPlayerControllerExt).setOnCustomViewClickListener(
                    object : HHTVideoPlayerControllerExt.OnCustomViewClickListener {
                        override fun onBackClick() {
                            exit()
                        }

                        override fun onPrevClick() {
                            previous()
                        }

                        override fun onNextClick() {
                            next(true)
                        }
                    })
        }
    }

    override fun initData() {
        super.initData()
//        mVideoAdapter = DownloadOptionAdapter(this, videoList)
//        mVideoAdapter!!.setNiceVideoPlayerActivity(true)
//        hhtSwipeRefreshLayout!!.initLinearAdapter(mVideoAdapter)
//        hhtSwipeRefreshLayout!!.setPage(1, 1)
        //////
//        mVideoAdapter!!.setOnItemClickLinster { view, pos, t ->
//            mPosition = pos
//            mVideoAdapter!!.setPosition(mPosition)
//            requestPlay()
//        }
        requestPlay()
    }

    companion object {

        /**
         * 启动当前页的接口在{@link VideoPlayerActivity}
         */
        fun launch(context: Context, mVideoList: ArrayList<VideoBean>, position: Int, source: String) {
            val i = Intent(context, NiceVideoPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelableArrayList(MediaConstant.INTENT_KEY_LIST, mVideoList as ArrayList<out Parcelable>)
            bundle.putInt(MediaConstant.INTENT_KEY_POSITION, position)
            i.putExtras(bundle)
            if (!AppUtils.isAppOnForeground()) {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            } else {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(i)
        }


        /**
         * 启动当前页面
         */
        fun launch(context: Activity, mVideoList: ArrayList<VideoBean>, position: Int, source: String, isLocal: Boolean, requestCode: Int = 0) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(MediaConstant.INTENT_KEY_LIST, mVideoList as ArrayList<out Parcelable>)
            bundle.putInt(MediaConstant.INTENT_KEY_POSITION, position)
            if (mVideoList.size <= position) {
                return
            }
            val intent = Intent(context, NiceVideoPlayerActivity::class.java)
            intent.putExtras(bundle)
            if (requestCode > 0) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
            context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
        }
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
        LoggerUtils.d(TAG, "url: $localUrl")
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
                callback.allow(localUrl!!)
            }
            //本地资源
        } else {
            callback.allow(localUrl!!)
        }
    }

}
