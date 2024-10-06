package com.booyue.database.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.booyue.base.util.CloseUtil;
import com.booyue.base.util.FileUtil;
import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.MD5Utils;
import com.booyue.base.util.SharePreUtil;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载器
 *
 * @author wangxinhua
 */
public class Downloader extends Thread {
    private static final String TAG = "Downloader------";
    private DownloadBean mBean;
//    private DownloadStateListener mListener;
    /**
     * 用于表征当前是否处于空闲状态，如果是的话，可以为其分配新的下载任务，
     * 进入空闲状态的只有两种方法：下载完成和取消下载
     */
    private boolean isIdle;
    //表示当前下载器是否已经激活，默认未激活
    private boolean mIsAlive = false;
    private int mState;//状态
    private int beanPercent;//进度
    long mFileSize = 0;
    private long mCompletedSize = 0;
    private RandomAccessFile mRandomAccessFile = null;
    private InputStream mIs = null;
    private HttpURLConnection mConn = null;
    private File mFile = null;
    private boolean DEBUG = false;

    //下载失败定义
    /**
     * 链接失败
     */
    public final int DOWNLOAD_ERROR_CONNECT_TIMEOUT = 100;
    /**
     * 文件不存在
     */
    public final int DOWNLOAD_ERROR_FILE_NOT_FOUND = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 1;
    /**
     * 链接中断
     */
    public final int DOWNLOAD_ERROR_CONNECT_INTERUNPT = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 2;
    /**
     * 获取文件大小失败
     */
    public final int DOWNLOAD_ERROR_GET_SIZE_FAIL = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 3;
    /**
     * 协议异常
     */
    public final int DOWNLOAD_ERROR_PROTOCOL = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 4;

    /**
     * URL不正确
     */
    public final int DOWNLOAD_ERROR_MALFORMEDURL = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 5;
    /**
     * 未知错误
     */
    public final int DOWNLOAD_ERROR_UNKNOWN = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 6;

    /**
     * 断点续传文件失败
     */
    public final int DOWNLOAD_ERROR_CONTINUE_DOWNLOAD_FAIL = DOWNLOAD_ERROR_CONNECT_TIMEOUT + 7;
    public Handler handler;
    private Context mContext;
    private DownloadDao downloadDao;//数据库操作对象

    /**
     * 创建对象
     */
    public Downloader(Context applicationContext) {
        isIdle = true;
        this.mContext = applicationContext;
        mState = Conf.DOWNLOADER_STATE_WAIT;
        downloadDao = DownloadDao.getInstance();
    }

    /**
     * 判断是否是暂停状态
     *
     * @return
     */
    public boolean isPause() {
        return (mState == Conf.DOWNLOADER_STATE_PAUSE);
    }

    /**
     * 判断是否是下载状态
     *
     * @return
     */
    public boolean isRun() {
        return (mState == Conf.DOWNLOADER_STATE_DOWNLOAD);
    }

    /**
     * 返回当前下载器的激活状态
     */
    public boolean isActive() {
        return mIsAlive;
    }

    /**
     * 获得当前下载器的状态
     */
    public int getstate() {
        return mState;
    }

    /**
     * 分配下载任务
     */
    public void assignTask(DownloadBean bean, Handler handler) {
        LoggerUtils.d(TAG, "assignTask: ");
        this.mBean = bean;
//        this.mListener = listener;
        this.handler = handler;
    }
    /**
     * 获取实体类
     */
    public DownloadBean getBean() {
        return this.mBean;
    }

//    /**
//     * 暂停任务
//     */
//    public void pause() {
//        mState = Conf.DOWNLOADER_STATE_PAUSE;
//        mListener.paused();
//    }

    /**
     * 恢复下载
     */
    public void recovery() {
        LoggerUtils.d(TAG + "恢复下载 ，recovery()");
        mState = Conf.DOWNLOADER_STATE_DOWNLOAD;
        mIsAlive = true;
        isIdle = false;
//        mListener.resumed();
        interrupt();
    }

    /**
     * 判断是否暂停
     * @return
     */
    public boolean isPaused() {
        return mState == Conf.DOWNLOADER_STATE_PAUSE;
    }

    /**
     * 取消当前任务
     */
    public void kill() {
        if (mBean != null) {
            LoggerUtils.d(TAG + "Kill the downloader with guid:" + mBean.guid);
        }
        mState = Conf.DOWNLOADER_STATE_WAIT;
        mIsAlive = false;
        isIdle = true;

    }

    /**
     * 判断当前是否是闲置状态
     *
     * @return
     */
    public boolean isIdle() {
        return isIdle;
    }


    /**
     * 下载失败处理
     *
     * @throws IOException
     */
    protected void downloadErrorHandler(int reason) {
        LoggerUtils.e(TAG + "下载失败：" + mBean.title + " url:" + mBean.url);
        mBean.state = Conf.STATE_TASK_ERROR;
        switch (reason) {
            case DOWNLOAD_ERROR_UNKNOWN:
                LoggerUtils.d(TAG, "未知错误: ");
                resetDownload(mBean);
                break;
            case DOWNLOAD_ERROR_GET_SIZE_FAIL:
                LoggerUtils.d(TAG, "获取文件大小失败: ");
                resetDownload(mBean);
                break;
            case DOWNLOAD_ERROR_CONTINUE_DOWNLOAD_FAIL:
                LoggerUtils.d(TAG, "继续下载失败: ");
                resetDownload(mBean);
                break;
            case DOWNLOAD_ERROR_PROTOCOL:
                LoggerUtils.d(TAG, "协议异常: ");
                resetDownload(mBean);
                break;
            default:
                break;
        }
    }

    /**
     * 让下载任务重新开始
     */
    void resetDownload(DownloadBean bean) {
        if (bean != null) {
            mCompletedSize = 0;
            bean.completeSize = 0;
            bean.isFinished = Conf.FALSE;
            bean.percent = 0;
        }
    }

    /**
     * download exception class.
     */
    class DownloadException extends Exception {
        int mReason;

        public DownloadException(String message, int reason) {
            super(message);
            mReason = reason;
        }
    }


    /**
     * 下载状态处理
     */
    protected void downloadResultProcess() {
        /**
         * 下载错误
         * 关闭数据流，数据持久化，回调监听
         * */
        if(beanPercent == 100){
            mBean.isFinished = Conf.TRUE;
            /**1.数据持久化动作,将数据写到数据库中*/
            mBean.state = Conf.STATE_TASK_COMPLETE;
            mBean.percent = beanPercent;
            mBean.completeSize = mCompletedSize;
            downloadDao.update(mBean);
            LoggerUtils.d(TAG + "---更新状态完毕--");

            //告知下载队列，请求新任务
//            mListener.finished(mBean);
            beanPercent = 0;
            mCompletedSize = 0;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handler.obtainMessage(mBean.state, mBean.guid).sendToTarget();
                }
            });

        } else if (mBean.state == Conf.STATE_TASK_ERROR) {
            //2.进行数据持久化
            mBean.percent = beanPercent;
            mBean.completeSize = mCompletedSize;
            downloadDao.update(mBean);
            //回调监听
//            mListener.error();
            handler.obtainMessage(mBean.state, mBean.guid).sendToTarget();
            /**
             * 暂停
             * 关闭数据流，持久化存储，回调监听
             **/
        }
        else if (mBean.state == Conf.STATE_TASK_PAUSE) {
                //2.进行数据持久化
                mBean.percent = beanPercent;
                mBean.completeSize = mCompletedSize;
                downloadDao.update(mBean);

//                mListener.paused();
                handler.obtainMessage(mBean.state, mBean.guid).sendToTarget();
            /*** 取消 删除数据库记录，回调监听**/
        }
//        else if (mState == Conf.STATE_TASK_CANCEL) {
//            //1.删除数据库数据
//            downloadDao.delete(mBean);
////            mListener.canceled(mBean.guid);//取消回调
//            handler.obtainMessage(mBean.state, mBean.guid).sendToTarget();
//            LoggerUtils.d(TAG + "任务取消---"+mBean.name);
//
//        }
        // TODO: 2018/7/4 断开连接，关闭通道
        if (mConn != null) {
            mConn.disconnect();
            CloseUtil.close(mIs);
            CloseUtil.close(mRandomAccessFile);
            mConn = null;
        }
    }

    private void LogBean(DownloadBean bean) {
//        if (bean != null) {
//            LoggerUtils.i(TAG + "log bean start................");
//            LoggerUtils.i(TAG + "任务名---"+bean.name);
//            LoggerUtils.i(TAG + "任务guid---"+bean.appId);
//            LoggerUtils.i(TAG + "任务存放路径---"+bean.localPath);
//            LoggerUtils.i(TAG + "任务网络路径---"+bean.filepath);
//            LoggerUtils.i(TAG + "任务完成大小---"+bean.completeSize + "");
//            LoggerUtils.i(TAG + "任务大小---"+bean.size + "");
//            LoggerUtils.d(TAG + "下载进度--" + bean.percent);
//            LoggerUtils.i(TAG + "log bean end..................");
//        }
    }

    @Override
    public void run() {
        synchronized (this) {
            mFileSize = 0;
            mRandomAccessFile = null;
            mCompletedSize = 0;
            mIs = null;
            mConn = null;
            mFile = null;
            mState = Conf.DOWNLOADER_STATE_DOWNLOAD;
            mIsAlive = true;


            while (mIsAlive) {
                isIdle = false;
                /**如果有可下载任务，就进行下载*/
                if (mBean.state == Conf.STATE_TASK_WAIT) {
                    mBean.state = Conf.STATE_TASK_PROCESSING;
                    // TODO: 2018/7/9
                    /**modify by : 2018/7/9 14:37*/
                    LogBean(mBean);
                    //获取下载进度和下载的长度
                    beanPercent = mBean.percent;
                    mCompletedSize = mBean.completeSize;
                    //获取本地文件路径  赋值给mBean.localPath
                    getLocalPath();
                    try {
                        //下载长度和数据库记录的不一致，则删除文件
                        mFile = new File(mBean.localPath);
                        if (!mFile.exists()) {
                            mFile.createNewFile();
                        }
                        long fileLen = mFile.length();
                        LoggerUtils.d(TAG, "fileLen: " + fileLen);
                        LoggerUtils.d(TAG, "mBean.completeSize: " + mBean.completeSize);
                        if (fileLen != mBean.completeSize) {
                            mFile.delete();
                            beanPercent = 0;
                            mCompletedSize = 0;
                            LoggerUtils.d(TAG, "after fileLen: " + fileLen);
                            LoggerUtils.d(TAG, "after mBean.completeSize: " + mBean.completeSize);
                            LoggerUtils.e(TAG, "recorded completeSize not equal file length have downloaded");
                            throw new DownloadException("Continue Download File Size Fail", DOWNLOAD_ERROR_CONTINUE_DOWNLOAD_FAIL);
                        }
                        //完成的长度大于文件的总长度，下载有问题，需删除
                        if (mBean.completeSize > mBean.FileSize) {
                            mFile.delete();
                            beanPercent = 0;
                            mCompletedSize = 0;
                            LoggerUtils.d(TAG, "after fileLen: " + fileLen);
                            LoggerUtils.d(TAG, "after mBean.completeSize: " + mBean.completeSize);
                            LoggerUtils.d(TAG, "mBean.completeSize > mBean.FileSize");
                            throw new DownloadException("Continue Download File Size Fail", DOWNLOAD_ERROR_CONTINUE_DOWNLOAD_FAIL);
                        }

                        /***从当前进度点开始数据下载*/
                        URL url;
                        url = new URL(mBean.url);//
                        LoggerUtils.d(TAG + "开始下载:" + mBean.title + ":" + mBean.url);
//                        HurlStack hurlStack = RequestManager.getHurlStack();
//                        SelfSignSslOkHttpStack mHurlStack = (SelfSignSslOkHttpStack) hurlStack;
//                        mConn = mHurlStack.createConnection(url);
                        mConn = (HttpURLConnection) url.openConnection();
                        mConn.setDoInput(true);
                        //conn.setDoOutput(true);
                        mConn.setConnectTimeout(Conf.NET_TIMEOUT_CONNECT);
                        mConn.setReadTimeout(Conf.NET_TIMEOUT_READ);
                        mConn.setRequestMethod("GET");
                        /**设置断点续传*/
                        if (mBean.completeSize != 0 && mBean.FileSize != 0) {
                            LoggerUtils.d(TAG + "断点续传:" + mBean.completeSize + "-" + mBean.FileSize + " :" + mBean.url);
                            mConn.setRequestProperty("Range", "bytes=" + mBean.completeSize + "-" + mBean.FileSize);
                            mFileSize = mBean.FileSize;
                        } else {
                            /**从头开始下载*/
                            mFileSize = mConn.getContentLength();
                            LoggerUtils.d(TAG + "新任务下载，获取文件大小:" + mFileSize);
                        }

                        /**文件大小获取失败*/
                        if (mFileSize <= 0) {
                            mCompletedSize = 0;
                            LoggerUtils.d(TAG + "获取文件大小错误:");
                            handler.obtainMessage(Conf.STATE_TASK_URL_ERROR, mBean.guid).sendToTarget();
                            throw new DownloadException("Get File Size Fail", DOWNLOAD_ERROR_GET_SIZE_FAIL);
                        }
                        mBean.FileSize = mFileSize;
                        beanPercent = (int) ((double) (mCompletedSize) / mFileSize * 100);
                        mRandomAccessFile = new RandomAccessFile(mFile, "rwd");
                        //mRandomAccessFile.setLength(1);
                        mRandomAccessFile.seek(mCompletedSize);

                        /**开始下载* */
                        int length = -1;
                        byte[] buffer = new byte[1024 * 10];
                        /**跳过已经下载的数据*/
                        mIs = mConn.getInputStream();
                        /**{@link #kill()}*/
                        int lastProgress = 0;
                        while (mBean.state == Conf.STATE_TASK_PROCESSING && (length = mIs.read(buffer)) != -1) {
                            mRandomAccessFile.write(buffer, 0, length);
                            mCompletedSize += length;
                            beanPercent = (int) ((double) (mCompletedSize) / mFileSize * 100);
                            mBean.percent = beanPercent;
                            mBean.completeSize = mCompletedSize;
                            /**发送消息,回调ui*/
                            if (lastProgress != beanPercent) {
                                handler.obtainMessage(mBean.state, mBean.guid).sendToTarget();
                                lastProgress = beanPercent;
                                LoggerUtils.d(TAG, "progress: " + beanPercent);
                            }
                            /**当前任务已完成，进入空闲状态*/
                            if (beanPercent == 100) {
                                LoggerUtils.d(TAG + "---一个下载任务完成--");
                                //调试模式 每个任务重复下载
                                if (DEBUG) {
                                    mBean.percent = 0;
                                    beanPercent = 0;
                                    mCompletedSize = 0;
                                    mBean.completeSize = 0;
                                    break;
                                }
                                downloadResultProcess();
                                break;
                            }//下载进度100执行闭包
                        }//循环写入文件闭包
                    }catch (Exception e){
                        e.printStackTrace();
                        LoggerUtils.e(TAG + "下载失败：" + mBean.title + " url:" + mBean.url);
                        mBean.state = Conf.STATE_TASK_ERROR;
//                        downloadErrorHandler(DOWNLOAD_ERROR_UNKNOWN);
                    }
                }//执行等待任务，加入状态等于waiting就执行
                /**调试模式 每个任务重复下载*/
                if (DEBUG) {
                    continue;
                }
                LoggerUtils.d(TAG, "downloadResultProcess: ----------");
                downloadResultProcess();//处理下载完成的结果
                //状态恢复
                try {
                    isIdle = true;
                    mState = Conf.DOWNLOADER_STATE_WAIT;
                    PostIdleMessage();//通知上层线程已处于限制状态，并进行等待
                    LoggerUtils.d(TAG, "This Downloader waiting for new task !");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUtils.d(TAG, "This Downloader error " + e.getMessage());
                }//重置
            }//控制线程中的死循环，不然线程就会结束
            LoggerUtils.d(TAG + "This Downloader" + mBean.guid + " has been killed!!!");
        }
    }

    /**
     * 获取本地文件的路径
     */
    private void getLocalPath() {
        /**创建本地文件目录*/
        String customPath = SharePreUtil.getInstance().getCustomDownloadPath();
        if(TextUtils.isEmpty(customPath)){
           customPath = Conf.getSaveDir(mContext);
        }
        File pathFile = new File(customPath + File.separator + mBean.grade);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (mBean.localPath == null) {
            /**合成文件保持路径*/
            //获取文件的扩展名
            String ext = FileUtil.extension(mBean.url);
            mBean.localPath = pathFile.getAbsolutePath() + File.separator + MD5Utils.getMD5(mBean.url + mBean.guid + mBean.title) + "." + ext;
        }
        LoggerUtils.d(TAG + "新任务:" + mBean.title + '.' + mBean.localPath + " url:" + mBean.url);
    }

    /**
     * 线程空闲时通知刷新下载器队列
     */
    void PostIdleMessage() {
        new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    LoggerUtils.d(TAG, "Post Thread Idle Message!");
                    Message msg = new Message();
                    msg.what = Conf.STATE_THREAD_IDLE;
                    handler.sendEmptyMessage(msg.what);
                }
            }
        }.run();
    }

    /**
     * 从文件/网路路径中解析出文件名
     *
     * @param filePath
     * @return
     */
    public String getFileNameFromPath(String filePath) {
        String[] ss = filePath.split("//");
        return ss[ss.length - 1];
    }

    /**
     * 下载过程的生命周期回调接口，用于刷新界面。注意：界面刷新是在数据更改之后的
     *
     * @author wly
     */
//    public interface DownloadStateListener {
//        /**
//         * 下载完成，发送通知Helper,Helper再通过Handler通知Activity
//         * 下载器在下载器数组中的位置索引
//         */
//        void finished(DownloadBean bean);
//
//        /**
//         * 已经开始下载，下载中，本方法的调用在ready()之后
//         */
//        void started();
//
//        /**
//         * 已经暂停下载
//         */
//        void paused();
//
//        /**
//         * 已经恢复下载
//         */
//        void resumed();
//
//        /**
//         * 已经取消下载
//         *
//         * @param guid
//         */
////        void canceled(int guid);
//
//        /**
//         * 已经准备就绪(文件校验,断点读取等)，可以开始下载
//         *
//         * @param guid
//         */
//        void ready(int guid);
//
//        /**
//         * 下载出错
//         */
//        void error();
//    }


}
