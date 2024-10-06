package com.booyue.poetry.adapter2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.booyue.poetry.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookTextTitleAdapter extends RecyclerView.Adapter<BookTextTitleAdapter.BookThree2ItemViewHolder>{
    private Context context;
    private String title;
    private List<String> titleList = new ArrayList<>();
    /**
     * 构造方法
     *
     * @param context    上下文
     * @param title       数据列表
     */
    public BookTextTitleAdapter(Context context,String title) {
        this.context = context;
        titleList.clear();
        for (int i = 0; i < title.length(); i++) {
            titleList.add(title.charAt(i)+"");
        }
    }

    @NonNull
    @Override
    public BookThree2ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookThree2ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book2text_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookThree2ItemViewHolder holder, int i) {
        holder.textTitle.setText(titleList.get(i));
    }

    @Override
    public int getItemCount() {
        if (titleList.size() == 0)
            return 0;
        return titleList.size();
    }


    class BookThree2ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        public BookThree2ItemViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.title_text);
        }
    }
}
