package com.booyue.poetry.adapter2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.booyue.poetry.R;
import com.booyue.poetry.bean.FirstOrderBean;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.request.RequestManager;
import com.booyue.poetry.ui.book2.NewDownloadActivity;
import com.google.gson.Gson;
import com.xiao.nicevideoplayer.NiceUtil;

import java.util.ArrayList;
import java.util.List;

public class FirstOrder2Adapter extends RecyclerView.Adapter<FirstOrder2Adapter.FirstOrder2ViewHolder>{
    private Context context;
    private  List<FirstOrderBean.DataDTOD.ListDTO.DataDTO> list;
    private List<String> list2 = new ArrayList<>();
    private boolean showFooter;
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public FirstOrder2Adapter(Context context, List<FirstOrderBean.DataDTOD.ListDTO.DataDTO> list, boolean showFooter) {
        this.context = context;
        this.list = list;
        this.showFooter = showFooter;
    }

    @NonNull
    @Override
    public FirstOrder2ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FirstOrder2ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_firstorder2_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FirstOrder2ViewHolder holder, int i) {
        if (i == (list.size()-1) && !showFooter){
            holder.itemView.setVisibility(View.GONE);
        }
        String[] split = list.get(i).getName().split("（");
        if (split.length == 1){
            holder.nameDi.setText(list.get(i).getName());
            holder.nameDi.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.GONE);
            holder.nikeName.setVisibility(View.GONE);
        }else{
            holder.name.setText(split[0]);
            holder.nikeName.setText("（"+split[1]);
            holder.nameDi.setVisibility(View.GONE);
            holder.name.setVisibility(View.VISIBLE);
            holder.nikeName.setVisibility(View.VISIBLE);
        }
        RequestManager.getInstance().loadImage(list.get(i).getImgUrl(),holder.fSImage, RequestManager.ImageSize.SIZE_RAW);

        holder.author.setText(list.get(i).getDynasty()+"·"+list.get(i).getAuthor());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewDownloadActivity.class);
            intent.putExtra("downloadData",new Gson().toJson( list.get(i)));
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }


    class FirstOrder2ViewHolder extends RecyclerView.ViewHolder {
        ImageView fSImage;
        TextView name,nameDi,nikeName,author;
        public FirstOrder2ViewHolder(View itemView) {
            super(itemView);
            fSImage = itemView.findViewById(R.id.fSImage);
            name = itemView.findViewById(R.id.name);
            nameDi = itemView.findViewById(R.id.nameDi);
            nikeName = itemView.findViewById(R.id.nikeName);
            author = itemView.findViewById(R.id.author);
        }
    }
}
