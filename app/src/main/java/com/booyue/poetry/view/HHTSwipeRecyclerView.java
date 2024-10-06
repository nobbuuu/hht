package com.booyue.poetry.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.booyue.base.bean.BaseBean2;
import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.FooterScrollListener;
import com.booyue.view.xrecyclerview.HHTRecyclerView;
import com.booyue.view.xrecyclerview.LinearDividerItemDecoration;

/**
 * @author: wangxinhua
 * @date: 2019/12/9 18:00
 * @description :
 */
public class HHTSwipeRecyclerView extends FrameLayout {
    public static final String TAG = "HHTSwipeRecyclerView";
    private int mFirstPage = BaseBean2.FIRST_PAGE;
    public int mPage = mFirstPage;
    public int mTotalPage = mFirstPage;
    public Context context;
    public SwipeRefreshLayout swipeRefreshLayout;
    public FrameLayout headerView;
    public HHTRecyclerView hhtRecyclerView;
    private RelativeLayout containerEmpty;//没结果布局
    private TextView tvEmptyDesc;//没结果说明
    private ImageView ivEmptyImage;//没结果图片
    public boolean SearchRecommendHeaderVisible = false;

    public HHTSwipeRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public HHTSwipeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }


    private void initLayout(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.activity_hht_swiperefreshlayout, this);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        headerView = findViewById(R.id.headerView);
        hhtRecyclerView = findViewById(R.id.recyclerView);
        containerEmpty = findViewById(R.id.containerEmpty);
        tvEmptyDesc = findViewById(R.id.tv_desc);
        ivEmptyImage = findViewById(R.id.ivEmptyImage);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mOnRefreshListener != null) {
                mPage = mFirstPage;
                mOnRefreshListener.onRefresh(mPage);
            }
        });
        //监听加载更多
        hhtRecyclerView.addOnScrollListener(new FooterScrollListener() {
            @Override
            public void onLoad() {
                LoggerUtils.d(TAG, "onLoad: mpage = " + mPage + ",mtotalPage = " + mTotalPage);
                if (mPage < mTotalPage && !SearchRecommendHeaderVisible) {
                    if (mOnRefreshListener != null) {
                        if (isAllowRequest) {
                            mOnRefreshListener.onLoadMore(mPage + 1);
                        }
                    }
                } else {
                    if (mAdapter != null) {
                        mAdapter.setNoMore();
                    }
                }
            }
        });
        //滚动监听，是否禁用swipeRefreshLayout
        hhtRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }

    public BaseRecyclerViewAdapter mAdapter;

    public HHTSwipeRecyclerView initLinearAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter) {
        mAdapter = baseRecyclerViewAdapter;
        hhtRecyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL)
//                .addItemDecoration(new LinearDividerItemDecoration(context, LinearLayoutManager.VERTICAL), false)
                .setHHTAdapter(baseRecyclerViewAdapter);
        return this;
    }

    public HHTSwipeRecyclerView initLinearAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, boolean showLine) {
        mAdapter = baseRecyclerViewAdapter;
        hhtRecyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        if (showLine) {
            hhtRecyclerView.addItemDecoration(new LinearDividerItemDecoration(context, LinearLayoutManager.HORIZONTAL), false);
        }
        hhtRecyclerView.setHHTAdapter(baseRecyclerViewAdapter);
        return this;
    }


    public HHTSwipeRecyclerView initGridAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, int spanCount, RecyclerView.ItemDecoration itemDecoration) {
        mAdapter = baseRecyclerViewAdapter;
        if (itemDecoration != null) {
            hhtRecyclerView.addItemDecoration(itemDecoration, true);
        }
        hhtRecyclerView.setGridViewLayoutManager(LinearLayoutManager.VERTICAL, spanCount)
                .setHHTAdapter(baseRecyclerViewAdapter);
        return this;
    }

    /**
     * 解析数据后调用
     *
     * @param page      当前页
     * @param totalPage 总页数
     */
    public void setPage(int page, int totalPage) {
        LoggerUtils.d(TAG, "setPage: " + page + " ,totalPage = " + totalPage);
        this.mPage = page;
        this.mTotalPage = totalPage;
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            showContentOnSuccess("");
        }
    }

    public void showContentOnSuccess(String tips) {
        LoggerUtils.d(TAG, "showContentOnSuccess: ");
        if (mAdapter.getList().size() > 0) {
            showContent();
        } else {
            if(TextUtils.isEmpty(tips)){
                tips = "空空如也~";
            }
            showErrorForEmpty(tips);
        }
    }

    /**
     * 隐藏加载视图
     *
     * @param end
     */
    public void hideLoading(boolean end) {
        swipeRefreshLayout.setRefreshing(false);
        if (end) {
            mAdapter.setNoMore();
        } else {
            mAdapter.setComplete();
        }
    }

    private boolean isAllowRequest = true;

    public void setIsAllowRequest(boolean allow) {
        isAllowRequest = allow;
    }

    /**
     * 隐藏加载视图
     * 数据返回调用
     */
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.setComplete();
        int firstVisibleItemPosition = hhtRecyclerView.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = hhtRecyclerView.findLastVisibleItemPosition();
        LoggerUtils.d(TAG, "firstPosition: " + firstVisibleItemPosition);
        LoggerUtils.d(TAG, "lastPosition: " + lastVisibleItemPosition);
        if (mPage == mTotalPage && lastVisibleItemPosition > firstVisibleItemPosition) {
            LoggerUtils.d(TAG, "hideLoading: setNoMore");
            mAdapter.setNoMore();
        } else {
            LoggerUtils.d(TAG, "hideLoading: setComplete");
            mAdapter.setComplete();
        }
    }

    /**
     * 搜索页面空数据提示
     */
    public void showErrorForEmpty(String tips) {
        LoggerUtils.d(TAG, "showErrorForEmpty: ");
        clearAnim();
        hideLoading(false);
        hhtRecyclerView.setVisibility(GONE);
        containerEmpty.setVisibility(View.VISIBLE);
        ivEmptyImage.setImageResource(R.drawable.pic_server);
        tvEmptyDesc.setText(tips);
    }


    public void showErrorForNetwork() {
        clearAnim();
        hideLoading(false);
        hhtRecyclerView.setVisibility(GONE);
        containerEmpty.setVisibility(View.VISIBLE);
        ivEmptyImage.setBackgroundResource(R.drawable.pic_wifi);
        tvEmptyDesc.setText(R.string.network_error);
    }


    public void showErrorForServer(String msg) {
        clearAnim();
        hideLoading(false);
        hhtRecyclerView.setVisibility(GONE);
        containerEmpty.setVisibility(View.VISIBLE);
        ivEmptyImage.setBackgroundResource(R.drawable.pic_server);
        tvEmptyDesc.setText(msg);
    }

    public void showLoading() {
        LoggerUtils.d(TAG, "showLoading: ");
        if (mAdapter.getList().size() > 0 && mPage != mTotalPage) {
            return;
        }
        LoggerUtils.d(TAG, "showLoading:1");
        clearAnim();
        hhtRecyclerView.setVisibility(GONE);
        containerEmpty.setVisibility(View.VISIBLE);
        tvEmptyDesc.setText(R.string.loading);
        startAnim();
    }

    /**
     * 显示列表数据
     */
    public void showContent() {
        LoggerUtils.d(TAG, "showContent: hide empty" );
        clearAnim();
        hideLoading();
        hhtRecyclerView.setVisibility(VISIBLE);
        containerEmpty.setVisibility(View.GONE);
    }


    public void startAnim() {
        ivEmptyImage.setBackgroundResource(R.drawable.anim_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivEmptyImage.getBackground();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.run();
        }
    }

    public void clearAnim() {
        if (ivEmptyImage.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) ivEmptyImage.getBackground();
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
                ivEmptyImage.clearAnimation();
            }
        }
    }


    OnRefreshListener mOnRefreshListener;

    public HHTSwipeRecyclerView setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
        return this;
    }
    public void setRefreshing(boolean refreshing){
        swipeRefreshLayout.setRefreshing(refreshing);
    }
    public int getFirstVisibleItemPosition(){
        return hhtRecyclerView.findFirstVisibleItemPosition();
    }
    public int getLastVisibleItemPosition(){
        return hhtRecyclerView.findLastVisibleItemPosition();
    }
    public RecyclerView.ViewHolder getViewHolder(int position){
        return hhtRecyclerView.findHHTViewHolderForLayoutPosition(position);
    }

    public interface OnRefreshListener {
        void onRefresh(int page);

        void onLoadMore(int page);// 加载下一页数据
    }

    //设置初始页
    public void setFirstPage(int firstPage) {
        mFirstPage = firstPage;
    }

//    public interface UIChangeListener{
//        void showErrorForServer();
//        void showErrorForNetwork();
//        void showErrorForEmpty();
//        void showLoading();
//        void showContent();
//    }
}
