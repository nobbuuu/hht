package com.booyue.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/6/19.13:55
 */

public class MatcherUtil {
    private static final Pattern CHINESE_MATCHER = Pattern.compile("[\\u4E00-\\u9FA5]+");
    private static final Pattern EMAIL_MATCHER = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    /**
     * "[1]"代表第1位为数字1，
     * "[358]"代表第二位可以为3、5、8中的一个，
     * "\\d{9}"代表后面是可以是0～9的数字，有9位。
     */
    private static final Pattern PHONE_MATCHER = Pattern.compile("[1][0-9]\\d{9}");
    private static final Pattern ILLEGAL_CHAR_MATCHER = Pattern.compile("^[a-zA-Z0-9\u4e00-\u9fa5]+$");
    private static final Pattern NUMBER_CHAR_MATCHER = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern NUMBER_MATCHER = Pattern.compile("[0-9]*");
    private static final Pattern MONEY_FLOAT = Pattern.compile("(\\d+\\.\\d+)");
    private static final Pattern INT = Pattern.compile("\\d+");
    /**
     * 匹配中文
     * @param str
     * @return
     */

    public static boolean isMatchChinese(String str) {
        if (str == null) {
            return false;
        }
        return CHINESE_MATCHER.matcher(str.trim()).find();
    }
    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isMatchEmail(String email){
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        return EMAIL_MATCHER.matcher(email).matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMatchPhoneNum(String mobiles) {
		/*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        return PHONE_MATCHER.matcher(mobiles).matches();
    }
    public static boolean isMatchInternationalPhoneNum(){
        return true;
    }

    /**
     * 判断是否有非法字符
     */
    public static boolean isMatchlegalChar(String text){
        if(ILLEGAL_CHAR_MATCHER.matcher(text).matches() ){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断是否包含数字和字母的字符
     */
    public static boolean isMatchNumAndChar(String text){
        if(NUMBER_CHAR_MATCHER.matcher(text).matches() ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断是否string中的内容是否是数字
     * @param str
     * @return
     */
    public static boolean isMatchNumberic(String str){
        return NUMBER_MATCHER.matcher(str).matches();
    }


    /**
     * 判断是否匹配日期格式
     * @param date 日期
     * @param yearlen  年份的长度
     * @return
     */
    public static boolean isMatchDate(String date, int yearlen) {
        // if(yearlen != 2 && yearlen != 4)
        // return false;
        int len = 4 + yearlen;
        if (date == null || date.length() != len)
            return false;

        if (!date.matches("[0-9]+"))
            return false;

        int year = Integer.parseInt(date.substring(0, yearlen));
        int month = Integer.parseInt(date.substring(yearlen, yearlen + 2));
        int day = Integer.parseInt(date.substring(yearlen + 2, yearlen + 4));

        if (year <= 0)
            return false;
        if (month <= 0 || month > 12)
            return false;
        if (day <= 0 || day > 31)
            return false;

        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return day > 30 ? false : true;
            case 2:
                //闰年的标准有俩个：1、被4整除并且不能被100整除  2、能被400整除
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    return day > 29 ? false : true;
                return day > 28 ? false : true;
            default:
                return true;
        }
    }

    private static final String TAG = "MatcherUtil";

    public static final String getMoneyNumber(String str){
        // 需要取整数和小数的字符串
//        String str = "需要提取的字符串1.111";
        // 控制正则表达式的匹配行为的参数(小数)
        String money = "";
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = MONEY_FLOAT.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            int groupCount = m.groupCount();
            LoggerUtils.d(TAG, "groupCount: " + groupCount);
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            money = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            m = INT.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                money = m.group();
                LoggerUtils.d(TAG, "money: " + money);
                while (m.find()){
                    money = m.group();
                    LoggerUtils.d(TAG, "money1: " + money);
                }
//                money = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                money = "";
            }
        }
       return money;
    }

    public static int getIntFromString(String name){
        if(name == null || name.isEmpty()) {
            return 0;
        }
        Matcher m = INT.matcher(name);
        String number = "";
        if (m.find()) {
            //如果有整数相匹配
            number = m.group();
            LoggerUtils.d(TAG, "money: " + number);
            while (m.find()){
                number = m.group();
                LoggerUtils.d(TAG, "money1: " + number);
                break;
            }
        } else {
            //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
            number = "";
        }
        if (!number.isEmpty()) {
            return Integer.parseInt(number);
        }
        return 0;
    }
    // 判断一个字符串是否含有数字
    public static boolean hasDigit(String content) {
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        return m.matches();
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

}
