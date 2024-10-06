package com.booyue.poetry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.request.RequestManager;

/**
 * @author: wangxinhua
 * @date: 2020/6/23 8:40
 * @description :
 */
public class BookView extends FrameLayout {
    private static final String TAG = "BookView";
    //一般状态
    public static final int STATE_NORMAL = 1;
    //编辑状态，显示复选框
    public static final int STATE_EDIT = 2;
    //下载状态，显示下载按钮，mask全屏
    public static final int STATE_DOWNLOAD = 3;
    //添加图标
    public static final int STATE_ADD = 4;
    public BookView(Context context) {
        this(context,null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private Context context;
    private ImageView ivBookCoverImage;
    private FrameLayout flDownload;
    private ImageView ivMask;
    private FrameLayout flMask;
    private TextView tvGrade;
    private TextView tvVersion;
    private CheckBox cbCheck;
    public void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_book,this);
        ivBookCoverImage = findViewById(R.id.rivBookCoverImage);
        flDownload = findViewById(R.id.flDownload);
        flDownload.setOnClickListener(view -> {
            if(onDownloadClickListener != null){
                onDownloadClickListener.onClick(view);
            }
        });
        ivMask = findViewById(R.id.ivMask);
        flMask = findViewById(R.id.flMask);
        tvGrade = findViewById(R.id.tvGrade);
        tvVersion = findViewById(R.id.tvVersion);
        cbCheck = findViewById(R.id.cbCheck);
    }

    /**
     * 更新状态
     * @param state
     */
    public BookView updateState(int state){
        LoggerUtils.d(TAG, "updateState: " + state);
        if(state == STATE_NORMAL){//一般状态
            flMask.setVisibility(VISIBLE);
            ivMask.setVisibility(VISIBLE);
            ivMask.setImageResource(R.drawable.bg_book_half);
            tvGrade.setVisibility(View.VISIBLE);
            tvVersion.setVisibility(View.VISIBLE);
            cbCheck.setVisibility(GONE);
        }else if(state == STATE_EDIT){//编辑状态
            flMask.setVisibility(VISIBLE);
            ivMask.setVisibility(VISIBLE);
            ivMask.setImageResource(R.drawable.bg_book_half);
            tvGrade.setVisibility(View.VISIBLE);
            tvVersion.setVisibility(View.VISIBLE);
            cbCheck.setVisibility(VISIBLE);
        }else if(state == STATE_ADD){
            flMask.setVisibility(GONE);
            ivMask.setVisibility(GONE);
            tvGrade.setVisibility(View.GONE);
            tvVersion.setVisibility(View.GONE);
            cbCheck.setVisibility(GONE);
        }
        return this;
    }

//    public BookView setDownloadVisible(int visible){
//        flDownload.setVisibility(visible);
//        return this;
//    }
    /**
     * 设置网路图片
     */
    public BookView loadImage(String coverImage){
        if(ivBookCoverImage == null)return this;
        LoggerUtils.d(TAG, "loadImage: ");
        RequestManager.getInstance().loadImage(coverImage,ivBookCoverImage, RequestManager.ImageSize.SIZE_RAW);
        return this;
    }

    /**
     * 加载本地图片
     * @param coverImage
     * @return
     */
    public BookView setImageResource(int coverImage){
        if(ivBookCoverImage == null)return this;
        LoggerUtils.d(TAG, "setImageResource: ");
        ivBookCoverImage.setImageResource(coverImage);
        return this;
    }

    /**
     * 设置年级和版本信息
     * @param grade
     * @param version
     * @return
     */
    public BookView setGradeAndVersion(String grade,String version){
        if(tvGrade == null && tvVersion == null)return this;
        tvGrade.setText(grade);
        tvVersion.setText(version);
        flMask.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 检测是否选中
     */
    public void setCbCheck(boolean check){
        cbCheck.setChecked(check);
    }
    public BookView setCheckListener(OnClickListener onClickListener){
//        cbCheck.setVisibility(View.VISIBLE);
        cbCheck.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 监听下载按钮
     */
    private OnClickListener onDownloadClickListener;
    public BookView setOnDownloadClickListener(OnClickListener onDownloadClickListener){
        flDownload.setVisibility(View.VISIBLE);
        this.onDownloadClickListener = onDownloadClickListener;
        return this;
    }
}
