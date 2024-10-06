package com.booyue.poetry.ui.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.booyue.base.dialog.BaseDialogFragment;
import com.booyue.base.util.CustomToast;
import com.booyue.base.util.MatcherUtil;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadOptionAdapter;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.poetry.model.MainModel;
import com.booyue.poetry.statistic.UmengStatisticManager;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/15 16:37
 * @description : 下载选择对话框
 */
public class DownloadOptionDialog extends BaseDialogFragment {
    private static final String TAG = "DownloadOptionDialog";
    private TextView tvGrade;
    private TextView tvVersion;
    private TextView tvCheckAll;
    private HHTRecyclerView hhtRecyclerView;
    private Button btnDownload;
    private DownloadOptionAdapter downloadOptionAdapter;
    private List<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> videoListBeanList;
    private boolean checkAll = false;

    @Override
    public View createView(LayoutInflater from) {
        View view = from.inflate(R.layout.dialog_download_option, null);
        if (videoListBeanList == null) {
            videoListBeanList = new ArrayList<>();
        }
        tvGrade = view.findViewById(R.id.tvGrade);
        String gradeStr = "";
        if(TextUtils.isEmpty(gradeAttr)){
            gradeStr = subject + grade;
        }else {
            gradeStr = subject + grade + gradeAttr + "册";
        }
        tvGrade.setText(gradeStr);
        tvVersion = view.findViewById(R.id.tvVersion);
        tvVersion.setText(version);
        tvCheckAll = view.findViewById(R.id.tvCheckAll);
        hhtRecyclerView = view.findViewById(R.id.hhtSwipeRecyclerView);
        downloadOptionAdapter = new DownloadOptionAdapter(getContext(), videoListBeanList, false);
        hhtRecyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        hhtRecyclerView.setHHTAdapter(downloadOptionAdapter);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = 0;
                for (int i = 0; i < downloadOptionAdapter.getCheckList().size(); i++) {
                    DownloadOptionAdapter.StateBean stateBean = downloadOptionAdapter.getCheckList().get(i);
                    SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean videoListBean = videoListBeanList.get(i);
                    if (stateBean.checked && !stateBean.downloaded) {
                        num++;
                        DownloadBean downloadBean = new DownloadBean();
                        downloadBean.guid = videoListBean.id;
                        downloadBean.title = videoListBean.name;
//                        downloadBean.url = "https://resource.alilo.com.cn/res/7,0175d9f82b02aa.mp4";
                        downloadBean.url = videoListBean.url;
                        downloadBean.subject = videoListBean.subject;
                        downloadBean.grade = videoListBean.grade;
                        downloadBean.version = videoListBean.version;
                        downloadBean.gradeAttr = videoListBean.gradeAtt;
                        downloadBean.coverImage = videoListBean.coverImage;
                        downloadBean.unit = videoListBean.unit;
//                        downloadBean.FileSize = videoListBean.fileSize;
                        MyApp.getInstance().getDownloadHelper().insertAndStart(downloadBean);
                    }
                }
                if (num == 0) {
                    CustomToast.showToast(R.string.tips_choose_download);
                    return;
                } else {
                    UmengStatisticManager.addDownload(subject + grade + version + gradeAttr);
                    CustomToast.showToast(R.string.tips_insert_download_success);
                }
                dismiss();
            }
        });
        //x
        tvCheckAll.setOnClickListener(view1 -> {
            checkAll = !checkAll;
            downloadOptionAdapter.checkAll(checkAll);
        });
        return view;
    }

    private String grade;
    private String gradeAttr;
    private String version;
    private String subject;

    public void setData(SearchResultBean.BookBean dataBean) {
        List<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean> videoListBeanList = MainModel.resList2VideoList(dataBean);
        grade = dataBean.grade;
        gradeAttr = dataBean.gradeAtt;
        version = dataBean.version;
        subject = dataBean.subject;
        Collections.sort(videoListBeanList, new Comparator<SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean>() {
            @Override
            public int compare(SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean bookUnitVideoBean, SearchResultBean.BookBean.BookUnitBean.BookUnitVideoBean t1) {
                if (MatcherUtil.hasDigit(bookUnitVideoBean.unit)) {
                    String num1 = MatcherUtil.getNumbers(bookUnitVideoBean.unit);
                    String num2 = "";
                    if (MatcherUtil.hasDigit(t1.unit)) {
                        num2 = MatcherUtil.getNumbers(t1.unit);
                    }
                    return Integer.parseInt(num1) - Integer.parseInt(num2);
                }
                return 0;
            }
        });
        if (downloadOptionAdapter != null) {
            downloadOptionAdapter.addAll(videoListBeanList);
        } else {
            this.videoListBeanList = videoListBeanList;
        }
    }

    /**
     * 显示dialogFragment
     *
     * @param fragmentActivity
     */
    public static DownloadOptionDialog show(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        DownloadOptionDialog downloadOptionDialog = new DownloadOptionDialog();
        downloadOptionDialog.show(fragmentManager, DownloadOptionDialog.TAG);
        return downloadOptionDialog;
    }
}
