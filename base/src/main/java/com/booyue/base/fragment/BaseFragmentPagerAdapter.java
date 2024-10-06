package com.booyue.base.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2019/3/14
 * @description :
 */
public class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    private List<T> list;
    public BaseFragmentPagerAdapter(List<T> list, FragmentManager fragmentManager){
        super(fragmentManager);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
