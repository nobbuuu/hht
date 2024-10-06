package com.booyue.net.callback;

import android.text.TextUtils;

import com.booyue.base.util.LoggerUtils;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author Administrator
 * @date 2018/7/10
 */

public abstract class ModelCallback<T> implements ICallback{
    private static final String TAG = "ModelCallback";

    @Override
    public void onFailure() {
//        Tips.show("检查网络接口");
        onError(0,"网络超时");
    }

    @Override
    public void onSuccess(String response) {
        if(TextUtils.isEmpty(response)){//返回失败
            onError(0,"response is null");
        }else {//返回成功
            LoggerUtils.d(TAG, ": response = " + response);
            Class<? extends T> genericClass = getGenericClass(this);
            T t = new Gson().fromJson(response, genericClass);
            if(t == null){
                onError(0,"parse json error");
            }else {
                onSuccess(t);
            }
        }
    }
    //获取泛型类型
    protected Class<T> getGenericClass(Object object){
        Type type = object.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];
        return (Class<T>) actualTypeArgument;
    }
    public abstract void onSuccess(T response);

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onError(int ret, String msg) {

    }

    @Override
    public void onPostExecute() {

    }
}
