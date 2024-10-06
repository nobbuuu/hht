package com.booyue.poetry.adapter2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.booyue.base.util.BitmapUtil;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.GradeChooserAdapter;
import com.booyue.poetry.bean.BookOneHomeBean;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.ui.book2.FirstOrderActivity;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookOne2Adapter extends RecyclerView.Adapter<BookOne2Adapter.BookOne2ViewHolder>{
    private Context context;
    private List<MainDataBean.MainDataDTO.ListDTO> list;
    private List<BookOneHomeBean> mList = new ArrayList<>();
    private int type;
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public BookOne2Adapter(Context context, MainDataBean.MainDataDTO list,int type, boolean showFooter) {
        this.context = context;
        this.type = type;
        mList.clear();
        if (type == 1){
//            this.list = list.getPeriodList();
//            Collections.reverse( this.list);
            mList.add(new BookOneHomeBean("img_lv11x","ic_level_text1","一阶·韵律启蒙","一阶"));
            mList.add(new BookOneHomeBean("img_lv21x","ic_level_text2","二阶·沉淀语感","二阶"));
            mList.add(new BookOneHomeBean("img_lv31x","ic_level_text3","三阶·乐于善学","三阶"));
            mList.add(new BookOneHomeBean("img_lv41x","ic_level_text4","四阶·理解联想","四阶"));
            mList.add(new BookOneHomeBean("img_lv51x","ic_level_text5","五阶·文学积淀","五阶"));
        }else {
//            this.list = list.getThemeList();
//            Collections.reverse(this.list);
//            mList.add(new LayoutBean("img_kind5",list.getThemeList().get(4).getTitle()));
            mList.add(new BookOneHomeBean("img_kind1","山水田园诗","山水田园诗"));
            mList.add(new BookOneHomeBean("img_kind3","送别诗","送别诗"));
            mList.add(new BookOneHomeBean("img_kind4","羁旅诗","羁旅诗"));
            mList.add(new BookOneHomeBean("img_kind2","咏物诗","咏物诗"));
            mList.add(new BookOneHomeBean("img_kind6","边塞诗","边塞诗"));
            mList.add(new BookOneHomeBean("img_kind5","抒怀咏志诗","抒怀咏志诗"));
            mList.add(new BookOneHomeBean("img_kind7","叙事诗","叙事诗"));
        }
    }

    @NonNull
    @Override
    public BookOne2ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookOne2ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book2one,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookOne2ViewHolder holder, int i) {
        holder.layotImg.setImageResource(BitmapUtil.getMipmapId(context,mList.get(i).getImgUrl()));

        if (mList.get(i).getTitleimgUrl()!=null){
            holder.titleImage.setImageResource(BitmapUtil.getMipmapId(context,mList.get(i).getTitleimgUrl()));
        }
        if (i == (mList.size()-1)){
            holder.nullView.setVisibility(View.VISIBLE);
        }
        if (type == 1){
            holder.titleImage.setVisibility(View.VISIBLE);
        }else {
            holder.titleImage.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FirstOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putString("title", mList.get(i).getTitle());
            bundle.putString("titleTwo", mList.get(i).getTitleTwo());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }


    class BookOne2ViewHolder extends RecyclerView.ViewHolder {
        ImageView layotImg;
        ImageView titleImage;
        View nullView;
        public BookOne2ViewHolder(View itemView) {
            super(itemView);
            layotImg = itemView.findViewById(R.id.layout_one_item_img);
            titleImage = itemView.findViewById(R.id.titleImage);
            nullView = itemView.findViewById(R.id.nullView);
        }
    }
}
