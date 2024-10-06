package com.booyue.base.activity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.booyue.base.R;
import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.WindowsUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2022 11/05
 */

public abstract class BaseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    public static List<Activity> activityList = new ArrayList<>();
    private PermissionListener mPermissionListener;
    //用于存放需要授权的权限
    private List<String> permissionList = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION = 1000;
    public interface PermissionListener {
        void permissionSuccess();
        void permissionFail();
    }
    public LayoutInflater layoutInflater;
    /**
     * 权限申请
     *
     * @param listener
     */

    /**
     * 状态栏 和 导航栏 设置
     */
    private void initStatusBar(){
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }

    public void checkRequestPermission(String[] permissions, PermissionListener listener) {
        this.mPermissionListener = listener;
        if(permissions == null || permissions.length <= 0) {
            return;
        }
//
//        for (int i = 0; i < permissions.length; i++) {
//            int selfPermission = PermissionChecker.checkSelfPermission(this, permissions[i]);
//            if (selfPermission != PermissionChecker.PERMISSION_GRANTED) {
//                listener.permissionFail();
//                return;
//            }
//        }
//        listener.permissionSuccess();
        permissionList.clear();
        for (String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, permission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.isEmpty()) {
            mPermissionListener.permissionSuccess();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionListener.permissionFail();
                        return;
                    }
                }
                mPermissionListener.permissionSuccess();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowFeature();
        initStatusBar();//沉浸式导航栏
        LoggerUtils.d(TAG, "onCreate: ");
        if (!activityList.contains(this)) {
            activityList.add(this);
        }
        layoutInflater = getLayoutInflater();
        setView();
        initView();
        initData();
        initListener();
    }
    public abstract void setView();
    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();


    public void windowFeature(){
        WindowsUtils.setStatusBar(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LoggerUtils.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoggerUtils.d(TAG, "onRestart: ");
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoggerUtils.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoggerUtils.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        LoggerUtils.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LoggerUtils.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (activityList.contains(this)) {
            activityList.remove(this);
        }
    }

    /**
     * 封装findviewbyid
     *
     * @param res
     * @param <T>
     * @return
     */
    public <T> T findView(int res) {
        T t = (T) findViewById(res);
        return t;
    }

    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    } /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Class<?> clazz,boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if(isFinish){
            finish();
        }
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }


    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpToForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }


    /***
     * 调到activity页面
     * @param clazz
     */
    public void jumpTo(Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void exit() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /**
     * 初始化旋转动画
     *
     * @param imageView
     */
    public RotateAnimation discAnim;
    public void initAnimation(ImageView imageView) {
        discAnim = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        discAnim.setRepeatCount(Animation.INFINITE);
        //给动画添加差值器，让角度变化匀速进行
        discAnim.setInterpolator(new LinearInterpolator());
        discAnim.setFillAfter(true);
        discAnim.setDuration(10000);
        imageView.startAnimation(discAnim);
    }
    public void cancelAnim(ImageView imageView){
        if(discAnim != null){
            discAnim.cancel();
            imageView.clearAnimation();
        }
    }



//    public void  requestLoading() {
//        CommonDialog.getInstance().showLoaingDialog(this);
//    }
//   public void  requestLoading(String tips) {
//        CommonDialog.getInstance().showLoaingDialog(this,tips);
//    }
//    public void  requestLoading(int tips) {
//        CommonDialog.getInstance().showLoaingDialog(this,getString(tips));
//    }
//    public void  requestComplete() {
//        CommonDialog.getInstance().hideProgressDialog();
//    }


}
