package com.booyue.poetry.constant;

/**
 * @author: wangxinhua
 * @date: 2020/6/2 14:49
 * @description :
 */
public class UrlConstant {
    public static final String CHANNEL = "hhtjgs";

    public static final String channelId = "88";//

    public static final String HOST_URL = "http://api.cloud.alilo.com.cn/";
    //    public static final String HOST_URL =                    "https://api.cloud.alilo.com.cn/";
    public static final String HOST_URL2 = "http://api.dcloud.alilo.com.cn/";
    //        public static final String HOST_URL =                 "http://api-dcloud.alilo.com.cn/";
    public static final String APP_KEY = "0929b620ece14482a980e10b6c7f57a7";
    //    public static final String APP_KEY =                      "ec15dec17f5140eb9b5c03587fb5116f";
    public static final String APP_SECRET = "zx4ODisSGRKHwGZwyxL+3TyYsG3YBIMq07wJTIsxs/N5uqg5H+f80A==";
//    public static final String APP_SECRET =                   "xoXsx3X/zZXs51mCo8efyxXsJ/Ysphp+2dOqWxWFEPZ5uqg5H+f80A==";

//    public static final String APP_KEY =                      "ec712c29295d4ce1bc121fdf3345bbdd";
//    public static final String APP_SECRET =                   "Y79fzZJZ3Iqn5Actjw8jqjzYgRVWT5r3pIynLT/Fknl5uqg5H+f80A==";


    //    public static final String HOST_URL =                    "http://192.168.137.1:35001/";
    public static final String BASE_URL2 = HOST_URL + "api/v4/";
    //获取数据
    public static final String Main_Data = "api/v4/poetry/query/poetry";
    public static final String Main_DataRAW = "http://api.cloud.alilo.com.cn/"; //正式服
    //    public static final String Main_DataRAW =                    "http://api-dcloud.alilo.com.cn/"; // 测试服
    //检测更新
    public static final String VERSION_UPDATE_URL2 = BASE_URL2 + "version-upgrade/get?";//检测新版本
    public static final String GET_PUBLIC_TOKEN = BASE_URL2 + "login/get-token?";//获取公共token
    //获取搜索条件
    public static final String SUBJECT_SEARCH_CONDITION_URL = BASE_URL2 + "quanpin-video/get-condition?";
    //搜索课程
    public static final String SEARCH_COURSE_URL = BASE_URL2 + "quanpin-video/search?";
    //升级APK

    //    public static final String HOST_URL2_C =                  "http://121.43.105.9:8082/";
    public static final String HOST_URL2_C = "http://cloud.alilo.com.cn/";

    public static final String CheckAPKCode = HOST_URL2_C + "l1/api/v2/checkNewVersion";
    public static final String natureUrl = "https://resource-baidu.alilo.com.cn/v11/natural-science/natural-kepu.json";
    public static final String scienceUrl = "https://resource-baidu.alilo.com.cn/v11/natural-science/science-qimeng.json";

}
