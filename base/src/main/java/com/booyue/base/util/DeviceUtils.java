package com.booyue.base.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * 设备工具类
 *
 */

public class DeviceUtils {
	/**
	 * 获取手机号码
	 * @param context
	 * @return
	 */
	public static String getPhoneNum(Context context){
		//创建电话管理
//
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		//获取手机号码

		String phoneId = tm.getLine1Number();
//		String phoneId = "";

		return phoneId;
	}
	/**
	 * 获取设备唯一标识
	 * @param context
	 * @return
	 */
	public static String getDeviceUUID(Context context){

		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), 
				android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

		String uniqueId = deviceUuid.toString();

		return uniqueId;
	}

}
