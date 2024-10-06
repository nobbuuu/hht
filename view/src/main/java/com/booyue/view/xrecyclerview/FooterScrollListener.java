package com.booyue.view.xrecyclerview;

import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Administrator on 2018/8/7.13:35
 */

public abstract class FooterScrollListener extends RecyclerView.OnScrollListener {
    private boolean isScrollUp = false;
    private static final String TAG = "FooterScrollListener";

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
        isScrollUp = dy >= 0;
    }

    /**
     * Callback method to be invoked when RecyclerView's scroll state changes.
     *
     * @param recyclerView The RecyclerView whose scroll state has changed.
     * @param newState     The updated scroll state. One of {@link RecyclerView#SCROLL_STATE_IDLE},
     *                     {@link RecyclerView#SCROLL_STATE_DRAGGING} or {@link RecyclerView#SCROLL_STATE_SETTLING}.
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        super.onScrollStateChanged(recyclerView, newState);
        Log.d(TAG, "onScrollStateChanged: newState = " + newState);
        int topRowVerticalPosition = (recyclerView == null ||
                recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
        }


        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = linearLayoutManager.getItemCount();
                boolean childCountEnable  = layoutManager.getChildCount() > 0;
                //最后一个可见
                boolean isLastItemPosition = lastVisiblePosition >= layoutManager.getItemCount() - 1;
                //item总数大于屏幕可见数量
                // TODO: 2018/8/7 2 is represent header and footer
                int childCount = layoutManager.getChildCount();
                boolean isItemCountMoreScreen = itemCount > childCount;
                if (isLastItemPosition && isScrollUp && childCountEnable && isItemCountMoreScreen) {
                    onLoad();
                }
            } else if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                int lastVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = gridLayoutManager.getItemCount();
                if (lastVisiblePosition + 1 == itemCount && isScrollUp) {
                    onLoad();
                }
            }
        }
    }

    public abstract void onLoad();

    private SwipeRefreshLayout swipeRefreshLayout;

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
}
