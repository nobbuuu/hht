package com.booyue.media.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description : 视频播放需要的数据对象
 */
public class VideoBean implements Parcelable {
    public String url;
    public String name;
    public String localUrl;
    public VideoBean(){}

    protected VideoBean(Parcel in) {
        url = in.readString();
        name = in.readString();
        localUrl = in.readString();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(localUrl);
    }
}
