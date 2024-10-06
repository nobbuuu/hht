package com.booyue.net.request;

import android.util.Log;

import com.booyue.net.callback.ICallback;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/15.10:03
 *
 * http request代理类
 */

public class HttpRequestPresenter implements HttpRequest{
    private static final String TAG = "HttpRequestPresenter";
    private HttpRequest mHttpRequest;
    private static HttpRequestPresenter sHttpRequestPresenter;

    private HttpRequestPresenter(HttpRequest mHttpRequest) {
        this.mHttpRequest = mHttpRequest;
    }
    //初始化操作
    public static void init(HttpRequest mHttpRequest){
        if(sHttpRequestPresenter == null){
            synchronized (HttpRequestPresenter.class){
                if(sHttpRequestPresenter == null){
                    sHttpRequestPresenter = new HttpRequestPresenter(mHttpRequest);
                }
            }
        }
    }
    //获取当前对象
    public static HttpRequestPresenter getInstance(){
        return sHttpRequestPresenter;
    }


    @Override
    public void get(String url, Map<String, String> params, ICallback iCallback) {
        Log.d(TAG, "url : " + url);
        if(mHttpRequest != null){
            mHttpRequest.get(url,params,iCallback);
        }
    }

    @Override
    public void post(String url, Map<String, String> params, ICallback iCallback) {
        if(mHttpRequest != null){
            mHttpRequest.post(url,params,iCallback);
        }
    }
}
