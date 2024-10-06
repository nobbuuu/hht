package com.booyue.media.video.ijk;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.booyue.base.util.LoggerUtils;
import com.booyue.media.R;

/**
 * Created by XiaoJianjun on 2017/6/21.
 * 仿腾讯视频热点列表页播放器控制器.
 */
public class HHTVideoPlayerControllerExt
        extends HHTVideoPlayerController {
    private static final String TAG = "HHTVideoPlayerControlle";
    /**
     * controllerView新增喜爱，收藏按钮
     */
    private ImageButton ibShare;
    private ImageButton ibLike;
//    private ImageButton ibPrev;
    private ImageButton ibNext;



    public HHTVideoPlayerControllerExt(Context context) {
        super(context);
    }

    @Override
    public void initCustomView(){
        /**初始化view*/
        ibShare = findViewById(R.id.ibShare);
        ibLike = findViewById(R.id.ibLike);
//        ibPrev = findViewById(R.id.prev);
        ibNext = findViewById(R.id.next);
        /**
         * 设置监听
         */
        ibShare.setOnClickListener(this);
        ibLike.setOnClickListener(this);
        ibNext.setOnClickListener(this);
    }

    @Override
    public void onCustomViewClick(View v) {
        /*else if(v == ibPrev){
            if(mOnCustomViewClickListener != null){
                mOnCustomViewClickListener.onPrevClick();
            }
        }*/ if(v == ibNext){
            if(mOnCustomViewClickListener != null){
                mOnCustomViewClickListener.onNextClick();
            }
        }
    }

    @Override
    public void initModeFullScreenView() {
        super.initModeFullScreenView();
        ibLike.setVisibility(View.VISIBLE);
        ibNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void initModeNormalView() {
        super.initModeNormalView();
        ibLike.setVisibility(View.GONE);
        ibNext.setVisibility(View.GONE);
    }

    @Override
    public void onFinish() {
        if(mOnCustomViewClickListener != null){
            mOnCustomViewClickListener.onBackClick();
        }
    }

    /**
     * 设置喜爱图标
     *
     * @param isLike
     */
    public void setLikeViewImage(boolean isLike) {
        LoggerUtils.d(TAG, ": setLikeViewImage " + isLike);
        if (ibLike != null) {
            if (isLike) {
                ibLike.setImageResource(R.drawable.ic_like_hov);
            } else {
                ibLike.setImageResource(R.drawable.ic_like_nor);
            }
        }
    }

    /**
     * 自定义view点击监听器
     */
    public interface OnCustomViewClickListener{
        void onPrevClick();
        void onNextClick();
        void onBackClick();
    }
    private OnCustomViewClickListener mOnCustomViewClickListener;

    /**
     * 外部设置UI点击监听
     * @param onCustomViewClickListener
     */
    public void setOnCustomViewClickListener(OnCustomViewClickListener onCustomViewClickListener){
        this.mOnCustomViewClickListener = onCustomViewClickListener;
    }
}
