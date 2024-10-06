package com.booyue.net.request.retrofit.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.booyue.base.app.ProjectInit;
import com.booyue.net.callback.ICallback;
import com.booyue.net.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 *
 * @author jett
 * @date 2018/6/6
 */

public class RxSaveFileTask extends AsyncTask<Object,Void,File> {
    private final ICallback iCallback;
    private final ICallback.ProgressCallback progressCallback;

    public RxSaveFileTask(ICallback iCallback, ICallback.ProgressCallback progressCallback) {
        this.iCallback = iCallback;
        this.progressCallback = progressCallback;
    }

    @Override
    protected File doInBackground(Object... params) {
        ResponseBody responseBody = (ResponseBody) params[0];
        String downloadDir=(String)params[1];
        String name=(String)params[2];
        String extension=(String)params[3];
        if(downloadDir==null || downloadDir.equals("")){
            downloadDir="down_loads";
        }
        if(extension==null){
            extension="";
        }
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(responseBody.bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(name==null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension,progressCallback);
        }else{
            return FileUtil.writeToDisk(is,downloadDir,name,progressCallback);
        }

    }
    //如果文件已经下完了
    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(iCallback != null){
            iCallback.onSuccess(file.getPath());
            iCallback.onPostExecute();
        }

    }

    private void autoInstallApk(File file) {
        if(FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install=new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            ProjectInit.getApplicationContext().startActivity(install);
        }
    }
}
