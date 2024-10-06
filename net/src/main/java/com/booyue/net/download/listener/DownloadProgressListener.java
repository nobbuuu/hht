package com.booyue.net.download.listener;

/**
 * @author: wangxinhua
 * @date: 2019/12/2 13:59
 * @description :
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void progress(long read, long count, boolean done);
}
