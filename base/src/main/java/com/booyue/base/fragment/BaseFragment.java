package com.booyue.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.booyue.base.R;
import com.booyue.base.util.LoggerUtils;

/**
 *
 * @author Administrator
 * @date 2018/7/5
 * <p>
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName();
    public Activity mActivity;
    public LayoutInflater mInflater;
    public View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mInflater = mActivity.getLayoutInflater();
        LoggerUtils.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggerUtils.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoggerUtils.d(TAG, "onCreateView: ");
        mRootView = getCustomView(inflater, container, savedInstanceState);
        initView();
        return mRootView;
    }

    public abstract View getCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void initView();

    public abstract void initData();

    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoggerUtils.d(TAG, "onActivityCreated: ");
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoggerUtils.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        LoggerUtils.d(TAG, "onResume: ");

    }

    @Override
    public void onPause() {
        super.onPause();
        LoggerUtils.d(TAG, "onPause: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        LoggerUtils.d(TAG, "onStop: ");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoggerUtils.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        LoggerUtils.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LoggerUtils.d(TAG, "onDetach: ");
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
