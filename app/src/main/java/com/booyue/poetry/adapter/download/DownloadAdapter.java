package com.booyue.poetry.adapter.download;

import androidx.fragment.app.FragmentManager;

import com.booyue.base.fragment.BaseFragment;
import com.booyue.base.fragment.BaseFragmentPagerAdapter;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 17:25
 * @description :
 */
public class DownloadAdapter extends BaseFragmentPagerAdapter<BaseFragment> {

    public DownloadAdapter(List<BaseFragment> list, FragmentManager fragmentManager) {
        super(list, fragmentManager);
    }
}
