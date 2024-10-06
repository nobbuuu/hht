package com.booyue.poetry.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.booyue.base.util.DimensionUtil;
import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.poetry.listener.OnItemButtonClickListener;
import com.booyue.poetry.view.BookView;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/2 19:10
 * @description :
 */
public class BookAdapter extends BaseRecyclerViewAdapter<SearchResultBean.BookBean> {
    public static final String TAG = "BookAdapter";
    public int state = BookView.STATE_NORMAL;


    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */

    public BookAdapter(Context context, List<SearchResultBean.BookBean> list, boolean showFooter) {
        super(context, list, showFooter);
    }

    public SparseArray<Boolean> checkList = new SparseArray<>();

    public void initCheckList(boolean check, boolean notifyDataChange) {
        LoggerUtils.d(TAG, "initCheckList: check = " + check + ",notifyDataChange = " + notifyDataChange);
        for (int i = 0; i < list.size(); i++) {
            checkList.put(i, check);
        }
        if (notifyDataChange) {
            notifyDataSetChanged();
        }
    }

    public SparseArray<Boolean> getCheckList() {
        return checkList;
    }

    /**
     * 设置状态
     *
     * @param state 一般状态，编辑状态（显示复选框）
     */
    public void setState(int state) {
        LoggerUtils.d(TAG, "setState: " + state);
        this.state = state;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_book, parent, false);
        return new CommonViewHolder(view);
    }


    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof CommonViewHolder) {
            SearchResultBean.BookBean resVo = list.get(position);
            if (isLocal()) {
                if (checkList.size() > position) {
                    boolean checked = checkList.get(position);
                    ((CommonViewHolder) holder).bookView.setCbCheck(checked);
                }
                //编辑状态
                if (state == BookView.STATE_EDIT) {
                    ((CommonViewHolder) holder).bookView.setCheckListener(view -> {
                        Boolean check = checkList.get(position);
                        LoggerUtils.d(TAG, "check: " + check + ",checkList " + checkList.size());
                        checkList.put(position, !check);
                    });
                }

            } else {
//                FrameLayout.LayoutParams marginLayoutParams = new FrameLayout.LayoutParams(
//                        DimensionUtil.dip2Px(context, 193),
//                        DimensionUtil.dip2Px(context, 243));
//                marginLayoutParams.leftMargin = DimensionUtil.dip2Px(context, 3);
//                marginLayoutParams.rightMargin = DimensionUtil.dip2Px(context, 3);
//                marginLayoutParams.topMargin = DimensionUtil.dip2Px(context, 6);
//                ((CommonViewHolder) holder).bookView.setLayoutParams(marginLayoutParams);
                ((CommonViewHolder) holder).bookView
                        .setOnDownloadClickListener(view -> {
                            if (onItemButtonClickListener != null) {
                                onItemButtonClickListener.onItemButtonClick(position);
                            }
                        });
            }
            String gradeAttr = "";
            if (!TextUtils.isEmpty(resVo.gradeAtt)) {
                gradeAttr = "(" + resVo.gradeAtt + "册)";
            }
            LoggerUtils.d(TAG, "loadImage position: " + position);
            if (TextUtils.isEmpty(resVo.grade)) {
                ((CommonViewHolder) holder).bookView.updateState(BookView.STATE_ADD);
                ((CommonViewHolder) holder).bookView.setImageResource(R.drawable.btn_addbook);
                ((CommonViewHolder) holder).bookView.setGradeAndVersion("", "");
            } else {
                ((CommonViewHolder) holder).bookView.updateState(state);
                ((CommonViewHolder) holder).bookView.loadImage(resVo.coverImage);
                ((CommonViewHolder) holder).bookView.setGradeAndVersion(/*resVo.subject + */resVo.grade + gradeAttr, resVo.version);
            }
        }
    }

    public boolean isLocal() {
        return false;
    }

    public boolean processLocal(CommonViewHolder holder, SearchResultBean.BookBean resVo, int position) {
        return false;
    }

    public class CommonViewHolder extends BaseRecyclerViewHolder {
        BookView bookView;

        public CommonViewHolder(View itemView) {
            super(itemView);
            bookView = itemView.findViewById(R.id.bookView);
        }

    }

    private OnItemButtonClickListener onItemButtonClickListener;

    public void setOnItemButtonClickListener(OnItemButtonClickListener onItemButtonClickListener) {
        this.onItemButtonClickListener = onItemButtonClickListener;
    }


}
