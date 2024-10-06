package com.booyue.database.greendao.extend;

import com.booyue.base.app.ProjectInit;
import com.booyue.database.greendao.gen.DownloadBeanDao;
import com.booyue.database.greendao.manager.DaoManager;

/**
 * @author: wangxinhua
 * @date: 2019/11/15
 * @description :
 */
public class HHTDaoManager extends DaoManager {

    public static DownloadBeanDao getDownloadBeanDao(){
        return getDaoSession(ProjectInit.getApplicationContext()).getDownloadBeanDao();
    }

}
