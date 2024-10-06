package com.booyue.poetry.adapter.download;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 10:19
 * @description :
 */
public class DownloadDirChooserAdapter extends BaseRecyclerViewAdapter<DownloadDirChooserAdapter.Folder> {
    private static final String TAG = "DownloadDirChooserAdapt";
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public DownloadDirChooserAdapter(Context context, List<DownloadDirChooserAdapter.Folder> list, boolean showFooter) {
        super(context, list, showFooter);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_folder, parent, false);
        return new GradeChooserViewHolder(view);
    }

    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof GradeChooserViewHolder) {
            ((GradeChooserViewHolder) holder).tvFolderName.setText(list.get(position).name);
//            if(position == checkPosition){
//                ((GradeChooserViewHolder) holder).tvFolderName.setBackgroundColor(context.getResources().getColor(R.color.item_bg_subject_choose));
//            }else {
//                ((GradeChooserViewHolder) holder).tvFolderName.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//            }
            String path = list.get(position).absolutePath;
            if (checkPosition == position && currentDir.equals(path)) {
                ((GradeChooserViewHolder) holder).cbCheck.setChecked(true);
            } else {
                ((GradeChooserViewHolder) holder).cbCheck.setChecked(false);
            }
            ((GradeChooserViewHolder) holder).cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean check = ((GradeChooserViewHolder) holder).cbCheck.isChecked();
                    LoggerUtils.d(TAG, "onClick check: " + check);
                    if (check) {
                        checkPosition = position;
                        currentDir = path;
                    }
//                    ((GradeChooserViewHolder) holder).cbCheck.setChecked(!check);
                }
            });
            ((GradeChooserViewHolder) holder).cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    LoggerUtils.d(TAG, "onCheckedChanged check: " + b);
                }
            });

        }
    }

    class GradeChooserViewHolder extends BaseRecyclerViewHolder {
        TextView tvFolderName;
        CheckBox cbCheck;
        LinearLayout llContainer;


        public GradeChooserViewHolder(View itemView) {
            super(itemView);
            llContainer = itemView.findViewById(R.id.llContainer);
            tvFolderName = itemView.findViewById(R.id.tvFolderName);
            cbCheck = itemView.findViewById(R.id.cbCheck);
        }
    }

    private int checkPosition = -1;
    private String currentDir = "";

    public void setCheckPosition(int checkPosition, String path) {
        this.checkPosition = checkPosition;
        currentDir = path;
        notifyDataSetChanged();
    }
    public String getCheckedDir(){
        return currentDir;
    }



    public static class Folder {
        public String name;
        public String path;
        public String absolutePath;
        public String parent;
    }
}
