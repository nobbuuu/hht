package com.booyue.base.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.WindowsUtils;

/**
 * @author Administrator
 * 对话框fragment基类
 */
public abstract class BaseDialogFragment extends DialogFragment {
    public String TAG = this.getClass().getSimpleName();
    /**
     * 对话框中心显示
     */
    public static int GRAVITY_CENTER = 1;
    /**
     * 对话框底部显示
     */
    public static int GRAVITY_BOTTOM = 2;
    public static int GRAVITY_RIGHT = 3;
    public int gravity = GRAVITY_CENTER;

    public OnButtonClickListener mOnButtonClickListener;


    public BaseDialogFragment setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
        return this;
    }

    /**
     * 确认点击监听
     */
    public interface OnButtonClickListener {
        /**
         * 确认点击回调
         */
        void onPositive();
    }

    public interface OnDoubleButtonClickListener extends OnButtonClickListener {
        /**
         * 取消点击回调
         */
        void onNegative();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        Log.d(TAG, "show: ");
        if (this.isAdded()) {
            return;
        }
        /**
         * 为了解决:mainActivity调用onSaveInstanceState以后又调用了show方法,
         * 出现的Can not perform this action after onSaveInstanceState
         * 这个异常(不应该用commit ,而是用commitAllowingStateLoss)
         * 得罪了,不会反射 ,先把你catch住吧.乖
         * @param manager
         * @param tag
         */
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = createView(LayoutInflater.from(getActivity()));
        builder.setView(view);
        return builder.create();
    }

    /**
     * 将xml布局膨胀成view对象，需要子类重写此方法
     *
     * @param from 布局膨胀对象
     * @return
     */
    public abstract View createView(LayoutInflater from);


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: isCancel " + isCancel);
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(isCancel);
        dialog.setCancelable(isCancel);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                BaseDialogFragment.this.onDismiss();
            }
        });
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            if (backgroundResource > 0) {
                dialog.getWindow().setBackgroundDrawableResource(backgroundResource);
            }
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthPixel = dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels;
//            int heightPixel = dm.heightPixels > dm.widthPixels ? dm.heightPixels : dm.widthPixels;
            LoggerUtils.d(TAG, "width: " + mWindowWidth);
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            if (gravity == GRAVITY_BOTTOM) {
                layoutParams.width = (int) (widthPixel * 1f);
                layoutParams.y = dm.heightPixels / 2;
            } else if (gravity == GRAVITY_RIGHT) {
                layoutParams.height = dm.heightPixels;
                layoutParams.x = dm.widthPixels / 2;
            }

            if (widhtAspect > 0f) {
                layoutParams.width = (int) (widthPixel * widhtAspect);
            } else if (mWindowWidth > 0) {
                layoutParams.width = mWindowWidth;
            }
            LoggerUtils.d(TAG, "layoutParams.width: " + layoutParams.width);
            LoggerUtils.d(TAG, "layoutParams.height: " + layoutParams.height);
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        dismiss();
    }

    /**
     * @param gravity {@link #GRAVITY_BOTTOM} or {@link #GRAVITY_CENTER}
     * @return
     */
    public BaseDialogFragment setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    private int mWindowWidth;

    public BaseDialogFragment setWidth(int width) {
        this.mWindowWidth = width;
        return this;
    }

    private float widhtAspect = 0f;

    /**
     * 设置对话框占屏幕宽度的比例
     *
     * @param aspect 百分比 如：0.7：  0.5 屏幕一半
     * @return
     */
    public BaseDialogFragment setWidthAspect(float aspect) {
        widhtAspect = aspect;
        return this;
    }

    private int backgroundResource;

    public BaseDialogFragment setBackground(int backgroundResource) {
        this.backgroundResource = backgroundResource;
        return this;
    }

    private boolean isCancel = true;

    public BaseDialogFragment setCanceledOnTouchOutside(boolean isCancel) {
        this.isCancel = isCancel;
        return this;
    }

    /**
     * 对话框关闭的时候调用此方法
     * 如果需要在对话框关闭的时候执行什么逻辑，可以重写此方法
     */
    public void onDismiss() {

    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }
}

