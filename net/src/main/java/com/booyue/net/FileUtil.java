package com.booyue.net;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.booyue.base.app.ProjectInit;
import com.booyue.base.util.CloseUtil;
import com.booyue.base.util.LoggerUtils;
import com.booyue.net.callback.ICallback;
import com.booyue.net.request.retrofit.download.DownloadInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;

/**
 * Created by jett on 2018/6/6.
 */

public class FileUtil {
    private String fullPath;
    private char pathSeparator, //文件分隔符
            extensionSeparator;//扩展名分隔符

    public FileUtil(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }
    public FileUtil(){}


    /**
     * 获得扩展名
     * @return
     */
    public String extension(){
        if(fullPath != null){

            int dot = fullPath.lastIndexOf(extensionSeparator);
            int slash = fullPath.lastIndexOf(pathSeparator);
            //没有扩展名
            if(dot != -1 && slash < dot ){

                return fullPath.substring(dot + 1);
            }
        }
        return null;
    }
    /**
     * 获得文件名(不包括 扩展名)
     * @return
     */
    public String filename() { // gets filename without extension
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }
    /**
     * 获取文件路径
     * @return
     */
    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }
    /**
     * 获取文件名（包括 扩展名）
     * @return
     */
    public String filePath() {
        return filename()+'.'+extension();
    }

    /**
     * 复制文件
     */

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            // 关闭
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }
    /**
     * 写入文件
     */
    public static void writeToFile(Context context, String msg){
        String fileName = "";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            fileName = Environment.getExternalStorageDirectory() +File.separator+ "Share.txt";
        }else{
            fileName = context.getExternalCacheDir().getAbsolutePath()+File.separator+ "Share.txt";
        }
        try{
            FileOutputStream fot = new FileOutputStream(new File(fileName),true);
            byte [] bytes = msg.getBytes();
            fot.write(bytes);
            fot.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * sdcard可用空间
     * @param context
     * @return
     */
    public String getSdAvailableSize(Context context){
        StatFs sf = new StatFs("/mnt/sdcard");
        long blockSize = sf.getBlockSize();
        long availableBlocks = sf.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize*availableBlocks);
    }
    public String getAvailableSize(Context context,String fullPath){
        StatFs sf = new StatFs("/mnt/sdcard");
        long blockSize = sf.getBlockSize();
        long availableBlocks = sf.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize*availableBlocks);
    }

    public static void deleteFile(String fileFullName){
        if(TextUtils.isEmpty(fileFullName)){
            return;
        }
        File file = new File(fileFullName);
        if(file == null){
            throw new NullPointerException("wav file is null");
        }
        if(file.exists()){
            file.delete();
        }
    }

    public void getFileSize(final String fullPath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(fullPath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    int  size = connection.getContentLength();
                    LoggerUtils.d("size = " + size);
                    connection.disconnect();
                    if(sOnFileSizeCallback != null){
                        sOnFileSizeCallback.onCallback(size);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private OnFileSizeCallback sOnFileSizeCallback;
    public interface OnFileSizeCallback{
        void onCallback(int size);
    }
    public void setOnFileSizeCallback(OnFileSizeCallback onFileSizeCallback){
        sOnFileSizeCallback = onFileSizeCallback;
    }












    //格式化的模板
    private static final String TIME_FORMAT = "_yyyyMMdd_HHmmss";

    private static final String SDCARD_DIR =
            Environment.getExternalStorageDirectory().getPath();

    //默认本地上传图片目录
    public static final String UPLOAD_PHOTO_DIR =
            Environment.getExternalStorageDirectory().getPath() + "/a_upload_photos/";

    //网页缓存地址
    public static final String WEB_CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/app_web_cache/";

    //系统相机目录
    public static final String CAMERA_PHOTO_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Camera/";

    private static String getTimeFormatName(String timeFormatHeader) {
        final Date date = new Date(System.currentTimeMillis());
        //必须要加上单引号
        final SimpleDateFormat dateFormat = new SimpleDateFormat("'" + timeFormatHeader + "'" + TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * @param timeFormatHeader 格式化的头(除去时间部分)
     * @param extension        后缀名
     * @return 返回时间格式化后的文件名
     */
    public static String getFileNameByTime(String timeFormatHeader, String extension) {
        return getTimeFormatName(timeFormatHeader) + "." + extension;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createDir(String sdcardDirName) {
        //拼接成SD卡中完整的dir
        final String dir = SDCARD_DIR + "/" + sdcardDirName + "/";
        final File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createFile(String sdcardDirName, String fileName) {
        return new File(createDir(sdcardDirName), fileName);
    }

    private static File createFileByTime(String sdcardDirName, String timeFormatHeader, String extension) {
        final String fileName = getFileNameByTime(timeFormatHeader, extension);
        return createFile(sdcardDirName, fileName);
    }

    //获取文件的MIME
    public static String getMimeType(String filePath) {
        final String extension = getExtension(filePath);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    //获取文件的后缀名
    public static String getExtension(String filePath) {
        String suffix = "";
        final File file = new File(filePath);
        final String name = file.getName();
        final int idx = name.lastIndexOf('.');
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 保存Bitmap到SD卡中
     *
     * @param dir      目录名,只需要写自己的相对目录名即可
     * @param compress 压缩比例 100是不压缩,值约小压缩率越高
     * @return 返回该文件
     */
    public static File saveBitmap(Bitmap mBitmap, String dir, int compress) {

        final String sdStatus = Environment.getExternalStorageState();
        // 检测sd是否可用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File fileName = createFileByTime(dir, "DOWN_LOAD", "jpg");
        try {
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {

                if (bos != null) {
                    bos.flush();
                }
                if (bos != null) {
                    bos.close();
                }
                //关闭流
                if (fos != null) {
                    fos.flush();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        refreshDCIM();

        return fileName;
    }

    public static File writeToDisk(InputStream is, String dir, String name, ICallback.ProgressCallback progressCallback) {
        final File file = FileUtil.createFile(dir, name);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            long fileSize = bis.available();
            Log.d(TAG, "writeToDisk: fileSize = " + fileSize);

            byte data[] = new byte[1024 * 4];
            int count;
            int completeLength = 0;
            while ((count = bis.read(data)) != -1) {
                bos.write(data, 0, count);
                completeLength += count;
                if(progressCallback != null){
                    progressCallback.onProgressUpdate((int) (completeLength * 100 / fileSize));
                }
            }
            bos.flush();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(bos,fos,bis);
        }

        return file;
    }

    public static File writeToDisk(InputStream is, String dir, String prefix, String extension, ICallback.ProgressCallback progressCallback) {
        final File file = FileUtil.createFileByTime(dir, prefix, extension);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            long fileSize = bis.available();
            Log.d(TAG, "writeToDisk: fileSize = " + fileSize);

            byte data[] = new byte[1024 * 4];
            int count;
            int completeLength = 0;
            while ((count = bis.read(data)) != -1) {
                bos.write(data, 0, count);
                completeLength += count;
                if(progressCallback != null){
                    progressCallback.onProgressUpdate((int) (completeLength * 100 / fileSize));
                }
            }
            bos.flush();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(bis,fos,bos);
        }
        return file;
    }

    /**
     * 通知系统刷新系统相册，使照片展现出来
     */
    private static void refreshDCIM() {
        if (Build.VERSION.SDK_INT >= 19) {
            //兼容android4.4版本，只扫描存放照片的目录
            MediaScannerConnection.scanFile(ProjectInit.getApplicationContext(),
                    new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()},
                    null, null);
        } else {
            //扫描整个SD卡来更新系统图库，当文件很多时用户体验不佳，且不适合4.4以上版本
            ProjectInit.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
                    Environment.getExternalStorageDirectory())));
        }
    }

    /**
     * 读取raw目录中的文件,并返回为字符串
     */
    public static String getRawFile(int id) {
        final InputStream is = ProjectInit.getApplicationContext().getResources().openRawResource(id);
        final BufferedInputStream bis = new BufferedInputStream(is);
        final InputStreamReader isr = new InputStreamReader(bis);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }


    public static void setIconFont(String path, TextView textView) {
        final Typeface typeface = Typeface.createFromAsset(ProjectInit.getApplicationContext().getAssets(), path);
        textView.setTypeface(typeface);
    }

    /**
     * 读取assets目录下的文件,并返回字符串
     */
    public static String getAssetsFile(String name) {
        InputStream is = null;
        BufferedInputStream bis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = null;
        final AssetManager assetManager = ProjectInit.getApplicationContext().getAssets();
        try {
            is = assetManager.open(name);
            bis = new BufferedInputStream(is);
            isr = new InputStreamReader(bis);
            br = new BufferedReader(isr);
            stringBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(br,isr,bis,is);
            assetManager.close();
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null){
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            final Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获得扩展名
     * @return
     */
    public static String extension(String fullPath, char pathSeparator, char extensionSeparator){
        if(fullPath != null){
            int dot = fullPath.lastIndexOf(extensionSeparator);
            int slash = fullPath.lastIndexOf(pathSeparator);
            //没有扩展名
            if(dot != -1 && slash < dot ){
                return fullPath.substring(dot + 1);
            }
        }
        return null;
    }
    /**
     * 获得文件名(不包括 扩展名)
     * @return
     */
    public static String filename(String fullPath, char pathSeparator, char extensionSeparator) { // gets filename without extension
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    private static final String TAG = "FileUtil";
    /**
     *
     * @param s
     * @return
     */
    public static String[] splitStringWith$(String s){
        if(s == null || s.isEmpty()) return null;
        if(s.contains("$")){
            String[] strings = s.split("\\$");
            for (int i = 0; i < strings.length; i++) {
                LoggerUtils.d(TAG, strings[i]);
            }
            return strings;
        }else {
            return new String[]{s};
        }
    }
 /**
     *
     * @param s
     * @return
     */
    public static String[] splitString(String s, String regex){
        if(s == null || s.isEmpty()) {
            return null;
        }
        if(s.contains(regex)){
            String[] strings = s.split(regex);
            for (int i = 0; i < strings.length; i++) {
                LoggerUtils.d(TAG, strings[i]);
            }
            return strings;
        }else {
            return new String[]{s};
        }
    }


    /**
     * 获取文件夹或文件的大小
     * @param file 文件
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 文件大小size转换成对应的KB,MB,GB格式
     */
    public static String getFormatSize(double size) {
        if(size == 0){
            return "0.0Kb";
        }
        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public static void writeCache(ResponseBody responseBody, File file, DownloadInfo info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getContentLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getContentLength();
        }

        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 4];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }


}
