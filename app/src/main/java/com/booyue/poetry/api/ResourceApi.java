package com.booyue.poetry.api;

import android.text.TextUtils;

import com.booyue.net.callback.ModelCallback;
import com.booyue.net.rxbus.RxBus;
import com.booyue.poetry.bean.PublicTokenBean;
import com.booyue.poetry.constant.UrlConstant;
import com.booyue.poetry.listener.PublickTokenCallback;
import com.booyue.poetry.request.RequestManager;

import java.util.HashMap;

/**
 * @author: wangxinhua
 * @date: 2020/6/2 15:58
 * @description :
 */
public class ResourceApi {
    private static final String TAG = "ResourceApi";
    /**
     * 获取公共token
     * 当用户未登录时使用此token
     */
    public static String publicToken = "";

    public static void getPublicToken(PublickTokenCallback publickTokenCallback) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("appKey", UrlConstant.APP_KEY);
        map.put("appSecret", UrlConstant.APP_SECRET);
        RequestManager.getInstance().get(UrlConstant.GET_PUBLIC_TOKEN, map, new ModelCallback<PublicTokenBean>() {
            @Override
            public void onSuccess(PublicTokenBean response) {
                if (response.isSuccess()) {
                    publicToken = response.data;
                    if (publickTokenCallback != null) {
                        publickTokenCallback.onSuccess();
                    }
                }
            }
        });
    }

    /**
     * 检查apk版本
     *
     * @param packageCode
     */
    public static void getAPKVersion(String packageCode, ModelCallback callback) {
//        channelId=65&pageNo=1&videoGroupId=47&pageSize=20
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("package_code", packageCode);
        RequestManager.getInstance().post(UrlConstant.CheckAPKCode, map, callback, UrlConstant.HOST_URL2_C);
    }

    /**
     * 获取全局数据
     */
    public static void postMainData(String json, ModelCallback modelCallback) {
//    public static void postMainData( HashMap<String, Object> map,ModelCallback modelCallback) {
//        RequestManager.getInstance().post(UrlConstant.Main_Data, map, modelCallback);
        RequestManager.getInstance().postRaw(UrlConstant.Main_Data, json, modelCallback, UrlConstant.Main_DataRAW);

    }

    /**
     * 检测版本升级
     *
     * @param versionCode   版本号
     * @param modelCallback 回调接口
     */
    public static void checkVersion(int versionCode, ModelCallback modelCallback) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("version", versionCode);
        map.put("channelId", UrlConstant.channelId);
        RequestManager.getInstance().post(UrlConstant.VERSION_UPDATE_URL2, map, modelCallback);
    }

    /**
     * 获取课程搜索条件
     *
     * @param callback
     */
    public static void getSearchCondition(ModelCallback callback) {
        HashMap<String, Object> map = new HashMap<>(2);
        RequestManager.getInstance().get(UrlConstant.SUBJECT_SEARCH_CONDITION_URL, map, callback, UrlConstant.HOST_URL);
    }

    /**
     * 搜索课程
     * @param callback
     */
    /**
     * 搜索课程
     *
     * @param subject     科目：语文、数学、英语等等
     * @param version     版本：人教版、苏教版等等
     * @param grade       年级：一年级、二年级等等
     * @param gradeAttr   上、下册
     * @param showDetails 是否需要显示具体的视屏课程：0->不需要 1->需要
     * @param callback
     */
    public static void searchCourse(String subject, String grade, String version, String gradeAttr, int showDetails, ModelCallback callback) {
        HashMap<String, Object> map = new HashMap<>(8);
        if (!TextUtils.isEmpty(subject)) {
            map.put("subject", subject);
        }
        if (!TextUtils.isEmpty(version)) {
            map.put("version", version);
        }
        if (!TextUtils.isEmpty(grade)) {
            map.put("grade", grade);
        }
        if (!TextUtils.isEmpty(gradeAttr)) {
            map.put("gradeAtt", gradeAttr);
        }
        map.put("showDetails", showDetails + "");
        RequestManager.getInstance().post(UrlConstant.SEARCH_COURSE_URL, map, callback, UrlConstant.HOST_URL);
    }

    public static void requestNatureData(ModelCallback callback) {
        HashMap<String, Object> map = new HashMap<>();
        RequestManager.getInstance().get(UrlConstant.natureUrl, map, callback);
    }

    public static void requestScienceData(ModelCallback callback) {
        HashMap<String, Object> map = new HashMap<>();
        RequestManager.getInstance().get(UrlConstant.scienceUrl, map, callback);
    }
}
