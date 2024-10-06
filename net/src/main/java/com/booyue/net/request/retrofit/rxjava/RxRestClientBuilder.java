package com.booyue.net.request.retrofit.rxjava;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author jett
 * @date 2018/6/6
 */

public class RxRestClientBuilder {
    private HashMap<String,Object> mParams;
    private HashMap<String,String> mHeader;
    private String mUrl;
    private String json;
    private  RequestBody mBody;
    //上传下载
    private File mFile;
    RxRestClientBuilder(){

    }
    //url
    public final RxRestClientBuilder url(String url){
        this.mUrl=url;
        return this;
    }
     public final RxRestClientBuilder json(String json){
        this.json=json;
        return this;
    }

    //参数
    public final RxRestClientBuilder params(HashMap<String,Object> params){
        this.mParams=params;
        return this;
    }

    public final RxRestClientBuilder header(HashMap<String,String> mHeader){
        this.mHeader = mHeader;
        return this;
    }

    public final RxRestClientBuilder raw(String raw){
        this.mBody= RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }
    //上传
    public final RxRestClientBuilder file(File file){
        this.mFile=file;
        return this;
    }
    public final RxRestClientBuilder file(String file){
        this.mFile=new File(file);
        return this;
    }

    public final RxRestClient build(){
        return new RxRestClient(mParams,mUrl,mHeader,mBody,mFile,json);
    }
}
