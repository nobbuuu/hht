package com.booyue.poetry.adapter2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.booyue.poetry.R;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.bean.ThreeFragmentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

//public class BookThree2Adapter extends RecyclerView.Adapter<BookThree2Adapter.BookThree2ViewHolder>{
public class BookThree2Adapter extends BaseQuickAdapter<ThreeFragmentBean.ThTDataDTO.ListDTO, BookThree2Adapter.BookThree2ViewHolder> {
    private Context context;
//    private List<MainDataBean.MainDataDTO.DynastyListDTO> list;

    public BookThree2Adapter(int layoutResId, @Nullable List<ThreeFragmentBean.ThTDataDTO.ListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BookThree2ViewHolder holder, ThreeFragmentBean.ThTDataDTO.ListDTO list) {
        BookTextTitleAdapter textTitleAdapter = new BookTextTitleAdapter(context, list.getTitle());
        holder.textRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.textRV.setAdapter(textTitleAdapter);

        BookThree2ItemAdapter bookThree2ItemAdapter = new BookThree2ItemAdapter(R.layout.item_book2three_item, list.getData());
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, (list.getData().size()+1)/2 ){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gridLayoutManager1.setAutoMeasureEnabled(true);
        gridLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        holder.doubleRV.setLayoutManager(gridLayoutManager1);
        holder.doubleRV.setAdapter(bookThree2ItemAdapter);
    }




    class BookThree2ViewHolder extends BaseViewHolder {
        RecyclerView textRV;
        RecyclerView doubleRV;
        public BookThree2ViewHolder(View itemView) {
            super(itemView);
            textRV = itemView.findViewById(R.id.text_RecyclerView);
            doubleRV = itemView.findViewById(R.id.three_adapter_item);
        }
    }
}
