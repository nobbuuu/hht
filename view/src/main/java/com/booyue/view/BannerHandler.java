package com.booyue.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 * <p>
 * viewpager实现无限循环效果
 * 暂时删除自动轮播效果
 */
public class BannerHandler {
    public static final String TAG = "BannerHandler";
    private Context mContext;
    private ViewPager mViewPager;
    //    private List<HomeBannerBean.BannerItem> mArrayList;

    //切换页面的时间间隔
    private static final int BANNER_INTERVAL = 5000;
    //自动轮播发送消息的what
    private static final int BANNER_REQUEST_CODE = 20;
    //viewpager中图片个数的倍数
    public static final int VIEWPAGER_SIZE = 10000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BANNER_REQUEST_CODE) {
                if (autoScroll) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
                handler.sendEmptyMessageDelayed(BANNER_REQUEST_CODE, BANNER_INTERVAL);
            }
        }
    };
    private BannerPagerAdapter mAdapter;
    private BannerFragmentPagerAdapter mFragmentAdapter;

    public BannerHandler(Context context, ViewPager viewPager, BannerPagerAdapter pagerAdapter) {
        this.mContext = context;
        mViewPager = viewPager;
        mAdapter = pagerAdapter;
        pageMargin = (int) mContext.getResources().getDimension(R.dimen.dimen_10);

    }

    public BannerHandler(Context context, ViewPager viewPager, BannerFragmentPagerAdapter pagerAdapter) {
        this.mContext = context;
        mViewPager = viewPager;
        mFragmentAdapter = pagerAdapter;
        pageMargin = (int) mContext.getResources().getDimension(R.dimen.dimen_10);

    }


    /**
     * 实现页面切换动画
     */
    public static final float DEFAULT_MIN_SCALE = 0.92f;
    private float minScale = 0.92f;
    private boolean autoScroll = true;
    private int pageMargin = 0;

    public void setParams(int pageMargin, float minScale, boolean autoScroll) {
        this.pageMargin = pageMargin;
        this.minScale = minScale;
        this.autoScroll = autoScroll;
    }


    /**
     * 显示数据
     */
    private boolean init = false;

    public void initViewpager() {
        if (!init) {
            mViewPager.setPageMargin(pageMargin);
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setPageTransformer(true, new ScalePageTransformer());
            if (mAdapter == null) {
                mViewPager.setAdapter(mFragmentAdapter);
                mViewPager.setCurrentItem(mFragmentAdapter.getCount() / 2);
            } else {
                mViewPager.setAdapter(mAdapter);
                mViewPager.setCurrentItem(mAdapter.getCount() / 2);
            }
            initViewPagetEvent();
            init = true;
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setCurrentIndex(int index) {
        mViewPager.setCurrentItem(index);
    }

    class ScalePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            if (position < -1) {
//                page.setAlpha(mMinScale);
//                page.setScaleX(mMinScale);
                page.setScaleY(minScale);
            } else if (position <= 1) { // [-1,1]
                if (position < 0) //[0，-1]
                {
                    float factor = minScale + (1 - minScale) * (1 + position);
//                    page.setAlpha(factor);
//                    page.setScaleX(factor);
                    page.setScaleY(factor);
                } else//[1，0]
                {
                    float factor = minScale + (1 - minScale) * (1 - position);
//                    page.setAlpha(factor);
//                    page.setScaleX(factor);
                    page.setScaleY(factor);
                }
            } else { // (1,+Infinity]
//                page.setAlpha(mMinScale);
//                page.setScaleX(mMinScale);
                page.setScaleY(minScale);
            }

        }
    }

    /**
     * 初始化监听事件
     */
    private void initViewPagetEvent() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mAdapter != null) {
                    int pos = position % mAdapter.getCount();
                    mAdapter.setCurrentPoint(pos);
                } else {
                    int pos = position % mFragmentAdapter.getCount();
                    mFragmentAdapter.setCurrentPoint(pos);
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        handler.sendEmptyMessageDelayed(BANNER_REQUEST_CODE, BANNER_INTERVAL);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(BANNER_REQUEST_CODE, BANNER_INTERVAL);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 开始切换
     */
    public void startViewPager() {
        if (!handler.hasMessages(BANNER_REQUEST_CODE)) {
            handler.sendEmptyMessageDelayed(BANNER_REQUEST_CODE, BANNER_INTERVAL);
        }
    }

    /**
     * 停止切换
     */
    public void endViewPager() {
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * banner适配器基类
     */
    public static abstract class BannerPagerAdapter<T> extends PagerAdapter {
        protected List<T> mArrayList;

        public BannerPagerAdapter(List<T> list) {
            mArrayList = list;
        }

        @Override
        public int getCount() {
            return mArrayList.size() * VIEWPAGER_SIZE;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        //item点击事件
        public OnBannerItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnBannerItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //当前选中的索引
        public int currentPoint;

        public void setCurrentPoint(int index) {
            currentPoint = index;
        }

        /**
         * 更新数据
         *
         * @param list
         */
        public void update(List<T> list) {
            mArrayList.clear();
            mArrayList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * fragmentpager适配器
     *
     * @param <T>
     */
    public static abstract class BannerFragmentPagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
        private List<T> list;
        private FragmentManager fm;

        public BannerFragmentPagerAdapter(List<T> list, FragmentManager fm) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        public List<T> getList() {
            return list;
        }

        @Override
        public int getItemPosition(Object object) {
            Log.d(TAG, "getItemPosition: ");
            return PagerAdapter.POSITION_NONE;
        }

        //当前选中的索引
        public int currentPoint;

        public void setCurrentPoint(int index) {
            currentPoint = index;
        }

        /**
         * 更新数据
         *
         * @param list
         */
        public void addAll(List<T> list, boolean clear) {
            if (clear) {
                this.list.clear();
            }
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void add(T t) {
            if (!list.contains(t)) {
                this.list.add(0, t);
                notifyDataSetChanged();
            }
        }

        public void remove(T t) {
            if (list.contains(t)) {
                this.list.remove(t);
                fm.beginTransaction().remove(t).commitAllowingStateLoss();
                notifyDataSetChanged();
            }
        }

        public void removeAll(List<T> list1) {
            if (list1 == null || list1.size() == 0) {
                return;
            }
            Iterator<Fragment> iterator = (Iterator<Fragment>) list1.iterator();
            if (iterator.hasNext()) {
                Fragment t = iterator.next();
                if (list.contains(t)) {
                    this.list.remove(t);
                    fm.beginTransaction().remove(t).commitAllowingStateLoss();
                }
            }
            notifyDataSetChanged();
        }
    }


    public interface OnBannerItemClickListener {
        void onBannerItemClick(int position);
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    private OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


}
