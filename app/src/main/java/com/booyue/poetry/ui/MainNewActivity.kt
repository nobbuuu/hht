package com.booyue.poetry.ui

import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ColorUtils
import com.booyue.base.activity.BaseActivity
import com.booyue.poetry.R
import com.booyue.poetry.adapter.CommonFraPagerAdapter
import com.booyue.poetry.constant.VideoType
import com.booyue.poetry.ui.book2.NewSetUpActivity
import com.booyue.poetry.ui.fragment.MainVideoFragment

class MainNewActivity : BaseActivity() {

    override fun setView() {
        setContentView(R.layout.activity_main_new)
    }

    override fun initView() {
        val mainRag = findViewById<RadioGroup>(R.id.mainRag)
        val natureRb = findViewById<RadioButton>(R.id.natureRb)
        val scienceRb = findViewById<RadioButton>(R.id.scienceRb)
        val mainBg = findViewById<ConstraintLayout>(R.id.mainBg)
        val mainVp = findViewById<ViewPager2>(R.id.mainVp)
        val setIv = findViewById<ImageView>(R.id.setIv)
        val backIv = findViewById<ImageView>(R.id.backIv)

        setIv.setOnClickListener {
            jumpTo(NewSetUpActivity::class.java)
        }
        backIv.setOnClickListener {
            finish()
        }
        mainRag.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.natureRb -> {
                    mainBg.setBackgroundResource(R.mipmap.bg_main_nature)
                    mainRag.setBackgroundResource(R.drawable.shape_main_rg_bg_nature)
                    natureRb.setTextColor(ColorUtils.getColor(R.color.color_green_97B824))
                    scienceRb.setTextColor(ColorUtils.getColor(R.color.white))
                    mainVp.setCurrentItem(0, false)
                }
                R.id.scienceRb -> {
                    mainBg.setBackgroundResource(R.mipmap.bg_main_science)
                    mainRag.setBackgroundResource(R.drawable.shape_main_rg_bg_science)
                    natureRb.setTextColor(ColorUtils.getColor(R.color.white))
                    scienceRb.setTextColor(ColorUtils.getColor(R.color.color_blue_3c3c96))
                    mainVp.setCurrentItem(1, false)
                }
            }
        }
        mainVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    natureRb.isChecked = true
                    mainBg.setBackgroundResource(R.mipmap.bg_main_nature)
                    mainRag.setBackgroundResource(R.drawable.shape_main_rg_bg_nature)
                    natureRb.setTextColor(ColorUtils.getColor(R.color.color_green_97B824))
                    scienceRb.setTextColor(ColorUtils.getColor(R.color.white))
                } else {
                    scienceRb.isChecked = true
                    mainBg.setBackgroundResource(R.mipmap.bg_main_science)
                    mainRag.setBackgroundResource(R.drawable.shape_main_rg_bg_science)
                    natureRb.setTextColor(ColorUtils.getColor(R.color.white))
                    scienceRb.setTextColor(ColorUtils.getColor(R.color.color_blue_3c3c96))
                }
            }
        })
    }

    override fun initListener() {

    }

    override fun initData() {
        val fragments = mutableListOf<MainVideoFragment>()
        val fraNature = MainVideoFragment(VideoType.TYPE_VIDEO_NATURE)
        val fraScience = MainVideoFragment(VideoType.TYPE_VIDEO_SCIENCE)
        fragments.add(fraNature)
        fragments.add(fraScience)
        val mainVp = findViewById<ViewPager2>(R.id.mainVp)
        val fraAdapter = CommonFraPagerAdapter(this, fragments)
        mainVp.adapter = fraAdapter
    }
}