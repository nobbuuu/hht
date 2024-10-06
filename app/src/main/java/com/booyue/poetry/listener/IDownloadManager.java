package com.booyue.poetry.listener;

import com.booyue.database.download.DownloadHelper;
import com.booyue.database.greendao.bean.DownloadBean;

/**
 * @author: wangxinhua
 * @date: 2020/7/18 11:27
 * @description :
 */
public interface IDownloadManager {
    void register(DownloadHelper.DownloadCallback subscriber);
    void unRegister(DownloadHelper.DownloadCallback subscriber);
    Boolean insertAndStart(DownloadBean bean);
    Boolean isTaskManaged(long id);
    Boolean IsTaskRunning(long id);
    void Pause(long id);
    void Resume(long id);
    int getTaskTotal();
    DownloadBean getTaskByPosition(int position);
    DownloadBean getTaskByid(long id);
    int getTaskPosition(int id);
    void removeTaskByid(long id);
    void removeCompleteTask(long id);
}
