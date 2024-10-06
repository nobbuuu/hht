package com.booyue.poetry.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.booyue.base.dialog.PopupWindowManager;
import com.booyue.base.util.TextDrawableUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.SubjectChooserAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/22 9:19
 * @description :
 */
public class SubjectChooseView extends FrameLayout {
    public static final int TYPE_SUBJECT = 1;
    public static final int TYPE_VERSION = 2;

    public SubjectChooseView(Context context) {
        this(context, null);
    }

    public SubjectChooseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubjectChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private TextView tvSubjectChooser;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> subjectList = new ArrayList<>();
    private String currentSubject;
    private SubjectChooserAdapter subjectChooserAdapter;
    private PopupWindow popupWindow;

    public void init(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_subject_choose, this);
        tvSubjectChooser = findViewById(R.id.tvSubjectChooser);
        tvSubjectChooser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjectChooserAdapter == null) {
                    showSubjectPopupWindow(tvSubjectChooser);
                } else {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 显示科目选择框
     *
     * @param archor
     */
    public void showSubjectPopupWindow(View archor) {
        TextDrawableUtils.setRightDrawable(context, tvSubjectChooser, R.drawable.btn_frame_up);
        tvSubjectChooser.setBackgroundResource(R.drawable.shape_subject_choose_button_bg_expand);
        View view = layoutInflater.inflate(R.layout.dialog_subject_chooser, null);
        popupWindow = PopupWindowManager.show((Activity) context, view,archor,R.drawable.shape_subject_choose_dialog_bg, new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                TextDrawableUtils.setRightDrawable(context, tvSubjectChooser, R.drawable.btn_frame_down);
                tvSubjectChooser.setBackgroundResource(R.drawable.shape_subject_choose_button_bg);
                subjectChooserAdapter = null;
                popupWindow = null;
            }
        });
        HHTRecyclerView recyclerView = view.findViewById(R.id.hhtRecylcerViewSubject);
        recyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        subjectChooserAdapter = new SubjectChooserAdapter(context, subjectList, false);
        subjectChooserAdapter.setOnItemClickLinster((view1, pos, o) -> {
            String subject = (String) o;
            if (subject.equals(this.currentSubject)) return;
            this.currentSubject = subject;
            tvSubjectChooser.setText(subject);
            subjectChooserAdapter.setCheckPosition(pos);
//            searchCourse();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view1, pos, o);
            }
        });
        recyclerView.setHHTAdapter(subjectChooserAdapter);
    }

    private int type = TYPE_SUBJECT;

    /**
     * 设置当前页面的类型
     *
     * @param type
     */
    public SubjectChooseView setType(int type) {
        this.type = type;
        if (type == TYPE_SUBJECT) {
            tvSubjectChooser.setText(R.string.chooser_all_subject);
        } else if (type == TYPE_VERSION) {
            tvSubjectChooser.setText(R.string.chooser_all_version);
        }
        return this;
    }

    private BaseRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 更新数据
     * @param list
     */
    public void notifyDataChanged(List<String> list) {
        list.add(0,tvSubjectChooser.getText().toString());
        if (list != null && subjectChooserAdapter != null) {
            subjectChooserAdapter.addAll(list, true);
        }else {
            subjectList = list;
        }
    }

}
