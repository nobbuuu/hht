package com.booyue.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Administrator on 2017/4/27.
 */
public class SwitchView extends View {
    public static final String TAG = "SlideSwitch";
    public static final int SWITCH_OFF = 0;//关闭状态
    public static final int SWITCH_ON = 1;//打开状态
    public static final int SWITCH_SCROLING = 2;//滚动状态

    //用于显示的文本
    private String mOnText = "";
    private String mOffText = "";

    private int mSwitchStatus = SWITCH_OFF;

    private boolean mHasScrolled = false;//表示是否发生过滚动

    private int mSrcX = 0, mDstX = 0;

    private int mBmpWidth = 0;
    private int mBmpHeight = 0;
    private int mThumbWidth = 0;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private OnSwitchChangedListener mOnSwitchChangedListener = null;

    //开关状态图
    Bitmap mSwitch_off, mSwitch_on, mSwitch_thumb;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    //初始化三幅图片
    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        Drawable on = mTypedArray.getDrawable(R.styleable.SwitchView_on);
        Drawable off = mTypedArray.getDrawable(R.styleable.SwitchView_off);
        Drawable thumb = mTypedArray.getDrawable(R.styleable.SwitchView_thumb);
        mSwitch_off = drawableToBitmap(off);
        mSwitch_on = drawableToBitmap(on);
        mSwitch_thumb = drawableToBitmap(thumb);
        mTypedArray.recycle();
        mBmpWidth = mSwitch_on.getWidth();
        mBmpHeight = mSwitch_on.getHeight();
        mThumbWidth = mSwitch_thumb.getWidth();
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = mBmpWidth;
        params.height = mBmpHeight;
        super.setLayoutParams(params);
    }

    /**
     * 为开关控件设置状态改变监听函数
     *
     * @param onSwitchChangedListener 参见 {@link OnSwitchChangedListener}
     */
    public void setOnSwitchChangedListener(OnSwitchChangedListener onSwitchChangedListener) {
        mOnSwitchChangedListener = onSwitchChangedListener;
    }

    /**
     * 设置开关上面的文本
     *
     * @param onText  控件打开时要显示的文本
     * @param offText 控件关闭时要显示的文本
     */
    public void setText(final String onText, final String offText) {
        mOnText = onText;
        mOffText = offText;
        invalidate();
    }

    /**
     * 设置开关的状态
     *
     * @param on 是否打开开关 打开为true 关闭为false
     */
    public void setStatus(boolean on) {
        mSwitchStatus = (on ? SWITCH_ON : SWITCH_OFF);
        invalidate();
    }
    public boolean isChecked(){
        return SWITCH_ON == mSwitchStatus;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        LoggerUtils.d(TAG, "onTouchEvent  x=" + event.getX());
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mSrcX = (int) event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mDstX = Math.max((int) event.getX(), 10);
//                mDstX = Math.min(mDstX, 62);
//                if (mSrcX == mDstX)
//                    return true;
//                mHasScrolled = true;
//                AnimationTransRunnable aTransRunnable = new AnimationTransRunnable(mSrcX, mDstX, 0);
//                new Thread(aTransRunnable).start();
//                mSrcX = mDstX;
//                break;
//            case MotionEvent.ACTION_UP:
//                if (mHasScrolled == false)//如果没有发生过滑动，就意味着这是一次单击过程
//                {
//                    mSwitchStatus = Math.abs(mSwitchStatus - 1);
//                    int xFrom = 10, xTo = 62;
//                    if (mSwitchStatus == SWITCH_OFF) {
//                        xFrom = 62;
//                        xTo = 10;
//                    }
//                    AnimationTransRunnable runnable = new AnimationTransRunnable(xFrom, xTo, 1);
//                    new Thread(runnable).start();
//                } else {
//                    invalidate();
//                    mHasScrolled = false;
//                }
//                //状态改变的时候 回调事件函数
//                if (mOnSwitchChangedListener != null) {
//                    mOnSwitchChangedListener.onSwitchChanged(this, mSwitchStatus);
//                }
//                break;
//
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘图的时候 内部用到了一些数值的硬编码，其实不太好，
        //主要是考虑到图片的原因，图片周围有透明边界，所以要有一定的偏移
        //硬编码的数值只要看懂了代码，其实可以理解其含义，可以做相应改进。
        mPaint.setTextSize(14);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        if (mSwitchStatus == SWITCH_OFF) {
            drawBitmap(canvas, null, null, mSwitch_off);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.translate(mSwitch_thumb.getWidth(), 0);
            canvas.drawText(mOffText, 0, 20, mPaint);

        } else if (mSwitchStatus == SWITCH_ON) {
            drawBitmap(canvas, null, null, mSwitch_on);
            int count = canvas.save();
            canvas.translate(mSwitch_on.getWidth() - mSwitch_thumb.getWidth(), 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.WHITE);
            canvas.restoreToCount(count);
            canvas.drawText(mOnText, 17, 20, mPaint);

        } else {//SWITCH_SCROLING
            mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;
            drawBitmap(canvas, new Rect(0, 0, mDstX, mBmpHeight), new Rect(0, 0, (int) mDstX, mBmpHeight), mSwitch_on);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(mOnText, 17, 20, mPaint);

            int count = canvas.save();
            canvas.translate(mDstX, 0);
            drawBitmap(canvas, new Rect(mDstX, 0, mBmpWidth, mBmpHeight),
                    new Rect(0, 0, mBmpWidth - mDstX, mBmpHeight), mSwitch_off);
            canvas.restoreToCount(count);

            count = canvas.save();
            canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight);
            canvas.translate(mThumbWidth, 0);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.drawText(mOffText, 0, 20, mPaint);
            canvas.restoreToCount(count);

            count = canvas.save();
            canvas.translate(mDstX - mThumbWidth / 2, 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            canvas.restoreToCount(count);
        }
    }

    public void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap) {
        dst = (dst == null ? new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()) : dst);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    /**
     * AnimationTransRunnable 做滑动动画所使用的线程
     */
    private class AnimationTransRunnable implements Runnable {
        private int srcX, dstX;
        private int duration;

        /**
         * 滑动动画
         *
         * @param srcX     滑动起始点
         * @param dstX     滑动终止点
         * @param duration 是否采用动画，1采用，0不采用
         */
        public AnimationTransRunnable(float srcX, float dstX, final int duration) {
            this.srcX = (int) srcX;
            this.dstX = (int) dstX;
            this.duration = duration;
        }

        @Override
        public void run() {
            final int patch = (dstX > srcX ? 5 : -5);
            if (duration == 0) {
                SwitchView.this.mSwitchStatus = SWITCH_SCROLING;
                SwitchView.this.postInvalidate();
            } else {
                int x = srcX + patch;
                while (Math.abs(x - dstX) > 5) {
                    mDstX = x;
                    SwitchView.this.mSwitchStatus = SWITCH_SCROLING;
                    SwitchView.this.postInvalidate();
                    x += patch;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mDstX = dstX;
                SwitchView.this.mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;
                SwitchView.this.postInvalidate();
            }
        }
    }

    public interface OnSwitchChangedListener {
        /**
         * 状态改变 回调函数
         *
         * @param status SWITCH_ON表示打开 SWITCH_OFF表示关闭
         */
        void onSwitchChanged(SwitchView obj, int status);
    }
}
