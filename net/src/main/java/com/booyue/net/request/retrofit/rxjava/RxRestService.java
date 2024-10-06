package com.booyue.net.request.retrofit.rxjava;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit的所有功能
 * @author Administrator
 */

public interface RxRestService {
    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params);
    @GET
    Observable<String> get(@HeaderMap Map<String, String> headers,@Url String url, @QueryMap Map<String, Object> params);


    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@HeaderMap Map<String, String> headers, @Url String url, @FieldMap Map<String, Object> params);




    @FormUrlEncoded
    @PUT
    Observable<String> put(@Url String url, @FieldMap Map<String, Object> params);
    @FormUrlEncoded
    @PUT
    Observable<String> put(@HeaderMap Map<String, String> headers, @Url String url, @FieldMap Map<String, Object> params);


    @PUT
    Observable<String> put(@Url String url, @Body MultipartBody body);
    @PUT
    Observable<String> put(@HeaderMap Map<String, String> headers, @Url String url, @Body MultipartBody body);





    @DELETE
    Observable<String> delete(@Url String url, @QueryMap Map<String, Object> params);

   @DELETE
    Observable<String> delete(@HeaderMap Map<String, String> headers,@Url String url, @QueryMap Map<String, Object> params);


    //下载是直接到内存,所以需要 @Streaming
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download( @Url String url,@Header("RANGE") String start);

    //上传
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part file);
    @Multipart
    @POST
    Observable<String> upload(@HeaderMap Map<String, String> headers,@Url String url, @Part MultipartBody.Part file);


     //上传
    @POST
    Observable<String> upload(@Url String url, @Body MultipartBody body);
     //上传
    @POST
    Observable<String> upload(@HeaderMap Map<String, String> headers,@Url String url, @Body MultipartBody body);




    //原始数据
    @POST
    Observable<String> postRaw(@Url String url, @Body RequestBody body);
    //原始数据
    @POST
    Observable<String> postRaw(@HeaderMap Map<String, String> headers , @Url String url, @Body RequestBody body);



}










