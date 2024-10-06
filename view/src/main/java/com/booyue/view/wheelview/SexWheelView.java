package com.booyue.view.wheelview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.booyue.view.R;

/**
 * Created by Administrator on 2016/12/21.
 */
public class SexWheelView extends LinearLayout {
    private static final String TAG = "SexWheelView";
    private WheelView wvSex;//性别
    private String mCurSex;
    private Context mContext;


    public SexWheelView(Context context) {
        super(context);
        initLayout(context);
    }

    public SexWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public SexWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        Log.d(TAG, "initLayout()");
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_wheel_sex, this);
        wvSex = view.findViewById(R.id.wv_sex);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）=720
        LayoutParams paramP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        wvSex.setLayoutParams(paramP);
        wvSex.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mCurSex = mSexArray[newValue];
            }
        });
        int textSize = (int) context.getResources().getDimension(R.dimen.wheelview_item_textsize);
        wvSex.TEXT_SIZE = textSize;
        wvSex.setAdapter(new ArrayWheelAdapter<>(mSexArray));
    }


    /**
     * 对json进行解析
     */
    private String[] mSexArray = new String[]{"男","女"};


    /**
     * 设置指定的地区
     * @param sex 男 女
     */
    public void setInitSex(String sex){
        int sexIndex = 0;
        for (int i = 0; i < mSexArray.length; i++) {
            if(mSexArray[i].equals(sex)){
                sexIndex = i;
                break;
            }
        }
        wvSex.setCurrentItem(sexIndex);
        mCurSex = sex;
    }
    /**
     * 获取当前性别
     * @return
     */

    public String getCurrentSex() {
        return mCurSex;
    }


}
