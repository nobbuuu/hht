package com.booyue.poetry.ui.book2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.booyue.poetry.R;

public class FontTextView  extends AppCompatTextView {

    private final int one = 100;
    private final int two = 200;
    private final int three = 300;
    private final int forr = 400;
    private final int five = 500;
    private final int six = 600;
    private final int seven = 700;

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取参数
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FontTextView, defStyleAttr, 0);

        int fontType = a.getInt(R.styleable.FontTextView_fontType, 1);

        String fontPath = null;
        switch (fontType) {
            case one:
                fontPath = "fonts/j100.ttf";
                break;
            case two:
                fontPath = "fonts/j200.ttf";
                break;
            case three:
                fontPath = "fonts/j300.ttf";
                break;
            case forr:
                fontPath = "fonts/j400.ttf";
                break;
            case five:
                fontPath = "fonts/j500.ttf";
                break;
            case six:
                fontPath = "fonts/j600.ttf";
                break;
            case seven:
                fontPath = "fonts/j700.ttf";
                break;
            default:
        }
        //设置字体
        if (!TextUtils.isEmpty(fontPath)) {
            Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), fontPath);
            setTypeface(typeFace);

        }
    }
}
