package com.booyue.poetry.ui.book2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.booyue.base.fragment.BaseFragment;
import com.booyue.base.util.JSONUtils;
import com.booyue.base.util.NetWorkUtils;
import com.booyue.net.callback.ModelCallback;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter2.BookOne2Adapter;
import com.booyue.poetry.adapter2.BookThree2Adapter;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.bean.FirstOrderBean;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.bean.ThreeFragmentBean;
import com.booyue.poetry.request.RequestManager;
import com.booyue.poetry.utils.DialogUtil;
import com.booyue.view.xrecyclerview.HHTRecyclerView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Book2ThreeFragment extends BaseFragment {

    public static final String KEY_Book2Three_TYPE = "book2Three_type";
    public static final int Book2Three_TYPE_ONE = 1;
    private Activity activity;
    private RecyclerView rvHome;
    private BookThree2Adapter mBookAdapter;
    private List<ThreeFragmentBean.ThTDataDTO.ListDTO>  mlist;
    private List<MainDataBean.MainDataDTO.DynastyListDTO>  dynastyList = new ArrayList<>();

    public Book2ThreeFragment() {

    }
    public static Book2ThreeFragment newInstance(int type) {
        Book2ThreeFragment book2ThreeFragment = new Book2ThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_Book2Three_TYPE, type);
        book2ThreeFragment.setArguments(bundle);
        return book2ThreeFragment;
    }

    public Book2ThreeFragment(Activity activity) {
        this.activity = activity;
    }

    public Book2ThreeFragment(List<MainDataBean.MainDataDTO.DynastyListDTO> dynastyList) {
        this.dynastyList = dynastyList;
    }

    @Override
    public View getCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookthree, container, false);
        rvHome = view.findViewById(R.id.rvHome);
        return view;
    }

    @Override
    public void initView() {

    }

    private int i = 1;
    @Override
    public void initData() {
        mlist = new ArrayList<>();
        mBookAdapter = new BookThree2Adapter(R.layout.item_book2three, mlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(10);
        rvHome.setItemViewCacheSize(50);
        rvHome.setHasFixedSize(true);
//        rvHome.isNestedScrollingEnabled = false;
        rvHome.setNestedScrollingEnabled(false);
        rvHome.setLayoutManager(linearLayoutManager);
        rvHome.setAdapter(mBookAdapter);
        getAllData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();

    }

    //请求全局数据
    public void getAllData() {
        if (!NetWorkUtils.isNetWorkAvailable(mActivity)) {
            DialogUtil.showNetWorkDialog(mActivity, 0);
            return;
        }
        HashMap<String, Object> map = new HashMap<>(5);
        map.put("type", "诗人朝代");//写死的参数
        map.put("pageNo", i);
        map.put("pageSize", 60);
        String toJson = JSONUtils.toJson(map);
        ResourceApi.postMainData(toJson , new ModelCallback<ThreeFragmentBean>() {
            @Override
            public void onSuccess(ThreeFragmentBean response) {
                if (response.isSuccess()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<ThreeFragmentBean.ThTDataDTO.ListDTO> list = response.getData().getList();
                            mlist.addAll(list);
                            mBookAdapter.notifyDataSetChanged();
                        }
                    },1000);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
            }
        });
    }

}
