package com.booyue.base.bean;

/**
 * Created by Administrator on 2018/8/3.09:24
 */

public class BaseBean {
    //解绑设备
// -1 openid为空
//-2 deviceid为空
//-3 did为空


//            case 4:
//                return "请求错误!未携带访问ID";
//            case 5:
//                return "请求错误！访问ID不正确";
//            case 6:
//                return "请求错误！访问ID被禁用";



    public static final String SUCCESS = "1";//成功
    public static final String FAIL = "0";//失败
    public static final String EXPIRED = "3";
    public static final String PHONE_USED = "-1";//手机号被使用
    public static final String SYSTEM_EXCEPTION = "9";//系统异常



    public String ret;
    public String msg;

    public boolean isSuccess(){
        if(SUCCESS.equals(ret)){
            return true;
        }
        return false;

    }
      public boolean isFail(){
        if(FAIL.equals(ret)){
            return true;
        }
        return false;
    }
    /**
     * 网络请求是否过期
     */
    public boolean isPast(){
        if(EXPIRED.equals(ret) || DEVICE_EXPIRED.equals(ret)){
            return true;
        }else{
            return false;
        }
    }


    public boolean isSuccess(String ret){
        if(SUCCESS.equals(ret)){
            return true;
        }
        return false;
    }

    /**
     * 是否已绑定设备
     * @return true 已绑定
     */
     public boolean boundDevice(){
        if(DEVICE_BINDED.equals(ret)){
            return true;
        }
        return false;
    }

    /**
     * 是否使用微信绑定了
     * @return true 未使用微信绑定
     */
     public boolean noBoundDeviceWithWechat(){
        if(DEVICE_NO_BIND_WECHAT.equals(ret)){
            return true;
        }
        return false;
    }

     public boolean deviceNotSupport(){
        if(DEVICE_NOT_SUPPORT.equals(ret)){
            return true;
        }
        return false;
    }
    //wifi设备接口
    public static final String DEVICE_EXPIRED = "-5";//token失效 wifi设备接口
    public static final String DEVICE_BINDED = "2";//设备已绑定
    public static final String DEVICE_NO_BIND_WECHAT = "-4";//设备未绑定微信
    public static final String DEVICE_NOT_SUPPORT = "-3";//设备不支持


}
