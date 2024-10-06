package com.booyue.base.util;

import androidx.annotation.StringRes;

import com.booyue.base.app.ProjectInit;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/19.13:56
 * 字符串处理工具类
 */

public class StringUtil {
    private static final String TAG = "StringUtil";
    /**
     * 获取字符串中长度（不包含空格）
     * @param s 字符串
     * @return 非空格长度
     */
    public static int getLengthWithoutSpace(CharSequence s) {
        int len = s.length();
        int rlen = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) != ' '){
                rlen++;
            }
        }
        return rlen;
    }

    public static String getString(@StringRes int stringId){
        return ProjectInit.getApplicationContext().getResources().getString(stringId);
    }

    public static String formatDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    public static String formatDate(Long date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
     public static String formatDateTime(Long date){
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date);
    }

    //播放量格式化
    public static String formatBrowseCount(int amount){
        if(amount > 100000000){
            int num = amount / 100000000;

            return num + "亿+";

        } else if (amount > 10000) {

            int num = amount / 10000;

            return num + "万+";

        } else {

            return amount +"";
        }
    }

    /**
     * 进行字符串解码
     * @param name
     * @return
     */
    public static String decode(String name){
        String decodeName = "";
        try {
            decodeName = URLDecoder.decode(name,"UTF-8");
            LoggerUtils.d(TAG, "decodeName: ==" + decodeName + "==");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeName;
    }

    public static String encode(String name){
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeName;
    }

    public static String[] splitSpace(String s){
        return s.split("\\s+");
    }



}
