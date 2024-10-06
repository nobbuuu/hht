package com.example.lamemp3;

import android.os.Handler;
import android.text.TextUtils;

import java.io.File;

/**
 * 封装音频录音工具类
 *
 * @author zhouyou
 *
 * 1、getinstance( dir )
 * 2、
 */
public class RecorderUtil {
    private static RecorderUtil recorderUtil;
    private MP3Recorder mRecorder = null;
//    private String mPlayingPath = null;

    private RecorderUtil(String dir) {
        if (mRecorder == null) {
            mRecorder = new MP3Recorder(dir);
        }
    }

    /**
     * 创建对象
     * @param dir 文件目录名,假如你想创建目录/sdcard/baby/mp3   dir只需要传baby/mp3
     * @return
     */
    public static RecorderUtil getInstance(String dir){
        //dir拼接在 Environment.getExternalStorageDirectory() + "/" 后面
        if(recorderUtil == null){
            recorderUtil = new RecorderUtil(dir);
        }
        return recorderUtil;

    }

    /**
     * 开始录音
     * @param fileName 已经添加后缀名，假如文件为abc.mp3 ，filename传abc
     */
    public void startRecording(String fileName) {
        mRecorder.start(fileName);
    }

    public void stopRecording() {
        mRecorder.stop();
    }


    public void pauseRecording() {
        mRecorder.pause();
    }

    public void resumeRecording() {
        mRecorder.restore();
    }

    public void release() {
        stopRecording();
    }

    /**
     * 获取录音路径
     *
     * @return
     */
    public String getRecorderPath() {
        return mRecorder.getFilePath();
    }

    /**
     * 获取录音类实例
     *
     * @return
     */
    public MP3Recorder getRecorder() {
        return mRecorder;
    }

    /**
     * 获取分贝值
     * @return
     */
    public int getVolume() {
        return mRecorder.getVolume();
    }

    /**
     * 删除当前录音文件
     *
     * @return true：删除成功 false： 删除失败
     */
    public boolean deleteRecorderPath() {
        if (!TextUtils.isEmpty(getRecorderPath())) {
            File file = new File(getRecorderPath());
            if (file.exists()) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    /**
     * 设置句柄
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        if (mRecorder != null) {
            mRecorder.setHandle(handler);
        }
    }

}
