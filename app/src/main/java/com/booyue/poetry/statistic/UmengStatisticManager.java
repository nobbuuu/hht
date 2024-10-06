package com.booyue.poetry.statistic;

import android.app.Application;
import android.text.TextUtils;

import com.booyue.poetry.MyApp;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.HashMap;


/**
 * @author: wangxinhua
 * @date: 2020/7/15 9:34
 * @description :
 */
public class UmengStatisticManager {

    public static void init(Application application) {
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.setLogEnabled(false);
        UMConfigure.init(application, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 显示页面
     */
    public static void showPage(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        MobclickAgent.onEventObject(MyApp.getInstance(), "page_show", map);
    }

    /**
     * 点击添加
     * 首页+
     * 详情添加
     *
     * @param from
     */
    public static void clickAddBook(String from) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", from);
        MobclickAgent.onEventObject(MyApp.getInstance(), "click_add_book", map);
    }

    /**
     * 点击下载列表
     * 1、首页
     * 2、choose_book
     *
     * @param from
     */
    public static void clickDownloadList(String from) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", from);
        MobclickAgent.onEventObject(MyApp.getInstance(), "click_download_list", map);
    }

    public static void clickEdit(String from) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", from);
        MobclickAgent.onEventObject(MyApp.getInstance(), "click_edit", map);
    }

    /**
     * 内置，TF卡
     *
     * @param type 内置卡，TF卡
     */
    public static void clickSetPath(String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        MobclickAgent.onEventObject(MyApp.getInstance(), "click_set_path", map);
    }

    /**
     * 更改的路径
     * @param path
     */
    public static void clickSetPathChange(String path) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("path", path);
        MobclickAgent.onEventObject(MyApp.getInstance(), "click_set_path_change", map);
    }

    /**
     * 选择年级
     * @param grade
     */
    public static void clickChooseGrade(String grade) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("grade", grade);
        MobclickAgent.onEventObject(MyApp.getInstance(), "choose_grade", map);
    }
    public static void addDownload(String path) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("path", path);
        MobclickAgent.onEventObject(MyApp.getInstance(), "add_download", map);
    }

    /**
     * 选择出版社
     * @param publisher
     */
    public static void clickChoosePublisher(String publisher) {
        HashMap<String, Object> map = new HashMap<>();
        if(TextUtils.isEmpty(publisher)){
            publisher = "全部出版社";
        }
        map.put("publisher", publisher);
        MobclickAgent.onEventObject(MyApp.getInstance(), "choose_publisher", map);
    }

    /**
     * 选择科目
     * @param subject
     */
    public static void clickChooseSubject(String subject) {
        HashMap<String, Object> map = new HashMap<>();
        if(TextUtils.isEmpty(subject)){
            subject = "全部科目";
        }
        map.put("subject", subject);
        MobclickAgent.onEventObject(MyApp.getInstance(), "choose_subject", map);
    }
}
