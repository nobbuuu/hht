package com.example.lamemp3;

import android.os.Environment;
import android.util.Log;

public class StorageUtil {
	private static String TAG = StorageUtil.class.getName();
	public static final String DIR = "Babylisten/mp3/";


	/**
	 * SD卡是否正常
	 * @return
	 */
	public static boolean isStorageAvailable() {
        String sdStatus = Environment.getExternalStorageState();
		// 检测SD是否可用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.v(TAG, "SD卡不可用");
            return false;
        }
        return true;
	}

	public static String getSDPath(){
		if(isStorageAvailable()){
			return Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
		}else{
			return null;
		}
	}
}
