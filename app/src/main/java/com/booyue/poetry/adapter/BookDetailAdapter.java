package com.booyue.poetry.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booyue.base.util.LoggerUtils;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadOptionAdapter;
import com.booyue.poetry.view.BookView;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/15 16:57
 * @description :
 */
public class BookDetailAdapter extends BaseRecyclerViewAdapter<DownloadBean> {
    private List<DownloadOptionAdapter.StateBean> stateBeanList;
    private static final String TAG = "DownloadOptionAdapter";
    private int state = BookView.STATE_NORMAL;

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public BookDetailAdapter(Context context, List<DownloadBean> list, boolean showFooter) {
        super(context, list, showFooter);
        initStateList(list);
    }

    /**
     * 设置状态
     * @param state 一般状态，编辑状态（显示复选框）
     */
    public void setState(int state){
        this.state = state;
        notifyDataSetChanged();
    }


    /**
     * 初始化状态列表
     */
    public void initStateList(List<DownloadBean> list){
        stateBeanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DownloadOptionAdapter.StateBean itemStateBean = new DownloadOptionAdapter.StateBean();
            itemStateBean.checked = false;
            itemStateBean.pos = i;

            DownloadBean bookUnitVideoBean = list.get(i);
            LoggerUtils.d(TAG, "bookUnitVideoBean.unit: " + bookUnitVideoBean.unit);
            if (i > 0 && list.get(i - 1) != null) {
                DownloadBean preBookUnitVideoBean = list.get(i - 1);
                if (!bookUnitVideoBean.unit.equals(preBookUnitVideoBean.unit)) {
                    itemStateBean.showHeader = true;
                    itemStateBean.unitChecked = false;
                }
            } else if (i == 0) {
                itemStateBean.showHeader = true;
                itemStateBean.unitChecked = false;
            }
            stateBeanList.add(itemStateBean);
        }

    }


    public List<DownloadOptionAdapter.StateBean> getCheckList() {
        return stateBeanList;
    }

    /**
     * 全部选中或全部取消选中
     *
     * @param checkAll
     */
    public void checkAll(boolean checkAll) {
        for (DownloadOptionAdapter.StateBean stateBean : stateBeanList) {
            stateBean.checked = checkAll;
            stateBean.unitChecked = checkAll;
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_download_option, parent, false);
        return new DownloadOptionViewHolder(view);
    }


    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof DownloadOptionViewHolder) {
            DownloadBean videoListBean = list.get(position);
            DownloadOptionAdapter.StateBean stateBean = stateBeanList.get(position);
            ((DownloadOptionViewHolder) holder).tvNum.setText(position + 1 +  "");
            ((DownloadOptionViewHolder) holder).tvName.setText(videoListBean.title);
            ((DownloadOptionViewHolder) holder).tvUnitName.setText(videoListBean.unit);

            if (stateBean.showHeader) {
                ((DownloadOptionViewHolder) holder).rlUnit.setVisibility(View.VISIBLE);
            } else {
                ((DownloadOptionViewHolder) holder).rlUnit.setVisibility(View.GONE);
            }
            LoggerUtils.d(TAG, "stateBean.checked: " + stateBean.checked + ",,,position = " + position);
            if(state == BookView.STATE_EDIT){
                ((DownloadOptionViewHolder) holder).cbCheck.setVisibility(View.VISIBLE);
            }else {
                ((DownloadOptionViewHolder) holder).cbCheck.setVisibility(View.GONE);
            }
            ((DownloadOptionViewHolder) holder).cbCheck.setChecked(stateBean.checked);
            ((DownloadOptionViewHolder) holder).cbCheck.setOnClickListener(view -> stateBean.checked = !stateBean.checked);
             ((DownloadOptionViewHolder) holder).cbUnitCheck.setVisibility(View.GONE);
//            ((DownloadOptionViewHolder) holder).cbUnitCheck.setChecked(stateBean.unitChecked);
//            ((DownloadOptionViewHolder) holder).cbUnitCheck.setOnClickListener(view -> {
//                stateBean.unitChecked = !stateBean.unitChecked;
//                //当前选中点，到下一个选中点之间非头部item的check状态为b
//                int num = 0;
//                for (int i = position; i < stateBeanList.size(); i++) {
//                    if (!stateBeanList.get(i).showHeader || num < 1) {
//                        stateBeanList.get(i).checked = stateBean.unitChecked;
//                        num++;
//                    } else {
//                        break;
//                    }
//                }
//                MyApp.mHandler.post(() -> {
//                    // 刷新操作
//                    notifyDataSetChanged();
//                });
//            });
        }
    }

    class DownloadOptionViewHolder extends BaseRecyclerViewHolder {
        RelativeLayout rlUnit;
        TextView tvUnitName;
        CheckBox cbUnitCheck;
        TextView tvNum;
        TextView tvName;
        TextView tvState;
        CheckBox cbCheck;

        public DownloadOptionViewHolder(View itemView) {
            super(itemView);
            rlUnit = itemView.findViewById(R.id.rlUnit);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            cbUnitCheck = itemView.findViewById(R.id.cbUnitCheck);
            tvNum = itemView.findViewById(R.id.tvNum);
            tvName = itemView.findViewById(R.id.tvName);
            tvState = itemView.findViewById(R.id.tvState);
            cbCheck = itemView.findViewById(R.id.cbCheck);
        }
    }

}
