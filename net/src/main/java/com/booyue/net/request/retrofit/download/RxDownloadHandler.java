//package com.booyue.net.request.retrofit.download;
//
//import android.os.AsyncTask;
//
//import com.booyue.net.callback.ICallback;
//
//import okhttp3.ResponseBody;
//
///**
// * Created by jett on 2018/6/6.
// * Rxjava，Retrofit封装下载操作类
// */
//
//public class RxDownloadHandler {
//    private final ICallback ICALLBACK;
//    private final String DOWNLOAD_DIR;
//    private final String EXTENSION;
//    private final String FILENAME;
//    private final ICallback.ProgressCallback DOWNLOADPROGRESS;
//    public RxDownloadHandler(ICallback iCallback,
//                             String downloadDir,
//                             String extension,
//                             String filename,
//                             ICallback.ProgressCallback iDownloadProgress) {
//        this.ICALLBACK = iCallback;
//        this.DOWNLOAD_DIR = downloadDir;
//        this.EXTENSION = extension;
//        this.FILENAME = filename;
//        this.DOWNLOADPROGRESS = iDownloadProgress;
//    }
//
//    /**
//     * 开始下载
//     * @param responseBody
//     */
//    public final void handleDownload(ResponseBody responseBody){
//        RxSaveFileTask task=new RxSaveFileTask(ICALLBACK,DOWNLOADPROGRESS);
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,responseBody, DOWNLOAD_DIR,FILENAME,EXTENSION);
//        //取消下载
//        if(task.isCancelled()){
//            if(ICALLBACK != null){
//                ICALLBACK.onPostExecute();
//            }
//        }
//    }
//}
