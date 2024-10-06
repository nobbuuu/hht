package com.booyue.media.video.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.booyue.media.R;

/**
 * Created by Administrator on 2018/5/12.10:57
 */

public class NumberOfVideoAdapter extends BaseAdapter{
    private Context context;
    private int mNumbers;//总集数
    private int mSelectPosition;//当选选中的集数

    public NumberOfVideoAdapter(Context context, int mNumbers) {
        this.context = context;
        this.mNumbers = mNumbers;
    }

    @Override
    public int getCount() {
        return mNumbers;
    }

    @Override
    public Object getItem(int position) {
        return position + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_of_vodeo_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNumber.setText(position + 1 + "");
        if(position == mSelectPosition){
            viewHolder.tvNumber.setTextColor(context.getResources().getColor(R.color.video_serial_num_item_bg_hov));
            viewHolder.tvNumber.setBackgroundResource(R.drawable.shape_number_video_item_bg_hov);
        }else {
            viewHolder.tvNumber.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvNumber.setBackgroundResource(R.drawable.shape_number_video_item_bg);
        }
        return convertView;
    }
    public static final String TAG = "NumberOfVideoAdapter";

    class ViewHolder{
        TextView tvNumber;

        public ViewHolder(View convertView) {
            this.tvNumber = (TextView) convertView.findViewById(R.id.tvSerialNum);
        }
    }

    /**
     * 设置当前播放的视频position
     * @param position
     */
    public void setSelectPosition(int position){
        Log.d(TAG, "setSelectPosition: " + position);
        mSelectPosition = position;
    }
}
