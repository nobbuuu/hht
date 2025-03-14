package com.booyue.view.deletelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


/**
 * @author Administrator
 */
public class ListViewCompat extends ListView {

    private static final String TAG = "ListViewCompat";

    private SlideView mFocusedItemView;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    ListItem data = (ListItem) getItemAtPosition(position);
                    if(getItemAtPosition(position) != null) {
                        mFocusedItemView = data.slideView;
                    }
                }
            }
            default:
                break;
        }
        if (mFocusedItemView != null) {

            mFocusedItemView.onRequireTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public static class ListItem<T>{
        public T bean;
        public SlideView slideView;
    }
}