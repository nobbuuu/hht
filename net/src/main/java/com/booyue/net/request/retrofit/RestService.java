package com.booyue.net.request.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit的所有功能
 */

public interface RestService {
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    //下载是直接到内存,所以需要 @Streaming
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);
    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Call<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    //上传
    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

    //原始数据
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);

}










