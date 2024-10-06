package com.booyue.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.booyue.base.R;
import com.booyue.base.toast.Tips;

import java.util.List;

/**
 * @author Administrator
 */
public class NetWorkUtils {
	public static final String TAG = "NetWorkUtils";
	private static ConnectivityManager mConnectivityManager;

	public static boolean isNetWorkAvailableWithTips(Context context){
		mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mNetworkInfo != null && mNetworkInfo.isConnected()){
			return mNetworkInfo.isAvailable();
		}else{
			Tips.show(R.string.error_check_network);
			return false;
		}
	}

	public static boolean isNetWorkAvailable(Context context){
		mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mNetworkInfo != null && mNetworkInfo.isConnected()){
			return mNetworkInfo.isAvailable();
		}
		return false;
	}



	/**
	 *
	 *判断当前是否使用的是 WIFI网络
	 */
	public static boolean isWifiActive(Context context){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifiInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mWifiInfo != null && mWifiInfo.isConnected()){
			boolean isWifiActive = mWifiInfo.getType() == ConnectivityManager.TYPE_WIFI;
			LoggerUtils.d(TAG, "isWifiActive: " + isWifiActive);
			return isWifiActive;
		}else {
			return false;
		}
	}

	public static boolean isMobileActive(Context context){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifiInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mWifiInfo != null && mWifiInfo.isConnected()){
			boolean isMobileActive = mWifiInfo.getType() == ConnectivityManager.TYPE_MOBILE;
			boolean isWifiActive = mWifiInfo.getType() == ConnectivityManager.TYPE_WIFI;
			LoggerUtils.d(TAG, "isMobileActive: " + isMobileActive);
			LoggerUtils.d(TAG, "isWifiActive: " + isWifiActive);
			return isMobileActive;
		}else {
			return false;
		}
	}



	/**
	 * 检测是否是移动wifi
	 * @param context
	 * @return
	 */
	public static boolean isMobileNetwork(Context context){
		if(isNetWorkAvailable(context) && !isWifiActive(context)){
//		if(isNetWorkAvailable(context)){
			return true;
		}
		return false;
	}




	/**
	 * 判断当前网络是否可用(通用方法)
	 * 耗时12秒
	 * @return
	 */
	public static boolean isNetPingUsable() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("ping -c 3 www.baidu.com");
			int ret = process.waitFor();
			if (ret == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断当前网络是否可用(6.0以上版本)
	 * 实时
	 * @param context
	 * @return
	 */
	public static boolean isNetSystemUsable(Context context){
		boolean isNetUsable = false;
		ConnectivityManager manager = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
			if(networkCapabilities == null) {
				return false;
			}
			isNetUsable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
		}else {
			isNetUsable = isNetPingUsable();
		}
		return isNetUsable;
	}

	/**
	 *
	 *判断当前是否使用的是 WIFI网络
	 */
	public static String getWifiSSID(Context context){
		String ssid = "";
		if (Build.VERSION.SDK_INT <= 26 || Build.VERSION.SDK_INT == 28){
			WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = mWifiManager.getConnectionInfo();
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				ssid =  info.getSSID();
			} else {
				ssid =  info.getSSID().replace("\"", "");
			}
		} else if(Build.VERSION.SDK_INT == 27){
			ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			assert connManager != null;
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo.isConnected()) {
				if (networkInfo.getExtraInfo()!=null){
					ssid =  networkInfo.getExtraInfo().replace("\"","");
				}
			}
		}
		//之前华为9.0获取ssid出现unknown ssid,
		if(ssid.contains("unknown")){
			ssid = getSSIDNetworkId(context);
		}
		return ssid;
	}
	public static String getSSIDNetworkId(Context context){
		String ssid = "";
		WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if(wifiManager != null){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int networkId = wifiInfo.getNetworkId();
			List<WifiConfiguration> netConfList =  wifiManager.getConfiguredNetworks();
			if(netConfList == null || netConfList.size() == 0){
				return "";
			}
			for (WifiConfiguration wifiConfiguration:netConfList){
				if(wifiConfiguration.networkId == networkId){
					ssid = wifiConfiguration.SSID;
					break;
				}
			}
		}
		if(ssid.contains("\"")){
			ssid = ssid.replace("\"","");
		}
		return ssid;
	}




}
