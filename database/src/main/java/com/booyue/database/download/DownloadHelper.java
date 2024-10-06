package com.booyue.database.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.booyue.base.app.ProjectInit;
import com.booyue.base.toast.Tips;
import com.booyue.base.util.LoggerUtils;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.dao.DownloadDao;
import com.booyue.database.greendao.gen.DownloadBeanDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 本类中包含两个队列一个是等待中的实体Bean，另一个是下载中的任务队列，
 * 当添加一个任务实体Bean时，会先检查是否有空余的下载器没有使用，如果有，就
 * 使用refreshDownloaderArray将该任务Bean从等待下载队列移动到下载中队列
 * 同样的，当一个任务下载成功后，先从下载中队列移除，然后得到一个空闲的下载器，
 * 最后调用插入流程，将一个新的任务Bean 添加到到下载中队列中去
 * 最后是删除(取消),如果发生在等待队列则删除数据即可，如果发生在下载中队列则复用下载完成逻辑即可。
 */
public class DownloadHelper{
    //模块标识
    public static final String TAG = "DownloadHelper--->";
    //等待下载队
    private DynamicArray<DownloadBean> waitArray;
    //下载任务队列（包含所有下载任务：正在下载、下载完成、暂停)
    private List<DownloadBean> mDownloadTaskQueue;
    //最大同时进行任务数量
    private int mMaxThread;
    //上下文
    private Context mContext;
    //数据库对象
    private DownloadDao downloadDao;
    //下载器队列
    private Downloader[] mDownladerQueue;
    private final CopyOnWriteArraySet<DownloadCallback> subscribers;
    //外部下载消息handle（通知下载进度等相关消息)
//    private Handler mHandler;


    /**
     * 创建实体Bean的队列
     */
    public DownloadHelper(int maxThread) {
        subscribers = new CopyOnWriteArraySet<>();
        this.mMaxThread = maxThread;//最大并发线程数量
        this.mContext = ProjectInit.getApplicationContext();
        this.waitArray = new DynamicArray<>(); //等待下载队列
        this.downloadDao = DownloadDao.getInstance();//数据库管理对象
        this.mDownloadTaskQueue = new ArrayList<>();//初始化任务队列
        initDownloaderArray();//初始化下载器
        initDownloaderTaskFromDB();//从数据库读取下载任务
    }

    /**
     * 注册
     */
    public synchronized void register(DownloadCallback subscriber) {
        if(!subscribers.contains(subscriber)){
           subscribers.add(subscriber);
        }
    }
    /**
     * 取消注册
     */
    public synchronized void unRegister(DownloadCallback subscriber) {
        if (subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
        }
    }




    /**
     * 所有下载的相关的消息都会发到这个
     */
    public static final String DOWNLOAD_CALLBACK_TAG = "download_callback_tag";
    private Handler msgFilterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /** task finished,the {@link Downloader#PostIdleMessage()} will be invoked */
            if (msg.what == Conf.STATE_THREAD_IDLE) {
                refreshDownloaderArray();//线程空闲，寻找新的下载任务
            } else if(msg.what == Conf.STATE_TASK_URL_ERROR){
                Tips.show("下载地址找不到了");
            }else {
                if(msg.what == Conf.STATE_TASK_COMPLETE){
                    long guid = (long) msg.obj;
                    DownloadHelper.this.removeCompleteTask(guid);
                }
                notifyDataChanged(msg);
            }
        }
    };

    /**
     * 向外部发送数据
     * @param msg
     */
    public void notifyDataChanged(Message msg){
        if(subscribers != null){
            Iterator iterator = subscribers.iterator();
           while (iterator.hasNext()){
                DownloadCallback downloadCallback = (DownloadCallback) iterator.next();
                if(downloadCallback != null){
                    downloadCallback.onCallback(msg);
                }
            }
        }
    }

    /**
     * 从数据库读取下载信息，放到等待队列
     * 数据库中的任务状态是下载中或等待中，一律置为暂停，再添加到任务队列中
     */
    public Boolean initDownloaderTaskFromDB() {
        if (downloadDao != null) {
            WhereCondition whereCondition = DownloadBeanDao.Properties.IsFinished.eq(0);
            List<DownloadBean> list = downloadDao.query(whereCondition);
            if (list != null) {
                for (DownloadBean b : list) {
                    if (b.state == Conf.STATE_TASK_PROCESSING || b.state == Conf.STATE_TASK_WAIT) {
                        b.state = Conf.STATE_TASK_PAUSE;
                    }
                    mDownloadTaskQueue.add(b);
                }
            }
        }
        return false;
    }


    /**
     * 初始化下载器队列
     * {@link #refreshDownloaderArray()}
     */
    public void initDownloaderArray() {
        LoggerUtils.d(TAG + ",initDownload（）");
        mDownladerQueue = new Downloader[mMaxThread];
        for (int i = 0; i < mMaxThread; i++) {
            mDownladerQueue[i] = new Downloader(mContext);
            //设置下载状态监听器和下载消息句柄
            mDownladerQueue[i].assignTask(null, msgFilterHandler);
        }
    }

    /**
     * 添加任务到队列，并启动下载任务
     * 注意：受 mMaxThread 限制，仅当有任务线程空闲时，才会即时启动当前插入的任务
     * 注意:如果下载任务已经在队列中，直接返回true
     */
    public Boolean insertAndStart(DownloadBean bean) {
        bean.percent = 0;
        /**参数校验*/
        if (IsValidTaskObject(bean)) {
            /**校验是否已经在队列中了*/
            if (isTaskManaged(bean.guid)) {
                LoggerUtils.e(TAG + "已经在下载队列中了");
                Resume(bean.guid);//如果在下载队列中就启动下载
                return true;
            }
            bean.state = Conf.STATE_TASK_WAIT;
            Message message = new Message();
            message.what = bean.state;
            message.obj = bean.guid;
            msgFilterHandler.sendMessage(message);
            downloadDao.insert(bean);

            /**添加任务到等待队列*/
//            waitArray.insert(bean);
            //添加入等待处理队列,让手动触发的优先处理
            waitArray.insertLeft(bean);
            /**添加任务到任务队里*/
            mDownloadTaskQueue.add(bean);
            /**启动下载任务*/
            refreshDownloaderArray();
            return true;
        }
        return false;
    }

    /**
     * 移除下载任务
     */
    public void removeTask(DownloadBean task) {
        if (task == null) {
            return;
        }
        long id = task.guid;
        //停止任务
        if (IsTaskRunning(id)) {
            stopTask(id);
        }
        if (mDownloadTaskQueue != null && mDownloadTaskQueue.size() > 0) {
            mDownloadTaskQueue.remove(task);
        }
        if (task.localPath != null) {
            File file = new File(task.localPath);
            if (file.exists()) {
                file.delete();
            }
        }
        downloadDao.delete(task);
    }

    /**
     * 移除任务
     *
     * @param id 任务对象唯一标识
     */
    public void removeTaskByid(long id) {
        //获取任务对象
        DownloadBean b = getTaskByid(id);
        removeTask(b);
    }

    /**
     * 删除完成的任务（只删除任务列表中的任务不删除数据库）
     *
     * @param id
     */
    public void removeCompleteTask(long id) {
        DownloadBean downloadBean = getTaskByid(id);
        if (downloadBean != null) {
            mDownloadTaskQueue.remove(downloadBean);
        }

    }

    /**
     * 移除所有下载任务
     */
    public void removeAllTask() {
        for (DownloadBean b : mDownloadTaskQueue) {
            removeTaskByid(b.guid);
        }

    }

    /**
     * 恢复任务,（先排队，等待下载）
     */
    public void Resume(long id) {
        DownloadBean bean = getTaskByid(id);
        //校验参数
        if (bean != null) {
            LoggerUtils.e(TAG, "Resume:" + bean.title);
            //设置状态
            bean.state = Conf.STATE_TASK_WAIT;
            //TODO
            Message message = new Message();
            message.what = bean.state;
            message.obj = id;
            msgFilterHandler.sendMessage(message);
            //添加入等待处理队列,让手动触发的优先处理
            waitArray.insertLeft(bean);
            //执行新的任务
            refreshDownloaderArray();
        }
    }

    /**
     * 暂停任务
     *
     * @param id 任务唯一标识符
     *             注意：直接调用 停止任务
     */
    public void Pause(long id) {
        DownloadBean bean = getTaskByid(id);
        //校验参数
        if (bean != null) {
            LoggerUtils.i(TAG + "Pause:" + bean.title);
            stopTask(id);
        }
    }

    /**
     * 暂停所有任务
     * 注意：直接调用 停止任务
     */
    public void PauseAll() {
        for (DownloadBean b : mDownloadTaskQueue) {
            Pause(b.guid);
        }
    }

    /**
     * 停止任务，并寻找新的任务执行
     * 注意:这个函数仅停止线程执行指定的任务，不会改变任务状态
     */
    private void stopTask(long id) {
        //参数校验
        if (id != 0 && IsTaskRunning(id)) {
            LoggerUtils.e("stopTask()");
            //获取下载器对象
            Downloader d = getDownloaderByid(id);
            //获取任务对象
            DownloadBean bean = getTaskByid(id);
            //设置线程线程状态
            if (d != null) {  //这里要注意，等待执行的任务没有被分配执行线程
                d.kill();
                LoggerUtils.e("downloader.kill()");
            }
            bean.state = Conf.STATE_TASK_PAUSE;//设置任务状态
//            downloadDao.update(bean);
            Message message = new Message();
            message.what = bean.state;
            message.obj = id;
            msgFilterHandler.sendMessage(message);
//			refreshDownloaderArray();
        }
    }

    /**
     * 判断任务是否在执行
     */
    public Boolean IsTaskRunning(long id) {
        DownloadBean b = getTaskByid(id);
        //参数校验
        if (b != null) {
            return (b.state == Conf.STATE_TASK_PROCESSING || b.state == Conf.STATE_TASK_WAIT);
        }
        return false;
    }

    /**
     * 检查下载器队列的状态，负责刷新下载器中的任务。通常在新增、更新、删除任务后调用
     */
    public void refreshDownloaderArray() {
        LoggerUtils.e(TAG + "refreshDownloaderArray");
        //检测等待任务队列，等待队列不为空，就新建下载器
        if (!waitArray.isEmpty()) {
            if(mDownladerQueue == null){
                mDownladerQueue = new Downloader[mMaxThread];
            }
            if (mDownladerQueue.length == 0) {
//                //创建下载器队列
                initDownloaderArray();
            }
        }
        for (int i = 0; i < mMaxThread; i++) {
            if (mDownladerQueue[i] != null && mDownladerQueue[i].isIdle()) {
                if (!waitArray.isEmpty()) {//有下载的文件
                    LoggerUtils.e("等待队列不为空");
                    //查询等待队列
                    DownloadBean bean = waitArray.poll();//从等待队列中弹出
                    while (bean != null && bean.state != Conf.STATE_TASK_WAIT) {
                        //等待队列中的任务已经变化
                        bean = waitArray.poll();
                    }
                    if (bean == null) {
                        break;
                    }
                    LoggerUtils.e(TAG + "分配工作线程：" + bean.title + ",分配的线程状态：" + mDownladerQueue[i].getState());
                    // 为下载器分配下载任务
                    // 检测线程状态，为退出的线程创建新的对象
                    if (mDownladerQueue[i].getState() == Thread.State.TERMINATED) {//下载器已经停止，就新建一个下载器
                        mDownladerQueue[i] = new Downloader(mContext);
                        LoggerUtils.d(TAG + ",线程终止，重新新建一个：new Downloader()");
                    }
                    mDownladerQueue[i].assignTask(bean, msgFilterHandler);//
                    if (mDownladerQueue[i].getState() == Thread.State.NEW) {//新的下载器，直接启动
                        mDownladerQueue[i].start();
                        LoggerUtils.d(TAG + ",新线程：开始启动 start()");
                    } else {
                        mDownladerQueue[i].recovery();
                    }
                } else {//等待队列为空，没有要下载的文件
                    mDownladerQueue[i].kill();
                    LoggerUtils.e(TAG + ",没有下载任务，杀死对于线程，kill()");
                }
            }
        }
    }

    /**
     * 查询是任务是否在任务队列中
     */
    public Boolean isTaskManaged(long id) {
        //参数校验
        if (id != 0) {
            for (int i = 0; i < mDownloadTaskQueue.size(); i++) {
                DownloadBean bean = mDownloadTaskQueue.get(i);
                if (bean != null) {
                    if (bean.guid == (id)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 根据id查询正在下载的下载器对象
     */
    public Downloader getDownloaderByid(long id) {
        for (Downloader d : mDownladerQueue) {
            if (d != null) {
                DownloadBean bean = d.getBean();
                if (bean != null) {
                    if (bean.guid == (id)){
                        return d;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取下载任务在下载队列中的位置
     */
    public DownloadBean getTaskByPosition(int position) {
        //参数校验
        if (position < mDownloadTaskQueue.size() && position >= 0) {
            return mDownloadTaskQueue.get(position);
        }
        return null;
    }

    /**
     * 获取任务总数
     */
    public int getTaskTotal() {
        return mDownloadTaskQueue.size();
    }

    /**
     * 获取任务position
     */
    public int getTaskPosition(int id) {
        if (IsValidTaskid(id)) {
            for (int i = 0; i < mDownloadTaskQueue.size(); i++) {
                if (mDownloadTaskQueue.get(i).guid == (id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取下载任务对象
     */
    public DownloadBean getTaskByid(long id) {
        //参数校验
        if (id != 0) {
            for (DownloadBean d : mDownloadTaskQueue) {
                if (d != null) {
                    if (d.guid == id){
                        return d;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 校验传入参数是否为有效的任务对象
     */
    public static Boolean IsValidTaskObject(DownloadBean taskObj) {
        if (taskObj != null) {
            if (taskObj.guid != 0 /*&& taskObj.picUrl != null*/ && taskObj.title != null && taskObj.url != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验id是否有效
     */
    public static Boolean IsValidTaskid(long id) {
        if (id != 0) {
            return true;
        }
        return false;
    }

    public void removeManyTask(List<DownloadBean> removeCollection) {
        if (removeCollection != null && removeCollection.size() > 0
                && mDownloadTaskQueue != null && mDownloadTaskQueue.size() > 0) {
            for (DownloadBean b : removeCollection) {
                //停止任务
                stopTask(b.guid);
                //从数据库中删除
                downloadDao.delete(b);
            }
            //从等待队列中删除，从下载队列删除
            mDownloadTaskQueue.removeAll(removeCollection);
        }
    }

    /**
     * 全部暂停
     */
    public void pauseAll() {
        for (int i = 0; i < getTaskTotal(); i++) {
            DownloadBean bean = getTaskByPosition(i);
            if (IsTaskRunning(bean.guid)) {
                Pause(bean.guid);
            }
        }
    }

    /**
     * 启动全部下载
     */
    public void startAll() {
        for (int i = 0; i < getTaskTotal(); i++) {
            DownloadBean bean = getTaskByPosition(i);
            if (bean.state == Conf.STATE_TASK_PAUSE || bean.state == Conf.STATE_TASK_ERROR) {
                Resume(bean.guid);
            }
        }
    }
    //下载回调
    public interface DownloadCallback{
        void onCallback(Message message);
    }
}