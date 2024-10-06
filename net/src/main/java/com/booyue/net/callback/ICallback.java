package com.booyue.net.callback;

/**
 * Created by Administrator on 2018/7/6.10:22
 *
 * 请求回调接口
 */

public interface ICallback {
    //请求之前回调
    void onPreExecute();
    //请求成功回调
    void onSuccess(String response);
    //请求失败回调，例如，网络问题，或者网络接口问题
    void onFailure();
    //请求错误回调 诸如404，
    void onError(int code, String msg);
    //请求完成回调
    void onPostExecute();
    //下载进度回调
    interface ProgressCallback{
        void onProgressUpdate(int progress);
    }
}
