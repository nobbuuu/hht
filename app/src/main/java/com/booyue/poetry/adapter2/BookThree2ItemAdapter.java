package com.booyue.poetry.adapter2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.booyue.poetry.R;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.bean.ThreeFragmentBean;
import com.booyue.poetry.request.RequestManager;
import com.booyue.poetry.ui.book2.NewDownloadActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

public class BookThree2ItemAdapter extends BaseQuickAdapter<ThreeFragmentBean.ThTDataDTO.ListDTO.DataDTO, BookThree2ItemAdapter.BookThree2ItemViewHolder> {
    private Context context;

    public BookThree2ItemAdapter(int layoutResId, @Nullable List<ThreeFragmentBean.ThTDataDTO.ListDTO.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BookThree2ItemViewHolder holder, ThreeFragmentBean.ThTDataDTO.ListDTO.DataDTO dataDTO) {
//        holder.itemView.setBackgroundColor(Color.parseColor("#ff00"));

        //这行注释
//        RequestManager.getInstance().loadImage(dataDTO.getImgUrl() ,holder.itemIImg, RequestManager.ImageSize.SIZE_RAW);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius(20);//这里是设置你希望的圆角的值
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(holder.itemView.getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);
        holder.itemIImg.setHierarchy(hierarchy);//一定要先设置Hierarchy，再去加载图片，否则会加载不出来图片
        holder.itemIImg.setImageURI(Uri.parse(dataDTO.getImgUrl()));
        String[] split = dataDTO.getName().split("（");
        if (split.length == 1){
            holder.itemIItext.setText(dataDTO.getName());
            holder.itemIIItext.setVisibility(View.GONE);
        }else{
            holder.itemIItext.setText(split[0]);
            holder.itemIIItext.setText("（"+split[1]);
            holder.itemIIItext.setVisibility(View.VISIBLE);
        }
        holder.author.setText(dataDTO.getAuthor());
        //子条目点击事件
        holder.itemView.setOnClickListener( v -> {
            Intent intent = new Intent(holder.itemView.getContext(), NewDownloadActivity.class);
            intent.putExtra("downloadData",new Gson().toJson(dataDTO));
            holder.itemView.getContext().startActivity(intent);
        });
    }


    class BookThree2ItemViewHolder extends BaseViewHolder {
        SimpleDraweeView itemIImg;
        TextView itemIItext;
        TextView itemIIItext;
        TextView author;
        public BookThree2ItemViewHolder(View itemView) {
            super(itemView);
            itemIImg = itemView.findViewById(R.id.itemIImg);
            itemIItext = itemView.findViewById(R.id.itemIItext);
            itemIIItext = itemView.findViewById(R.id.itemIIItext);
            author = itemView.findViewById(R.id.author);
        }
    }
}
