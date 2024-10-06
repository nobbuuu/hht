package com.booyue.poetry.ui.book2;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

public class BaseBarActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();//沉浸式导航栏
    }

    /**
     * 状态栏 和 导航栏 设置
     */
    private void initStatusBar(){
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }
}
