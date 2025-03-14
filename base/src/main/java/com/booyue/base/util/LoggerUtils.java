package com.booyue.base.util;

import android.content.Context;
import android.util.Log;

import com.booyue.base.app.ConfigKeys;
import com.booyue.base.app.ProjectInit;

import java.io.File;

/**
 * @author Administrator
 */
public class LoggerUtils {
	private static final String TAG = "LoggerUtils";
	
	public static void v(String tag, String msg){
		if(isDebug() && msg!= null && tag != null){
			Log.v(tag, msg);
		}
	}
	public static void v(String msg){
		if(isDebug() && msg!= null){
			Log.v(TAG,msg);
		}
	}

	public static void i(String tag, String msg){
		if(isDebug() && msg!= null && tag != null){
			Log.i(tag, msg);
		}
	}
	public static void i(String msg){
		if(isDebug() && msg!= null){
			Log.i(TAG,msg);
		}

	}


	public static void d(String tag, String msg){
		if(isDebug() && msg!= null && tag != null){
			Log.d(TAG,"[" + tag + "] " + msg);
		}
	}
	public static void d(String msg){
		if(isDebug() && msg!= null){
			Log.d(TAG, msg);

		}
	}


	public static void w(String tag, String msg){
		if(isDebug() && msg!= null && tag != null){
			Log.e(tag, msg);
		}
	}

	public static void w(String msg){
		if(isDebug() && msg!= null){
			Log.e(TAG, msg);
		}
	}



	public static void e(String tag, String msg){
		if(isDebug() && msg!= null && tag != null){
			Log.e(tag, "[" + msg + "] " + msg);
		}
	}

	public static void e(String msg){
		if(isDebug() && msg!= null){
			Log.e(TAG, msg);
		}
	}

    //是否debug模式
	public static boolean isDebug(){
		return ProjectInit.getConfiguration(ConfigKeys.DEBUG.name());
	}

	public static boolean isDebugFileExist(Context context){
		File dir = context.getApplicationContext().getExternalCacheDir();
		if(dir != null && dir.exists()){
			return true;
		}
		return false;
	}
}

