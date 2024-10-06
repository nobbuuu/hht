package com.booyue.poetry.ui.book2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booyue.base.toast.Tips;
import com.booyue.base.util.CustomToast;
import com.booyue.base.util.NetWorkUtils;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;
import com.booyue.media.bean.VideoBean;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadOptionAdapter;
import com.booyue.poetry.bean.LayoutBean;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.statistic.UmengStatisticManager;
import com.booyue.poetry.ui.player.ExoVideoPlayerActivity;
import com.booyue.poetry.utils.DialogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewDownloadActivity extends BaseBarActivity {

    //下载的4个板块
    private RelativeLayout layoutOne,layoutTwo,layoutThree,layoutFor;
    private ImageView runFinSh;
    private TextView firstTitle;
    private ImageView downloadImageOne,downloadImageTwo,downloadImageThree,downloadImageFor;

    private MainDataBean.MainDataDTO.ListDTO.DataDTO dataDTO;
    private List<LayoutBean> idList;
    private String typeOne="儿歌课-";
    private String typeTwo="朗诵课-";
    private String typeThree="故事课-";
    private String typeFor="涂色作业课-";
    private Boolean isDownloadOne,isDownloadTwo,isDownloadThree,isDownloadFor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_download);

        layoutOne = findViewById(R.id.layoutOne);
        layoutTwo = findViewById(R.id.layoutTwo);
        layoutThree = findViewById(R.id.layoutThree);
        layoutFor = findViewById(R.id.layoutFor);
        downloadImageOne = findViewById(R.id.downloadImageOne);
        downloadImageTwo = findViewById(R.id.downloadImageTwo);
        downloadImageThree = findViewById(R.id.downloadImageThree);
        downloadImageFor = findViewById(R.id.downloadImageFor);

        firstTitle = findViewById(R.id.firstTitle);
        runFinSh = findViewById(R.id.runFinSh);
        runFinSh.setOnClickListener(v -> {
            finish();
//            DialogUtil.showNetWorkDialog(this, 1);  //测试代码
        });
        dataDTO = new Gson().fromJson(getIntent().getStringExtra("downloadData"), MainDataBean.MainDataDTO.ListDTO.DataDTO.class);
        idList = new ArrayList<>();
        idList.add(new LayoutBean(dataDTO.getChildrenId(),1));//儿歌ID
        idList.add(new LayoutBean(dataDTO.getReadId(),2));//朗诵课ID
        idList.add(new LayoutBean(dataDTO.getStoryId(),3));//故事课ID
        idList.add(new LayoutBean(dataDTO.getHomeworkId(),4));//涂色作业课ID
        firstTitle.setText(dataDTO.getName());

        //先一个一个查询
        for (int i = 0; i <idList.size() ; i++) {
            List<DownloadBean> downloadBeanListAll = DownloadDao.getInstance().queryDownloadComplete(idList.get(i).getId());
            initSeleteGreenDaoData(downloadBeanListAll,idList.get(i).getType());
        }


        layoutOne.setOnClickListener(v -> {
            List<DownloadBean> downloadBeanListOne1 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getChildrenId());
            initSeleteGreenDaoData(downloadBeanListOne1,1);
            if (isDownloadOne){
                newDownloadVideo(dataDTO.getChildrenId(),typeOne+dataDTO.getName(),dataDTO.getChildrenUrl(),"儿童课",dataDTO.getImgUrl());
            }else {
//                newStartVideoPlayer(downloadBeanListOne1,typeOne+dataDTO.getName());
                startVideoPlayer(typeOne+dataDTO.getName());
            }
        });
        layoutTwo.setOnClickListener(v -> {
            List<DownloadBean> downloadBeanListTwo2 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getReadId());
            initSeleteGreenDaoData(downloadBeanListTwo2,2);
            if (isDownloadTwo){
                newDownloadVideo(dataDTO.getReadId(),typeTwo+dataDTO.getName(),dataDTO.getReadUrl(),"朗诵课",dataDTO.getImgUrl());
            }else {
//                newStartVideoPlayer(downloadBeanListTwo2,typeTwo+dataDTO.getName());
                startVideoPlayer(typeTwo+dataDTO.getName());
            }
        });
        layoutThree.setOnClickListener(v -> {
            List<DownloadBean> downloadBeanListThree3 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getStoryId());
            initSeleteGreenDaoData(downloadBeanListThree3,3);
            if (isDownloadThree){
                newDownloadVideo(dataDTO.getStoryId(),typeThree+dataDTO.getName(),dataDTO.getStoryUrl(),"故事课",dataDTO.getImgUrl());
            }else {
//                newStartVideoPlayer(downloadBeanListThree3,typeThree+dataDTO.getName());
                startVideoPlayer(typeThree+dataDTO.getName());
            }
        });
        layoutFor.setOnClickListener(v -> {
            List<DownloadBean> downloadBeanListFor4 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getHomeworkId());
            initSeleteGreenDaoData(downloadBeanListFor4,4);
            if (isDownloadFor){
                newDownloadVideo(dataDTO.getHomeworkId(),typeFor+dataDTO.getName(),dataDTO.getHomeworkUrl(),"涂色作业课",dataDTO.getImgUrl());
            }else{
//                newStartVideoPlayer(downloadBeanListFor4,typeFor+dataDTO.getName());
                startVideoPlayer(typeFor+dataDTO.getName());
            }
        });


    }

    private void startVideoPlayer(String name){
        List<DownloadBean> downloadBeanListOne1 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getChildrenId());
        initSeleteGreenDaoData(downloadBeanListOne1,1);
        List<DownloadBean> downloadBeanListTwo2 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getReadId());
        initSeleteGreenDaoData(downloadBeanListTwo2,2);
        List<DownloadBean> downloadBeanListThree3 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getStoryId());
        initSeleteGreenDaoData(downloadBeanListThree3,3);
        List<DownloadBean> downloadBeanListFor4 = DownloadDao.getInstance().queryDownloadComplete(dataDTO.getHomeworkId());
        initSeleteGreenDaoData(downloadBeanListFor4,4);
        ArrayList<VideoBean> videoList = new ArrayList<>();
        if (!isDownloadOne){
            if (downloadBeanListOne1.get(0).title.equals(typeOne+dataDTO.getName())){
                VideoBean videoBean = new VideoBean();
                videoBean.name = downloadBeanListOne1.get(0).getTitle();
                videoBean.localUrl = downloadBeanListOne1.get(0).getLocalPath();
                videoBean.url = downloadBeanListOne1.get(0).getUrl();
                videoList.add(videoBean);
            }
        }
        if (!isDownloadTwo){
            if (downloadBeanListTwo2.get(0).title.equals(typeTwo+dataDTO.getName())){
                VideoBean videoBean = new VideoBean();
                videoBean.name = downloadBeanListTwo2.get(0).getTitle();
                videoBean.localUrl = downloadBeanListTwo2.get(0).getLocalPath();
                videoBean.url = downloadBeanListTwo2.get(0).getUrl();
                videoList.add(videoBean);
            }
        }
        if (!isDownloadThree){
            if (downloadBeanListThree3.get(0).title.equals(typeThree+dataDTO.getName())){
                VideoBean videoBean = new VideoBean();
                videoBean.name = downloadBeanListThree3.get(0).getTitle();
                videoBean.localUrl = downloadBeanListThree3.get(0).getLocalPath();
                videoBean.url = downloadBeanListThree3.get(0).getUrl();
                videoList.add(videoBean);
            }
        }
        if (!isDownloadFor){
            if (downloadBeanListFor4.get(0).title.equals(typeFor+dataDTO.getName())){
                VideoBean videoBean = new VideoBean();
                videoBean.name = downloadBeanListFor4.get(0).getTitle();
                videoBean.localUrl = downloadBeanListFor4.get(0).getLocalPath();
                videoBean.url = downloadBeanListFor4.get(0).getUrl();
                videoList.add(videoBean);
            }
        }
        int j=0;
        for (int i = 0; i < videoList.size(); i++) {
            if (name.equals(videoList.get(i).name)){
               j = i;
               break;
            }
        }
        ExoVideoPlayerActivity.Companion.launchV(this, videoList,j);

    }
    //跳转视频播放器
    private void newStartVideoPlayer(List<DownloadBean> downloadBeanList,String name) {
        ArrayList<VideoBean> videoList = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < downloadBeanList.size(); i++) {
            if (downloadBeanList.get(i).title.equals(name)){
                VideoBean videoBean = new VideoBean();
                videoBean.name = downloadBeanList.get(i).getTitle();
                videoBean.localUrl = downloadBeanList.get(i).getLocalPath();
                videoBean.url = downloadBeanList.get(i).getUrl();
                videoList.add(videoBean);
                j = i;
            }
        }
        ExoVideoPlayerActivity.Companion.launchV(this, videoList,j);
    }

    private String netWorkError="";
    private AlertDialog alertDialog;
    //下载视频
    private void newDownloadVideo(int id,String name,String url,String type,String imageurl) {
        if (!NetWorkUtils.isNetWorkAvailable(this)) {
//            Tips.showCustom(this,R.string.error_network2,0);
            AlertDialog alertDialog = DialogUtil.showNetWorkDialog(this, 0);
            this.alertDialog = alertDialog;
            return;
        }
        DownloadBean downloadBean = new DownloadBean();
        downloadBean.guid = id;
        downloadBean.title = name;
        downloadBean.url = url;
        downloadBean.subject = type;
        downloadBean.coverImage = imageurl;
        downloadBean.isChack = false;
        MyApp.getInstance().getDownloadHelper().insertAndStart(downloadBean);
//        CustomToast.showToast(R.string.tips_insert_download_success);
        try{
            AlertDialog alertDialog = DialogUtil.showNetWorkDialog(this, 1);
            this.alertDialog = alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //初始化查询功能
    private void initSeleteGreenDaoData(List<DownloadBean> downloadBeanList, int type) {
        try{
            if (downloadBeanList != null && downloadBeanList.size() > 0) {
                switch (type){
                    case 1:
                        downloadImageOne.setVisibility(View.GONE);
                        isDownloadOne = false;
                        break;
                    case 2:
                        downloadImageTwo.setVisibility(View.GONE);
                        isDownloadTwo = false;
                        break;
                    case 3:
                        downloadImageThree.setVisibility(View.GONE);
                        isDownloadThree = false;
                        break;
                    case 4:
                        downloadImageFor.setVisibility(View.GONE);
                        isDownloadFor = false;
                        break;
                }
            }else {
                switch (type){
                    case 1:
                        downloadImageOne.setVisibility(View.VISIBLE);
                        isDownloadOne = true;
                        break;
                    case 2:
                        downloadImageTwo.setVisibility(View.VISIBLE);
                        isDownloadTwo = true;
                        break;
                    case 3:
                        downloadImageThree.setVisibility(View.VISIBLE);
                        isDownloadThree = true;
                        break;
                    case 4:
                        downloadImageFor.setVisibility(View.VISIBLE);
                        isDownloadFor = true;
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DialogUtil.showNetWorkDialog(this, 1);
        if (alertDialog != null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}