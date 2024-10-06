package com.booyue.poetry.ui.fragment

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azhon.appupdate.utils.DensityUtil
import com.blankj.utilcode.util.LogUtils
import com.booyue.base.fragment.BaseFragment
import com.booyue.database.download.Conf
import com.booyue.database.download.DownloadHelper
import com.booyue.database.greendao.dao.DownloadDao
import com.booyue.net.callback.ModelCallback
import com.booyue.poetry.MyApp
import com.booyue.poetry.R
import com.booyue.poetry.adapter2.MainVideoAdapter
import com.booyue.poetry.api.ResourceApi
import com.booyue.poetry.bean.NatureVideoBean
import com.booyue.poetry.bean.VideoVo
import com.booyue.poetry.constant.ItemType
import com.booyue.poetry.constant.VideoType
import com.booyue.poetry.core.WaterFallItemDecoration

class MainVideoFragment(private val videoType: Int) : BaseFragment(), DownloadHelper.DownloadCallback {

    private lateinit var mainRv: RecyclerView
    private lateinit var scrollBar: SeekBar
    private val mAdapter = MainVideoAdapter()
    override fun getCustomView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_main_rv, container, false)
        mainRv = view.findViewById(R.id.mainRv)
        scrollBar = view.findViewById(R.id.scrollBar)
        return view
    }

    override fun initView() {
        MyApp.getInstance().downloadHelper.register(this)
        val manager = GridLayoutManager(requireContext(), 4)
        manager.spanSizeLookup = mAdapter.spanSize
        val itemDecoration = WaterFallItemDecoration(DensityUtil.dip2px(requireContext(), 14f), DensityUtil.dip2px(requireContext(), 25f))
        mainRv.apply {
            removeItemDecoration(itemDecoration)
            addItemDecoration(itemDecoration)
            layoutManager = manager
            adapter = mAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (videoType == VideoType.TYPE_VIDEO_NATURE) {
            ResourceApi.requestNatureData(object : ModelCallback<NatureVideoBean>() {
                override fun onSuccess(response: NatureVideoBean) {
                    LogUtils.dTag("tempTag", "code = " + response.code)
                    val dataList = mutableListOf<VideoVo>()
                    response.groupList.forEach { group ->
                        val subBean = VideoVo()
                        subBean.type = ItemType.TYPE_VIDEO_SUB
                        subBean.subImg = R.mipmap.nature_video_sub
                        dataList.add(subBean)
                        group.videoVoList.forEach { videoBean ->
                            videoBean.type = ItemType.TYPE_VIDEO_VIDEO
                            videoBean.groupName = group.groupName
                            videoBean.groupId = group.groupId
                            val taskByid = DownloadDao.getInstance().queryByGuid(videoBean.id.toString())
                            LogUtils.dTag("tempTag", "taskByid = $taskByid")
                            taskByid?.let {
                                videoBean.status = it.state
                                videoBean.percent = it.percent
                            }
                            dataList.add(videoBean)
                        }
                    }
                    mAdapter.setNewData(dataList)
                }

                override fun onError(ret: Int, msg: String?) {
                    LogUtils.dTag("tempTag", "ret = $ret; msg = $msg")
                    super.onError(ret, msg)
                }
            })
        } else {
            ResourceApi.requestScienceData(object : ModelCallback<NatureVideoBean>() {
                override fun onSuccess(response: NatureVideoBean) {
                    LogUtils.dTag("tempTag", "code = " + response.code)
                    val dataList = mutableListOf<VideoVo>()
                    response.groupList.forEach { group ->
                        val subBean = VideoVo()
                        subBean.type = ItemType.TYPE_VIDEO_SUB
                        subBean.subImg = R.mipmap.science_video_sub
                        dataList.add(subBean)
                        group.videoVoList.forEach { videoBean ->
                            videoBean.type = ItemType.TYPE_VIDEO_VIDEO
                            videoBean.groupName = group.groupName
                            videoBean.groupId = group.groupId
                            val taskByid = DownloadDao.getInstance().queryByGuid(videoBean.id.toString())
                            LogUtils.dTag("tempTag", "taskByid = $taskByid")
                            taskByid?.let {
                                videoBean.status = it.state
                                videoBean.percent = it.percent
                            }
                            dataList.add(videoBean)
                        }
                    }
                    mAdapter.setNewData(dataList)
                }

                override fun onError(ret: Int, msg: String?) {
                    LogUtils.dTag("tempTag", "ret = $ret; msg = $msg")
                    super.onError(ret, msg)
                }
            })
        }
    }

    override fun initData() {
    }

    override fun onCallback(message: Message) {
        val guid = message.obj as Long
        if (message.what == Conf.STATE_TASK_COMPLETE) {
            LogUtils.d(TAG, "onCallback: $guid Task complete")
            MyApp.getInstance().downloadHelper.removeCompleteTask(guid)
            val item = mAdapter.data.find { it.id == guid }
            item?.status = Conf.STATE_TASK_COMPLETE
            val index = mAdapter.data.indexOf(item)
            mAdapter.notifyItemChanged(index)
        } else {
            val bean = MyApp.getInstance().downloadHelper.getTaskByid(guid)
            val item = mAdapter.data.find { it.id == guid }
            item?.percent = bean.percent
            LogUtils.d(TAG, "bean.percent ${bean.percent}")
            val index = mAdapter.data.indexOf(item)
            mAdapter.notifyItemChanged(index)
        }
    }

    override fun onDestroy() {
        MyApp.getInstance().downloadHelper.unRegister(this)
        super.onDestroy()
    }

}