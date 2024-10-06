package com.booyue.database.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.booyue.base.util.LoggerUtils;
import com.booyue.database.greendao.manager.DaoManager;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/5.10:07
 * 数据库操作基类
 */

public abstract class IDao<T> {
    private static final String TAG = "IDao";

    public abstract AbstractDao<T, Long> getDao();

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = DaoManager.getDevOpenHelper().getReadableDatabase();
        return db;
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = DaoManager.getDevOpenHelper().getWritableDatabase();
        return db;
    }

    public void insert(T t) {
        getDao().insert(t);
    }

    public void insertList(List<T> list) {
        if (list == null || list.size() == 0) return;
        getDao().insertInTx(list);
    }

    public void update(T t) {
        getDao().update(t);
    }

    public void updateList(List<T> list) {
        if (list == null || list.size() == 0) return;
        getDao().updateInTx(list);
    }

    public void delete(T t) {
        getDao().delete(t);
    }

    public void deleteList(List<T> list) {
        if (list == null || list.size() == 0) return;
        getDao().deleteInTx(list);
    }

    public void deleteAll() {
        getDao().deleteAll();
    }

    public void delete(List<WhereCondition> whereConditionList) {
        QueryBuilder<T> qb = getDao().queryBuilder();
        for (WhereCondition whereCondition : whereConditionList) {
            qb.where(whereCondition);
        }
        qb.buildDelete().executeDeleteWithoutDetachingEntities();
        LoggerUtils.d(TAG, ": ");
    }

    public void delete(WhereCondition whereCondition) {
        QueryBuilder<T> qb = getDao().queryBuilder();
        qb.where(whereCondition);
        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //查询全部
    public List<T> query() {
        return getDao().queryBuilder().list();
    }

    //按条件查询
    public List<T> query(List<WhereCondition> whereConditionList) {
        QueryBuilder<T> qb = getDao().queryBuilder();
        for (WhereCondition whereCondition : whereConditionList) {
            qb.where(whereCondition);
        }
        List<T> list = qb.list();
        if (list == null) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }


    //按条件查询
    public List<T> query(WhereCondition whereCondition) {
        QueryBuilder<T> qb = getDao().queryBuilder();
        qb.where(whereCondition);
        List<T> list = qb.list();
        if (list == null) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }


}
