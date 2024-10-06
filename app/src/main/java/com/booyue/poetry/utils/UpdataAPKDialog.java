package com.booyue.poetry.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.booyue.base.util.LoggerUtils;
import com.booyue.poetry.R;

import java.io.File;

public class UpdataAPKDialog extends Dialog {

    private static final String TAG = "UpdataAPKDialog";
    public final String APkNAME = "小学同步辅导.apk";
    private Context context;
    private OnDialogClickListener dialogClickListener;
    private RelativeLayout but_Layout;
    private TextView btn_update;
    private TextView updataTitle,topTxt,butTxt;
    private ProgressBar np_bar;
    private String newApkFilePath;

    public UpdataAPKDialog(Context context) {
        super(context);
        this.context = context;
        newApkFilePath = setApkFilePath(context) +"/" + APkNAME;
        initalize();
        initAPKView();
    }

    private Boolean initAPKView() {
        PackageInfo apkInfo = getApkInfo(context, newApkFilePath);
        if (apkInfo != null){
//            Log.i("11111",apkInfo.packageName);
//            Log.i("111112222",context.getPackageName());
            if (apkInfo.packageName.equals(context.getPackageName())){
                if (apkInfo.versionCode >= getAppVersionCode(context)){
                    //显示安装View
                    btn_update.setText("安装");
                    return true;
                }
            }
        }
        btn_update.setText("立即更新");
        return false;
    }

    //升级的时候更改的值  显示隐藏
    public void goneUpdataView() {
        updataTitle.setText("升级中");
        topTxt.setText("正在升级,");
        butTxt.setText("请误断电以免数据丢失!");
    }
    public void visibleUpdataView() {
        updataTitle.setText("发现新版本");
        topTxt.setText("检测到有升级安装包,");
        butTxt.setText("请升级后继续使用!");
        initAPKView();
    }
    //升级失败的  显示隐藏
    public void goneUpgradeFailed() {
        updataTitle.setText("升级失败");
        topTxt.setText("请检查网络");
        butTxt.setText("保持充足电量!");
        btn_update.setText("重试");
    }
    public void visibleUpgradeFailed() {
        updataTitle.setText("发现新版本");
        topTxt.setText("检测到有升级安装包,");
        butTxt.setText("请升级后继续使用!");
    }
    //确认更新按钮的显示隐藏
    public void goneBtnUpdate() {
        btn_update.setVisibility(View.GONE);
    }
    public void visiBtnUpdate() {
        btn_update.setVisibility(View.VISIBLE);
    }
    //进度条的显示隐藏
    public void goneNpBar() {
        np_bar.setVisibility(View.GONE);
    }
    public void visibleNpBar() {
        np_bar.setVisibility(View.VISIBLE);
    }
    //底部Layout的显示隐藏
    public void goneButLayout() {
        but_Layout.setVisibility(View.GONE);
    }
    public void visibleButLayout() {
        but_Layout.setVisibility(View.VISIBLE);
    }
    //初始化View
    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.updata_apk, null);
        setContentView(view);
        initWindow();

        updataTitle = (TextView) findViewById(R.id.updataTitle);
        topTxt = (TextView) findViewById(R.id.topTxt);
        butTxt = (TextView) findViewById(R.id.butTxt);
        but_Layout = (RelativeLayout) findViewById(R.id.but_Layout);
        btn_update = (TextView) findViewById(R.id.btn_update);
        TextView ib_close = (TextView) findViewById(R.id.ib_close);
        np_bar = (ProgressBar) findViewById(R.id.np_bar);
//
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);

//                getApkInfo(context, "/storage/emulated/0/Android/data/com.booyue.videoplayer/cache/古典国学.apk");
//                setApkFilePath(context);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean aBoolean = initAPKView();
                if (aBoolean){
                    install(context,new File(newApkFilePath));
                }else {
                    if(dialogClickListener != null){
                        dialogClickListener.onUpdataAPK(np_bar);
                    }
                }
            }
        });
    }

    /**
     *添加黑色半透明背景
     */
    private void initWindow() {
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(com.booyue.base.R.style.dialogWindowAnim);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));//设置window背景
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//设置输入法显示模式

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();//获取屏幕尺寸
//        lp.width = (int) (d.widthPixels * 1); //宽度为屏幕80%
//        lp.width = (int)context.getResources().getDimension(R.dimen.dimen_666);//宽高可设置具体大小

        dialogWindow.setAttributes(lp);
        setCancelable(false);
    }

    public void setOnDialogClickListener(OnDialogClickListener clickListener){
        dialogClickListener = clickListener;
    }

    /**
     *添加按钮点击事件
     */
    public interface OnDialogClickListener{
        void onUpdataAPK(ProgressBar texbar);
    }

    /**
     * 检查一个 apk 是否已安装
     *
     * @param context
     * @param pkg
     * @return
     */
    public static boolean isInstalled(Context context, String pkg) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(pkg, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        LoggerUtils.d(TAG + "包名 = " + pkg + ",是否安装 = " + packageInfo);
        Log.d("TAG",  "TTT包名 = " + pkg + ",是否安装 = " + packageInfo);

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
    //查看当前是否有新下载的apk
    public static PackageInfo getApkInfo(Context context, String path){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        if (pi!=null){
            //包名
            String bm = pi.packageName;
            //版本名称
            String bbmc = pi.versionName;
            //版本号
            int bbh = pi.versionCode;
        }
        return pi;
    }


    /**
     * 得到本地文件保存路径
     * @return
     */
    public String setApkFilePath(Context context) {
        String fileDir;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { //判断SD卡是否存在
            File f = context.getExternalCacheDir();
            if (null == f) {
                fileDir = Environment.getExternalStorageDirectory().getPath()
                        + File.separator + context.getPackageName()
                        + File.separator + "cache";
            } else {
                fileDir = f.getPath();
            }
        } else {
            File f = context.getCacheDir();
            fileDir = f.getPath();
        }
        LoggerUtils.e("------------------------------------------下载文件存放目录："+fileDir);
        return fileDir;

    }
    /**
     * 获取当前app version code
     */
    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }


    /**
     * 安装apk
     *
     * @param context
     * @param file
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    public void install(Context context, File file) {
//        File file = new File(path.toString());
        Log.i("LXH","fileSavePath:"+file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(context, context.getPackageName()+".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
            intent.setDataAndType(data, "application/vnd.android.package-archive");
        } else {
//            data = Uri.fromFile(file);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }


}
