package com.booyue.poetry.ui.download

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.booyue.base.fragment.BaseFragment
import com.booyue.base.util.LoggerUtils
import com.booyue.database.download.Conf
import com.booyue.database.download.DownloadHelper
import com.booyue.database.greendao.bean.DownloadBean
import com.booyue.database.greendao.dao.DownloadDao
import com.booyue.media.bean.VideoBean
import com.booyue.poetry.MyApp
import com.booyue.poetry.R
import com.booyue.poetry.adapter.download.DownloadListAdapter
import com.booyue.poetry.ui.player.ExoVideoPlayerActivity
import com.booyue.poetry.view.HHTSwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_download_list.*
import org.greenrobot.eventbus.EventBus

/**
 * @author: wangxinhua
 * @date: 2020/6/16 15:38
 * @description : 下载列表页面
 */
class DownloadCompleteFragment(var state: Int) : BaseFragment(), DownloadHelper.DownloadCallback {

    override fun onCallback(message: Message) {
        if (message.what == Conf.STATE_TASK_COMPLETE) {
            MyApp.mHandler.postDelayed({
                updateData()
            }, 500)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MyApp.getInstance().downloadHelper.register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        MyApp.getInstance().downloadHelper.unRegister(this)
        super.onDestroyView()
    }

    override fun getCustomView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_download_list, container, false)
    }


    override fun initView() {

    }

    private var downloadListAdapter: DownloadListAdapter? = null
    private var downloadList: MutableList<DownloadBean>? = null
    private var videoList: ArrayList<VideoBean>? = null
    override fun initData() {
        videoList = arrayListOf()
        downloadList = mutableListOf()
        downloadListAdapter = DownloadListAdapter(mActivity, downloadList, false)
        downloadListAdapter!!.setState(state)
        hhtSwipeRecyclerView!!.initLinearAdapter(downloadListAdapter)
        hhtSwipeRecyclerView!!.setOnRefreshListener(object : HHTSwipeRecyclerView.OnRefreshListener {
            override fun onRefresh(page: Int) {
                hhtSwipeRecyclerView!!.setRefreshing(false)
                updateData()
            }

            override fun onLoadMore(page: Int) {

            }
        })
        downloadListAdapter!!.setOnItemClickLinster { view, pos, t ->
            downloadBean2VideoBean()
            ExoVideoPlayerActivity.launch(mActivity, videoList!!, pos)

        }
        updateData()
    }

    fun updateData() {
        var downloadList = DownloadDao.getInstance().queryDownloadComplete()

        LoggerUtils.d(TAG, "downloadList: " + downloadList!!.size)
        LoggerUtils.d(TAG, "downloadListAdapter: $downloadListAdapter")
        downloadListAdapter?.addAll(downloadList,true)
        try{
            hhtSwipeRecyclerView.showContentOnSuccess("")
        }catch (err:NullPointerException){
            LoggerUtils.d(HHTSwipeRecyclerView.TAG, "异常了:但没事 ")
        }
    }

    fun updataLayoutItem(boolean: Boolean){
        downloadListAdapter?.IsShowDelete(boolean);
        updateData();
    }
    fun getListDownloadBean(): MutableList<DownloadBean>? {
        return downloadListAdapter?.getList();
    }
    fun updateDataIsChack(isChackAll: Boolean) {
        var downloadList = DownloadDao.getInstance().queryDownloadComplete()

        for (i in downloadList.indices) {
            downloadList[i].isChack = isChackAll
        }

        downloadListAdapter?.addAll(downloadList,true)
        try{
            hhtSwipeRecyclerView.showContentOnSuccess("")
        }catch (err:NullPointerException){
            LoggerUtils.d(HHTSwipeRecyclerView.TAG, "异常了:但没事 ")
        }
    }

    private fun downloadBean2VideoBean() {
        videoList!!.clear()
        downloadListAdapter!!.getList().forEachIndexed { index, downloadBean ->
            var videoBean = VideoBean()
            videoBean.name = downloadBean.title
            videoBean.localUrl = downloadBean.localPath
            videoBean.url = downloadBean.url
            videoList!!.add(videoBean)
        }
    }

//    @Override
//    public void onStart(){
//        super.onStart()
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//   }

//    override fun onStart() {
//        super.onStart()
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//                EventBus.getDefault().register(this)
//            }
//    }

}