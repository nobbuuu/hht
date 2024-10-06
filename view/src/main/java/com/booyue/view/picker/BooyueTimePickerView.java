package com.booyue.view.picker;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.BooyueWheelTime;
import com.bigkoo.pickerview.view.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: wangxinhua
 * @date: 2019/2/28
 * @description :
 *
 */
//使用案例
//fun showPickerView(){
//        val pvTime = BooyueTimePickerView.Builder(this, BooyueTimePickerView.OnTimeSelectListener { date2, v ->
//        //选中事件回调
//        val time = getTime(date2)
////            button3.setText(time)
//        })
//        .setType(TimePickerView.Type.HOURS_MINS)//默认全部显示
////                .setCancelText("取消")//取消按钮文字
////                .setSubmitText("确定")//确认按钮文字
//        .setContentSize(20)//滚轮文字大小
////                .setTitleText("请选择时间")//标题文字
////                .setTitleColor(Color.BLACK)//标题文字颜色
////                .setTitleSize(20)//标题文字大小
//        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//        .isCyclic(false)//是否循环滚动
//        .setTextColorCenter(Color.RED)//设置选中项的颜色
//        .setTextColorOut(Color.GRAY)
////                .setLayoutRes(R.layout.booyue_pickerview_time_layout) {
////                    var layoutParmas = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
////                    it.layoutParams = layoutParmas
////                    wheelView = it as LinearLayout
////                    rootView.addView(wheelView)
//////                    rootView.addView(it)
////                }
//        .setLayoutRes(wheelView){
//
//        }
////                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
////                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//        //                        .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
////                .setBgColor(R.color.color_d8d8d8)//滚轮背景颜色 Night mode
//        //                        .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
//        //                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//        //                        .setRangDate(startDate,endDate)//起始终止年月日设定
//        .setLabel("年", "月", "日", "时", "分", "秒")
//        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//        .isDialog(false)//是否显示为对话框样式
//        .build()
//        pvTime.setDate(Calendar.getInstance())//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
////        pvTime.showLoaingDialog() 对话框需要显示
//        var layoutParmas = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        layoutParmas.gravity = Gravity.CENTER
//        wheelView.layoutParams = layoutParmas
//        rootView.addView(wheelView)
//        }
public class BooyueTimePickerView extends BasePickerView {

//    private int layoutRes;
    private View view;
//    private CustomListener customListener;

//    public enum Type {
//        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH, YEAR_MONTH_DAY_HOUR_MIN
//    } // 六种选择模式，年月日时分秒，年月日，时分，月日时分，年月，年月日时分

    BooyueWheelTime wheelTime; //自定义控件

    private BooyueTimePickerView.OnTimeSelectListener timeSelectListener;//回调接口
    private int gravity = Gravity.CENTER;//内容显示位置 默认居中
    private TimePickerView.Type type;// 显示类型

    private int Color_Background_Wheel;//滚轮背景颜色

    private int Size_Content;//内容字体大小

    private Calendar date;//当前选中时间
    private Calendar startDate;//开始时间
    private Calendar endDate;//终止时间
    private int startYear;//开始年份
    private int endYear;//结尾年份

    private boolean cyclic;//是否循环
    private boolean cancelable;//是否能取消
    private boolean isCenterLabel ;//是否只显示中间的label

    private int textColorOut; //分割线以外的文字颜色
    private int textColorCenter; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色

    // 条目间距倍数 默认1.6
    private float lineSpacingMultiplier = 1.6F;
    private boolean isDialog;//是否是对话框模式
    private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;
    private WheelView.DividerType dividerType;//分隔线类型


//    public BooyueTimePickerView(Context context) {
//        super(context);
//    }
    //构造方法
    public BooyueTimePickerView(BooyueTimePickerView.Builder builder) {
        super(builder.context);
        this.timeSelectListener = builder.timeSelectListener;
        this.gravity = builder.gravity;
        this.type = builder.type;
        this.Color_Background_Wheel = builder.Color_Background_Wheel;
        this.Size_Content = builder.Size_Content;
        this.startYear = builder.startYear;
        this.endYear = builder.endYear;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.date = builder.date;
        this.cyclic = builder.cyclic;
        this.isCenterLabel = builder.isCenterLabel;
        this.cancelable = builder.cancelable;
        this.label_year = builder.label_year;
        this.label_month = builder.label_month;
        this.label_day = builder.label_day;
        this.label_hours = builder.label_hours;
        this.label_mins = builder.label_mins;
        this.label_seconds = builder.label_seconds;
        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
//        this.customListener = builder.customListener;
//        this.layoutRes = builder.layoutRes;
        this.view = builder.view;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.isDialog = builder.isDialog;
        this.dividerType = builder.dividerType;
        initView(builder.context);
    }


    //建造器
    public static class Builder {
//        private int layoutRes = R.layout.pickerview_time;
        private View view;
//        private CustomListener customListener;
        private Context context;
        private BooyueTimePickerView.OnTimeSelectListener timeSelectListener;

        private TimePickerView.Type type = TimePickerView.Type.ALL;//显示类型 默认全部显示
        private int gravity = Gravity.CENTER;//内容显示位置 默认居中

        private int Color_Background_Wheel;//滚轮背景颜色
        private int Color_Background_Title;//标题背景颜色

        private int Size_Submit_Cancel = 17;//确定取消按钮大小
        private int Size_Title = 18;//标题字体大小
        private int Size_Content = 18;//内容字体大小
        private Calendar date;//当前选中时间
        private Calendar startDate;//开始时间
        private Calendar endDate;//终止时间
        private int startYear;//开始年份
        private int endYear;//结尾年份

        private boolean cyclic = false;//是否循环
        private boolean cancelable = true;//是否能取消
        private boolean isCenterLabel = true ;//是否只显示中间的label

        private int textColorOut; //分割线以外的文字颜色
        private int textColorCenter; //分割线之间的文字颜色
        private int dividerColor; //分割线的颜色
        private WheelView.DividerType dividerType;//分隔线类型
        // 条目间距倍数 默认1.6
        private float lineSpacingMultiplier = 1.6F;

        private boolean isDialog;//是否是对话框模式

        private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;//单位

        //Required
        public Builder(Context context, BooyueTimePickerView.OnTimeSelectListener listener) {
            this.context = context;
            this.timeSelectListener = listener;
        }

        //Option
        public BooyueTimePickerView.Builder setType(TimePickerView.Type type) {
            this.type = type;
            return this;
        }
         public BooyueTimePickerView.Builder setDateType() {
            this.type = TimePickerView.Type.YEAR_MONTH_DAY;
            return this;
        }
         public BooyueTimePickerView.Builder setTimeType() {
            this.type = TimePickerView.Type.HOURS_MINS;
            return this;
        }


        public BooyueTimePickerView.Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }


        public BooyueTimePickerView.Builder isDialog(boolean isDialog) {
            this.isDialog = isDialog;
            return this;
        }

        public BooyueTimePickerView.Builder setBgColor(int Color_Background_Wheel) {
            this.Color_Background_Wheel = Color_Background_Wheel;
            return this;
        }

        public BooyueTimePickerView.Builder setSubCalSize(int Size_Submit_Cancel) {
            this.Size_Submit_Cancel = Size_Submit_Cancel;
            return this;
        }

        public BooyueTimePickerView.Builder setTitleSize(int Size_Title) {
            this.Size_Title = Size_Title;
            return this;
        }

        public BooyueTimePickerView.Builder setContentSize(int Size_Content) {
            this.Size_Content = Size_Content;
            return this;
        }

        /**
         * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * @param date
         * @return
         */
        public BooyueTimePickerView.Builder setDate(Calendar date) {
            this.date = date;
            return this;
        }

//        public BooyueTimePickerView.Builder setLayoutRes(int res, CustomListener customListener) {
//            this.layoutRes = res;
//            this.customListener = customListener;
//            return this;
//        }
             public BooyueTimePickerView.Builder setLayoutRes(View view) {
            this.view = view;
//            this.customListener = customListener;
            return this;
        }



        public BooyueTimePickerView.Builder setRange(int startYear, int endYear) {
            this.startYear = startYear;
            this.endYear = endYear;
            return this;
        }

        /**
         * 设置起始时间
         * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * @return
         */

        public BooyueTimePickerView.Builder setRangDate(Calendar startDate, Calendar endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            return this;
        }


        /**
         * 设置间距倍数,但是只能在1.2-2.0f之间
         *
         * @param lineSpacingMultiplier
         */
        public BooyueTimePickerView.Builder setLineSpacingMultiplier(float lineSpacingMultiplier) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        /**
         * 设置分割线的颜色
         *
         * @param dividerColor
         */
        public BooyueTimePickerView.Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * 设置分割线的类型
         *
         * @param dividerType
         */
        public BooyueTimePickerView.Builder setDividerType(WheelView.DividerType dividerType) {
            this.dividerType = dividerType;
            return this;
        }

        /**
         * 设置分割线之间的文字的颜色
         *
         * @param textColorCenter
         */
        public BooyueTimePickerView.Builder setTextColorCenter(int textColorCenter) {
            this.textColorCenter = textColorCenter;
            return this;
        }

        /**
         * 设置分割线以外文字的颜色
         *
         * @param textColorOut
         */
        public BooyueTimePickerView.Builder setTextColorOut(int textColorOut) {
            this.textColorOut = textColorOut;
            return this;
        }

        public BooyueTimePickerView.Builder isCyclic(boolean cyclic) {
            this.cyclic = cyclic;
            return this;
        }

        public BooyueTimePickerView.Builder setOutSideCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public BooyueTimePickerView.Builder setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
            this.label_year = label_year;
            this.label_month = label_month;
            this.label_day = label_day;
            this.label_hours = label_hours;
            this.label_mins = label_mins;
            this.label_seconds = label_seconds;
            return this;
        }

        public BooyueTimePickerView.Builder isCenterLabel(boolean isCenterLabel) {
            this.isCenterLabel = isCenterLabel;
            return this;
        }


        public BooyueTimePickerView build() {
            return new BooyueTimePickerView(this);
        }
    }


    private void initView(Context context) {
//        setDialogOutSideCancelable(cancelable);
        // TODO: 2019/2/28
//        initViews();


        init();
        initEvents();
//        LinearLayout timePickerView = null;
//        if (customListener == null) {
//            LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
//            timePickerView = (LinearLayout) findViewById(R.id.timepicker);
//        } else {
////            View view = LayoutInflater.from(context).inflate(layoutRes,null);
////            customListener.customLayout(LayoutInflater.from(context).inflate(layoutRes,null));
//             timePickerView =  view.findViewById(R.id.timepicker);
//        }
        // 时间转轮 自定义控件

//        View view = LayoutInflater.from(context).inflate(R.layout.booyue,null);
        view.setBackgroundColor(Color_Background_Wheel == 0 ? bgColor_default : Color_Background_Wheel);

        wheelTime = new BooyueWheelTime(view, type, gravity, Size_Content);

        if (startYear != 0 && endYear != 0 && startYear <= endYear) {
            setRange();
        }

        if (startDate != null && endDate != null) {
            if (startDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
                setRangDate();
            }
        } else if (startDate != null && endDate == null) {
            setRangDate();
        } else if (startDate == null && endDate != null) {
            setRangDate();
        }

        setTime();
        wheelTime.setLabels(label_year, label_month, label_day, label_hours, label_mins, label_seconds);

        setOutSideCancelable(cancelable);
        wheelTime.setCyclic(cyclic);
        wheelTime.setDividerColor(dividerColor);
        wheelTime.setDividerType(dividerType);
        wheelTime.setLineSpacingMultiplier(lineSpacingMultiplier);
        wheelTime.setTextColorOut(textColorOut);
        wheelTime.setTextColorCenter(textColorCenter);
        wheelTime.isCenterLabel(isCenterLabel);
    }



    /**
     * 设置默认时间
     */
    public void setDate(Calendar date) {
        this.date = date;
        setTime();
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRange() {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);

    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate() {
        wheelTime.setRangDate(startDate, endDate);
        //如果设置了时间范围
        if (startDate != null && endDate != null) {
            //判断一下默认时间是否设置了，或者是否在起始终止时间范围内
            if (date == null||date.getTimeInMillis() < startDate.getTimeInMillis()
                    || date.getTimeInMillis() > endDate.getTimeInMillis()){
                date = startDate;
            }
        } else if (startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            date = startDate;
        } else if (endDate != null) {
            date = endDate;
        }
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year,month,day,hours,minute,seconds;

        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH);
            hours = date.get(Calendar.HOUR_OF_DAY);
            minute = date.get(Calendar.MINUTE);
            seconds = date.get(Calendar.SECOND);
        }


        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }

    public void returnData() {
        if (timeSelectListener != null) {
            try {
                Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                timeSelectListener.onTimeSelect(date, clickView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        dismiss();
    }

    public String getTime(){
        return wheelTime.getTime();
    }


    public interface OnTimeSelectListener {
        void onTimeSelect(Date date, View v);
    }

    @Override
    public boolean isDialog() {
        return isDialog;
    }




}
