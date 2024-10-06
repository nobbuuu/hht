package com.booyue.poetry.model;

import com.booyue.base.util.LoggerUtils;
import com.booyue.net.callback.ModelCallback;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.poetry.bean.SearchConditionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/9 9:24
 * @description :
 */
public class MainModel {
    private static final String TAG = "MainModel";
    /**
     * 获取数据
     * @param callback
     */
    public void getMainData(SearchConditionCallback callback) {
//        ResourceApi.postMainData(new ModelCallback<SearchConditionBean>() {
//            @Override
//            public void onSuccess(SearchConditionBean response) {
//                if (response.isSuccess()) {
//                    callback.onSuccess(response.data);
//                }
//            }
//        });
    }
    /**
     * 获取搜索条件
     * @param callback
     */
    public void getSearchCondition(SearchConditionCallback callback) {
        ResourceApi.getSearchCondition(new ModelCallback<SearchConditionBean>() {
            @Override
            public void onSuccess(SearchConditionBean response) {
                if (response.isSuccess()) {
                    callback.onSuccess(response.data);
                }
            }
        });
    }

    /**
     * 搜索课程
     * @param subject 科目：语文、数学、英语等等
     * @param version 版本：人教版、苏教版等等
     * @param grade 年级：一年级、二年级等等
     * @param gradeAttr 上、下册
     * @param showDetails 是否需要显示具体的视屏课程：0->不需要 1->需要
     * @param searchResultCallback
     */
    public void searchCourse(String subject,String grade,String version,String gradeAttr,
                             int showDetails,SearchResultCallback searchResultCallback){
        LoggerUtils.d(TAG, "subject:" + subject + ",version:" + version + ",grade: " + grade + ",gradeAttr:" + gradeAttr + ",showDetails:" + showDetails);
        ResourceApi.searchCourse(subject,grade,version,gradeAttr,showDetails,new ModelCallback<SearchResultBean>() {
            @Override
            public void onSuccess(SearchResultBean response) {
                if (response.isSuccess()) {
                    searchResultCallback.onSuccess(response.data);
                }
            }
        });
    }

    public interface SearchConditionCallback {
        void onSuccess(SearchConditionBean.DataBean dataBean);
    }
      public interface SearchResultCallback {
        void onSuccess(List<SearchResultBean.BookBean> dataBean);
    }

    /**
     * 数据结果转换
     * @param dataBean
     * @return
     */
    public static List<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> resList2VideoList(SearchResultBean.BookBean dataBean){
        List<SearchResultBean.BookBean.BookUnitBean> resListBeanList = dataBean.resList;
        List<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> videoListBeanList = new ArrayList<>();
        for (int i = 0; i < resListBeanList.size(); i++) {
            //一个单元数据
            SearchResultBean.BookBean.BookUnitBean resListBean = resListBeanList.get(i);
//          videoListBeanList.add(new SearchResultBean.DataBean.ResListBean.VideoListBean());
            for (int i1 = 0; i1 < resListBean.videoList.size(); i1++) {
                SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean videoListBean = resListBean.videoList.get(i1);
                videoListBean.subject = dataBean.subject;
                videoListBean.grade = dataBean.grade;
                videoListBean.gradeAtt = dataBean.gradeAtt;
                videoListBean.version = dataBean.version;
                videoListBean.coverImage = dataBean.coverImage;
                videoListBean.unit = resListBean.unit;
            }
            videoListBeanList.addAll(resListBean.videoList);
        }
        return videoListBeanList;
    }

}
