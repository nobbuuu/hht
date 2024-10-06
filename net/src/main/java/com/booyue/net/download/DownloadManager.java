//package com.booyue.net.download;
//
//import android.os.Environment;
//import android.util.Log;
//
//import com.booyue.net.FileUtil;
//import com.booyue.net.download.listener.DownloadProgressListener;
//import com.booyue.net.request.okhttp.intercepter.LogInterceptor;
//import com.booyue.net.request.retrofit.download.DownloadInfo;
//import com.booyue.net.request.retrofit.rxjava.RxRestService;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.OkHttpClient;
//import okhttp3.ResponseBody;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//import rx.functions.Action1;
//import rx.functions.Func1;
//
//
///**
// * @author: wangxinhua
// * @date: 2019/12/2 15:56
// * @description :
// */
//public class DownloadManager implements DownloadProgressListener {
//    private DownloadInfo info;
//    private ProgressListener progressObserver;
//    private File outFile;
//    private Subscription subscribe;
//    private long currentRead;
//
//    private DownloadManager() {
//        info = new DownloadInfo();
//        outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "yaoshi.apk");
//        info.setSavePath(outFile.getAbsolutePath());
//    }
//
//    public static DownloadManager getInstance() {
//        return Holder.manager;
//    }
//    private
//
//    public static class Holder {
//        private static DownloadManager manager = new DownloadManager();
//    }
//
//    @Override
//    public void progress(long read, final long contentLength, final boolean done) {
//        Log.e("progress : ", "read = " + read + "contentLength = " + contentLength);
//        // 该方法仍然是在子线程，如果想要调用进度回调，需要切换到主线程，否则的话，会在子线程更新UI，直接错误
//        // 如果断电续传，重新请求的文件大小是从断点处到最后的大小，不是整个文件的大小，info中的存储的总长度是
//        // 整个文件的大小，所以某一时刻总文件的大小可能会大于从某个断点处请求的文件的总大小。此时read的大小为
//        // 之前读取的加上现在读取的
//        if (info.getContentLength() > contentLength) {
//            read = read + (info.getContentLength() - contentLength);
//        } else {
//            info.setContentLength(contentLength);
//        }
//        info.setReadLength(read);
//
//        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                if (progressObserver != null) {
//                    progressObserver.progressChanged(info.getReadLength(), info.getContentLength(), done);
//                }
//            }
//        });
//    }
//    private RxRestService service;
//    /**
//     * 开始下载
//     *
//     * @param url
//     */
//    public void start(String url) {
//        info.setUrl(url);
//        final DownloadInterceptor interceptor = new DownloadInterceptor(this);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(8, TimeUnit.SECONDS)
//                .readTimeout(8, TimeUnit.SECONDS)
//                .addInterceptor(LogInterceptor.httpLoggingInterceptor())
//                .addInterceptor(interceptor)
//                .retryOnConnectionFailure(true);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl()
//                .build();
//
//        service = retrofit.create(RxRestService.class);
//
//        downLoad();
//    }
//
//    /**
//     * 开始下载
//     */
//    private void downLoad() {
//        Log.e("下载：", info.toString());
//        subscribe = service.download("bytes=" + info.getReadLength() + "-", info.getUrl())
//                /*指定线程*/
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
////                .retryWhen(new RetryWhenNetworkException())
//                /* 读取下载写入文件，并把ResponseBody转成DownInfo */
//                .map(new Func1<ResponseBody, DownloadInfo>() {
//                    @Override
//                    public DownloadInfo call(ResponseBody responseBody) {
//                        try {
//                            //写入文件
//                            FileUtil.writeCache(responseBody, new File(info.getSavePath()), info);
//                        } catch (IOException e) {
//                            Log.e("异常:", e.toString());
//                        }
//                        return info;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DownloadInfo>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e("下载", "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("下载", "onError" + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(DownloadInfo downloadInfo) {
//                        Log.e("下载", "onNext");
//                    }
//                });
//    }
//
//    /**
//     * 暂停下载
//     */
//    public void pause() {
//        if (subscribe != null)
//            subscribe.unsubscribe();
//    }
//
//    /**
//     * 继续下载
//     */
//    public void reStart() {
//        downLoad();
//    }
//
//    /**
//     * 进度监听
//     */
//    public interface ProgressListener {
//        void progressChanged(long read, long contentLength, boolean done);
//    }
//
//    public void setProgressListener(ProgressListener progressObserver) {
//        this.progressObserver = progressObserver;
//    }
//}
