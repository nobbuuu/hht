package com.booyue.base.bean;

/**
 * Created by Administrator on 2018/8/3.09:24
 * 用户系统改版后的数据结构的基类
 */

public class BaseBean2 {

    public static int STATE_SUCCESS = 1;
    public static int STATE_FAILED = 0;
    public static int STATE_PUBLIC_TOKEN_PAST = 2;//公共token过期
    public static int STATE_USER_TOKEN_PAST = 3;//token过期
    public static int STATE_PHONE_HAS_BIND = 5;//手机号已绑定

    public static int STATE_BABY_SUBSCRIBE_COURSE = -1;//删除宝宝的时候提示购买了课程
    public static int STATE_DEVICE_HAS_BIND = -4;//设备已绑定
    public static int STATE_DEVICE_TOKEN_PAST = -5;//设备token过期
    public static int STATE_DEVICE_NOT_SUPPORT = -3;//设备不支持
    public static int STATE_TYPE_NOT_SUPPORT = -6;//设备型号不支持
    public static int STATE_MAXINUM_DEVICE = -7;// 单个用户最多可绑定5个设备
    public static int STATE_MAXINUM_USER = -8;//当前设备最多可绑定5个用户
    public static int STATE_NOT_ACTIVITED = -9;//设备未激活
    public boolean publicResrouce = false;//是否是用户资源，用户资源直接清除用户信息，然后跳转至登录页面
    public int code;//2 token过期  3 token与用户信息不匹配
    public String info;////操作成功，已经绑定，非管理。。。
    public boolean success;//true  false
    //设置是否请求公共资源
    public void setPublicResrouce(boolean publicResrouce){
        this.publicResrouce = publicResrouce;
    }


    public boolean isSuccess() {
        if (code == STATE_SUCCESS) {
            return true;
        }
        return false;
    }

    public boolean isFail() {
        if (code == STATE_FAILED) {
            return true;
        }
        return false;
    }



    public boolean isPast() {
        if (code == STATE_USER_TOKEN_PAST || code == STATE_DEVICE_TOKEN_PAST) {
            return true;
        }
        return false;
    }

    /**
     * 公共token过期
     * @return true 过期 false 未过期
     */
    public boolean isPublicTokenPast(){
        if (code == STATE_PUBLIC_TOKEN_PAST) {
            return true;
        }
        return false;
    }

   public static int FIRST_PAGE = 1;
    public static class Data {
        public int pageNo;//第几页
        public int totalPage;//总页数
        public int pageSize;//每页数据条数
        public int total;//总数据条数

    }


    /**
     * 是否已绑定设备
     * @return true 已绑定
     */
    public boolean deviceHasBind(){
        if(code == STATE_DEVICE_HAS_BIND){
            return true;
        }
        return false;
    }


    public boolean deviceNotSupport(){
        if(code == STATE_DEVICE_NOT_SUPPORT || code == STATE_TYPE_NOT_SUPPORT){
            return true;
        }
        return false;
    }

    /**
     * 绑定手机号判断是否
     * @return
     */
    public boolean isPhoneHasBinded(){
        return code == STATE_PHONE_HAS_BIND;
    }

    /**
     * 删除宝宝判断宝宝是否绑定了课程
     * @return
     */
    public boolean isBabySubscribeCourse(){
        return code == STATE_BABY_SUBSCRIBE_COURSE;
    }
    public boolean isBeyondMaximunDevice(){
        return code == STATE_MAXINUM_DEVICE;
    }
    public boolean isBeyondMaximunUser(){
        return code == STATE_MAXINUM_USER;
    }
  public boolean isNotActivited(){
        return code == STATE_NOT_ACTIVITED;
    }

}
