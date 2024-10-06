package com.booyue.media.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import androidx.fragment.app.FragmentActivity;

import com.booyue.base.dialog.BaseDialogFragment;
import com.booyue.base.util.DimensionUtil;
import com.booyue.media.R;
import com.booyue.media.video.adapter.NumberOfVideoAdapter;

/**
 * @author: wangxinhua
 * @date: 2019/12/23 10:40
 * @description : 视频选集对话框
 */
public class VideoSerialNumDialog extends BaseDialogFragment {
    private static final String TAG = "VideoSerialNumDialog";
    private GridView gvSerialNum;

    private NumberOfVideoAdapter numberOfVideoAdapter;
    private int mTotal;
    private int mPosition;

    @Override
    public View createView(LayoutInflater from) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_video_serial_num, null);
        gvSerialNum = view.findViewById(R.id.gvSeraiNum);
        initGridViewOfVideo(mTotal, mPosition);
        return view;
    }

    /**
     * 初始化视频集数布局
     */
    public void initGridViewOfVideo(int total, int pos) {
        if (gvSerialNum == null) {
            return;
        }
        mPosition = pos;
        numberOfVideoAdapter = new NumberOfVideoAdapter(getContext(), total);
        numberOfVideoAdapter.setSelectPosition(mPosition);
        gvSerialNum.setAdapter(numberOfVideoAdapter);
        gvSerialNum.setOnItemClickListener((parent, view, position, id) -> {
            //设置选中集数的颜色
            mPosition = position;
            numberOfVideoAdapter.setSelectPosition(position);
            numberOfVideoAdapter.notifyDataSetChanged();
            //通知播放指定的集数
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position);
            }
            dismiss();
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public VideoSerialNumDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }


    /**
     * 上一集，下一集的时候调用
     *
     * @param mPosition
     */
    public VideoSerialNumDialog setPosition(int mPosition) {
        this.mPosition = mPosition;
        if (numberOfVideoAdapter != null) {
            numberOfVideoAdapter.notifyDataSetChanged();
        }
        return this;
    }

    public VideoSerialNumDialog setVideoCount(int total) {
        mTotal = total;
        return this;
    }

    public static VideoSerialNumDialog show(FragmentActivity activity, int total, int pos, OnItemClickListener onItemClickListener) {
        VideoSerialNumDialog videoSerialNumDialog = new VideoSerialNumDialog();
        videoSerialNumDialog.setVideoCount(total)
        .setPosition(pos)
        .setOnItemClickListener(onItemClickListener)
                .setCanceledOnTouchOutside(true)
                .setWidth(DimensionUtil.dip2Px(activity,300))
                .setBackground(R.color.video_serial_num_bg)
                .setGravity(GRAVITY_RIGHT);

        videoSerialNumDialog.show(activity.getSupportFragmentManager(), VideoSerialNumDialog.TAG);
        return videoSerialNumDialog;
    }


}
