package com.booyue.database.greendao.manager;

import android.content.Context;

import com.booyue.base.app.ProjectInit;
import com.booyue.database.greendao.gen.DaoMaster;
import com.booyue.database.greendao.gen.DaoSession;


/**
 * @author: wangxinhua
 * @date: 2018/11/14
 * @description :
 */
public class DaoManager {
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    public static String DB_NAME = "babylisten.db";

    private static DaoMaster.DevOpenHelper devOpenHelper;

    public static DaoMaster.DevOpenHelper getDevOpenHelper(){
        if(devOpenHelper == null){
            synchronized (DaoManager.class){
                if(devOpenHelper == null){
                    devOpenHelper = new DaoMaster.DevOpenHelper(ProjectInit.getApplicationContext(),ProjectInit.getDatabaseName());
                }
            }
        }
        return devOpenHelper;
    }



    /**
     * 获取DaoMaster
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (null == mDaoMaster) {
            synchronized (DaoManager.class) {
                if (null == mDaoMaster) {
                    // TODO: 2018/7/5 MyOpenhelper自己写的，为了实现数据库升级
                    MyOpenHelper helper = new MyOpenHelper(context, DB_NAME, null);
                    mDaoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return mDaoMaster;
    }


    /**
     * 获取DaoSession
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context){
        if(mDaoSession == null){
            synchronized (DaoManager.class){
                if(mDaoSession == null){
                    mDaoSession = getDaoMaster(context).newSession();
                }
            }
        }
        return mDaoSession;
    }

}
