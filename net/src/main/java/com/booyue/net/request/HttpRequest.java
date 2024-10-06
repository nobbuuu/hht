package com.booyue.net.request;


import com.booyue.net.callback.ICallback;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/15.09:41
 *
 * http请求顶层接口
 */

public interface HttpRequest {
    void get(String url, Map<String, String> params, ICallback iCallback);
    void post(String url, Map<String, String> params, ICallback iCallback);
}
