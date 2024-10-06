package com.booyue.poetry.ui

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.booyue.base.toast.Tips
import com.booyue.base.util.LoggerUtils
import com.booyue.database.greendao.bean.DownloadBean
import com.booyue.database.greendao.dao.DownloadDao
import com.booyue.database.greendao.gen.DownloadBeanDao
import com.booyue.media.bean.VideoBean
import com.booyue.poetry.R
import com.booyue.poetry.adapter.BookDetailAdapter
import com.booyue.poetry.model.MainModel
import com.booyue.poetry.statistic.UmengStatisticManager
import com.booyue.poetry.ui.dialog.DownloadOptionDialog
import com.booyue.poetry.ui.player.ExoVideoPlayerActivity
import com.booyue.poetry.utils.DialogUtil
import com.booyue.poetry.view.BookView
import com.booyue.poetry.view.HHTSwipeRecyclerView
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_header_base.*
import kotlinx.android.synthetic.main.layout_edit.*
import org.greenrobot.greendao.query.WhereCondition
import java.io.File
import java.util.*

/**
 * @author: wangxinhua
 * @date: 2020/6/16 19:38
 * @description : 书本详情页面
 */
class BookDetailActivity : HeaderBaseActivity() {
    companion object {
        val TAG = "BookDetailActivity"
        val KEY_SUBJECT = "subject"
        val KEY_GRADE = "grade"
        val KEY_GRADE_ATTR = "grade_attr"
        val KEY_PUBLISHER = "publisher"
        val KEY_COVER_IMAGE = "coverImage"
    }

    private var subject = ""
    private var grade = ""
    private var gradeAttr = ""
    private var publisher = ""
    private var coverImage = ""
    private var downloadBeanList: MutableList<DownloadBean>? = null
    private var bookDetailAdapter: BookDetailAdapter? = null

    private var videoList: ArrayList<VideoBean>? = null

    private var state = BookView.STATE_NORMAL
    override fun initContentView(): View {
        return layoutInflater.inflate(R.layout.activity_book_detail, null)
    }

    override fun getPageTitle(): String {
        return "书本详情"
    }

    override fun initView() {
        super.initView()
        initState(false)
    }

    private var checkAll = false
    override fun initListener() {
        /**
         * 编辑按钮点击监听
         */
        ibEdit.setOnClickListener {
            initState(true)
            var from = "$grade/$subject/$publisher/$gradeAttr"
            UmengStatisticManager.clickEdit(from)
        }
        /**
         * 添加按钮点击监听
         */
        ibAdd.setOnClickListener {
            MainModel().searchCourse(subject, grade, publisher, gradeAttr, 1) { dataBean1 ->
                val downloadOptionDialog = DownloadOptionDialog.show(this)
                downloadOptionDialog.setData(dataBean1[0])
            }
            var from = "$grade/$subject/$publisher/$gradeAttr"
            UmengStatisticManager.clickAddBook(from)
        }
        /**
         * 删除按钮点击监听
         */
        ibDelete.setOnClickListener {
            var checkList = mutableListOf<DownloadBean>()
            bookDetailAdapter?.checkList!!.forEachIndexed { index, stateBean ->
                if (stateBean.checked) {
                    checkList.add(downloadBeanList!![index])
                }
            }
            if (checkList.size < 0) {
                Tips.show(R.string.tips_choose_delete)
                return@setOnClickListener
            }
            DialogUtil.showConfirmDialog(this@BookDetailActivity, getString(R.string.dialog_delete_download_title, checkList.size)) {
                deleteVideo()
            }
        }
        /**
         * 完成按钮点击监听
         */
        ibComplete.setOnClickListener {
            initState(true)
        }
        /**
         * 全选按钮点击监听
         */
        tvCheckAll.setOnClickListener {
            checkAll = !checkAll
            bookDetailAdapter?.checkAll(checkAll)
        }
        hhtSwipeRecyclerView.setOnRefreshListener(object : HHTSwipeRecyclerView.OnRefreshListener {
            override fun onRefresh(page: Int) {
                getBookDetail()
            }

            override fun onLoadMore(page: Int) {

            }
        })
        bookDetailAdapter!!.setOnItemClickLinster { view, pos, t ->
            if (state == BookView.STATE_NORMAL) {
                downloadBean2VideoBean()
                ExoVideoPlayerActivity.launch(this, videoList!!, pos)
            }
        }
        bookDetailAdapter!!.setOnItemLongClickLinster {
            initState(true)
        }


    }

    fun deleteVideo() {
        var checkList = mutableListOf<DownloadBean>()
        bookDetailAdapter?.checkList!!.forEachIndexed { index, stateBean ->
            if (stateBean.checked) {
                checkList.add(downloadBeanList!![index])
            }
        }
        if (checkList.size <= 0) return
        //删除文件
        for (downloadBean in checkList) {
            var file = File(downloadBean.localPath)
            if (file != null && file.exists()) {
                var deleteSuccess = file.delete()
                LoggerUtils.d(TAG, "deleteSuccess  = $deleteSuccess")
            }
        }
        DownloadDao.getInstance().deleteList(checkList)
        getBookDetail()
    }

    //    private var videoList: ArrayList<VideoBean>? = null
    override fun initData() {
        var bundle = intent.extras
        LoggerUtils.d(TAG, "bundle: $bundle")
        subject = bundle.getString(KEY_SUBJECT)
        grade = bundle.getString(KEY_GRADE)
        gradeAttr = bundle.getString(KEY_GRADE_ATTR)
        publisher = bundle.getString(KEY_PUBLISHER)
        coverImage = bundle.getString(KEY_COVER_IMAGE)
        downloadBeanList = mutableListOf()
        videoList = arrayListOf()
        bookDetailAdapter = BookDetailAdapter(this, downloadBeanList, false)
        hhtSwipeRecyclerView.initLinearAdapter(bookDetailAdapter, false)
        getBookDetail()
        tvTitle.text = getBookTitle()
    }

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    private fun getBookDetail() {
        var conditionList: MutableList<WhereCondition>? = mutableListOf()
        var subjectCondition = DownloadBeanDao.Properties.Subject.eq(subject)
        var versionCondition = DownloadBeanDao.Properties.Version.eq(publisher)
        var gradeCondition = DownloadBeanDao.Properties.Grade.eq(grade)
        if (!TextUtils.isEmpty(gradeAttr)) {
            var gradeAttrCondition = DownloadBeanDao.Properties.GradeAttr.eq(gradeAttr)
            conditionList!!.add(gradeAttrCondition)
        }
        var finishedCondition = DownloadBeanDao.Properties.IsFinished.eq(1)
        conditionList!!.add(subjectCondition)
        conditionList!!.add(versionCondition)
        conditionList!!.add(gradeCondition)
        conditionList!!.add(finishedCondition)
        var downloadList = DownloadDao.getInstance().query(conditionList)
        Collections.sort(downloadList, object : java.util.Comparator<DownloadBean> {
            override fun compare(p0: DownloadBean?, p1: DownloadBean?): Int {
                return p0!!.unit.compareTo(p1!!.unit)
            }
        })

        tvBookName.text = getBookTitle()
        tvBookPublisher.text = "$publisher"
        tvVideoNum.text = getString(R.string.sub_name_video_num, "${downloadList.size}")
        bookView.loadImage(coverImage)
        bookDetailAdapter?.initStateList(downloadList)
        bookDetailAdapter?.addAll(downloadList, true)
        hhtSwipeRecyclerView?.setRefreshing(false)
    }

    /**
     * 获取标题
     */
    private fun getBookTitle(): String {
        var gradeAttrStr = if (TextUtils.isEmpty(gradeAttr)) "" else "(${gradeAttr}册)"
        return "$subject$grade$gradeAttrStr"
    }

    /**
     * 根据状态初始化页面
     */
    private fun initState(change: Boolean) {
        if (change) {
            if (state == BookView.STATE_EDIT) {
                state = BookView.STATE_NORMAL
                containerEdit.visibility = View.GONE
                containerNormal.visibility = View.VISIBLE
            } else {
                state = BookView.STATE_EDIT
                containerEdit.visibility = View.VISIBLE
                containerNormal.visibility = View.GONE
            }
            bookDetailAdapter?.setState(state)
        } else {
            if (state == BookView.STATE_NORMAL) {
                containerEdit.visibility = View.GONE
                containerNormal.visibility = View.VISIBLE
            } else {
                containerEdit.visibility = View.VISIBLE
                containerNormal.visibility = View.GONE
            }
        }
    }

    /**
     * 数据类型转换
     */
    private fun downloadBean2VideoBean() {
        videoList!!.clear()
        bookDetailAdapter!!.getList().forEachIndexed { index, downloadBean ->
            var videoBean = VideoBean()
            videoBean.name = downloadBean.title
            videoBean.localUrl = downloadBean.localPath
            videoBean.url = downloadBean.url
            videoList!!.add(videoBean)
        }
    }
}