package com.booyue.net.request.retrofit.rxjava;

/**
 * Created by Administrator on 2018/7/10.09:50
 */

public class Response<T> {
    private int code;
    private String msg;
    private T content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
