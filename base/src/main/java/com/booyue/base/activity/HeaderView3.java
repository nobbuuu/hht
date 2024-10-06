package com.booyue.base.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.booyue.base.R;
import com.booyue.base.util.TypefaceUtils;


/**
 * Created by Administrator on 2016/10/20.
 *
 * 头布局
 */
public class HeaderView3 extends Toolbar {

    private ImageButton ibLeft;
    private ImageButton ibRight1;
    private ImageButton ibRight2;
    private TextView tvTitle;
    private TextView tvRight;

    public HeaderView3(Context context) {
        super(context);
        initLayout(context);
    }

    public HeaderView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public HeaderView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }


    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.headerview3, null);
//        int width = WindowsUtils.getDisplayWidth((Activity) context);
//        int height = WindowsUtils.getDisplayHeight((Activity) context);
//        if(width == 1080 && height == 1821 && Build.VERSION.SDK_INT == Build.VERSION_CODES.N){
//            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        }
        ibLeft = (ImageButton) view.findViewById(R.id.ib_left);
        ibRight1 = (ImageButton) view.findViewById(R.id.ib_right1);
        ibRight2 = (ImageButton) view.findViewById(R.id.ib_right2);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        ibLeft.setColorFilter(Color.BLACK);
        tvTitle.setTypeface(TypefaceUtils.setTypeface(context));
        tvRight.setTypeface(TypefaceUtils.setTypeface(context));
        Toolbar.LayoutParams layoutparams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        addView(view, layoutparams);
        setContentInsetsRelative(10,10);
    }

    /**
     * 设置标题
     * @param text
     */
    public void setText(String text){
        tvTitle.setText(text);
    }
    public void setText(int resId){
        tvTitle.setText(resId);
    }
    public void setTextColor(int resId){
        tvTitle.setTextColor(resId);
    }

    /**
     * 设置监听
     */
    public void setLeftListener(final Activity activity){
        ibLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
    }
    public void setLeftListener(OnClickListener listener){
        ibLeft.setOnClickListener(listener);
    }
    public void setLeftImageVisible(int visible){
        ibLeft.setVisibility(visible);
    }


    /**
     * 存在webview的返回处理
     * @param listenerWebview
     */
    public void setLeftListenerWebview(OnClickListener listenerWebview){
        ibLeft.setOnClickListener(listenerWebview);
    }
    public void setRightListener(OnClickListener listener){
        ibRight1.setOnClickListener(listener);
    }
    /**
     * 设置左侧图片
     */
    public void setLeftImage(Bitmap bitmap){
        ibLeft.setImageBitmap(bitmap);
    }
    public void setLeftImage(int  resId){
        ibLeft.setImageResource(resId);
    }
    public void setLeftImageColor(int color){
        ibLeft.setColorFilter(color);
    }
    public void setRightImageColor(int color){
        ibRight1.setColorFilter(color);
    }


    public void setRightTextColor(int color){
        tvRight.setTextColor(color);
    }
    /**
     * 设置右侧图片
     * @param bitmap
     */
    public void setRightImage(Bitmap bitmap){
        ibRight1.setImageBitmap(bitmap);
    }
    public void setRightImage(int resId){
        ibRight1.setImageResource(resId);
    }

    public void setRight2Image(Bitmap bitmap){
        ibRight2.setVisibility(View.VISIBLE);
        ibRight2.setImageBitmap(bitmap);
    }
    public void setRight2Image(int resId){
        ibRight2.setVisibility(View.VISIBLE);
        ibRight2.setImageResource(resId);
    }
    public void setRight2Listener(OnClickListener listener){
        ibRight2.setOnClickListener(listener);
    }
    public void setRight2ImageColor(int color){
        ibRight2.setColorFilter(color);
    }


    /**
     * 获取左右图片
     * @return
     */
//    public ImageButton getImageLeft(){
//        return ibLeft;
//    }
//    public ImageButton getImageRight(){
//        return ibRight1;
//    }

    /**
     * 右侧文字和监听
     * @param text
     * @param listener
     */
    public void setRightTextAndListener(int text,OnClickListener listener){
        tvRight.setText(text);
        tvRight.setOnClickListener(listener);
    }

    public void setRightText(String text){
        tvRight.setText(text);
    }
     public void setRightText(int text){
        tvRight.setText(text);
    }

}
