package com.booyue.poetry.ui.download

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booyue.base.fragment.BaseFragment
import com.booyue.base.util.LoggerUtils
import com.booyue.database.download.Conf
import com.booyue.database.download.DownloadHelper
import com.booyue.database.greendao.bean.DownloadBean
import com.booyue.poetry.MyApp
import com.booyue.poetry.R
import com.booyue.poetry.adapter.download.DownloadListAdapter
import com.booyue.poetry.view.HHTSwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_download_list.*

/**
 * @author: wangxinhua
 * @date: 2020/6/16 15:38
 * @description : 下载列表页面
 */
class DownloadListFragment(var state: Int) : BaseFragment(), DownloadHelper.DownloadCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApp.getInstance().downloadHelper.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApp.getInstance().downloadHelper.unRegister(this)
    }

    override fun getCustomView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_download_list, container, false)
    }

    private var hhtRecyclerView: HHTSwipeRecyclerView? = null
    override fun initView() {
        hhtRecyclerView = mRootView.findViewById(R.id.hhtSwipeRecyclerView)
    }

    private var downloadListAdapter: DownloadListAdapter? = null
    private var downloadList: MutableList<DownloadBean>? = null

    override fun initData() {
        downloadList = mutableListOf()
        downloadListAdapter = DownloadListAdapter(mActivity, downloadList, false)
        downloadListAdapter!!.setState(state)
        hhtRecyclerView!!.initLinearAdapter(downloadListAdapter)
        hhtRecyclerView!!.setOnRefreshListener(object : HHTSwipeRecyclerView.OnRefreshListener {
            override fun onRefresh(page: Int) {
                hhtRecyclerView!!.setRefreshing(false)
                getDownloadTask()
            }

            override fun onLoadMore(page: Int) {
            }
        })
        getDownloadTask()
    }

    private fun getDownloadTask() {
        downloadList!!.clear()
        var taskTotal = MyApp.getInstance().downloadHelper.taskTotal
        for (i in 0 until taskTotal) {
            var bean = MyApp.getInstance().downloadHelper.getTaskByPosition(i)
            Log.d(TAG, "add: guid " + bean.guid)
            downloadList!!.add(bean)
        }
        downloadListAdapter?.notifyDataSetChanged()
        hhtSwipeRecyclerView.showContentOnSuccess((resources.getString(R.string.empty_download)))
    }


    /**
     * 更新ui
     */
    @SuppressLint("SetTextI18n")
    private fun updateTaskItemUI(state: Int, guid: Long) {
        val item = getVisibleItemById(guid) ?: return
        var viewHolder = item as DownloadListAdapter.DownloadListViewHolder
        if (item != null && viewHolder!!.btnDownload != null) {
            val bean = MyApp.getInstance().downloadHelper.getTaskByid(guid)
               viewHolder.btnDownload.updateState(bean, state)

        }
    }


    /**
     * 通过guid获取可见的view
     */
    private fun getVisibleItemById(guid: Long): RecyclerView.ViewHolder? {
        var downloadHelper = MyApp.getInstance().downloadHelper
        /**首先校验listview和task是否无效 */
        if (hhtRecyclerView != null && guid > 0) {
            val pos = downloadHelper.getTaskPosition(guid.toInt())
            val first = hhtRecyclerView!!.firstVisibleItemPosition
            val last = hhtRecyclerView!!.lastVisibleItemPosition
            if (pos != -1 && pos >= first && pos <= last) {
                return hhtRecyclerView!!.getViewHolder(pos)
            }

        }
        return null
    }

    override fun onCallback(message: Message) {
        var guid = message.obj as Long
        if (message.what == Conf.STATE_TASK_COMPLETE) {
            LoggerUtils.d(TAG, "onCallback:111 $guid Task complete")
            MyApp.getInstance().downloadHelper.removeCompleteTask(guid)
//            getDownloadTask()
            var deleteItem:DownloadBean? = null
            downloadListAdapter?.apply {
                getList().forEachIndexed { index, downloadBean ->
                    if(downloadBean.guid == guid){
                        deleteItem = downloadBean
                        return@forEachIndexed
                    }
                }
                if(deleteItem != null){
                    getList().remove(deleteItem)
                    notifyDataSetChanged()
                    hhtSwipeRecyclerView.showContentOnSuccess((resources.getString(R.string.empty_download)))
                }
            }
        } else {
            LoggerUtils.d(TAG, "onCallback:111 $guid")
            updateTaskItemUI(message.what, guid)
        }
    }

}