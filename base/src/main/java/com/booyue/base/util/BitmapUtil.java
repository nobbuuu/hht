package com.booyue.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 */
public class BitmapUtil {
    private static final String TAG = "bitmamUtils";

    /**
     * @param bmp
     * @param filepath
     * @param isOverWrite 是否覆盖
     * @return
     */
    public static Boolean saveCroppedImage(Bitmap bmp, String filepath, Boolean isOverWrite) {
        LoggerUtils.d(TAG, ": saveCroppedImage filePath " + filepath);
        if (filepath != null) {
            File file = new File(filepath);
            if (file.isDirectory()) {
                return false;
            }
            if (isOverWrite && file.exists()) {
                file.delete();
            }

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(CompressFormat.JPEG, 50, fos);
                fos.flush();
                fos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 图片压缩
     */
    public static void loadBigBitmap(Context context, String url, ImageView ivAvatar) {
        if (url == null) {
            LoggerUtils.e(TAG, "本地头像地址为空：");
            return;
        }
        File f = new File(url);
        if (f == null || !f.exists()) {
            LoggerUtils.e(TAG, "加载本地头像失败：" + url);
            return;
        }
        //解析图片需要的参数都封装到option中
        int degree = readPictureDegree(url);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //不为像素分配内存，只读取边界宽高信息
        opt.inJustDecodeBounds = true;
        //返回的bitmap对象为空，
        BitmapFactory.decodeFile(url, opt);
        //为图片的宽和高
        int ivWidth = opt.outWidth;
        int ivHeight = opt.outHeight;
        //屏幕的宽和高
        //		int width = context.getResources().getDisplayMetrics().widthPixels;
        //		int height = context.getResources().getDisplayMetrics().heightPixels;
        Display dp = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = dp.getWidth();
        int height = dp.getHeight();
        //设置缩放比例，默认为1
        int scale = 1;
        int scaleWidth = ivWidth / width;
        int scaleHeight = ivHeight / height;
        //哪个的缩放比例大就选择哪个缩放比例，并且缩放比例必须大于1时才进行缩放，否则不进行缩放
        if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
            scale = scaleWidth;
        } else if (scaleWidth < scaleHeight && scaleHeight >= 1) {
            scale = scaleHeight;
        }
        //设置缩放比例
        opt.inSampleSize = scale;
        //只有设置为false才能对图片进行解析
        opt.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(url, opt);
        ivAvatar.setImageBitmap(bitmap);
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
//		int width = Math.round(image.getWidth() * BITMAP_SCALE);
//		int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
//		Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap inputBitmap = image;
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        rs.destroy();
        return outputBitmap;
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getApplicationIcon(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        if (d instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) d;
            Bitmap bm = bd.getBitmap();
            return  bm;
        }else {
            return null;
        }
    }

    //type用string、color、mipmap替换，根据需求。可以多传一个参数type；也可以写三个方法，指定获取什么类型
    public static int getMipmapId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "mipmap",
                paramContext.getPackageName());
    }


}
