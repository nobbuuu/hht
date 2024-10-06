package com.booyue.poetry.ui.book2;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.booyue.base.util.JSONUtils;
import com.booyue.base.util.NetWorkUtils;
import com.booyue.net.callback.ModelCallback;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter2.FirstOrder2Adapter;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.bean.FirstOrderBean;
import com.booyue.poetry.utils.DialogUtil;
import com.booyue.view.xrecyclerview.HHTRecyclerView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstOrderActivity extends BaseBarActivity {

    private HHTRecyclerView topRV;
    private ImageView runFinSh;
    private TextView firstTitle;
    private FirstOrder2Adapter mFirstAdapter;
    private List<FirstOrderBean.DataDTOD.ListDTO.DataDTO> mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_order);
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("type");
        String title = bundle.getString("title");
        String titleTwo = bundle.getString("titleTwo");
        mlist = new ArrayList<>();

        HashMap<String, Object> map = new HashMap<>(5);
        if (type == 1){
            map.put("type", "古诗学段");
            map.put("studyPhase", titleTwo);
        }else if(type == 2){
            map.put("type", "古诗题材");
            map.put("theme", titleTwo);
        };
        map.put("pageNo", 1);
        map.put("pageSize", 60);
        getAllData(map);

        firstTitle = (TextView) findViewById(R.id.firstTitle);
        firstTitle.setText(title);//设置标题
        runFinSh = (ImageView) findViewById(R.id.runFinSh);
        topRV = (HHTRecyclerView) findViewById(R.id.first_top_recycleView);

        runFinSh.setOnClickListener(v -> {
            finish();
        });
        mFirstAdapter = new FirstOrder2Adapter(this, mlist, true);
        topRV.setLayoutManager(new GridLayoutManager(FirstOrderActivity.this,3));
        topRV.setAdapter(mFirstAdapter);

        topRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (RecyclerView.SCROLL_STATE_IDLE != recyclerView.getScrollState()) {
//                    first_but_recycleView.scrollBy(dx, dy);
                }
            }
        });
    }


    //请求全局数据
    public void getAllData(HashMap<String, Object> map) {
        if (!NetWorkUtils.isNetWorkAvailable(this)) {
            DialogUtil.showNetWorkDialog(this, 0);
            return;
        }
        String toJson = JSONUtils.toJson(map);
        ResourceApi.postMainData( toJson , new ModelCallback<FirstOrderBean>() {
            @Override
            public void onSuccess(FirstOrderBean response) {
                if (response.isSuccess()){
                    List<FirstOrderBean.DataDTOD.ListDTO.DataDTO> data = response.getData().getList().get(0).getData();
//                    clearAnim();
                    mlist.addAll(data);
                    mFirstAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
            }
        });
    }

}