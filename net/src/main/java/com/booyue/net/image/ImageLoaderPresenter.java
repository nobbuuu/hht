package com.booyue.net.image;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.booyue.net.image.glide.GlideImageLoader;


/**
 *
 * @author Administrator
 * @date 2018/6/15
 */

public class ImageLoaderPresenter implements ImageLoader{
    private ImageLoader mImageLoader;
    private static ImageLoaderPresenter sImageLoaderPresenter;
    private ImageLoaderPresenter(ImageLoader imageLoader){
        mImageLoader = imageLoader;
    }
//
//    public static void init(ImageLoader imageLoader){
//        if(sImageLoaderPresenter == null){
//            synchronized (ImageLoaderPresenter.class){
//                if(sImageLoaderPresenter == null){
//                    sImageLoaderPresenter = new ImageLoaderPresenter(imageLoader);
//                }
//            }
//        }
//    }
    public static ImageLoaderPresenter getInstance(){
        if(sImageLoaderPresenter == null){
            synchronized (ImageLoaderPresenter.class){
                if(sImageLoaderPresenter == null){
                    sImageLoaderPresenter = new ImageLoaderPresenter(new GlideImageLoader());
                }
            }
        }
        return sImageLoaderPresenter;
    }

    @Override
    public void loadImage(Activity activity, String imageUrl, ImageView imageView) {
        if(mImageLoader != null){
            mImageLoader.loadImage(activity,imageUrl,imageView);
        }
    }

    @Override
    public void loadImage(Fragment fragment, String imageUrl, ImageView imageView) {
        if(mImageLoader != null){
            mImageLoader.loadImage(fragment,imageUrl,imageView);
        }
    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView) {
        if(mImageLoader != null){
            mImageLoader.loadImage(context,imageUrl,imageView);
        }
    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView, int placeHolder) {
        if(mImageLoader != null){
            mImageLoader.loadImage(context,imageUrl,imageView,placeHolder);
        }
    }

    @Override
    public void loadImage(Context context, int resid, ImageView imageView) {
        if(mImageLoader != null){
            mImageLoader.loadImage(context,resid,imageView);
        }
    }

    @Override
    public void getBitmap(Context context, String imageUrl,GetBitmapListener getBitmapListener) {
        if(mImageLoader != null){
            mImageLoader.getBitmap(context,imageUrl,getBitmapListener);
        }
    }

    @Override
    public void loadGif(Context context, int resid, ImageView imageView) {
        if(mImageLoader != null){
            mImageLoader.loadGif(context,resid,imageView);
        }
    }
}
