package com.booyue.poetry.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommonFraPagerAdapter(fragmentActivity: FragmentActivity, private val mFragments: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment = mFragments[position]
}