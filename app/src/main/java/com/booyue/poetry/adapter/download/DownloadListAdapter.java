package com.booyue.poetry.adapter.download;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.booyue.base.util.DataCleanManager;
import com.booyue.base.util.LoggerUtils;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.utils.DialogUtil;
import com.booyue.poetry.view.DownloadNewButton;
import com.booyue.view.xrecyclerview.BaseRecyclerViewAdapter;
import com.booyue.view.xrecyclerview.BaseRecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/16 15:39
 * @description :
 */
public class DownloadListAdapter extends BaseRecyclerViewAdapter<DownloadBean> {
    private static final String TAG = "DownloadListAdapter";
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_COMPPLETE = 2;
    public int state = STATE_DOWNLOADING;


    /**
     * 构造方法
     *
     * @param context    上下文
     * @param list       数据列表
     * @param showFooter 是否显示脚布局
     */
    public DownloadListAdapter(Context context, List<DownloadBean> list, boolean showFooter) {
        super(context, list, showFooter);

    }
    /**
     * 构造方法
     * @param isDelete 是否显示多选删除
     */
    private Boolean isDelete = false;
    public void IsShowDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolderImpl(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_download_two, parent, false);
        return new DownloadListViewHolder(view);
    }

    @Override
    public void onBindViewHolderImpl(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof DownloadListViewHolder) {
            DownloadBean bean = list.get(position);
            ((DownloadListViewHolder) holder).tvName.setText(bean.title);
            ((DownloadListViewHolder) holder).tvParentPath.setText(context.getString(R.string.sub_name
                    , bean.subject, bean.version, bean.grade, bean.gradeAttr, bean.unit));
            String fileSize = DataCleanManager.getFormatSize(bean.FileSize);
            ((DownloadListViewHolder) holder).tvSize.setText(fileSize);
            if (state == STATE_COMPPLETE) {
                ((DownloadListViewHolder) holder).tvCompete.setVisibility(View.VISIBLE);
                ((DownloadListViewHolder) holder).btnDownload.setVisibility(View.GONE);
                ((DownloadListViewHolder) holder).icDelete.setVisibility(View.VISIBLE);
                ((DownloadListViewHolder) holder).icDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showConfirmDialog((Activity) context, "确认删除"+bean.title+"?", view1 -> {
                            List<DownloadBean> downloadBeans = new ArrayList<>();
                            downloadBeans.add(list.get(position));
                            DownloadDao.getInstance().deleteList(downloadBeans);
                            for (int i = 0; i < list.size(); i++) {
                                if (bean.guid == list.get(i).guid) {
                                    list.remove(list.get(i));
                                    notifyDataSetChanged();
                                    EventBus.getDefault().post(new Boolean(true));
                                    return;
                                }
                            }
                        });
                    }
                });
                if (isDelete){
                    ((DownloadListViewHolder) holder).cdeUnitCheck.setVisibility(View.VISIBLE);//  正常情况下 应该写 View.VISIBLE
                    ((DownloadListViewHolder) holder).cdeUnitCheck.setChecked(bean.isChack);
                    ((DownloadListViewHolder) holder).cdeUnitCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (list.get(position).isChack){
                                list.get(position).isChack = false;
                            }else{
                                list.get(position).isChack = true;
                            }
                            EventBus.getDefault().post(new Boolean(false));
                        }
                    });
                }else {
                    ((DownloadListViewHolder) holder).cdeUnitCheck.setVisibility(View.INVISIBLE);//  正常情况下 应该写 View.VISIBLE
                }
            } else {
                ((DownloadListViewHolder) holder).icDelete.setVisibility(View.GONE);
                ((DownloadListViewHolder) holder).cdeUnitCheck.setVisibility(View.GONE);
                LoggerUtils.d(TAG, "------------------------------------------: ");
                LoggerUtils.d(TAG, "bean.title = : " + bean.title);
                LoggerUtils.d(TAG, "bean.guid = : " + bean.guid);
                LoggerUtils.d(TAG, "bean.pecent = : " + bean.percent);
                LoggerUtils.d(TAG, "bean.FileSize = " + bean.FileSize);
                LoggerUtils.d(TAG, "bean.state = : " + bean.state);
                LoggerUtils.d(TAG, "============================================: ");
                LoggerUtils.d(TAG, "onCallback:111: " +  bean.guid + "---adapter");
                ((DownloadListViewHolder) holder).btnDownload.initState(bean,view -> {
                    DialogUtil.showConfirmDialog((Activity) context, context.getString(R.string.dialog_cancel_download_title), view1 -> {
                        MyApp.getInstance().getDownloadHelper().removeTaskByid(bean.guid);
                        for (int i = 0; i < list.size(); i++) {
                            if (bean.guid == list.get(i).guid) {
                                list.remove(list.get(i));
                                notifyDataSetChanged();
                                return;
                            }
                        }
                    });
                });
            }


        }
    }

    public void setState(int state) {
        this.state = state;
    }


    public DownloadBeanID beanID;

    public interface DownloadBeanID{
        void setDownloadBeanID(DownloadBean list);
    }


    public void setDownloadBeanID(DownloadBeanID beanID) {
        this.beanID = beanID;
    }

    public class DownloadListViewHolder extends BaseRecyclerViewHolder {
        TextView tvName;
        TextView tvParentPath;
        TextView tvSize;
        TextView tvCompete;
        ImageView icDelete;
        CheckBox cdeUnitCheck;
        public DownloadNewButton btnDownload;


        public DownloadListViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvParentPath = itemView.findViewById(R.id.tvParentPath);
            tvSize = itemView.findViewById(R.id.tvSize);
            btnDownload = itemView.findViewById(R.id.downloadButton);
            tvCompete = itemView.findViewById(R.id.tvComplete);
            icDelete = itemView.findViewById(R.id.icDelete);
            cdeUnitCheck = itemView.findViewById(R.id.cdeUnitCheck);

        }
    }
}
