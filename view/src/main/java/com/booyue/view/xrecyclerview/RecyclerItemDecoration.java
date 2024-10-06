package com.booyue.view.xrecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/7/26.09:24
 */

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "RecyclerItemDecoration";
    private Drawable mDivider;
    private int mOrientation;
    private boolean mHasFooterView = false;

    /**
     * Sole constructor. Takes in a {@link Drawable} to be used as the interior
     * divider.
     *
     * @param divider A divider {@code Drawable} to be drawn on the RecyclerView
     */
    public RecyclerItemDecoration(Drawable divider, boolean hasFooterView) {
        mDivider = divider;
        mHasFooterView = hasFooterView;
    }

    /**
     * Draws horizontal or vertical dividers onto the parent RecyclerView.
     *
     * @param canvas The {@link Canvas} onto which dividers will be drawn
     * @param parent The RecyclerView onto which dividers are being added
     * @param state  The current RecyclerView.State of the RecyclerView
     */
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontalDividers(canvas, parent);
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDividers(canvas, parent);
        }
    }

    /**
     * Determines the size and location of offsets between items in the parent
     * RecyclerView.
     *
     * @param outRect The {@link Rect} of offsets to be added around the child
     *                view
     * @param view    The child view to be decorated with an offset
     * @param parent  The RecyclerView onto which dividers are being added
     * @param state   The current RecyclerView.State of the RecyclerView
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
//        int childCount = parent.getChildCount();
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.right = mDivider.getIntrinsicWidth();
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.bottom = mDivider.getIntrinsicHeight();
        }

//        if (parent != null) {
////                int childIndex=parent.getChildPosition(view);//deprecated
//            int childIndex = parent.getChildAdapterPosition(view);
//            RecyclerView.Adapter adapter = parent.getAdapter();
//            if (adapter != null) {
//                int childCount = adapter.getItemCount();
//                if (mHasFooterView && childIndex == childCount - 1) {
//                    outRect.top = 0;
//                    outRect.left = 0;
//                    outRect.right = 0;
//                    outRect.bottom = 0;
//                    return;
//                }
//                if (childIndex % 5 == 0) {//the first one，第一个，左边缘间距
////                    outRect.left = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 40);//12dp;
////                    outRect.right = 0;
//                } else if (childIndex % 5 == 4) {//the last one,最后一个，右边缘间距
////                    outRect.left = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 0);
////                    outRect.right = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 40);//12dp
//                } else {
////                    outRect.left = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 23);
////                    outRect.right = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 0);
//                }
//                outRect.top = DimensionUtil.dip2Px(ProjectInit.getApplicationContext(), 20);
//                outRect.bottom = 0;
//                LoggerUtils.d(TAG, "--->getItemOffsets()--childIndex:" + childIndex + ",childCount=" + childCount);
//            }
//        }
    }

    /**
     * Adds dividers to a RecyclerView with a LinearLayoutManager or its
     * subclass oriented horizontally.
     *
     * @param canvas The {@link Canvas} onto which horizontal dividers will be
     *               drawn
     * @param parent The RecyclerView onto which horizontal dividers are being
     *               added
     */
    private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 1; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int parentLeft = 0;
            int parentRight = 0;
            if (i == 0) {
            } else {
                parentLeft = child.getRight() + params.rightMargin;
                parentRight = parentLeft + mDivider.getIntrinsicWidth();
                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }
    }

    /**
     * Adds dividers to a RecyclerView with a LinearLayoutManager or its
     * subclass oriented vertically.
     *
     * @param canvas The {@link Canvas} onto which vertical dividers will be
     *               drawn
     * @param parent The RecyclerView onto which vertical dividers are being
     *               added
     */
    private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int parentTop = child.getBottom() + params.bottomMargin;
            int parentBottom = parentTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }
}
