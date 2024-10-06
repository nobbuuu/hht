package com.booyue.poetry.bean;

import com.booyue.base.bean.BaseBean2;

/**
 * @author: wangxinhua
 * @date: 2020/6/24 10:30
 * @description :
 */
public class PublicTokenBean extends BaseBean2 {
    public String data;//验证码  公共token
    //检测手机号是否已使用
    public boolean phoneUsed(){
        if (code == -1) {
            return true;
        }
        return false;
    }
}
