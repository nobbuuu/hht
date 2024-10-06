package com.booyue.view.xrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.booyue.view.R;

import java.util.List;

/**
 * 自己封装的一个baseAdapter，使其具有加载更多的功能
 *
 * @author Administrator
 * @date 2018/8/7
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder>{
    //一般item
    public static final int TYPE_ITEM = 1;
    //footer
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_HEADER = 3;
    public List<T> list;
    public Context context;
    public final LayoutInflater layoutInflater;
    private boolean mShowFooter;

    public boolean isSearchRecommendVisible;
    public boolean isNiceVideoPlayerActivity = false;
    public void setSearchRecommendVisible(boolean isSearchRecommendVisible){
        this.isSearchRecommendVisible = isSearchRecommendVisible;
    }
    public void setNiceVideoPlayerActivity(boolean isNiceVideoPlayerActivity){
        this.isNiceVideoPlayerActivity = isNiceVideoPlayerActivity;
    }

    /**
     * 构造方法
     * @param context 上下文
     * @param list 数据列表
     * @param showFooter 是否显示脚布局
     */
    public BaseRecyclerViewAdapter(Context context, List<T> list, boolean showFooter) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        mShowFooter =  showFooter;
    }
    //加载状态，默认是加载完成
    private int mLoadState = STATE_LOAD_COMPLETE;
    public static final int STATE_LOADING = 101;
    public static final int STATE_LOAD_COMPLETE = 102;
    public static final int STATE_LOAD_NO_MORE = 103;
    //更改加载状态
    public void setLoadState(int state){
        mLoadState = state;
        notifyDataSetChanged();
    }
    public void setLoading(){
        setLoadState(STATE_LOADING);
    }
     public void setNoMore(){
        setLoadState(STATE_LOAD_NO_MORE);
    }
     public void setComplete(){
        setLoadState(STATE_LOAD_COMPLETE);
    }



    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder baseViewHolder = null;
        if(viewType == TYPE_FOOTER){
            View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swiperefresh_footer,parent,false);
            baseViewHolder = new FooterViewHolder(footerView);
        }else if(viewType == TYPE_ITEM){
            baseViewHolder = onCreateViewHolderImpl(parent,viewType);
        }else if(viewType == TYPE_HEADER){
            baseViewHolder = onCreateHeaderViewHolderImpl(parent,viewType);
        }
        return baseViewHolder;
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        View itemView = holder.itemView;
        //脚布局
        if(holder instanceof BaseRecyclerViewAdapter.FooterViewHolder){
            FooterViewHolder footerViewHolder = ((FooterViewHolder) holder);
            switch (mLoadState){
                case STATE_LOAD_NO_MORE:
//                    footerViewHolder.itemView.setVisibility(View.VISIBLE);
                    footerViewHolder.loadingContainer.setVisibility(View.GONE);
                    footerViewHolder.endContainer.setVisibility(View.VISIBLE);
                    break;
                case STATE_LOAD_COMPLETE:
                    footerViewHolder.loadingContainer.setVisibility(View.GONE);
                    footerViewHolder.endContainer.setVisibility(View.GONE);
//                    footerViewHolder.itemView.setVisibility(View.GONE);
                    break;
                case STATE_LOADING:
//                    footerViewHolder.itemView.setVisibility(View.VISIBLE);
                    footerViewHolder.loadingContainer.setVisibility(View.VISIBLE);
                    footerViewHolder.endContainer.setVisibility(View.GONE);
                    break;
            }
            //item
        }else {
            itemView.setOnClickListener(new RecyclerViewListener(itemView, position));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemLongClickListener != null){
                        onItemLongClickListener.onItemLongClickListener();
                    }
                    return true;
                }
            });
            onBindViewHolderImpl(holder, position);
        }
    }

    public abstract BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType);

    public abstract void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        if(mShowFooter && hasListHeader()){
            return list.size() + 2;
        }else if (mShowFooter || hasListHeader()) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount() && mShowFooter){
            return TYPE_FOOTER;
        }else if(hasListHeader() && position == 0){
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    /**
     * 重写了此方法，就必须重写onCreateHeaderViewHolderImpl，不然会崩溃
     * @return
     */
    public boolean hasListHeader(){
        return false;
    }
    public BaseRecyclerViewHolder onCreateHeaderViewHolderImpl(ViewGroup parent, int viewType){
        return null;
    }

    /**
     * 可以监听item某个按钮的点击，需要子类调用
     */
    public OnItemButtonClick mOnItemButtonClick;
    public void setOnItemButtonClick(OnItemButtonClick listener) {
        this.mOnItemButtonClick = listener;
    }
    public interface OnItemButtonClick{
        void onButtonClick();
    }

    /**
     *
     */
    class FooterViewHolder extends BaseRecyclerViewHolder{
        public LinearLayout loadingContainer;
        public TextView endContainer;
        public FooterViewHolder(View itemView) {
            super(itemView);
            loadingContainer = (LinearLayout) itemView.findViewById(R.id.loading_container);
            endContainer = (TextView) itemView.findViewById(R.id.end_container);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_FOOTER ? gridLayoutManager.getSpanCount():1;
                }
            });
        }
    }



    public class  RecyclerViewListener implements View.OnClickListener {
        private View itemView;
        private int adapterPosition;
        public RecyclerViewListener(View itemView, int position) {
            this.itemView = itemView;
            this.adapterPosition = position;
        }
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                if(hasListHeader() && adapterPosition == 0){
//                    onItemClickListener.onItemClickListener(itemView, adapterPosition,list.get(adapterPosition - 1));
                }else if(hasListHeader()){
                    onItemClickListener.onItemClickListener(itemView, adapterPosition - 1,list.get(adapterPosition - 1));
                }else {
                    onItemClickListener.onItemClickListener(itemView, adapterPosition,list.get(adapterPosition));
                }
            }
        }

    }
    public List<T> getList(){
        return list;
    }

    /**
     *
     * @param list
     */
    public void addAll(List<T> list){
        if(list != null && list.size() > 0){
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 更新数据
     * @param list
     * @param clear
     */
    public void addAll(List<T> list, boolean clear){
        if(list == null) {
            return;
        }
        if(clear){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }
      /**
     * 更新数据
     * @param list
     * @param page
     */
    public void addAll(List<T> list, int page){
        if(list == null) {
            return;
        }
        if(page == 0){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener<T> {
        void onItemClickListener(View view, int pos, T t);
    }
    public OnItemClickListener onItemClickListener;

    /**
     * 设置item监听接口
     * @param listener
     */
    public void setOnItemClickLinster(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

     public interface OnItemLongClickListener {
        void onItemLongClickListener();
    }
    public OnItemLongClickListener onItemLongClickListener;

    /**
     * 设置item监听接口
     * @param listener
     */
    public void setOnItemLongClickLinster(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }



    /**
     * insert item to the list 向列表中插入数据
     * @param index 插入的索引位置
     * @param t 插入的数据
     */
    public void insertItem(int index,T t){
        if(index >= list.size()){
            list.add(t);
            notifyDataSetChanged();
            return;
        }else if(index < 0){
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        list.add(index,t);
        notifyItemInserted(index);
        notifyItemRangeChanged(index,list.size() - index);
    }

    /**
     * 删除指定位置的数据 delete data with specified position
     * @param index 需要删除数据的下标
     */
    public void removeItem(int index){
        if(index < 0 || index > list.size() - 1){
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        list.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, list.size() - index);
    }

    /**
     * insert multiple data 插入多条数据
     * @param index the index of need to insert
     * @param list1 data set
     */
    public void insertItems(int index, List<T> list1){
        if(index >= list.size()){
            addAll(list1);
            return;
        }else if(index < 0){
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        //插入一串数据
        list.addAll(index,list1);
        notifyItemRangeInserted(index,list1.size());
        notifyItemRangeChanged(index,list.size() - index + list1.size());
    }

    /**
     * remove multiple data
     * @param list1
     */
    public void removeItems(List<T> list1){
        list.removeAll(list1);
        notifyDataSetChanged();
    }
}
