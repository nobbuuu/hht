package com.booyue.base.util;

import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/19.12:34
 */

public class OSUtil {
    private static final String TAG = "OSUtil";
    /**
     * 获取系统版本�?
     *
     * @return
     */
    private static final int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static final boolean isVersionM() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static final boolean isVersionN() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static final boolean isVersionO() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static final boolean isVersionP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    public static final boolean isVersionQ() {
        return Build.VERSION.SDK_INT >= 29;
    }

    /**
     * 设备型号
     *
     * @return
     */
    public static final String getModel() {
        return Build.MODEL;
    }

    /**
     * 发布版本
     *
     * @return
     */
    public static final String getRelease() {
        return Build.VERSION.RELEASE;
    }


    //获取本地IP函数
    public static String getLocalIPAddress() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    /**
     * 获取系统语言
     *
     * @return
     */
    public static String getLocaleLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        String language = locale.getLanguage() + "-" + locale.getCountry();
        return language;
    }
//    private LocationManager myLocationManager;
//    private Location getLocation(Context context) {
//        //获取位置管理服务
//        //查找服务信息
//        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
//        criteria.setAltitudeRequired(false); //海拔信息：不需要
//        criteria.setBearingRequired(false); //方位信息: 不需要
//        criteria.setCostAllowed(true);  //是否允许付费
//        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗
////        String provider = myLocationManager.getBestProvider(criteria, true); //获取GPS信息
////        myLocationManager.requestLocationUpdates(provider,2000,5,locationListener);
////        Log.e("provider", provider);
////        List<String> list = myLocationManager.getAllProviders();
////        Log.e("provider", list.toString());
////
//        Location gpsLocation = null;
//        Location netLocation = null;
//        myLocationManager.addGpsStatusListener(myListener);
//        if (netWorkIsOpen()) {
//            //2000代表每2000毫秒更新一次，5代表每5秒更新一次
//            myLocationManager.requestLocationUpdates("network", 2000, 5, locationListener);
//            netLocation = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        }
//
//        if (gpsIsOpen()) {
//            myLocationManager.requestLocationUpdates("gps", 2000, 5, locationListener);
//            gpsLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        }
//
//        if (gpsLocation == null && netLocation == null) {
//            return null;
//        }
//        if (gpsLocation != null && netLocation != null) {
//            if (gpsLocation.getTime() < netLocation.getTime()) {
//                gpsLocation = null;
//                return netLocation;
//            } else {
//                netLocation = null;
//                return gpsLocation;
//            }
//        }
//        if (gpsLocation == null) {
//            return netLocation;
//        } else {
//            return gpsLocation;
//        }
//    }
//
//    private boolean gpsIsOpen() {
//        boolean isOpen = true;
//        if (!myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//没有开启GPS
//            isOpen = false;
//        }
//        return isOpen;
//    }
//
//    private boolean netWorkIsOpen() {
//        boolean netIsOpen = true;
//        if (!myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//没有开启网络定位
//            netIsOpen = false;
//        }
//        return netIsOpen;
//    }
//
//    //监听GPS位置改变后得到新的经纬度
//    private LocationListener locationListener = new LocationListener() {
//
//        public void onLocationChanged(Location location) {
//            Log.e("location", location.toString() + "....");
//            // TODO Auto-generated method stub
//            if (location != null) {
//                //获取国家，省份，城市的名称
//                Log.e("location", location.toString());
////                getAddress()
////                List<Address> m_list = getAddress(location);
////                new MyAsyncExtue().execute(location);
////                Log.e("str", m_list.toString());
////                String city = "";
//////                if (m_list != null && m_list.size() > 0) {
//////                    city = m_list.get(0).getLocality();//获取城市
//////                }
////                city = m_list;
////                show_GPS.setText("location:" + m_list.toString() + "\n" + "城市:" + city + "\n精度:" + location.getLongitude() + "\n纬度:" + location.getLatitude() + "\n定位方式:" + location.getProvider());
//            } else {
//                LoggerUtils.d(TAG, "获取不到数据 ");
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//    };
//
//    // 获取地址信息
//    private static List<Address> getAddress(Context context,Location location) {
//        List<Address> result = null;
//        try {
//            if (location != null) {
//                Geocoder gc = new Geocoder(context, Locale.getDefault());
//                result = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


}
