package com.booyue.poetry.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booyue.poetry.R;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 10:19
 * @description :
 */
public class SubjectChooserAdapter extends BaseRecyclerViewAdapter<String> {
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public SubjectChooserAdapter(Context context, List<String> list, boolean showFooter) {
        super(context, list, showFooter);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_subject_chooser, parent, false);
        return new GradeChooserViewHolder(view);
    }

    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof GradeChooserViewHolder) {
            ((GradeChooserViewHolder) holder).tvSubject.setText(list.get(position));
            if(position == checkPosition){
                ((GradeChooserViewHolder) holder).tvSubject.setBackgroundColor(context.getResources().getColor(R.color.item_bg_subject_choose));
            }else {
                ((GradeChooserViewHolder) holder).tvSubject.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
        }
    }

    class GradeChooserViewHolder extends BaseRecyclerViewHolder {
        TextView tvSubject;


        public GradeChooserViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);

        }
    }

    private int checkPosition = -1;

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }



}
