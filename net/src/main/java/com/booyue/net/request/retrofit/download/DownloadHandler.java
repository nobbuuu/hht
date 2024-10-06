//package com.booyue.net.request.retrofit.download;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.booyue.net.callback.ICallback;
//import com.booyue.net.request.retrofit.RestCreator;
//
//import java.util.HashMap;
//
//import okhttp3.HttpUrl;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// *
// * @author jett
// * @date 2018/6/6
// * Retrofit下载操作类
// */
//
//public class DownloadHandler {
//    private static final String TAG = "DownloadHandler";
//    private final HashMap<String,Object> PARAMS;
//    private final String URL;
//    private final String DOWNLOAD_DIR;
//    private final String EXTENSION;
//    private final String FILENAME;
//    private final ICallback ICALLBACK;
//
//    public DownloadHandler(
//            HashMap<String, Object> params,
//            String url,
//            ICallback iCallback,
//            String downloadDir,
//            String extension,
//            String filename) {
//        this.PARAMS = params;
//        this.URL = url;
//        this.ICALLBACK = iCallback;
//        this.DOWNLOAD_DIR = downloadDir;
//        this.EXTENSION = extension;
//        this.FILENAME = filename;
//    }
//
//    public final void handleDownload(){
//        RestCreator.getRestService().download(URL,PARAMS)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        HttpUrl url = call.request().url();
//                        Log.d(TAG, "onResponse: url = " + url.url());
//                        if(response.isSuccessful()){
//                            //开始保存文件,开一个异步任务来做
//                            SaveFileTask task=new SaveFileTask(ICALLBACK);
//                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
//                                    DOWNLOAD_DIR,EXTENSION,response.body(),FILENAME);
//                           //是否已经取消
//                            if(task.isCancelled()){
//                                if(ICALLBACK != null){
//                                    ICALLBACK.onPostExecute();
//                                }
//                            }
//                            //诸如404之类错误
//                        }else{
//                            if(ICALLBACK != null){
//                                ICALLBACK.onError(response.code(),response.message());
//                            }
//                        }
//                    }
//                    //网络错误，诸如没有网络，网线没插好
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        if(ICALLBACK != null){
//                            ICALLBACK.onFailure();
//                        }
//                    }
//                });
//    }
//
//}
