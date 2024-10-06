package com.booyue.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/4/26.10:54
 */

public class UpgradeUtil {
    private static final String TAG = "UpgradeUtil";
    /*
     * 从服务器中下载APK
     */
    public static boolean downloading =false;
    public static void downLoadApk(final Context context, final String apkUrl
    ) {
//		final ProgressDialog pd;    //进度条对话框
//		pd = new  ProgressDialog(context);
//		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		pd.setMessage("正在下载更新");
//		pd.show();
        new AsyncTask<String, Integer, File>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected File doInBackground(String[] params) {
                downloading = true;
                String apkUrl = params[0];
//				File file = getFileFromServer(apkUrl);
                //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
                HttpURLConnection conn = null;
                InputStream is = null;
                FileOutputStream fos = null;
                BufferedInputStream bis = null;
                File file = null;
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        URL url = new URL(apkUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        //获取到文件的大小
                        long length = conn.getContentLength();
//                        pd.setMax(conn.getContentLength());
                        is = conn.getInputStream();
                        file = new File(Environment.getExternalStorageDirectory(), "booyue.apk");
                        fos = new FileOutputStream(file);
                        bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        long total = 0;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            total += len;
                            //获取当前下载量
//                            pd.setProgress(total);
//							LoggerUtils.d(TAG, "Total = " + total + ",length = " + length);
                            int progress = (int) (total * 100 / length);
                            publishProgress(progress);
                        }
                        LoggerUtils.d(TAG, "downloadComplete: ");
                    }
                } catch (IOException e) {
                    LoggerUtils.d(TAG, "IOException: ");
                    e.printStackTrace();
                } finally {
                    if (conn != null) conn.disconnect();
                    CloseUtil.close(is);
                    CloseUtil.close(fos);
                    CloseUtil.close(bis);
                }
                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                LoggerUtils.d(TAG, "onPostExecute: file = " + file);
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
                downloading = false;
                if (file != null) {
                    installApk(file, context);
                }
            }

            @Override
            protected void onProgressUpdate(Integer[] values) {
                int progress = values[0];
//				LoggerUtils.d(TAG, "onProgressUpdate: " + progress);
//                if (progressBar != null) {
//                    progressBar.setProgress(progress);
//                }
//                if (tvProgress != null) {
//                    tvProgress.setText("正在下载..." + progress + "%");
//                }
            }
        }.execute(apkUrl);
    }

    //安装apk
    public static void installApk(File file, Context context) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
