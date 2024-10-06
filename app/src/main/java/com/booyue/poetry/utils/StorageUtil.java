package com.booyue.poetry.utils;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author: wangxinhua
 * @date: 2020/6/30 8:27
 * @description :
 */
public class StorageUtil {
    private static final String TAG = "StorageUtil";
    /*
     获取全部存储设备信息封装对象
   */
    public static ArrayList<Volume> getVolume(Context context) {
        Log.d(TAG, "getVolume: ");
        ArrayList<Volume> list_storagevolume = new ArrayList<>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method method_volumeList = StorageManager.class.getMethod("getVolumeList");
            method_volumeList.setAccessible(true);
            Object[] volumeList = (Object[]) method_volumeList.invoke(storageManager);
            if (volumeList != null) {
                Volume volume;
                for (int i = 0; i < volumeList.length; i++) {
                    try {
                        volume = new Volume();
                        String path = (String) volumeList[i].getClass().getMethod("getPath").invoke(volumeList[i]);
                        volume.setPath(path);
                        boolean removable = (boolean) volumeList[i].getClass().getMethod("isRemovable").invoke(volumeList[i]);
                        volume.setRemovable(removable);
                        String state = (String) volumeList[i].getClass().getMethod("getState").invoke(volumeList[i]);
                        volume.setState(state);
                        Log.d(TAG, "getVolume: path = " + path + ",removable = " + removable + ",state = " + state);
                        list_storagevolume.add(volume);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Log.e("null", "null-------------------------------------");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return list_storagevolume;
    }


    /*
     存储设备信息封装类
     */
    public static class Volume {
        protected String path;
        protected boolean removable;
        protected String state;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isRemovable() {
            return removable;
        }

        public void setRemovable(boolean removable) {
            this.removable = removable;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
