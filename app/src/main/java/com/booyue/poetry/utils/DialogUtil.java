package com.booyue.poetry.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.booyue.base.dialog.BaseDialog;
import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.SharePreUtil;
import com.booyue.base.util.UpgradeUtil;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadDirChooserAdapter;
import com.booyue.poetry.bean.CheckUpgradeBean2;
import com.booyue.poetry.listener.OnSelectedDownloadPathListener;
import com.booyue.poetry.statistic.UmengStatisticManager;
import com.booyue.poetry.ui.book2.NewSetUpActivity;
import com.booyue.poetry.ui.download.DownloadActivity;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/28 8:48
 * @description :
 */
public class DialogUtil {
    private static final String TAG = "DialogUtil";

    /**
     * 创建确认对话框
     *
     * @param activity
     * @param title
     * @param onClickListener
     */
    public static void showConfirmDialog(Activity activity, String title, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm, null);
        AlertDialog confirmDialog = BaseDialog.showDialog(activity, view, false, true);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageButton ibCancel = view.findViewById(R.id.ibCancel);
        ImageView ibConfirm = view.findViewById(R.id.ibConfirm);
        tvTitle.setText(title);
        ibCancel.setOnClickListener(view1 -> {
            confirmDialog.dismiss();
        });
        ibConfirm.setOnClickListener(view12 -> {
            confirmDialog.dismiss();
            if (onClickListener != null) {
                onClickListener.onClick(view12);
            }
        });
    }

    /**
     * 设置下载路径
     *
     * @param activity
     * @param
     */
    public void showSetPathDialog(Activity activity) {
        String downloadDisk = SharePreUtil.getInstance().getCustomDownloadPath();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_set_path, null);
        AlertDialog confirmDialog = BaseDialog.showDialog(activity, view, false, true);
        TextView tvPath = view.findViewById(R.id.tvPath);;
        tvPath.setSelected(true);
        TextView tvChoosePath = view.findViewById(R.id.tvChoosePath);
        ImageView ibConfirm = view.findViewById(R.id.ibConfirm);
        tvPath.setText(downloadDisk);
        tvChoosePath.setOnClickListener(view1 -> {
            new DialogUtil().showChoosePathDialog(activity, new OnSelectedDownloadPathListener() {
                @Override
                public void onSelect(String path) {
                    tvPath.setText(path);
                }
            });
        });
        ibConfirm.setOnClickListener(view12 -> {
            SharePreUtil.getInstance().setCustomDownloadPath(tvPath.getText().toString());
            UmengStatisticManager.clickSetPathChange(tvPath.getText().toString());
            confirmDialog.dismiss();
        });
    }

    public static final int LEVEL_ROOT = 1;
    public static final int LEVEL_PARENT_ROOT = 2;
    /**
     * 显示选择下载目录对话框
     *
     * @param activity
     * @param
     */

    private int curentLevel = LEVEL_ROOT;
    private DownloadDirChooserAdapter mDownloadDirChooserAdapter;

    public void showChoosePathDialog(Activity activity, OnSelectedDownloadPathListener onSelectedDownloadPathListener) {
        curentLevel = LEVEL_ROOT;
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_choose_path, null);
        AlertDialog confirmDialog = BaseDialog.showDialog(activity, view, false, true);
        HHTRecyclerView hhtRecyclerView = view.findViewById(R.id.hhtRecyclerView);
        ImageView ibConfirm = view.findViewById(R.id.ibConfirm);
        TextView tvPrev = view.findViewById(R.id.tvPrev);
        ibConfirm.setOnClickListener(view12 -> {
            String checkDir = mDownloadDirChooserAdapter.getCheckedDir();
            if (!TextUtils.isEmpty(checkDir)) {
                onSelectedDownloadPathListener.onSelect(checkDir);
            }
            confirmDialog.dismiss();
        });
        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        confirmDialog.setOnDismissListener(dialogInterface -> {
            mDownloadDirChooserAdapter = null;
        });
        List<DownloadDirChooserAdapter.Folder> listFileName = new ArrayList<>();
        mDownloadDirChooserAdapter = new DownloadDirChooserAdapter(activity, listFileName, false);
        hhtRecyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        hhtRecyclerView.setHHTAdapter(mDownloadDirChooserAdapter);
        mDownloadDirChooserAdapter.setOnItemClickLinster((view1, pos, o) -> {
            DownloadDirChooserAdapter.Folder folder = mDownloadDirChooserAdapter.getList().get(pos);
            if (folder.name.equals(TF) || folder.name.equals(MEMORY)) {
                UmengStatisticManager.clickSetPath(folder.name);
            }
            File file = new File(folder.absolutePath);
            scan(file, false);
        });
        queryDisk();
    }

    public static final String TF = "外置TF卡";
    public static final String MEMORY = "内置存储";

    /**
     * 查询磁盘
     */
    public void queryDisk() {
        List<StorageUtil.Volume> diskList = StorageUtil.getVolume(MyApp.getInstance().getApplicationContext());
        List<DownloadDirChooserAdapter.Folder> listFileName = new ArrayList<>();
        for (int i = 0; i < diskList.size(); i++) {
            DownloadDirChooserAdapter.Folder folder = new DownloadDirChooserAdapter.Folder();
            StorageUtil.Volume volume = diskList.get(i);
            if (volume.getPath().contains("emulated")) {
                folder.name = MEMORY;
            } else {
                folder.name = TF;
            }
            folder.path = volume.getPath();
            folder.absolutePath = volume.getPath();
            folder.parent = "";
            curentLevel = LEVEL_ROOT;
            listFileName.add(folder);
        }
        mDownloadDirChooserAdapter.addAll(listFileName, true);//获取完该文件的所有文件夹之后刷新一下listview的显示数据
    }

    private String parent = "";

    public void scan(File file, boolean back) {
        LoggerUtils.d(TAG, "scan: " + file.getAbsolutePath());
        List<DownloadDirChooserAdapter.Folder> listFileName = new ArrayList<>();
        if (file.isDirectory()) {// 判断下是否为文件夹
            File[] files = file.listFiles();//获取该文件夹下的所有文件
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {//如果该文件是文件夹则把它存入一个list里面
                        DownloadDirChooserAdapter.Folder folder = new DownloadDirChooserAdapter.Folder();
                        folder.name = files[i].getName();
                        folder.path = files[i].getPath();
                        folder.absolutePath = files[i].getAbsolutePath();
                        folder.parent = files[i].getParentFile().getParent();
                        LoggerUtils.d(TAG, "name: " + folder.name + ",path = " + folder.path + ",absolutePath = " + folder.absolutePath + ",parent = " + parent);
                        listFileName.add(folder);
                    }
                }
            }
            if (back) {
                curentLevel--;
            } else {
                curentLevel++;
            }

            parent = file.getParent();
            mDownloadDirChooserAdapter.addAll(listFileName, true);//获取完该文件的所有文件夹之后刷新一下listview的显示数据
        }
    }

    /**
     * 返回上一层
     *
     * @return
     */
    public boolean back() {
        LoggerUtils.d(TAG, "back:parent " + parent);
        if (curentLevel == LEVEL_PARENT_ROOT) {
            queryDisk();
            return true;
        } else if (curentLevel > 2 && !TextUtils.isEmpty(parent)) {
            File file = new File(parent);
            scan(file, true);
            return true;
        }
        return false;
    }

    /**
     * 弹出升级对话框
     *
     * @param context
     * @return
     */
    public static AlertDialog showUpdateDialog(Context context, CheckUpgradeBean2.Content content) {
        String tips = content.remark;
        boolean force = content.forceState == 1;
        String apkUrl = content.url;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_upgrade, null);
        AlertDialog dialog = BaseDialog.showDialog((Activity) context, view, false, true);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvTips = view.findViewById(R.id.tvTips);
        ImageButton btnCancel = view.findViewById(R.id.ibCancel);
        ImageButton btnConfirm = view.findViewById(R.id.ibConfirm);
//        tvTitle.setText(R.string.dialog_check_version_title);
//        btnConfirm.setText(R.string.dialog_check_version_positive_button);
        tvTips.setText(tips);
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setGravity(Gravity.LEFT);
        if (force) {
            btnCancel.setVisibility(View.GONE);
        }
        btnConfirm.setOnClickListener(v -> {
            UpgradeUtil.downLoadApk(MyApp.getInstance(), apkUrl);
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        return dialog;
    }

    /**
     * 无网络弹框
     *
     * @param context
     * @return
     */
    public static AlertDialog showNetWorkDialog(Context context, int type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.network_toast_view, null);
        AlertDialog dialog = BaseDialog.showLandscapeDialog((Activity) context, view, false);
        TextView textView =(TextView) view.findViewById(R.id.toastTitle);
        if (type == 0){
            SpannableString spannableString = createSpannableString(context, context.getString(R.string.error_check_network2));
            if(spannableString != null){
                textView.setText(spannableString);
            }
        }else{
            SpannableString spannableString = createSpannableStringD(context, context.getString(R.string.download_toast));
            if(spannableString != null){
                textView.setText(spannableString);
            }
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        return dialog;
    }
    /**
     * 创建文本样式
     * @param content
     * @param content
     * @return
     */
    private static SpannableString createSpannableString(final Context context, String content){
        SpannableString spanText = new SpannableString(content);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }
            @Override
            public void onClick(@NonNull View widget) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
            }
        },6,10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanText;
    }
    private static SpannableString createSpannableStringD(final Context context, String content){
        SpannableString spanText = new SpannableString(content);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }
            @Override
            public void onClick(@NonNull View widget) {
                Log.i("TTAA","123");
                context.startActivity(new Intent(context, DownloadActivity.class));
            }
        },11,15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanText;
    }
}
