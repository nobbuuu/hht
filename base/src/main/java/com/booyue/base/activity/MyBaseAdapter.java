package com.booyue.base.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11.17:06
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public   final String TAG = this.getClass().getSimpleName() + "-----";
    public List<T> list;
    public Context context;
    public LayoutInflater inflater;

    public MyBaseAdapter(Context context, List<T> ts) {
        this.list = ts;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position,convertView,parent);
    }
    /**
     * 抽象方法，需要继承者实现，用来返回ListView中条目的布局
     */
    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    /**
     * 用来刷新ListView中数据的工具方法
     */
    public void addAll(List<T> list, boolean isClearDatasource){
        if(isClearDatasource){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加所有数据
     * @param page 当前页数，如果是0，清除之前的，否则尾部添加
     * @param list 数据集合
     */
    public void addAll(int page,List<T> list){
        if(list == null) {
            return;
        }
        if (page == 0) {
            addAll(list, true);
        } else {
            addAll(list, false);
        }
    }
    public void addAllHeader(int position,List<T> list){
        if(list == null) {
            return;
        }
        this.list.addAll(position,list);
        notifyDataSetChanged();

    }

    /**
     * 添加item到指定位置，如果添加位置大于总长度
     * @param t 添加的数据
     * @param position 位置
     */
    public void addItem(T t, int position) {
        if (t != null && list != null) {
            if (position < list.size()) {
                list.add(position, t);
            } else {
                list.add(t);
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(T t){
        if (t != null && list != null) {
            list.add(t);
        }
        notifyDataSetChanged();
    }

    /**
     * 返回Adapter中使用的数据源
     */
    public List<T> getDatasource(){
        return list;
    }
    /**
     * 返回Adapter中使用的上下文
     */
    public Context getContext(){
        return context;
    }
    /**
     * 返回Adapter中使用的LayoutInflater对象
     */
    public LayoutInflater getInflater(){
        return inflater;
    }

    public void clear() {
        if(list != null && list.size() > 0){
            list.clear();
            notifyDataSetChanged();
        }
    }
}
