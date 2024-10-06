package com.booyue.poetry.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azhon.appupdate.utils.DensityUtil
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.booyue.database.greendao.dao.DownloadDao
import com.booyue.media.bean.VideoBean
import com.booyue.media.video.exo.ExoPlayerBaseActivity
import com.booyue.media.video.exo.ExoPlayerControlView
import com.booyue.media.video.exo.ExoPlayerView
import com.booyue.poetry.R
import com.booyue.poetry.adapter2.PlayVideoAdapter
import com.booyue.poetry.bean.VideoVo
import com.booyue.poetry.constant.ItemType
import com.booyue.poetry.core.WaterFallItemDecoration
import kotlinx.android.synthetic.main.activity_video_player_exo.*

class CompleteVideoActivity : ExoPlayerBaseActivity<VideoBean>() {
    private val playAdapter = PlayVideoAdapter()

    override fun setView() {
        setContentView(R.layout.activity_download_complete)
    }

    override fun initView() {
        val itemDecoration = WaterFallItemDecoration(DensityUtil.dip2px(this, 14f), DensityUtil.dip2px(this, 25f))
        findViewById<RecyclerView>(R.id.videoRv).apply {
            removeItemDecoration(itemDecoration)
            addItemDecoration(itemDecoration)
            adapter = playAdapter
        }
    }

    override fun initListener() {
        val backIv = findViewById<ImageView>(R.id.backIv)
        backIv.setOnClickListener {
            finish()
        }
        playAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val videoVo = playAdapter.data[i]
            val videoBean = VideoBean()
            videoBean.name = videoVo.name
            videoBean.url = videoVo.url
            mBean = videoBean
            localUrl = videoBean.url
            prepare(videoBean)
        }
    }

    override fun initData() {
        super.initData()
        initPlayer(playerView, object : ExoPlayerControlView.OnViewClickListener {
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
        val completes = DownloadDao.getInstance().queryDownloadComplete()
        LogUtils.dTag("tempTag", GsonUtils.toJson(completes))
        val data = mutableListOf<VideoVo>()
        completes.filter { it.subject.isNullOrEmpty() }.groupBy { it.groupName }.forEach {
            val bean = VideoVo()
            bean.type = ItemType.TYPE_VIDEO_SUB
            bean.name = it.key
            data.add(bean)
            it.value.forEach {
                val videoVo = VideoVo()
                videoVo.id = it.guid
                videoVo.url = it.url
                videoVo.image = it.coverImage
                videoVo.name = it.title
                videoVo.type = ItemType.TYPE_VIDEO_VIDEO
                data.add(videoVo)
            }
        }
        playAdapter.setNewData(data)
    }

    override fun showLoadingDialog(s: String) {
    }

    override fun hideLoadingDialog() {
    }

    override fun onPrepared() {

    }

    override fun requestPlay(callback: RequestPlayCallback) {
        val uid = intent.getLongExtra("uid", 0L)
        val download = DownloadDao.getInstance().queryByGuid(uid.toString())
        val videoNameTv = findViewById<TextView>(R.id.videoNameTv)
        videoNameTv.text = download.title
        val videoBean = VideoBean()
        videoBean.name = download.title
        videoBean.url = download.url
        videoBean.localUrl = download.localPath
        mBean = videoBean
        localUrl = videoBean.url
        callback.allow()
    }
}