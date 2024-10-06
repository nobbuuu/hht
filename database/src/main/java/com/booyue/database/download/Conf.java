package com.booyue.database.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 配置参数类
 *
 * @author wangxinhua
 */
public class Conf {
    public final static int NET_TIMEOUT_READ = 20000; //读取超时
    public final static int NET_TIMEOUT_CONNECT = 20000; //连接超时
    //下载器状态
    public final static int DOWNLOADER_STATE_DOWNLOAD = 1; //下载中状态
    public final static int DOWNLOADER_STATE_PAUSE = 2; //暂停状态
    public final static int DOWNLOADER_STATE_FINISH = 3;//完成状态
    public final static int DOWNLOADER_STATE_CANCEL = 4; //DownloadBean被取消
    public final static int DOWNLOADER_STATE_WAIT = 5; //等待下载任务状态，可能刚下载完一个任务，可能还没还是下载任务
//
//    public final static int STATE_CANCELING = 7; //任务取消中
//
//    public final static int STATE_FILEERROR = 6; //文件异常，文件版本不连续

    public final static int MSG_STATECHANGED = 9;//表示列表状态发生改变，通知Adapter刷新界面
    public final static int RETRY_TIMES = 3; //网络请求重试次数
    public final static int FALSE = 0;
    public final static int TRUE = 1;

    public final static int STATE_TASK_START_FLAG = 10000;                    //任务状态开始标识
    public final static int STATE_TASK_WAIT = 10000 + 1;        //任务状态：等待
    public final static int STATE_TASK_PAUSE = 10000 + 2;            //任务状态：暂停
    public final static int STATE_TASK_COMPLETE = 10000 + 3;         //任务状态：完成
    public final static int STATE_TASK_PROCESSING = 10000 + 4;    //任务状态：处理中
    public final static int STATE_TASK_CANCEL = 10000 + 5;    //任务状态：取消
    public final static int STATE_TASK_ERROR = 10000 + 6;        //任务状态：出错
    public final static int STATE_TASK_URL_ERROR = 10000 + 7;        //任务状态：出错


    /***
     * 主要有新任务，下载中，下载完，安装完成
     */
    public final static int INSTALL_STATE_NEW_TASK = 100;//插入下载队列
    public final static int INSTALL_STATE_DOWNLOADING = 101;//插入下载队列中并执行下载
    public final static int INSTALL_STATE_DOWNLOADED = 102;//下载完成之后
    public final static int INSTALL_STATE_INSTALLED = 103;//安装完成调用
    public final static int INSTALL_STATE_INSTALLING = 104;//安装之前调用
    public final static int INSTALL_STATE_UPDATED = 105;
    public final static int INSTALL_STATE_UPDATING = 106;
    public final static int INSTALL_STATE_UNINSTALLING = 107;
    public final static int INSTALL_STATE_UNISTALL = 108;



    public final static int STATE_TASK_END_FLAG = STATE_TASK_PROCESSING + 100;   //任务状态结束标识

    public final static int STATE_THREAD_START = 20000; //线程状态开始标识
    public final static int STATE_THREAD_IDLE = 20001; //线程空闲状态，下载完成之后线程状态，然后重新刷新下载队列
    public final static int STATE_THREAD_BUSY = 20002; //线程工作状态
    public final static int STATE_THREAD_END = 20003;//线程状态结束标识


    public final static int STATE_ICON_DOWNLOAD = 1000;
    public final static int STATE_ICON_UPDATE = 1001;
    public final static int STATE_ICON_INSTALL = 1002;
    public final static int STATE_ICON_OPEN = 1003;
    public final static int STATE_PROGRESS_UPDATE = 1004;
    public final static int STATE_PROGRESS_GOON = 1005;
    public final static int STATE_PROGRESS_WAITING = 1006;


    /**
     * 得到本地文件保存路径
     *
     * @return
     */
    public static String getSaveDir(Context context) {
        String fileDir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //判断SD卡是否存在
            File f = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (null == f) {
                fileDir = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
            } else {
                fileDir = f.getPath();
            }
        }
        Log.d(TAG, "getSaveDir: " + fileDir);
        return fileDir;
    }


    private static final String TAG = "Conf";

}
