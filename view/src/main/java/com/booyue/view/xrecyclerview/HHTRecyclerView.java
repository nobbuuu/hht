package com.booyue.view.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


//
//import android.content.Context;
//import android.graphics.drawable.ShapeDrawable;
//import android.graphics.drawable.shapes.RectShape;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//
//import com.booyue.core.R;
//import com.jcodecraeer.xrecyclerview.ProgressStyle;
//import com.jcodecraeer.xrecyclerview.XRecyclerView;
//
///**
// * Created by Administrator on 2018/6/20.15:14
// */
//
public class HHTRecyclerView extends RecyclerView {


    private Context mContext;
    private int scaledTouchSlop;
    private int startX;
    private int startY;
    private int moveX;
    private int moveY;

    public HHTRecyclerView(Context context) {
        this(context, null);
    }

    public HHTRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HHTRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getRawX();
                moveY = (int) ev.getRawY();
                //垂直方向滑动父控件拦截
                if (Math.abs(moveY - startY) > Math.abs(moveX - startX) &&
                        Math.abs(moveY - startY) > scaledTouchSlop) {
                    return true;
                    //水平方向滑动不要拦截
                } else if (Math.abs(moveY - startY) < Math.abs(moveX - startX) &&
                        Math.abs(moveX - startX) > scaledTouchSlop) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
//
//    /**
//     * set linearlayout orientation
//     *
//     * @param orientation {@link LinearLayoutManager#HORIZONTAL} or {@link LinearLayoutManager#VERTICAL}
//     */
    public HHTRecyclerView setLinearLayoutManager(int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(orientation);
        setLayoutManager(linearLayoutManager);
        return this;
    }
//
//    /**
//     * set gridlayout orientation
//     *
//     * @param orientation {@link LinearLayoutManager#HORIZONTAL} or {@link LinearLayoutManager#VERTICAL}
//     * @param spanCount   the number of column in the grid
//     */
    public HHTRecyclerView setGridViewLayoutManager(int orientation, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(orientation);
        setLayoutManager(gridLayoutManager);
        return this;
    }
//
//    /**
//     * Add an {@link ItemDecoration} to this RecyclerView. Item decorations can
//     * affect both measurement and drawing of individual item views.
//     */
    private boolean isItemDecorationSet = false;
    public HHTRecyclerView addItemDecoration(ItemDecoration itemDecoration, boolean hasFooterView) {
//      Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.base_header_bg);
//        Drawable drawable = ContextCompat.getDrawable(mContext, res);
//        XRecyclerView.DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(drawable);
//        addItemDecoration(dividerItemDecoration);

        //create shapeDrawable object
//        ShapeDrawable drawable = new ShapeDrawable(new RectShape());
//        drawable.getPaint().setColor(0x00000000);
//        drawable.setIntrinsicWidth((int) getResources().getDimension(R.dimen.dimen_22));
//        drawable.setIntrinsicHeight((int) getResources().getDimension(R.dimen.dimen_30));
//        XRecyclerView.DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(drawable);
//        addItemDecoration(dividerItemDecoration);
//        return this;

        if(isItemDecorationSet){
            return this;
        }
        isItemDecorationSet = true;
//        ShapeDrawable drawable = new ShapeDrawable(new RectShape());
//        drawable.getPaint().setColor(0xebebeb);
////        drawable.setIntrinsicWidth(1);
//        drawable.setIntrinsicHeight(1);
//        RecyclerItemDecoration dividerItemDecoration = new RecyclerItemDecoration(drawable,hasFooterView);
        addItemDecoration(itemDecoration);
        return this;
    }



    /**
     * @param mAdapter
     * @return
     */
    public HHTRecyclerView setHHTAdapter(BaseRecyclerViewAdapter mAdapter) {
        if (mAdapter == null) {
            throw new NullPointerException("adapter is null");
        }
        setAdapter(mAdapter);
        return this;
    }

    /**
     * 获取第一个可见的位置
     *
     * @return
     */
    public int findFirstVisibleItemPosition() {
        int firstVisibleItemPosition = 0;
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager gridLayoutManager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//                firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPositions()
        }
        return firstVisibleItemPosition;
    }

    /**
     * 获取最后一个可见的位置
     *
     * @return
     */
    public int findLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager gridLayoutManager = (LinearLayoutManager) layoutManager;
            lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//                firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPositions()
        }
        return lastVisibleItemPosition;
    }

    /**
     * 获取itemviewholder通过position
     *
     * @param position 位置
     * @return viewholder
     */
    public RecyclerView.ViewHolder findHHTViewHolderForLayoutPosition(int position) {
        RecyclerView.ViewHolder viewHolderForItemId = findViewHolderForLayoutPosition(position);
        return viewHolderForItemId;
    }

}
