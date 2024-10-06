package com.booyue.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的ListView
 *
 * @author Kevin
 */
public class RefreshListView extends ListView implements OnScrollListener,
        AdapterView.OnItemClickListener {
    final String TAG = "RefreshListView";
    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private View mHeaderView;
    private int startY = -1;// 滑动起点的y坐标
    private int mHeaderViewHeight;

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tvTitle;
//    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private Context mContext;

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        mContext = context;
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    public void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_listview_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
//        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
//        ivArrow.setColorFilter(Color.parseColor("#39000000"));
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局

        initArrowAnim();

//        tvTime.setText("最后刷新时间:" + getCurrentTime());
    }

    /*
     * 初始化脚布局
     */
    public void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏
        this.setOnScrollListener(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }
                if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
                    break;
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动偏移量
                if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy - mHeaderViewHeight;// 计算padding
                    mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding
                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
//				return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                startY = -1;// 重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    public void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText(R.string.xlistview_header_hint_normal);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText(R.string.xlistview_header_hint_ready);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText(R.string.xlistview_header_hint_loading);
                ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    public void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    OnRefreshListener mListener;
    private View mFooterView;
    private int mFooterViewHeight;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();// 加载下一页数据
    }

    /*
     * 收起下拉刷新的控件
     */
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {// 正在加载更多...
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
            isLoadingMore = false;
        } else {
            mHeaderView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
            },1000);
            mCurrrentState = STATE_PULL_REFRESH;
            ivArrow.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.INVISIBLE);
            if (success) {
                tvTitle.setText("刷新成功");
            }
        }
    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private boolean isLoadingMore;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getChildCount() >= getCount()) {
            return;
        }
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {

            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
                Log.d(TAG, "onScrollStateChanged: 到底了");
                mFooterView.setPadding(0, 0, 0, 0);// 显示
                setSelection(getCount() - 1);// 改变listview显示位置
                isLoadingMore = true;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) { }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {

        super.setOnItemClickListener(this);

        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null) {

            mItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
        }
    }

    /**
     * 是否执行刷新动作
     * @param context
     * @return
     */
    public boolean executeRefresh(Context context){
        if(!isNetWorkAvailable(context)){
            onRefreshComplete(false);
            return false;
        }
        return true;
    }

    /**
     * 是否执行加载更多
     * @param context 上下文
     * @param mPage 当前页数
     * @param mPageSize 每页加载的数据个数
     * @param mTotal 总共数据个数
     * @return true 执行加载 false 不执行
     */
    public boolean executeLoadMore(Context context,int mPage,int mPageSize,int mTotal){
        if(!isNetWorkAvailable(context)){
            show(context,R.string.error_check_network);
            onRefreshComplete(false);
            return false;
        }
        if((mPage + 1) * mPageSize < mTotal){
            return true;
        }else {
            show(context,R.string.last_page);
            onRefreshComplete(false);
            return false;
        }
    }
       public boolean executeLoadMore2(Context context,int mPage,int mTotalPage){
        if(!isNetWorkAvailable(context)){
            show(context,R.string.error_check_network);
            onRefreshComplete(false);
            return false;
        }
        if(mPage < mTotalPage){
            return true;
        }else {
            show(context,R.string.last_page);
            onRefreshComplete(false);
            return false;
        }
    }


    //提示信息
    public static void  show(Context context,int tips){
        Toast.makeText(context,tips, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断网路是否连接
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(mNetworkInfo != null && mNetworkInfo.isConnected()){
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

}
