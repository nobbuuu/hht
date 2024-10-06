package com.booyue.net.request.retrofit.download;

import android.os.AsyncTask;

import com.booyue.net.callback.ICallback;
import com.booyue.net.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 *
 * @author jett
 * @date 2018/6/6
 */

public class SaveFileTask extends AsyncTask<Object,Void,File> {

    private final ICallback iCallback;
    public SaveFileTask(ICallback iCallback) {
        this.iCallback = iCallback;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir=(String)params[0];
        String extension=(String)params[1];
        ResponseBody body=(ResponseBody)params[2];
        String name=(String)params[3];
        InputStream is=body.byteStream();
        if(downloadDir==null || downloadDir.equals("")){
            downloadDir="down_loads";
        }
        if(extension==null){
            extension="";
        }
        if(name==null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension,null);
        }else{
            return FileUtil.writeToDisk(is,downloadDir,name,null);
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
}
