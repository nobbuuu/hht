package com.booyue.poetry.adapter.download;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booyue.base.util.LoggerUtils;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/15 16:57
 * @description :
 */
public class DownloadOptionAdapter extends BaseRecyclerViewAdapter<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> {

    private List<StateBean> stateBeanList;
    private static final String TAG = "DownloadOptionAdapter";

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public DownloadOptionAdapter(Context context, List<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> list, boolean showFooter) {
        super(context, list, showFooter);
        stateBeanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            StateBean itemStateBean = new StateBean();
            itemStateBean.checked = false;
            itemStateBean.pos = i;

            SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean bookUnitVideoBean = list.get(i);
            LoggerUtils.d(TAG, "bookUnitVideoBean.unit: " + bookUnitVideoBean.unit);
            if (i > 0 && list.get(i - 1) != null) {
                SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean preBookUnitVideoBean = list.get(i - 1);
                if (!bookUnitVideoBean.unit.equals(preBookUnitVideoBean.unit)) {
                    itemStateBean.showHeader = true;
                    itemStateBean.unitChecked = false;
                }
            } else if (i == 0) {
                itemStateBean.showHeader = true;
                itemStateBean.unitChecked = false;
            }
            List<DownloadBean> downloadBeanList = DownloadDao.getInstance().queryDownloadComplete(bookUnitVideoBean.id);
            if (downloadBeanList != null && downloadBeanList.size() > 0) {
                itemStateBean.downloaded = true;
                boolean insert = false;
                DownloadBean downloadBean1 = null;//如果数据库中没有记录，需要增加一条记录
                for (DownloadBean downloadBean : downloadBeanList) {
                    if(downloadBean.grade.equals(bookUnitVideoBean.grade) && downloadBean.subject.equals(bookUnitVideoBean.subject) &&
                            downloadBean.version.equals(bookUnitVideoBean.version) && downloadBean.gradeAttr.equals(bookUnitVideoBean.gradeAtt)){
                        insert = true;
                        break;
                    }else {
                        downloadBean1 = downloadBean;
                        continue;
                    }
                }
                if(!insert && downloadBean1 != null){
                    //使用数据保存的记录，修改头部条件
                    DownloadBean downloadBean = new DownloadBean();
                    downloadBean.guid = bookUnitVideoBean.id;
                    downloadBean.title = bookUnitVideoBean.name;
                    downloadBean.url = bookUnitVideoBean.url;
                    downloadBean.subject = bookUnitVideoBean.subject;
                    downloadBean.grade = bookUnitVideoBean.grade;
                    downloadBean.version = bookUnitVideoBean.version;
                    downloadBean.gradeAttr = bookUnitVideoBean.gradeAtt;
                    downloadBean.coverImage = bookUnitVideoBean.coverImage;
                    downloadBean.unit = bookUnitVideoBean.unit;

                    downloadBean.FileSize = downloadBean1.FileSize;
                    downloadBean.isFinished = downloadBean1.isFinished;
                    downloadBean.percent = downloadBean1.percent;
                    downloadBean.completeSize = downloadBean1.completeSize;
                    downloadBean.state = downloadBean1.state;
                    downloadBean.localPath = downloadBean1.localPath;

                    DownloadDao.getInstance().insert(downloadBean);
                }
            } else {
                itemStateBean.downloaded = false;
            }

            stateBeanList.add(itemStateBean);
        }

    }

    public List<StateBean> getCheckList() {
        return stateBeanList;
    }

    /**
     * 全部选中或全部取消选中
     *
     * @param checkAll
     */
    public void checkAll(boolean checkAll) {
        for (StateBean stateBean : stateBeanList) {
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
            SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean videoListBean = list.get(position);
            StateBean stateBean = stateBeanList.get(position);
            ((DownloadOptionViewHolder) holder).tvNum.setText(position + 1 + "");
            ((DownloadOptionViewHolder) holder).tvName.setText(videoListBean.name);
            ((DownloadOptionViewHolder) holder).tvUnitName.setText(videoListBean.unit);
            if (stateBean.showHeader) {
                ((DownloadOptionViewHolder) holder).rlUnit.setVisibility(View.VISIBLE);
            } else {
                ((DownloadOptionViewHolder) holder).rlUnit.setVisibility(View.GONE);
            }
            LoggerUtils.d(TAG, "stateBean.checked: " + stateBean.checked + ",,,position = " + position);

            ((DownloadOptionViewHolder) holder).cbCheck.setChecked(stateBean.checked);
            ((DownloadOptionViewHolder) holder).cbCheck.setOnClickListener(view -> stateBean.checked = !stateBean.checked);

            ((DownloadOptionViewHolder) holder).cbUnitCheck.setChecked(stateBean.unitChecked);
            ((DownloadOptionViewHolder) holder).cbUnitCheck.setOnClickListener(view -> {
                stateBean.unitChecked = !stateBean.unitChecked;
                //当前选中点，到下一个选中点之间非头部item的check状态为b
                int num = 0;
                for (int i = position; i < stateBeanList.size(); i++) {
                    if (!stateBeanList.get(i).showHeader || num < 1) {
                        stateBeanList.get(i).checked = stateBean.unitChecked;
                        num++;
                    } else {
                        break;
                    }
                }
                MyApp.mHandler.post(() -> {
                    // 刷新操作
                    notifyDataSetChanged();
                });
            });

            if (stateBean.downloaded) {
                ((DownloadOptionViewHolder) holder).tvNum.setTextColor(context.getResources().getColor(R.color.color_downloaded));
                ((DownloadOptionViewHolder) holder).tvName.setTextColor(context.getResources().getColor(R.color.color_downloaded));
                ((DownloadOptionViewHolder) holder).tvState.setVisibility(View.VISIBLE);
                ((DownloadOptionViewHolder) holder).cbCheck.setButtonDrawable(R.drawable.btn_frame_checked02);
                ((DownloadOptionViewHolder) holder).cbCheck.setEnabled(false);
            } else {
                ((DownloadOptionViewHolder) holder).tvNum.setTextColor(context.getResources().getColor(R.color.text_color_subject_choose));
                ((DownloadOptionViewHolder) holder).tvName.setTextColor(context.getResources().getColor(R.color.text_color_subject_choose));
                ((DownloadOptionViewHolder) holder).tvState.setVisibility(View.GONE);
                ((DownloadOptionViewHolder) holder).cbCheck.setButtonDrawable(R.drawable.selector_book_video_check);
                ((DownloadOptionViewHolder) holder).cbCheck.setEnabled(true);
            }
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

    public static class StateBean {
        public DownloadBean downloadBean;//下载书本详情页面删除需要用到
        public boolean checked;//是否选中
        public boolean unitChecked;//单元是否选中
        public boolean showHeader;//是否显示头部
        public boolean downloaded;//是否已经下载
        public int pos;//在列表中的位置
    }
}
