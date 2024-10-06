package com.booyue.poetry.ui.download;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.SharePreUtil;
import com.booyue.poetry.R;
import com.booyue.poetry.adapter.download.DownloadDirChooserAdapter;
import com.booyue.poetry.ui.HeaderBaseActivity;
import com.booyue.poetry.utils.StorageUtil;
import com.booyue.view.xrecyclerview.HHTRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/19 8:49
 * @description :
 */
public class DownloadDirChooserActivity extends HeaderBaseActivity {
    public static final int LEVEL_ROOT = 1;
    public static final int LEVEL_PARENT_ROOT = 2;
    public int curentLevel = LEVEL_ROOT;
    private static final String TAG = "DownloadDirChooserActiv";
    private HHTRecyclerView hhtRecyclerView;

    @NotNull
    @Override
    public View initContentView() {
        return layoutInflater.inflate(R.layout.activity_download_directory_chooser, null);
    }

    @NotNull
    @Override
    public String getPageTitle() {
        return "下载路径选择";
    }

    @Override
    public void initListener() {

    }

    private DownloadDirChooserAdapter mDownloadDirChooserAdapter;
    private List<DownloadDirChooserAdapter.Folder> listFileName = new ArrayList<>();
    private String currentDir = "";
    @Override
    public void initData() {
        hhtRecyclerView = findViewById(R.id.hhtRecyclerView);
        mDownloadDirChooserAdapter = new DownloadDirChooserAdapter(this, listFileName, false);
        hhtRecyclerView.setLinearLayoutManager(LinearLayoutManager.VERTICAL);
        hhtRecyclerView.setHHTAdapter(mDownloadDirChooserAdapter);
        mDownloadDirChooserAdapter.setOnItemClickLinster((view, pos, o) -> {
            currentDir = ((DownloadDirChooserAdapter.Folder)o).absolutePath;
            mDownloadDirChooserAdapter.setCheckPosition(pos,currentDir);
            SharePreUtil.getInstance().setCustomDownloadPath(currentDir);
            LoggerUtils.d(TAG, "currentDir: " + currentDir);
        });
//        mDownloadDirChooserAdapter.setButtonClickListener((position) -> {
//            DownloadDirChooserAdapter.Folder folder = mDownloadDirChooserAdapter.getList().get(position);
//            File file = new File(folder.absolutePath);
//            scan(file, false);
//        });
        initDownloadDir();
    }

    private void initDownloadDir() {
//        String lastSave = "";
//        String currentPath;
//        if (lastSave == null || lastSave.equals("")) {//如果不存在设为默认路径
//            currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        } else {
//            currentPath = lastSave;
//        }
//        File currentFile = new File(currentPath);
//        if (!currentFile.exists()) {//判断该路径的文件夹是否存在不存在就创建一个，该方法也可以用来实现创建文件夹
//            currentFile.mkdirs();
//        }

//        scan(currentFile);//扫除该路径下的所有文件夹
        queryDisk();
    }

    /**
     * 查询磁盘
     */
    public void queryDisk() {
        List<StorageUtil.Volume> diskList = StorageUtil.getVolume(this);
        List<DownloadDirChooserAdapter.Folder> listFileName = new ArrayList<>();
        for (int i = 0; i < diskList.size(); i++) {
            DownloadDirChooserAdapter.Folder folder = new DownloadDirChooserAdapter.Folder();
            StorageUtil.Volume volume = diskList.get(i);
            if (volume.getPath().contains("emulated")) {
                folder.name = "内置存储";
            } else {
                folder.name = "外置SD卡";
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

    @Override
    public void onBackPressed() {
        if (back()) return;
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (back()) return true;
        }
        return super.onKeyDown(keyCode, event);
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

}
