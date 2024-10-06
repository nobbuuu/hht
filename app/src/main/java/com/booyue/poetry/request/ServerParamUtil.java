package com.booyue.poetry.request;

import com.booyue.base.app.ProjectInit;
import com.booyue.base.util.MACUtil;

/**
 * @author: wangxinhua
 * @date: 2019/3/11
 * @description :
 */
public class ServerParamUtil {
    public static final String getParam() {
        return "?appKey=A01A6B3988EAC607&mac=" + MACUtil.getMACAddress(ProjectInit.getApplicationContext()) + "&type=hhtjgs_app";
    }

}
