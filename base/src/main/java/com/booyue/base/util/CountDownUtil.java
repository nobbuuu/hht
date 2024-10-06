package com.booyue.base.util;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2018/10/25
 * @description :
 */
public class CountDownUtil {
    private static final String TAG = "CountDownUtil";
    private MyCountDownTimer countDownTimer;

    /**
     * 1000 * 60,1000
     *
     * @param millisInFuture    倒计时时长 ms
     * @param countDownInterval 倒计时间隔时长   ms
     */
    public void launchCountDown(long millisInFuture, long countDownInterval) {
        if (countDownTimer == null) {
            countDownTimer = new MyCountDownTimer(millisInFuture, countDownInterval);
        }
        countDownTimer.start();
    }

     /**
      * 默认倒计时器
      * 时间12os,间隔1s
     * 1000 * 120,1000
     */
    public void launchDefaultCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new MyCountDownTimer(120 * 1000, 1000);
        }
        countDownTimer.start();
    }




    /**
     * 取消倒计时
     */
    public void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public final class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            LoggerUtils.d(TAG, "onTick: " + millisUntilFinished / 1000);
            CountDownUtil.this.onTick(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            LoggerUtils.d(TAG, "onFinish: ");
            CountDownUtil.this.onFinish();
        }
    }

    public void onFinish() {
        if (mCountDownCallbackList != null && mCountDownCallbackList.size() > 0) {
            for (CountDownCallback countDownCallback : mCountDownCallbackList) {
                countDownCallback.onFinish();
            }
        }
    }

    public void onTick(long millisUntilFinished) {
        if (mCountDownCallbackList != null && mCountDownCallbackList.size() > 0) {
            for (CountDownCallback countDownCallback : mCountDownCallbackList) {
                countDownCallback.onTick(millisUntilFinished);
            }
        }
    }


    /**
     * 倒计时监听器
     */
    public interface CountDownCallback {
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    private List<CountDownCallback> mCountDownCallbackList = new ArrayList<>();

    /**
     * 添加回调监听
     * @param countDownCallback
     */
    public void addCountDownCallback(CountDownCallback countDownCallback) {
        if (mCountDownCallbackList != null && !mCountDownCallbackList.contains(countDownCallback)) {
            mCountDownCallbackList.add(countDownCallback);
        }
    }

    /**
     * 移除回调监听
     * @param countDownCallback
     */
    public void removeCountDownCallback(CountDownCallback countDownCallback) {
        if (mCountDownCallbackList != null && mCountDownCallbackList.contains(countDownCallback)) {
            mCountDownCallbackList.remove(countDownCallback);
        }
    }
}
