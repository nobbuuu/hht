package com.booyue.net.image.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.booyue.net.R;
import com.booyue.net.image.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.ExecutionException;


/**
 * Created by Administrator on 2018/6/15.17:28
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void loadImage(Activity activity, String imageUrl, ImageView imageView) {
        Glide.with(activity)
                .load(imageUrl)
                .asBitmap()
//                .placeholder(R.drawable.ic_launcher_round)
//                .error(R.drawable.ic_launcher_round)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .override(100,100)
                .into(imageView);


    }

    @Override
    public void loadImage(Fragment fragment, String imageUrl, ImageView imageView) {
//        Glide.with(fragment)
//                .load(imageUrl)
//                .asBitmap()
//                .placeholder(R.drawable.default_image)
//                .error(R.drawable.default_image)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
////                .override(100,100)
//                .into(imageView);
    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .override(100,100)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView, int placeHolder) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .placeholder(placeHolder)
                .error(placeHolder)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .override(100,100)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resid, ImageView imageView) {
        Glide.with(context).load(resid).into(imageView);
    }

    @Override
    public void getBitmap(final Context context, final String imageUrl, final GetBitmapListener getBitmapListener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = Glide.with(context)
                                .load(imageUrl)
                                .asBitmap() //必须
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(144, 144)
                                .get();
                        if(getBitmapListener != null){
                            getBitmapListener.onGetBitmap(bitmap);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    @Override
    public void loadGif(Context context, int resid, ImageView imageView) {
        Glide.with(context).load(resid).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }


}
