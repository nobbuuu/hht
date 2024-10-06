package com.booyue.poetry.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.booyue.poetry.R;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 10:19
 * @description :
 */
public class GradeChooserAdapter extends BaseRecyclerViewAdapter<String> {
    private float density;
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public GradeChooserAdapter(Context context, List<String> list, boolean showFooter,float density) {
        super(context, list, showFooter);
        this.density = density;
//        Toast.makeText(context, ""+density, Toast.LENGTH_SHORT).show();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_grade_chooser, parent, false);
        return new GradeChooserViewHolder(view);
    }

    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof GradeChooserViewHolder) {
            ((GradeChooserViewHolder) holder).tvGrade.setText(list.get(position));

            if(position == pos){
                if (density > 159 && density < 161){
                    ((GradeChooserViewHolder) holder).tvGrade.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
                }else{
                    ((GradeChooserViewHolder) holder).tvGrade.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                }
                ((GradeChooserViewHolder) holder).tvGrade.setTextColor(context.getResources().getColor(R.color.white));
            }else {

                if (density > 159 && density < 161){
                    ((GradeChooserViewHolder) holder).tvGrade.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                }else{
                    ((GradeChooserViewHolder) holder).tvGrade.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }
                ((GradeChooserViewHolder) holder).tvGrade.setTextColor(context.getResources().getColor(R.color.grade_unchecked_color));
            }
        }
    }

    class GradeChooserViewHolder extends BaseRecyclerViewHolder {
        TextView tvGrade;
        public GradeChooserViewHolder(View itemView) {
            super(itemView);
            tvGrade = itemView.findViewById(R.id.tvGrade);
        }
    }

    private int pos =  -1;
    public void setCurrentPos(int pos){
        this.pos = pos;
        notifyDataSetChanged();
    }

}
