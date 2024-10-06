package com.booyue.poetry.ui;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booyue.base.activity.BaseActivity;
import com.booyue.base.fragment.BaseFragment;
import com.booyue.base.util.BitmapUtil;
import com.booyue.base.util.NetWorkUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.ui.book2.Book2OneFragment;
import com.booyue.poetry.ui.book2.Book2ThreeFragment;
import com.booyue.poetry.ui.book2.NewSetUpActivity;
import com.booyue.poetry.utils.DialogUtil;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocalBook2Activity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {


    private RelativeLayout mianBg;
    private ViewPager viewPager;
    private RadioGroup tabs;
    private ImageView setUp;
    @Override
    public void setView() {
        setContentView(R.layout.activity_local_book2);
    }
    @Override
    public void initView() {
        mianBg = findViewById(R.id.mianBg);
        tabs = findViewById(R.id.tabs);
        setUp = findViewById(R.id.setUp);
        tabs.setOnCheckedChangeListener(this);
        viewPager = findViewById(R.id.book2viewPage);

        //请求公用Token
        getAllData();
        initViewPager();
        HHTRecyclerView rv = new HHTRecyclerView(this);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        setUp.setOnClickListener(v -> {
            jumpTo(NewSetUpActivity.class);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //请求全局数据
    public void getAllData() {
        if (!NetWorkUtils.isNetWorkAvailable(this)) {
            DialogUtil.showNetWorkDialog(this, 0);
            return;
        }
    }

    /**
     * 初始化viewpager
     */
    private List<BaseFragment> mTabs;
    private void initViewPager() {
        mTabs = new ArrayList<>();
        mTabs.add(0,Book2OneFragment.newInstance(Book2OneFragment.Book2_TYPE_ONE));
        mTabs.add(1,Book2OneFragment.newInstance(Book2OneFragment.Book2_TYPE_TWO));
        mTabs.add(2,Book2ThreeFragment.newInstance(Book2ThreeFragment.Book2Three_TYPE_ONE));
//        mTabs.add(2,new Book2ThreeFragment(this));
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
        if (viewPager != null) {
            viewPager.setAdapter(fragmentPagerAdapter);
            viewPager.addOnPageChangeListener(this);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setCurrentItem(0);
        }
    }
    /**
     * tabs点击监听
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.tab_recommend:
                viewPager.setCurrentItem(0);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg1"));
                break;
            case R.id.tab_classify:
                viewPager.setCurrentItem(1);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg2"));
                break;
            case R.id.tab_rank:
                viewPager.setCurrentItem(2);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg3"));
                break;
        }
    }



    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabs.check(R.id.tab_recommend);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg1"));
                break;
            case 1:
                tabs.check(R.id.tab_classify);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg2"));
                break;
            case 2:
                tabs.check(R.id.tab_rank);
                mianBg.setBackgroundResource(BitmapUtil.getMipmapId(this,"img_bg3"));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


}