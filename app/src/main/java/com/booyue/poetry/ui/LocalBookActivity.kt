package com.booyue.poetry.ui


import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.listener.OnDownloadListenerAdapter
import com.azhon.appupdate.manager.DownloadManager
import com.booyue.base.activity.BaseActivity
import com.booyue.base.app.ProjectInit
import com.booyue.base.toast.Tips
import com.booyue.base.util.*
import com.booyue.base.util.LoggerUtils.d
import com.booyue.database.download.Conf
import com.booyue.database.greendao.bean.DownloadBean
import com.booyue.database.greendao.dao.DownloadDao
import com.booyue.database.greendao.gen.DownloadBeanDao
import com.booyue.net.callback.ModelCallback
import com.booyue.poetry.MyApp
import com.booyue.poetry.R
import com.booyue.poetry.adapter.BookAdapter
import com.booyue.poetry.adapter.LocalBookAdapter
import com.booyue.poetry.api.ResourceApi
import com.booyue.poetry.bean.ApkBean
import com.booyue.poetry.bean.CheckUpgradeBean2
import com.booyue.poetry.bean.SearchResultBean
import com.booyue.poetry.listener.PublickTokenCallback
import com.booyue.poetry.statistic.UmengStatisticManager
import com.booyue.poetry.ui.download.DownloadActivity
import com.booyue.poetry.utils.DialogUtil
import com.booyue.poetry.utils.UpdataAPKDialog
import com.booyue.poetry.view.BookView
import com.booyue.poetry.view.HHTSwipeRecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_local_book.*
import kotlinx.android.synthetic.main.layout_edit.*
import org.greenrobot.greendao.query.WhereCondition
import java.io.File
import java.util.*

/**
 * @author: wangxinhua
 * @date: 2020/6/19 10:19
 * @description :
 */
class LocalBookActivity : BaseActivity() {
    companion object {
        val TAG = "LocalBookActivity"
        val REQUEST_CODE_CHOOSE_DOWNLOAD_DIRECTORY = 100
        var checkVersionTips = true
    }

    private var bookBeanList: MutableList<SearchResultBean.BookBean>? = null
    private var bookAdapter: BookAdapter? = null

    private var state = BookView.STATE_NORMAL
    override fun setView() {
        setContentView(R.layout.activity_local_book)
    }

    override fun initView() {
        UmengStatisticManager.showPage("首页")
        ResourceApi.getPublicToken(object : PublickTokenCallback {
            override fun onFailed() {

            }

            override fun onSuccess() {
                checkVersion()
            }

        })
    }

    private var checkAll = false
    override fun initListener() {
        ibDownloadList.setOnClickListener { view ->
            UmengStatisticManager.clickDownloadList("首页")
            jumpTo(DownloadActivity::class.java)
        }
        ibEdit.setOnClickListener { view ->
            initState(true)
            UmengStatisticManager.clickEdit("首页")
        }
        for (i in 0 until 3) {
            d(TAG, "initListener: $i")
        }
        ibDelete.setOnClickListener {
            var size = bookAdapter?.checkList!!.size()
            var needDeleteList = mutableListOf<DownloadBean>()
            for (i in 0 until size) {
                var check = bookAdapter?.checkList!!.get(i)
                if (check) {
                    var bookBean = bookAdapter?.getList()!!.get(i)
                    var conditionList: MutableList<WhereCondition>? = mutableListOf()
                    var subjectCondition = DownloadBeanDao.Properties.Subject.eq(bookBean!!.subject)
                    var versionCondition = DownloadBeanDao.Properties.Version.eq(bookBean.version)
                    var gradeCondition = DownloadBeanDao.Properties.Grade.eq(bookBean.grade)
                    var gradeAttrCondition = DownloadBeanDao.Properties.GradeAttr.eq(bookBean.gradeAtt)
                    var finishedCondition = DownloadBeanDao.Properties.IsFinished.eq(1)
                    conditionList!!.add(subjectCondition)
                    conditionList!!.add(versionCondition)
                    conditionList!!.add(gradeCondition)
                    conditionList!!.add(gradeAttrCondition)
                    conditionList!!.add(finishedCondition)
                    var downloadList = DownloadDao.getInstance().query(conditionList)
                    needDeleteList.addAll(downloadList)
                    DownloadDao.getInstance().delete(conditionList)
                }
            }
            //删除文件
            for (downloadBean in needDeleteList) {
                var file = File(downloadBean.localPath)
                if (file != null && file.exists()) {
                    file.delete()
                }
            }
            //删除数据库
            getData()
        }
        tvCheckAll.setOnClickListener {
            checkAll = !checkAll
            bookAdapter?.initCheckList(checkAll, true)
        }
        ibComplete.setOnClickListener {
            initState(true)
        }
        btnSetDownloadPath.setOnClickListener {
            DialogUtil().showSetPathDialog(this)
        }
        SharePreUtil.getInstance().customDownloadPath = Conf.getSaveDir(this)
    }

    override fun initData() {
        getDataApkCode()
        checkVersion()
        bookBeanList = ArrayList()
        bookAdapter = LocalBookAdapter(this, bookBeanList, false)
        hhtSwipeRecyclerViewLocalBook!!.initGridAdapter(bookAdapter, 4, null)
        hhtSwipeRecyclerViewLocalBook!!.setOnRefreshListener(object : HHTSwipeRecyclerView.OnRefreshListener {
            override fun onRefresh(page: Int) {
                hhtSwipeRecyclerViewLocalBook!!.setRefreshing(false)
            }

            override fun onLoadMore(page: Int) {

            }
        })
        bookAdapter!!.setOnItemClickLinster { view, pos, o ->
            val bookBean = o as SearchResultBean.BookBean
            if (!TextUtils.isEmpty(bookBean.grade)) {
                if (state == BookView.STATE_EDIT) {
                    return@setOnItemClickLinster
                }
                val bundle = Bundle()
                bundle.putString(BookDetailActivity.KEY_SUBJECT, bookBean.subject)
                bundle.putString(BookDetailActivity.KEY_GRADE, bookBean.grade)
                bundle.putString(BookDetailActivity.KEY_GRADE_ATTR, bookBean.gradeAtt)
                bundle.putString(BookDetailActivity.KEY_PUBLISHER, bookBean.version)
                bundle.putString(BookDetailActivity.KEY_COVER_IMAGE, bookBean.coverImage)
                jumpTo(bundle, BookDetailActivity::class.java)
            } else {
                if (state == BookView.STATE_EDIT) {
                    initState(true)
                }
                UmengStatisticManager.clickAddBook("首页")
                jumpTo(MainNewActivity::class.java)
            }
        }
        /**
         * 请求权限
         */
        checkRequestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), object : BaseActivity.PermissionListener {
            override fun permissionSuccess() {

            }

            override fun permissionFail() {

            }
        })
        initState(false)

    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * 从服务器获取检测一下apk版本
     */
    private fun getDataApkCode() {
        ResourceApi.getAPKVersion(this.packageName+"-"+ PackageUtil.getVersioncode(), object : ModelCallback<ApkBean>() {
            override fun onSuccess(apkBean: ApkBean) {
                var  json1= Gson().toJson(apkBean)
                if(apkBean.ret == "0"){
                    if(apkBean.content.list != null  && apkBean.content.list.size != 0){
//                        Log.i("TTT", "000000000000000"+ response.content.list[0].apk);
                        if(apkBean.content.list[0].packageName == packageName ){
                            if(apkBean.content.list[0].versionCode > PackageUtil.getVersioncode()){
                                showUpdataAPKDialog(apkBean.content.list[0])
                            }else{
//                                Log.i("TTT","当前是最新版本")
                            }
                        }else{
//                            Log.i("TTT","请求成功，但是当前没有请求到数据，可能是应用市场上没有这个包")
                        }
                    }else{
                        Tips.show("当前包名错误")
                    }
                }else{
                    Tips.show("请求数据失败")
                }
            }

            override fun onError(ret: Int, msg: String) {
                super.onError(ret, msg)
                Tips.show(msg)
                Log.i("TTT","111"+msg);

            }
        })
    }
    /**
     * 检测版本升级
     */
    private fun checkVersion() {
        d(TAG, "checkVersion: $checkVersionTips")
        if (!checkVersionTips) return
        ResourceApi.checkVersion(AppUtils.getVersionCode(MyApp.getInstance()), object : ModelCallback<CheckUpgradeBean2>() {
            override fun onSuccess(response: CheckUpgradeBean2) {
                if (response.isSuccess && !TextUtils.isEmpty(response.data.url)) {
                    showUpgradeDialog(response.data)
                }
            }
        })
    }


    private fun showUpgradeDialog(content: CheckUpgradeBean2.Content) {
        if(UpgradeUtil.downloading)return
        DialogUtil.showUpdateDialog(this, content)
        checkVersionTips = false
        d(TAG, "checkVersionTips: $checkVersionTips")


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
            bookAdapter?.setState(state)
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
     * 获取数据
     */
    private fun getData() {
        bookAdapter?.apply {
            getList().clear()
            notifyDataSetChanged()
        }
        val downloadBeanList = DownloadDao.getInstance().query()
        var bookBeanList = mutableListOf<SearchResultBean.BookBean>()
        if (downloadBeanList.size > 0) {
            val bookList = ArrayList<String>()
            for (i in downloadBeanList.indices) {
                val bookBean = SearchResultBean.BookBean()
                var downloadBean = downloadBeanList[i]
                bookBean.subject = downloadBean.subject
                bookBean.grade = downloadBean.grade
                bookBean.version = downloadBean.version
                bookBean.coverImage = downloadBean.coverImage
                bookBean.gradeAtt = downloadBean.gradeAttr
                val bookPath = (bookBean.subject
                        + "/" + bookBean.grade
                        + "/" + bookBean.gradeAtt
                        + "/" + bookBean.version
                        + "/" + bookBean.coverImage)
                if (!bookList.contains(bookPath)) {
                    bookList.add(bookPath)
                    LoggerUtils.d(TAG, "bookPath: $bookPath ---${downloadBean.title}")
                    bookBeanList!!.add(bookBean)
                }
            }
        }
        d(TAG, "bookBeanList: " + bookBeanList!!.size)
        val bookBean = SearchResultBean.BookBean()
        bookBean.grade = ""
        bookBeanList.add(bookBean)
        bookBeanList.forEachIndexed { index, bookBean ->
            d(TAG, "grade: " + bookBean.grade)
        }
        bookAdapter!!.addAll(bookBeanList, true)
        bookAdapter!!.initCheckList(false, false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    private fun showUpdataAPKDialog(listDTO: ApkBean.ContentDTO.ListDTO?) {
        var updataAPKDialog = UpdataAPKDialog(this);
        updataAPKDialog.setOnDialogClickListener {
            if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
                Tips.show(com.booyue.net.R.string.error_check_network)
                return@setOnDialogClickListener
            }else{

            }
            updataAPKDialog.goneButLayout()//隐藏按钮
            updataAPKDialog.visibleNpBar()//打开进度调
            updataAPKDialog.goneUpdataView()//升级的时候需要更改值

            it.progress = 0

            val listenerAdapter: OnDownloadListenerAdapter = object : OnDownloadListenerAdapter() {
                override fun downloading(max: Int, progress: Int) {
                    val curr = (progress / max.toDouble() * 100.0).toInt()
                    it.max = 100
                    it.progress = curr

                    if(curr >= 100){
                        updataAPKDialog.visibleButLayout()//打开按钮
                        updataAPKDialog.goneNpBar()//隐藏进度调
                        updataAPKDialog.visibleUpdataView()//升级结束 更改回来值
                        // 杀掉APP
                    }
                }
            }
            val configuration = UpdateConfiguration();
            configuration.setOnDownloadListener(listenerAdapter);


//            configuration.onDownloadListener = listenerAdapter
            val manager = DownloadManager.getInstance(this)
            manager.apkName = updataAPKDialog.APkNAME
            manager.apkUrl = listDTO?.apk
            manager.downloadPath = updataAPKDialog.setApkFilePath(this)
            manager.smallIcon = R.mipmap.ic_launcher
            manager.configuration = configuration
//                manager.setDownloadPath();
            //                manager.setDownloadPath();
            manager.download()
        };
        updataAPKDialog.show();
    }
}
