package com.booyue.poetry.ui.download;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.booyue.base.fragment.BaseFragment;
import com.booyue.base.toast.Tips;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadAdapter;
import com.booyue.poetry.adapter.download.DownloadListAdapter;
import com.booyue.poetry.ui.HeaderBaseActivity;
import com.booyue.poetry.ui.book2.NewDownloadActivity;
import com.booyue.poetry.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 9:43
 * @description :
 */
public class DownloadActivity extends HeaderBaseActivity implements DownloadListAdapter.DownloadBeanID {
    private DownloadAdapter downloadAdapter;
    private List<BaseFragment> fragments;
    private ViewPager viewPager;
    private RadioGroup rgCheck;
    private RadioButton rbDownloading;
    private RadioButton rbDownloadComplete;
    private CheckBox seleteAll;
    private TextView seleteText;
    private TextView deleteText;
    private DownloadCompleteFragment downloadCompleteFragment;
    @NotNull
    @Override
    public View initContentView() {
        return layoutInflater.inflate(R.layout.activity_download,null);
    }

    @Override
    public void initListener() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.densityDpi;

        rgCheck.setOnCheckedChangeListener((radioGroup, i) -> {
            if(radioGroup.getCheckedRadioButtonId() == R.id.rbDownloading){
                viewPager.setCurrentItem(0);
                seleteText.setVisibility(View.INVISIBLE);
                seleteAll.setVisibility(View.INVISIBLE);
                deleteText.setVisibility(View.INVISIBLE);
                isShowChack = false;
                downloadCompleteFragment.updataLayoutItem(false);
                if (density > 159 && density < 161){
                    rbDownloading.setTextSize(TypedValue.COMPLEX_UNIT_SP,36);
                    rbDownloadComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                }else{
                    rbDownloading.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    rbDownloadComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }
                rbDownloading.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                rbDownloadComplete.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }else {
                viewPager.setCurrentItem(1);
                INVISIBLELayout();//隐藏控件
                if (density > 159 && density < 161){
                    rbDownloading.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                    rbDownloadComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP,36);
                }else{
                    rbDownloading.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    rbDownloadComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                }
                rbDownloading.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                rbDownloadComplete.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });

    }

    public Boolean isChackAll= false;
    public Boolean isShowChack= false;
    @Override
    public void initData() {
        viewPager = findViewById(R.id.viewPage);
        rgCheck = findViewById(R.id.rgCheck);
        seleteText = findViewById(R.id.seleteText);
        seleteAll = findViewById(R.id.seleteAll);
        deleteText = findViewById(R.id.deleteText);
        rbDownloading = findViewById(R.id.rbDownloading);
        rbDownloadComplete = findViewById(R.id.rbDownloadComplete);
        findViewById(R.id.runFinSh).setOnClickListener(v -> finish());

        fragments = new ArrayList<>();
        fragments.add(new DownloadListFragment(DownloadListAdapter.STATE_DOWNLOADING));
        downloadCompleteFragment = new DownloadCompleteFragment(DownloadListAdapter.STATE_COMPPLETE);
        fragments.add(downloadCompleteFragment);
        downloadAdapter = new DownloadAdapter(fragments,getSupportFragmentManager());
        viewPager.setAdapter(downloadAdapter);
        viewPager.setCurrentItem(0);


        seleteText.setVisibility(View.INVISIBLE);
        seleteAll.setVisibility(View.INVISIBLE);
        deleteText.setVisibility(View.INVISIBLE);


        seleteText.setOnClickListener(v -> {
            seleteAll.setVisibility(View.VISIBLE);
            deleteText.setVisibility(View.VISIBLE);
            seleteText.setVisibility(View.INVISIBLE);
            if(isShowChack){
                isShowChack = false;
                downloadCompleteFragment.updataLayoutItem(false);
            }else{
                isShowChack = true;
                downloadCompleteFragment.updataLayoutItem(true);
            }
        });
        seleteAll.setOnClickListener(v -> {
            List<DownloadBean> listDownloadBean = downloadCompleteFragment.getListDownloadBean();//查询List数据
            if (listDownloadBean == null || listDownloadBean.size() == 0)
                return;
            if (seleteAll.isChecked()){
                downloadCompleteFragment.updateDataIsChack(true);
            }else {
                downloadCompleteFragment.updateDataIsChack(false);
            }
        });
        deleteText.setOnClickListener(v -> {
            List<DownloadBean> listDownloadBean = downloadCompleteFragment.getListDownloadBean();//查询List数据
            if (listDownloadBean == null || listDownloadBean.size() == 0)
                return;
            List<DownloadBean> downloadBeans = new ArrayList<>();//删除创建的数据
            for (int i = 0; i < listDownloadBean.size(); i++) {
                if (listDownloadBean.get(i).isChack == true){
                    downloadBeans.add(listDownloadBean.get(i));
                }
            }
            String dialogTitle;
            if (downloadBeans.size() == 0){
                Tips.show("请选择所要删除的内容");
                return;
            }
            if (downloadBeans.size() == 1){
                dialogTitle = "确认删除"+downloadBeans.get(0).getTitle()+"?";
            }else{
                dialogTitle = "确认删除这"+downloadBeans.size()+"项吗?";
            }
            DialogUtil.showConfirmDialog(this, dialogTitle , view1 -> {
                DownloadDao.getInstance().deleteList(downloadBeans);
                downloadCompleteFragment.updateData();
                INVISIBLELayout2();
            });

        });
    }

    //隐藏控件
    private void INVISIBLELayout(){
        List<DownloadBean> listDownloadBeanAl = downloadCompleteFragment.getListDownloadBean();//查询List数据
        if (listDownloadBeanAl == null || listDownloadBeanAl.size() == 0){
            seleteAll.setVisibility(View.INVISIBLE);
            deleteText.setVisibility(View.INVISIBLE);
            seleteText.setVisibility(View.INVISIBLE);
        }
        else {
            seleteText.setVisibility(View.VISIBLE);
        }
    }
    //隐藏控件效果2
    private void INVISIBLELayout2(){
        List<DownloadBean> listDownloadBeanAl = downloadCompleteFragment.getListDownloadBean();//查询List数据
        if (listDownloadBeanAl == null || listDownloadBeanAl.size() == 0){
            seleteAll.setVisibility(View.INVISIBLE);
            deleteText.setVisibility(View.INVISIBLE);
            seleteText.setVisibility(View.INVISIBLE);
        }else {

        }
    }

    @NotNull
    @Override
    public String getPageTitle() {
        setHeaderGone();
        return "下载列表";
    }

    private List<DownloadBean> list = new ArrayList<>();
    @Override
    public void setDownloadBeanID(DownloadBean bean) {
        this.list.add(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadCompleteFragment!=null)
        downloadCompleteFragment.updateDataIsChack(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Boolean isRefresh) {
        if (isRefresh){
            List<DownloadBean> listDownloadBean = downloadCompleteFragment.getListDownloadBean();//查询List数据
            if (listDownloadBean.size() == 0)
                seleteText.setVisibility(View.INVISIBLE);
            downloadCompleteFragment.updateData();
            return;
        }
        Boolean isCheackAll = false;
        List<DownloadBean> listDownloadBean = downloadCompleteFragment.getListDownloadBean();//查询List数据
        for (int i = 0; i < listDownloadBean.size(); i++) {
            if (listDownloadBean.get(i).getIsChack()){
                isCheackAll = true;
            }else {
                isCheackAll = false;
                break;
            }
        }
        seleteAll.setChecked(isCheackAll);
    }





}
