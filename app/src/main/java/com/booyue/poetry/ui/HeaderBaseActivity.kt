package com.booyue.poetry.ui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.booyue.base.activity.BaseActivity
import com.booyue.poetry.R
import kotlinx.android.synthetic.main.activity_header_base.*

/**
 * @author: wangxinhua
 * @date: 2020/6/16 9:56
 * @description :
 */
abstract class HeaderBaseActivity:BaseActivity(){

    override fun setView() {
       setContentView(R.layout.activity_header_base)
    }

    override fun initView() {
        ivBack.setOnClickListener {
            finish()
        }
        tvTitle.text = getPageTitle()
        var view = initContentView()
        replaceLayout(view)
    }
    fun setHeaderGone(){
        head_view.visibility = View.GONE
    }
    fun setBackVisibility(visibility:Int){
        ivBack.visibility = visibility
    }

    fun setDownloadPathVisible(onClickListener:View.OnClickListener){
        btnSetDownloadPath.visibility = View.VISIBLE
        btnSetDownloadPath.setOnClickListener (onClickListener)
    }


    /**
     * 布局替换
     * @param view
     */
    private fun replaceLayout(view: View) {
        val lp = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp
        fl_content.addView(view)
    }
    abstract fun  initContentView():View
    abstract fun getPageTitle():String

}