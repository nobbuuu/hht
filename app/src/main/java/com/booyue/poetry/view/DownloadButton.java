package com.booyue.poetry.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.booyue.base.util.LoggerUtils;
import com.booyue.database.download.Conf;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;

/**
 * Created by Administrator on 2018/7/23.15:23
 * <p>
 * 封装下载按钮
 */

public class DownloadButton extends FrameLayout {
    private static final String TAG = "DownloadButton";
    private ProgressBar pbProgress;
    private ImageView ivDownloadControl;
    private View rootView;
    public TextView tvPercent;
    TextView tvCancel;
    TextView tvComplete;
    private Context context;
    private DownloadBean downloadBean;


    public DownloadButton(@NonNull Context context) {
        this(context, null);
    }

    public DownloadButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    public DownloadButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_download_button, this);
        rootView = findViewById(R.id.fl_root);
        pbProgress = findViewById(R.id.progress);
        tvPercent = findViewById(R.id.tvPercent);
        tvCancel = findViewById(R.id.tvCancel);
        tvComplete = findViewById(R.id.tvComplete);
        this.context = context;
        ivDownloadControl = findViewById(R.id.ivDownloadControl);
        ivDownloadControl.setOnClickListener(view -> {
            if (downloadBean != null) {
                resumeOrPauseDownload(downloadBean);
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCancelClickListener != null){
                    onCancelClickListener.onClick(view);
                }
            }
        });
    }
    private OnClickListener onCancelClickListener;
    public void initState(DownloadBean downloadBean,OnClickListener onCancelClickListener) {
        this.downloadBean = downloadBean;
        updateState(downloadBean,downloadBean.state);
        this.onCancelClickListener = onCancelClickListener;
    }


    /**
     * 设置下载进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        if (pbProgress != null) {
            pbProgress.setProgress(progress);
        }
    }

    /**
     * 设置按钮图标
     *
     * @param imageRes
     */
    public void setImageView(int imageRes) {
        if (ivDownloadControl != null) {
            ivDownloadControl.setImageResource(imageRes);
        }
    }


    /**
     * 初始化重置之后的状态
     *
     * @param downloadBean 数据库实体类
     * @param appInfo      当前服务器返回实体类
     */
//    public void initDownloadViewForReset(DownloadBean downloadBean, DownloadBean appInfo, int stateDes) {
//        if (appInfo == null) {
//            MyApp.getInstance().getDownloadHelper().removeTaskByid(appInfo.guid);
//        }
//        if (downloadBean != null) {
//            File file = new File(downloadBean.localPath);
//            file.deleteOnExit();
//            DownloadDao.getInstance().delete(downloadBean);
//        }
//        setText(stateDes);
//    }


    /**
     * 播放或者暂停下载
     */
    public void resumeOrPauseDownload(DownloadBean bean) {
        //3、正在等待或正在下载,执行暂停
        if (!MyApp.getInstance().getDownloadHelper().isTaskManaged(bean.guid)) {
            return;
        }
        if (MyApp.getInstance().getDownloadHelper().IsTaskRunning(bean.guid)) {
            MyApp.getInstance().getDownloadHelper().Pause(bean.guid);
        } else {
            //4、已暂停 ，执行继续
            MyApp.getInstance().getDownloadHelper().Resume(bean.guid);
        }
    }

    private boolean isError = false;

    /**
     * 更新下载状态
     *
     * @param
     * @param state        下载状态 \
     *                     {@link Conf#STATE_TASK_WAIT} 等待中
     *                     {@link Conf#STATE_TASK_PAUSE} 暂停
     *                     {@link Conf#STATE_TASK_COMPLETE} 完成
     *                     {@link Conf#STATE_TASK_PROCESSING} 下载中。。。
     *                     {@link Conf#STATE_TASK_ERROR} 下载错误
     *                     {@link Conf#INSTALL_STATE_INSTALLING} 安装中。。。
     */
    public void updateState(DownloadBean downloadBean, int state) {
        if (downloadBean == null) {
            LoggerUtils.d(TAG, "mAppInfoBean == null || mAppInfoBean.localPath == null");
            return;
        }
        if (isError) {
            pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.download_progress));
//            ivDownloadControl.setImageResource(R.drawable.btn_download_refresh);
            rootView.setBackgroundResource(R.drawable.shape_download_button_bg);
            tvPercent.setTextColor(context.getResources().getColor(R.color.color_999999));
            isError = false;
        }
        LoggerUtils.d(TAG, "updateState: " + state);
        switch (state) {
            case Conf.STATE_TASK_WAIT:
//                setText("等待中");
                tvPercent.setText(context.getString(R.string.download_state_wait));
                ivDownloadControl.setImageResource(R.drawable.btn_download_stop);
                if (pbProgress != null) {
                    pbProgress.setProgress(downloadBean.percent);
                }
                break;
            case Conf.STATE_TASK_PAUSE:
//                setText("继续");
                ivDownloadControl.setImageResource(R.drawable.btn_download_start);
                if (pbProgress != null) {
                    pbProgress.setProgress(downloadBean.percent);
                }
                if(tvPercent != null){
                    tvPercent.setText(downloadBean.percent + "%");
                }
                break;
            case Conf.STATE_TASK_COMPLETE:
                setProgress(0);
//                setText("下载完成");
//                AppUtil.installApp(activity, new File(mAppInfoBean.localPath));
//                UmengStatisticMgr.downloadComplete(activity,mAppInfoBean.name);
////                installApk(mAppInfoBean);
                break;
            case Conf.STATE_TASK_PROCESSING:
                LoggerUtils.d(TAG, "progress: " + downloadBean.percent);
//                setText(mAppInfoBean.percent + "%");
                ivDownloadControl.setImageResource(R.drawable.btn_download_stop);
                if (pbProgress != null) {
                    pbProgress.setProgress(downloadBean.percent);
                }
                if(tvPercent != null){
                    tvPercent.setText(downloadBean.percent + "%");
                }

                break;
            case Conf.STATE_TASK_ERROR:
                isError = true;
                pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.download_progress_error));
                ivDownloadControl.setImageResource(R.drawable.btn_download_refresh);
                rootView.setBackgroundResource(R.drawable.shape_download_button_bg_error);
                tvPercent.setText(R.string.download_state_error);
                tvPercent.setTextColor(context.getResources().getColor(R.color.color_ff735b));
                break;
        }
    }


}
