package com.booyue.base.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: wangxinhua
 * @date: 2019/3/27
 * @description :
 */
public class TimeUtil {
    private static final String TAG = "TimeUtil";

    /**
     * GMT+08:00转成一下格式GMT+8格式
     * "GMT+8" 时区，GMT[+|-]{0-9}+ 格式，默认GMT+8
     *
     * @return
     */
    public static String getStantartTimeZone() {
        String zone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        String preZone = zone.substring(0, 4);
        String lastZone = zone.substring(4, zone.length());
        String[] times = lastZone.split(":");
        int hour = Integer.valueOf(times[0]);
        return preZone + hour;
    }

    /*获取星期几*/
    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return 1;
        }
    }

    /**
     * 获取现在距离00:00的时间（秒）
     *
     * @param time
     * @return
     */
    public static int parseAlarmTime(String time) {
        String[] times = time.split("\\s+");
        if (times.length >= 2) {
            time = times[1];
        }
        if (time.contains(":")) {
            String[] strs = time.split(":");
            if (strs.length >= 2) {
                int hour = Integer.valueOf(strs[0]);
                int minute = Integer.valueOf(strs[1]);
                return hour * 3600 + minute * 60;
            }
        }
        return -1;
    }

    public static String formatAlarmTime(int time) {
        StringBuilder timeStr = new StringBuilder();
        int hour = time / 3600;
        int minute = (time % 3600) / 60;
        if (String.valueOf(hour).length() == 1) {
            timeStr.append("0").append(hour);
        } else {
            timeStr.append(hour);
        }
        timeStr.append(":");
        if (String.valueOf(minute).length() == 1) {
            timeStr.append("0").append(minute);
        } else {
            timeStr.append(minute);
        }
        return timeStr.toString();
    }

    public static String[] spilitDateTime(String dateFormat) {
        String[] date_time = dateFormat.split("\\s+");
        return date_time;
    }

    public static int[] splitTime(String time) {
        int[] s = new int[3];
        if (time.contains(":")) {
            String[] strs = time.split(":");
            if (strs.length >= 2) {
                int hour = Integer.valueOf(strs[0]);
                int minute = Integer.valueOf(strs[1]);
                int second = Integer.valueOf(strs[2]);
                s = new int[]{hour, minute, second};
            }
        }
        return s;
    }

    public static int[] splitDate(String dateFormat) {
        int[] s = new int[3];
        if (dateFormat.contains("-")) {
            String[] strs = dateFormat.split("-");
            if (strs.length >= 2) {
                int year = Integer.valueOf(strs[0]);
                int month = Integer.valueOf(strs[1]);
                int day = Integer.valueOf(strs[2]);
                s = new int[]{year, month, day};
            }
        }
        return s;
    }

    /**
     *  * 返回指定pattern样的日期时间字符串。
     * <p>
     *  *
     * <p>
     *  * @param dt
     * <p>
     *  * @param pattern
     * <p>
     *  * @return 如果时间转换成功则返回结果，否则返回空字符串""
     * <p>
     *  * @author 即时通讯网([url=http://www.52im.net]http://www.52im.net[/url])
     * <p>
     *  
     */

    public static String getTimeString(Date dt, String pattern) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"yyyy-MM-dd HH:mm:ss"sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(dt);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 仿照微信中的消息时间显示逻辑，将时间戳（单位：毫秒）转换为友好的显示格式.
     * <p>
     * <p>
     * <p>
     * 1）7天之内的日期显示逻辑是：今天、昨天(-1d)、前天(-2d)、星期？（只显示总计7天之内的星期数，即<=-4d）；<br>
     * <p>
     * 2）7天之外（即>7天）的逻辑：直接显示完整日期时间。
     *
     * @param srcDate         要处理的源日期时间对象
     * @param mustIncludeTime true表示输出的格式里一定会包含“时间:分钟”，否则不包含（参考微信，不包含时分的情况，用于首页“消息”中显示时）
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     * @since 4.5
     */

    public static String getTimeStringAutoShort2(Date srcDate, boolean mustIncludeTime) {
        String ret = "";
        try {
            //当前时间
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);
            //聊天记录时间
            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);
            // 要额外显示的时间分钟
            String timeExtraStr = (mustIncludeTime ? " " + getTimeString(srcDate, "HH:mm") : "");
            // 当年
            if (currentYear == srcYear) {
                long currentTimestamp = gcCurrent.getTimeInMillis();
                long srcTimestamp = gcSrc.getTimeInMillis();
                // 相差时间（单位：毫秒）
                long delta = (currentTimestamp - srcTimestamp);
                // 当天（月份和日期一致才是）
                if (currentMonth == srcMonth && currentDay == srcDay) {
                    // 时间相差60秒以内
                    if (delta < 60 * 1000) {
                        ret = "刚刚";
                    } else {
                        ret = getTimeString(srcDate, "HH:mm");
                    }// 否则当天其它时间段的，直接显示“时:分”的形式
                } else {
                    // 当年 && 当天之外的时间（即昨天及以前的时间）
                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);
                    // 前天（以“现在”的时候为基准-2天）
                    GregorianCalendar beforeYesterdayDate = new GregorianCalendar();
                    beforeYesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -2);
                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较，是最为准确的（如果用时间戳差值
                    // 的形式，是不准确的，比如：现在时刻是2019年02月22日1:00、而srcDate是2019年02月21日23:00，
                    // 这两者间只相差2小时，直接用“delta/(3600 * 1000)” > 24小时来判断是否昨天，就完全是扯蛋的逻辑了）
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1) && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
                        ret = "昨天" + timeExtraStr;// -1d
                    } else if (srcMonth == (beforeYesterdayDate.get(GregorianCalendar.MONTH) + 1) && srcDay == beforeYesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
                        // “前天”判断逻辑同上
                        ret = "前天" + timeExtraStr;// -2d
                    } else {
                        // 跟当前时间相差的小时数
                        long deltaHour = (delta / (3600 * 1000));
                        // 如果小于 7*24小时就显示星期几
                        if (deltaHour < 7 * 24) {
                            String[] weekday = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                            // 取出当前是星期几
                            String weedayDesc = weekday[gcSrc.get(GregorianCalendar.DAY_OF_WEEK) - 1];
                            ret = weedayDesc + timeExtraStr;
                        } else {
                            // 否则直接显示完整日期时间
                            ret = getTimeString(srcDate, "yyyy/M/d") + timeExtraStr;
                        }
                    }
                }
            } else {
                ret = getTimeString(srcDate, "yyyy/M/d") + timeExtraStr;
            }
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }
        return ret;
    }

    /**
     * 将时长mm:ss装换成毫秒值
     */
    public static long changeTimeStringToLong_ms(String duration) throws ParseException {
        Pattern p = Pattern.compile("(\\d+):(\\d+)");
        Matcher m = p.matcher(duration);

        long milliseconds = 0;

        if (m.find() && m.groupCount() == 2) {
            int minute = Integer.parseInt(m.group(1));
            milliseconds += TimeUnit.MILLISECONDS.convert(minute,
                    TimeUnit.MINUTES);
            int second = Integer.parseInt(m.group(2));

            milliseconds += TimeUnit.MILLISECONDS.convert(second,
                    TimeUnit.SECONDS);
        } else {
            throw new ParseException("Cannot parse duration " + duration, 0);
        }

        return milliseconds;
    }


    /**
     * 将时长HH:mm装换成毫秒
     */
    public static long changeTimeStringToLong_hm(String duration) throws ParseException {
        Pattern p = Pattern.compile("(\\d+):(\\d+)");
        Matcher m = p.matcher(duration);

        long milliseconds = 0;

        if (m.find() && m.groupCount() == 2) {
            int hours = Integer.parseInt(m.group(1));
            milliseconds += TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);

            int minutes = Integer.parseInt(m.group(2));
            milliseconds += TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);
        } else {
            throw new ParseException("Cannot parse duration " + duration, 0);
        }

        return milliseconds;
    }

    /**
     * 将毫秒转换成mm:ss格式或hh:mm:ss
     *
     * @param time
     * @return
     */
    public static String changeLongToTimeString(int time) {
        // make ms change to s
        time /= 1000;
        int hour;
        int minute;
        int second;
        if (time >= 3600) {
            hour = time / 3600;
            minute = (time % 3600) / 60;
            second = (time % 3600) % 60;// time-hour*3600-minute*60
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            minute = time / 60;
            second = time % 60;
            return String.format("%02d:%02d", minute, second);
        }
    }

    /**
     * "yyyy-MM-dd HH:mm:ss" "2013-11-29 15:24:00"
     */
    public static long changeDateStringToLong(String pattern, String dateString) {
        long time = 0;
        try {
            time = new SimpleDateFormat(pattern).parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * long类型转换成时间格式 2015-11-12 15：12：11
     */
    public static String changeLongToDateString(String pattern, long dateTime) {
        String time;
        time = new SimpleDateFormat(pattern).format(new Date(dateTime));
        return time;
    }

    /**
     * 将2019-5-5 转换成 2019年5月5日格式，请求服务器格式为年月日
     *
     * @param birthday
     * @return
     */
    public static String changeBirthdayFormat(String birthday) {
        if (birthday.contains("-")) {
            String[] strs = birthday.split("-");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(strs[0]).append("年").append(strs[1]).append("月").append(strs[2]).append("日");
            return stringBuilder.toString();
        } else {
            return birthday;
        }
    }


    /**
     * 生日装换成年龄
     *
     * @param birthday 2019年9月9日
     * @return
     */
    public static String birthday2Age(String birthday) {
        LoggerUtils.d(TAG, "birthday before: " + birthday);
        if (birthday == null || birthday.isEmpty()) {
            return "";
        }
        birthday = formatBirthday(birthday);
        String[] ss = birthday.split("\\s+");
        if (ss != null && ss.length > 0) {
            birthday = ss[0];
        }

        int years = 0;
        int months = 0;
        int days = 0;
        try {
            //String类型转换为date类型
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(birthday);
            calendar.setTime(date);
            if (date != null) {
                //date类型转成long类型
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                // 得到当前时间的年、月、日
                Calendar cal = Calendar.getInstance();
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH) + 1;
                int dayNow = cal.get(Calendar.DAY_OF_MONTH);
                LoggerUtils.d("yearNow：" + yearNow);
                LoggerUtils.d("monthNow：" + monthNow);
                LoggerUtils.d("dayNow：" + dayNow);
                // 用当前年月日减去出生年月日
                years = yearNow - year;
                months = monthNow - month;
                days = dayNow - day;
                if(days<0){
                    months --;
                    int prevMonth = monthNow - 1;
                    if(prevMonth == 1 || prevMonth == 3 || prevMonth == 5 || prevMonth == 7
                            || prevMonth == 8 || prevMonth == 10 || prevMonth == 12){
                          days = days + 31;
                    }else if(prevMonth == 4 || prevMonth == 6 || prevMonth == 9 || prevMonth == 11){
                        days = days + 30;
                    }else if(yearNow%4 == 0 && yearNow%100 == 0){
                        days = days + 29;
                    }else {
                        days = days + 28;
                    }
                    //上一年的下一个月或当月
                    if (months <= 0 && year > 0) {
                        months = 12 + months;
                        years--;
                    } else {
                        months--;
                    }
                } else {
                    if (years > 0 && months < 0) {
                        months = 12 + months;
                        years--;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerUtils.d(TAG, "year= " + years + ",mounth= " + months + ",day = " + days);
        StringBuilder sb = new StringBuilder();
        if (years > 0) {
            sb.append(years + "岁");
        }
        if (months > 0) {
            sb.append(months + "个月");
        }
        if (days > 0) {
            sb.append(days + "天");
        }
        return sb.toString();
    }

    /**
     * 将年月日转2019-10-30
     *
     * @return
     */
    public static String formatBirthday(String birthday) {
        String yearStr = birthday.substring(0, 4);
        String monthStr = birthday.substring(5, 7);
        String dayStr = birthday.substring(8, 10);
        birthday = yearStr + "-" + monthStr + "-" + dayStr;
        LoggerUtils.d(TAG, "birthday after: " + birthday);
        return birthday;
    }

    /**
     * 65      * String类型转换为long类型
     * 66      * .............................
     * 67      * strTime为要转换的String类型时间
     * 68      * formatType时间格式
     * 69      * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * 70      * strTime的时间格式和formatType的时间格式必须相同
     * 71
     */
    public static void stringToInt(String strTime, String formatType) {
        int year = 0;
        int month = 0;
        int day = 0;
        try {
            //String类型转换为date类型
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date date = formatter.parse(strTime);
            calendar.setTime(date);
            if (date == null) {
            } else {
                //date类型转成long类型
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
            // 得到当前时间的年、月、日
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DATE);
            LoggerUtils.d("yearNow：" + yearNow);
            LoggerUtils.d("monthNow：" + monthNow);
            LoggerUtils.d("dayNow：" + dayNow);
            // 用当前年月日减去出生年月日
            int yearMinus = yearNow - year;
            int monthMinus = monthNow - month;
            int dayMinus = dayNow - day;

        } catch (Exception e) {

        }
    }
}
