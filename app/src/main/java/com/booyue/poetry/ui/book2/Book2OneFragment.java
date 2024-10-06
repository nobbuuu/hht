package com.booyue.poetry.ui.book2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.booyue.base.fragment.BaseFragment;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter2.BookOne2Adapter;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.request.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book2OneFragment extends BaseFragment {

    public static final String KEY_Book2_TYPE = "fragment_type";
    public static final int Book2_TYPE_ONE = 1;
    public static final int Book2_TYPE_TWO = 2;
    private RecyclerView hhtSwipeRecyclerView;
    private BookOne2Adapter mBookAdapter;
    private MainDataBean.MainDataDTO dataList;
    private int type;
    public Book2OneFragment() {

    }

    public Book2OneFragment(MainDataBean.MainDataDTO data,int type) {
        this.dataList = data;
        this.type = type;
    }
    public static Book2OneFragment newInstance(int type) {
        Book2OneFragment book2OneFragment = new Book2OneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_Book2_TYPE, type);
        book2OneFragment.setArguments(bundle);
        return book2OneFragment;
    }

    @Override
    public View getCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookone, container, false);
        hhtSwipeRecyclerView = view.findViewById(R.id.book2oneRecyclerView);
        return view;
    }
    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        type = getArguments().getInt(KEY_Book2_TYPE, Book2_TYPE_ONE);
        mBookAdapter = new BookOne2Adapter(mActivity, dataList, type,true);
        hhtSwipeRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        hhtSwipeRecyclerView.setAdapter(mBookAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();

    }

}
