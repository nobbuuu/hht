package com.booyue.net.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2018/6/15.15:19
 */

public interface ImageLoader {
    void loadImage(Activity activity, String imageUrl, ImageView imageView);
    void loadImage(Fragment fragment, String imageUrl, ImageView imageView);
    void loadImage(Context context, String imageUrl, ImageView imageView);
    void loadImage(Context context, String imageUrl, ImageView imageView,int placeHolder);
    void loadImage(Context context, int resid, ImageView imageView);
    void getBitmap(Context context, String imageUrl,GetBitmapListener getBitmapListener);
    void loadGif(Context context,int imageUrl,ImageView imageView);

    interface GetBitmapListener{
        void onGetBitmap(Bitmap bitmap);
    }
}
