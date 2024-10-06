package com.booyue.poetry.ui.book2;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.14:18
 */

public class Book2Adapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public Book2Adapter(FragmentManager fm) {
        super(fm);
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(0,new Book2OneFragment());
            fragmentList.add(1,new Book2OneFragment());
//            fragmentList.add(2,new Book2ThreeFragment(this));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
