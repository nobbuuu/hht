package com.booyue.database.greendao.dao;

import com.booyue.database.greendao.IDao;
import com.booyue.database.greendao.bean.DownloadBean;
import com.booyue.database.greendao.extend.HHTDaoManager;
import com.booyue.database.greendao.gen.DownloadBeanDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

public class DownloadDao extends IDao<DownloadBean> {
    private final String TAG = "DownloadDao";
    private static DownloadDao db;
    private final DownloadBeanDao mDownloadBeanDao;


    private DownloadDao() {
        mDownloadBeanDao = HHTDaoManager.getDownloadBeanDao();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static DownloadDao getInstance() {
        if (db == null) {
            synchronized (DownloadDao.class) {
                if (db == null) {
                    db = new DownloadDao();
                }
            }
        }
        return db;
    }


    @Override
    public AbstractDao<DownloadBean, Long> getDao() {
        return mDownloadBeanDao;
    }


    /**
     * 查询数据库已经下载的文件
     */

    public List<DownloadBean> queryDownloadComplete() {
        List<WhereCondition> list = new ArrayList<>();
        WhereCondition conditionIsfinished = DownloadBeanDao.Properties.IsFinished.eq(1);
        list.add(conditionIsfinished);
        List<DownloadBean> downloadBeanList = query(list);
        if (downloadBeanList == null) {
            return new ArrayList<>();
        }
        return downloadBeanList;
    }

    public List<DownloadBean> queryDownloadComplete(int guid) {
        List<WhereCondition> list = new ArrayList<>();
        WhereCondition conditionIsfinished = DownloadBeanDao.Properties.IsFinished.eq(1);
        WhereCondition conditionGuid = DownloadBeanDao.Properties.Guid.eq(guid);
        list.add(conditionIsfinished);
        list.add(conditionGuid);
        List<DownloadBean> downloadBeanList = query(list);
        return downloadBeanList;
    }


    public DownloadBean queryByGuid(String guid) {
        List<WhereCondition> list = new ArrayList<>();
        list.add(DownloadBeanDao.Properties.Guid.eq(guid));
        List<DownloadBean> downloadBeanList = query(list);
        if (downloadBeanList == null || downloadBeanList.size() == 0) {
            return null;
        }
        return downloadBeanList.get(0);
    }

}

