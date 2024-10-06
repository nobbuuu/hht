package com.booyue.poetry.ui;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.BookAdapter;
import com.booyue.poetry.adapter.GradeChooserAdapter;
import com.booyue.poetry.bean.SearchResultBean;
import com.booyue.poetry.model.MainModel;
import com.booyue.poetry.statistic.UmengStatisticManager;
import com.booyue.poetry.ui.dialog.DownloadOptionDialog;
import com.booyue.poetry.ui.download.DownloadActivity;
import com.booyue.poetry.view.HHTSwipeRecyclerView;
import com.booyue.poetry.view.SubjectChooseView;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends HeaderBaseActivity {
    private static final String TAG = "MainActivity";
    private BookAdapter mBookAdapter;
    private List<SearchResultBean.BookBean> list;
    private List<String> gradeList;

    private GradeChooserAdapter mGradeChooserAdapter;
    private MainModel mainModel;

    private HHTSwipeRecyclerView hhtSwipeRecyclerView;
    private HHTRecyclerView hhtSwipeRecyclerViewGrade;
    private SubjectChooseView subjectChooseView;
    private SubjectChooseView versionChooseView;
    private TextView tvDownload;


    private String subject = "";
    private String grade = "";
    private String version = "";
    LinearLayout containerEmpty;
    ImageView ivEmptyImage;
    TextView tv_desc;
    ImageButton ibRefresh;


    @NotNull
    @Override
    public View initContentView() {
        View view = layoutInflater.inflate(R.layout.activity_main, null);
        hhtSwipeRecyclerView = view.findViewById(R.id.hhtSwipeRecyclerView);
        hhtSwipeRecyclerViewGrade = view.findViewById(R.id.hhtSwipeRecyclerViewGrade);
        subjectChooseView = view.findViewById(R.id.subjectChooseView);
        versionChooseView = view.findViewById(R.id.versionChooseView);
        tvDownload = view.findViewById(R.id.tvDownload);
        containerEmpty = view.findViewById(R.id.containerEmpty);
        ivEmptyImage = view.findViewById(R.id.ivEmptyImage);
        tv_desc = view.findViewById(R.id.tv_desc);
        ibRefresh = view.findViewById(R.id.ibRefresh);
        return view;
    }

    @Override
    public void initListener() {
        subjectChooseView.setOnItemClickListener((view, pos, o) -> {
            if (pos > 0) {
                subject = (String) o;
            } else {
                subject = "";
            }
            UmengStatisticManager.clickChooseSubject(subject);
            searchCourse();
        });
        versionChooseView.setOnItemClickListener((view, pos, o) -> {
            if (pos > 0) {
                version = (String) o;
            } else {
                version = "";
            }
            UmengStatisticManager.clickChoosePublisher(version);
            searchCourse();
        });

        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpTo(DownloadActivity.class);
                UmengStatisticManager.clickDownloadList("选课");
            }
        });
        hhtSwipeRecyclerView.setOnRefreshListener(new HHTSwipeRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh(int page) {
                searchCourse();
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
    }

    public static final int REQUEST_CHOOSEFILE = 100;

    @Override
    public void initData() {
        list = new ArrayList<>();
        mBookAdapter = new BookAdapter(this, list, true);
        hhtSwipeRecyclerView.initGridAdapter(mBookAdapter, 4, null);
        mBookAdapter.setOnItemClickLinster((view, pos, o) -> {
            LoggerUtils.d(TAG, "详情: ");
            SearchResultBean.BookBean dataBean = (SearchResultBean.BookBean) o;
            LoggerUtils.d(TAG, "subject:" + dataBean.subject + ",version:" +
                    dataBean.version + ",grade: " + dataBean.grade + ",gradeAttr:" +
                    dataBean.gradeAtt);
//            Bundle bundle = new Bundle();
//            bundle.putString(BookDetailActivity.Companion.getKEY_SUBJECT(), dataBean.subject);
//            bundle.putString(BookDetailActivity.Companion.getKEY_GRADE(), dataBean.grade);
//            bundle.putString(BookDetailActivity.Companion.getKEY_GRADE_ATTR(), dataBean.gradeAtt);
//            bundle.putString(BookDetailActivity.Companion.getKEY_PUBLISHER(), dataBean.version);
//            bundle.putString(BookDetailActivity.Companion.getKEY_COVER_IMAGE(), dataBean.coverImage);
//            jumpTo(bundle, BookDetailActivity.class);
            mainModel.searchCourse(dataBean.subject, dataBean.grade, dataBean.version, dataBean.gradeAtt, 1, dataBean1 -> {
                DownloadOptionDialog downloadOptionDialog = DownloadOptionDialog.show(MainActivity.this);
                downloadOptionDialog.setData(dataBean1.get(0));
            });
        });
        mBookAdapter.setOnItemButtonClickListener(position -> {
            LoggerUtils.d(TAG, "下载: ");
            SearchResultBean.BookBean dataBean = list.get(position);
            mainModel.searchCourse(dataBean.subject, dataBean.grade, dataBean.version, dataBean.gradeAtt, 1, dataBean1 -> {
                DownloadOptionDialog downloadOptionDialog = DownloadOptionDialog.show(MainActivity.this);
                downloadOptionDialog.setData(dataBean1.get(0));
            });
        });
        getSearchCondition();
        gradeList = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.densityDpi;

        mGradeChooserAdapter = new GradeChooserAdapter(this, gradeList, false,density);
        hhtSwipeRecyclerViewGrade.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        hhtSwipeRecyclerViewGrade.setHHTAdapter(mGradeChooserAdapter);
        mGradeChooserAdapter.setOnItemClickLinster((view, pos, o) -> {
            String grade = (String) o;
            if (grade.equals(this.grade)) return;
            this.grade = grade;
            UmengStatisticManager.clickChooseGrade(grade);
            mGradeChooserAdapter.setCurrentPos(pos);
            searchCourse();
        });
        subjectChooseView.setType(SubjectChooseView.TYPE_SUBJECT);
        versionChooseView.setType(SubjectChooseView.TYPE_VERSION);
        UmengStatisticManager.showPage("选课");
    }

    /**
     * 获取搜索条件
     */
    public void getSearchCondition() {
        if (mainModel == null) {
            mainModel = new MainModel();
        }
        containerEmpty.setVisibility(View.GONE);
        mainModel.getSearchCondition(dataBean -> {
            if (subjectChooseView != null) {
                subjectChooseView.notifyDataChanged(dataBean.subjectList);
            }
            if (versionChooseView != null) {
                versionChooseView.notifyDataChanged(dataBean.versionList);
            }
            List<String> gradeList = dataBean.gradeList;
            if (mGradeChooserAdapter != null) {
                mGradeChooserAdapter.addAll(gradeList, true);
            }
            searchCourse();
        });
    }

    /**
     * 按条件搜索书本
     */
    public void searchCourse() {
        containerEmpty.setVisibility(View.GONE);
        mainModel.searchCourse(subject, grade, version, "", 0, dataBean1 -> {
            mBookAdapter.addAll(dataBean1, true);
            hhtSwipeRecyclerView.showContentOnSuccess("");
        });
    }


    @NotNull
    @Override
    public String getPageTitle() {
        return "选课";
    }

    public void showEmpty() {
        containerEmpty.setVisibility(View.VISIBLE);
        tv_desc.setText(R.string.empty);
        ivEmptyImage.setImageResource(R.drawable.pic_server);
        ibRefresh.setVisibility(View.GONE);
    }

    public void showServerError() {
        containerEmpty.setVisibility(View.VISIBLE);
        tv_desc.setText(R.string.empty);
        ivEmptyImage.setImageResource(R.drawable.pic_server);
        ibRefresh.setVisibility(View.VISIBLE);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSearchCondition();
            }
        });
    }


    public void showNetworkError() {
        containerEmpty.setVisibility(View.VISIBLE);
        tv_desc.setText(R.string.network_error);
        ivEmptyImage.setImageResource(R.drawable.pic_wifi);
        ibRefresh.setVisibility(View.VISIBLE);
        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSearchCondition();
            }
        });
    }


}
