package com.booyue.net.request.retrofit;

import com.booyue.base.app.ProjectInit;
import com.booyue.net.request.okhttp.intercepter.LogInterceptor;
import com.booyue.net.request.retrofit.rxjava.RxRestService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 * @author jett
 * @date 2018/6/6
 */

public final class RestCreator {
    /**
     * 产生一个全局的Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = ProjectInit.getApiHost();

        private static Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OKHttpHolder.OK_HTTP_CLIENT);



        /**
         * 默认使用base_url创建的retrofit对象
         */

        private static final Retrofit RETROFIT_CLIENT = builder.baseUrl(BASE_URL).build();

        /**
         * 动态使用用户传入的url创建retrofit
         *
         * @param baseUrl
         * @return
         */
        private static final Retrofit getRetrofitClient(String baseUrl) {
            return builder.baseUrl(baseUrl).build();
        }


    }

    /**
     * 创建OkHttpClient对象
     */
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 30;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(LogInterceptor.httpLoggingInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 提供接口让调用者得到retrofit对象
     */
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    /**
     * 提供接口让调用者得到retrofit对象
     */
    private static final class RxRestServiceHolder {
        private static final RxRestService REST_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RxRestService.class);
    }

    public static RxRestService getRxRestService() {
        return RxRestServiceHolder.REST_SERVICE;
    }

    /**
     * 动态修改hostUrl
     *
     * @param baseUrl
     * @return
     */
    public static RxRestService getRxRestCustomService(String baseUrl) {
        return RetrofitHolder.getRetrofitClient(baseUrl).create(RxRestService.class);
    }

}








