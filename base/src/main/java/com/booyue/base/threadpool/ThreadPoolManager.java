package com.booyue.base.threadpool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tianluhua on 2018/04/10
 * @author Administrator
 */
public final class ThreadPoolManager {

    private static ThreadPoolManager sThreadPoolManager;

    private static final int SIZE_CORE_POOL = 5;

    private static final int SIZE_MAX_POOL = 10;

    private static final int TIME_KEEP_ALIVE = 2000;

    private static final int SIZE_WORK_QUEUE = 500;

    private static final int PERIOD_TASK_QOS = 1000;

    public static ThreadPoolManager newInstance() {
        if (sThreadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (sThreadPoolManager == null) {
                    sThreadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return sThreadPoolManager;
    }

    private ThreadPoolManager() {
    }


    public void addExecuteTask(Runnable task) {
        if (task != null) {
            mThreadPool.execute(task);
        }
    }


    private final Queue<Runnable> mTaskQueue = new LinkedList<Runnable>();


    private final RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            mTaskQueue.offer(task);
        }
    };


    private final Runnable mAccessBufferThread = new Runnable() {
        @Override
        public void run() {
            if (hasMoreAcquire()) {
                mThreadPool.execute(mTaskQueue.poll());
            }
        }
    };


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    protected final ScheduledFuture<?> mTaskHandler = scheduler
            .scheduleAtFixedRate(mAccessBufferThread, 0, PERIOD_TASK_QOS,
                    TimeUnit.MILLISECONDS);


    private final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(
            SIZE_CORE_POOL, SIZE_MAX_POOL, TIME_KEEP_ALIVE, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(SIZE_WORK_QUEUE), mHandler);

    private boolean hasMoreAcquire() {
        return !mTaskQueue.isEmpty();
    }

    protected boolean isTaskEnd() {
        if (mThreadPool.getActiveCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void shutdown() {
        mTaskQueue.clear();
        mThreadPool.shutdown();
    }
}