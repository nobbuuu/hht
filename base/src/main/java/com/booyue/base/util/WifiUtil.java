package com.booyue.base.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.booyue.base.app.ProjectInit;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2019/2/13
 * @description :
 */
public class WifiUtil {
    /**
     * 调用之前检测定义权限是否打开
     * @return
     */
    private static final String TAG = "WifiUtil";
//    public List<ScanResult> getWifiList() {
//        WifiManager wifiManager = (WifiManager) ProjectInit.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        List<ScanResult> scanWifiList = wifiManager.getScanResults();
//        List<ScanResult> wifiList = new ArrayList<>();
//        if (scanWifiList != null && scanWifiList.size() > 0) {
//            HashMap<String, Integer> signalStrength = new HashMap();
//            for (int i = 0; i < scanWifiList.size(); i++) {
//                ScanResult scanResult = scanWifiList.get(i);
//                if (!scanResult.SSID.isEmpty()) {
//                    String key = scanResult.SSID + " " + scanResult.capabilities;
//                    if (!signalStrength.containsKey(key)) {
//                        signalStrength.put(key, i);
//                        wifiList.add(scanResult);
//                    }
//                }
//            }
//        }
//        return wifiList;
//    }

    private WifiManager wifiManager;//WiFi管理器
    private WifiInfo wifiInfo;
    private List<ScanResult> wifilist;//扫描到的WiFi列表
    private List<WifiConfiguration> configurations;//配置好(连接过)的WiFi列表
    //锁屏后，其他程序没有用到WiFi，过一会WiFi会自动关闭，加WiFi后则不会
    private WifiManager.WifiLock lock;//wifi锁。

    public WifiUtil() {
        wifiManager = (WifiManager) ProjectInit.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lock=wifiManager.createWifiLock(2,"mylock");
        /*locktype 1:会自动连接一个记住的接入点。没有连接到记住的接入点的时候，自动扫描，
        2：只扫描，在这种模式下，程序必须显示地请求扫描
        3：以最好的性能执行1
        */
    }
    //打开WiFi，锁定WiFi,扫描WiFi
    public void openwifi(){
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            Log.e("Wifi","打开WiFi");
            lock.acquire();
        }
        scan();
    }
    //扫描WiFi
    public void scan(){
        wifiManager.startScan();
    }
    //关闭WiFi
    public void closewifi(){
        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
    }
    //检查WiFi状态
    public int checkwifistate(){
        //0:wifi正在关闭。1：WiFi已经关闭。2：WiFi正在开启。3：WiFi已经开启
        return wifiManager.getWifiState();
    }
    //获取搜索到的WiFi列表
    public List<ScanResult> getWifilist(){
        wifilist=wifiManager.getScanResults();
        return wifilist;
    }
    //获取连接过的WiFi列表
    public List<WifiConfiguration> getConfigurations(){
        configurations=wifiManager.getConfiguredNetworks();
        return configurations;
    }
    //连接指定WiFi
    public void wificonnect(ScanResult scan, String password){
        WifiConfiguration config=new WifiConfiguration();
        //对config初始化，将他的各个参数清空
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        config.SSID=scan.SSID;//通过ssid来标识一个WiFi
        LoggerUtils.d(TAG, "WiFi连接,scan.SSID " + scan.SSID);
        //判断锁的类型。通过scanresult的结果是否含有某些值来确定
        //根据锁的类型来设置某些参数
        if(scan.capabilities.contains("WEP")){
            LoggerUtils.d("WiFi连接","wep加密");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//            config.allowedAuthAlgorithms.set(WifiConfiguration.GroupCipher.WEP40);
//            config.allowedAuthAlgorithms.set(WifiConfiguration.GroupCipher.CCMP);
//            config.allowedAuthAlgorithms.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.wepTxKeyIndex=0;
            config.wepKeys[0]=password;
        }else if(scan.capabilities.contains("PSK")){
            LoggerUtils.d("WiFi连接","psk加密");
            config.preSharedKey="\""+password+"\"";
        }else if(scan.capabilities.contains("EAP")){
            LoggerUtils.d("WiFi连接","eap加密");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.preSharedKey="\""+password+"\"";
        }else {//没有加密
            LoggerUtils.d("WiFi连接","没有加密");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //config.wepTxKeyIndex=0;
            //config.wepKeys[0]="";
        }
        int netID=wifiManager.addNetwork(config);//连接到WiFi。
        LoggerUtils.d("连接结果", String.valueOf(netID));

        boolean enabled = wifiManager.enableNetwork(netID, true);
        LoggerUtils.d("enableNetwork status enable=" + enabled);
//        boolean connected = wifiManager.con();
//        LoggerUtils.d("enableNetwork connected=" + connected);
        LoggerUtils.d("连接成功!");
    }
    //断开连接
    public void closeconnect(){
        //wifiManager.disconnect();
        wifiInfo=wifiManager.getConnectionInfo();//获取当前连接
        int netid=wifiInfo.getNetworkId();
        wifiManager.disableNetwork(netid);
    }

    public WifiInfo getCurrentConnectionInfo(){
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();//获取当前连接
        return wifiInfo;
    }


}
