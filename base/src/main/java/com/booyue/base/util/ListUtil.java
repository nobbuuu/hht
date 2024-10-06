package com.booyue.base.util;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2019/5/14
 * @description :
 */
public class ListUtil {
    /**
     * 检测列表是否有效
     * @param list
     * @param index
     * @param <T>
     * @return
     */
    public  static <T> boolean isInvalid(List<T> list,int index){
        if(list == null || list.size() <= index || index < 0){
            return true;
        }
        return false;
    }
}
