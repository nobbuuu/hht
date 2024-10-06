package com.booyue.view.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.booyue.view.R;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2016/12/21
 * <p>
 * 生日view的封装
 */
public class BirthdayWheelView extends LinearLayout {
    private int YEAR_MAX_LENGTH = 111;
    private int MONTH_MAX_LENGTH = 12;
    private String[] years = new String[YEAR_MAX_LENGTH];
    private String[] months = new String[MONTH_MAX_LENGTH];
    private String[] day31s = new String[31];
    private String[] day30s = new String[30];
    private String[] day29s = new String[29];
    private String[] day28s = new String[28];
    private String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private String[] months_little = {"4", "6", "9", "11"};
    List<String> list_big = Arrays.asList(months_big);
    List<String> list_little = Arrays.asList(months_little);

    public static final int START_YEAR = 2000;
    public static int END_YEAR  = 2100;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    public String birthdayText;
    public String birthdayNum;
    private DecimalFormat decimal;


    public BirthdayWheelView(Context context) {
        super(context);
        initLayout(context);
    }

    public BirthdayWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public BirthdayWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_wheel_birthday, this);
        wv_year = (WheelView) view.findViewById(R.id.wv_year);
        wv_month = (WheelView) view.findViewById(R.id.wv_month);
        wv_day = (WheelView) view.findViewById(R.id.wv_day);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        String parten = "00";
        decimal = new DecimalFormat(parten);
        for (int i = 0; i < YEAR_MAX_LENGTH; i++) {
            years[i] = 1990 + i + "年";
        }
        for (int i = 0; i < MONTH_MAX_LENGTH; i++) {
            months[i] = 1 + i + "月";
        }
        for (int i = 0; i < 30; i++) {
            day30s[i] = 1 + i + "日";
        }
        for (int i = 0; i < 29; i++) {
            day29s[i] = 1 + i + "日";
        }
        for (int i = 0; i < 31; i++) {
            day31s[i] = 1 + i + "日";
        }
        for (int i = 0; i < 28; i++) {
            day28s[i] = 1 + i + "日";
        }
        END_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
//        wv_year.setCyclic(true);
        wv_year.setLabel("年");

        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
//        wv_month.setCyclic(true);
        wv_month.setLabel("月");

//        wv_day.setCyclic(true);
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            } else {
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
        }
        wv_day.setLabel("日");

        /**
         * 年份滚动监听
         */
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
            }
        };
        /**
         * 月份滚动监听
         */
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
        /**
         * 默认字体大小
         */
        int textSize = (int) context.getResources().getDimension(R.dimen.wheelview_item_textsize);
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        setDate(year,month,day);
    }

    private static final String TAG = "BirthdayView";
    /**
     * 设置显示日期
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year,int month,int day){
        Log.d(TAG, "year: " + year + ",month:" + month + ",day: " + day);
        wv_year.setCurrentItem(year - START_YEAR);
        wv_month.setCurrentItem(month);
        wv_day.setCurrentItem(day - 1);
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
    }

    /**
     * 更新文本
     */
    public void getBirthdayTextAndNum() {
        birthdayText = (wv_year.getCurrentItem() + START_YEAR) + "年"
                + decimal.format(wv_month.getCurrentItem() + 1) + "月"
                + decimal.format((wv_day.getCurrentItem() + 1)) + "日";

        birthdayNum = (wv_year.getCurrentItem() + START_YEAR) + "-"
                + decimal.format(wv_month.getCurrentItem() + 1) + "-"
                + decimal.format((wv_day.getCurrentItem() + 1));
    }

    public String getBirthdayText() {
        return birthdayText;
    }

    public String getBirthdayNum() {
        return birthdayNum;
    }

}
