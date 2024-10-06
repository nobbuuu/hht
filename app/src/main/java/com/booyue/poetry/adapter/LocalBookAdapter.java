package com.booyue.poetry.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.poetry.view.BookView;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/2 19:10
 * @description :
 */
public class LocalBookAdapter extends BookAdapter {

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public LocalBookAdapter(Context context, List<SearchResultBean.BookBean> list, boolean showFooter) {
        super(context, list, showFooter);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public boolean processLocal(CommonViewHolder holder, SearchResultBean.BookBean resVo, int position) {
        if (checkList.size() > position) {
            boolean checked = checkList.get(position);
            holder.bookView.setCbCheck(checked);
        }
        if (!TextUtils.isEmpty(resVo.grade)) {
            //编辑状态
            if (state == BookView.STATE_EDIT) {
                holder.bookView.setCheckListener(view -> {
                    Boolean check = checkList.get(position);
                    LoggerUtils.d(TAG, "check: " + check + ",checkList " + checkList.size());
                    checkList.put(position, !check);
                });
            }

        } else {
            holder.bookView.updateState(BookView.STATE_ADD)
                    .setImageResource(R.drawable.btn_addbook);
            LoggerUtils.d(TAG, "setImageResource: " + position);
            return true;
        }
        return false;
    }


}
