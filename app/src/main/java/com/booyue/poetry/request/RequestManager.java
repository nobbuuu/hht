package com.booyue.poetry.request;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.booyue.base.app.ProjectInit;
import com.booyue.base.util.LoggerUtils;
import com.booyue.base.util.PhoneDisplayUtil;
import com.booyue.net.callback.ModelCallback;
import com.booyue.net.image.ImageLoader;
import com.booyue.net.image.ImageLoaderPresenter;
import com.booyue.net.request.retrofit.upload.FileUploadRequestBody;
import com.booyue.net.rxbus.RxBus;
import com.booyue.poetry.R;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.constant.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2016/9/29.
 */
public class RequestManager {
    private static final String TAG = "RequestManager";
    private static RequestManager instance;

    /**
     * 获取当前对象
     */
    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    private RequestManager() {
    }

    public class ImageSize {
        public static final int SIZE_ALBUM_LIST = 54;
        public static final int SIZE_ALBUM_DETAIL = 102;
        public static final int SIZE_HOME_ONE_COLUME = 332;
        public static final int SIZE_HOME_TWO_COLUME = 158;
        public static final int SIZE_HOME_THREE_COLUME = 120;
        public static final int SIZE_RAW = 0;
    }


    /**
     * 获取post请求的url
     */
    public String getHttpsUrl(String url, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        Set<Map.Entry<String, String>> set = map.entrySet();
        int size = set.size();
        int currentPos = 0;

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            currentPos += 1;
            if (currentPos == size) {

                Map.Entry mapEntry = (Map.Entry) iterator.next();
                sb.append(mapEntry.getKey() + "=").append(mapEntry.getValue());
            } else {

                Map.Entry mapEntry = (Map.Entry) iterator.next();
                sb.append(mapEntry.getKey() + "=").append(mapEntry.getValue() + "&");
            }

        }
        return sb.toString();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 加载网络图片
     *
     * @param requestUrl 图片的url
     * @param imageView  显示图片的iamgeview
     * @param imageSize  imageview的大小类型，用于加载失败后，显示默认的图片
     */
    public void loadImage(final String requestUrl, final ImageView imageView, int imageSize) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(requestUrl)) {
            ImageLoaderPresenter.getInstance().loadImage(ProjectInit.getApplicationContext(), R.drawable.default_image, imageView);
            return;
        }
        LoggerUtils.d(TAG, "loadImage: " + requestUrl);
        String compressParam = getCompressParam(imageSize);
        ImageLoaderPresenter.getInstance().loadImage(ProjectInit.getApplicationContext(),
                requestUrl + ServerParamUtil.getParam() + compressParam, imageView);
    }

    public void loadImageWithPlaceHolder(final String requestUrl, final ImageView imageView, int placeHolder) {
        if (imageView == null) {
            return;
        }
        String compressParam = getCompressParam(ImageSize.SIZE_RAW);
        ImageLoaderPresenter.getInstance().loadImage(ProjectInit.getApplicationContext(),
                requestUrl + ServerParamUtil.getParam() + compressParam, imageView, placeHolder);
    }

    public void getBitmap(Context context, String imageUrl, ImageLoader.GetBitmapListener getBitmapListener) {
        ImageLoaderPresenter.getInstance().getBitmap(context, imageUrl,getBitmapListener);
    }

//    x-oss-process=image/resize,w_500/quality,Q_80

    public String getCompressParam(int width) {
        String compressParam = "";
        if (width > 0) {
            int density = (int) PhoneDisplayUtil.sDensity;
            compressParam = "&x-oss-process=image/resize,w_" + width* density + "/quality,Q_80";
        }
        LoggerUtils.d(TAG + "compressParam " + compressParam);
        return compressParam;
    }


    /**
     * 加载本地资源
     *
     * @param resid
     * @param imageView
     */
    public void loadImage(final int resid, final ImageView imageView) {
        ImageLoaderPresenter.getInstance().loadImage(ProjectInit.getApplicationContext(), resid, imageView);
    }
    public void loadGif(final int resid, final ImageView imageView){
        ImageLoaderPresenter.getInstance().loadGif(ProjectInit.getApplicationContext(), resid, imageView);
    }



    public static JSONObject getJsonData(Context context){
        try {
            AssetManager assetManager = context.getAssets(); //获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("jsondata.json"),"UTF-8"); //使用IO流读取json文件内容
            BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine())!=null){
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();
            JSONObject testJson = new JSONObject(builder.toString()); // 从builder中读取了json中的数据。
            return testJson;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post
     */
    public synchronized void post(final String url, final HashMap<String, Object> params, ModelCallback modelCallback) {
        RxBus.Companion.getInstance().post(url, params, modelCallback, "", createHttpHeader());
    }

    /**
     * post 更换HostUrl
     */
    public synchronized void post(final String url, final HashMap<String, Object> params,
                                  ModelCallback modelCallback, String hostUrl) {

        RxBus.Companion.getInstance().post(url, params, modelCallback, hostUrl, createHttpHeader());
    }

    /**
     * post 更换HostUrl
     */
    public synchronized void postRaw(final String url, final String json,
                                     ModelCallback modelCallback, String hostUrl) {

        RxBus.Companion.getInstance().postRaw(url, json, modelCallback, hostUrl, createHttpHeader());
    }


    /**
     * get 更换HostUrl
     */
    public synchronized void get(String url, HashMap<String, Object> params,
                                 ModelCallback modelCallback, String hostUrl) {

        RxBus.Companion.getInstance().get(url,params, modelCallback, hostUrl,createHttpHeader());
    }

    /**
     * get
     */
    public synchronized void get(String url, HashMap<String, Object> params, ModelCallback modelCallback) {

        RxBus.Companion.getInstance().get(url, params, modelCallback, "", createHttpHeader());
    }

    /**
     * put 更换HostUrl
     */
    public synchronized void put(String url, HashMap<String, Object> params,
                                 ModelCallback modelCallback, String hostUrl) {
        RxBus.Companion.getInstance().put(url, createHttpHeader(), params, modelCallback, hostUrl);
    }

    /**
     * put
     */
    public synchronized void put(String url, HashMap<String, Object> params, ModelCallback modelCallback) {
        RxBus.Companion.getInstance().put(url, createHttpHeader(), params, modelCallback, "");
    }

    /**
     * delete 更换HostUrl
     */
    public synchronized void delete(String url, HashMap<String, Object> params,
                                    ModelCallback modelCallback, String hostUrl) {
        RxBus.Companion.getInstance().delete(url, createHttpHeader(), params, modelCallback, hostUrl);
    }

    /**
     * delete
     */
    public synchronized void delete(String url, HashMap<String, Object> params, ModelCallback modelCallback) {
        RxBus.Companion.getInstance().delete(url, createHttpHeader(), params, modelCallback, "");
    }


    /**
     * upload上传文件
     */
    public synchronized void uploadFile(String url,
                                        HashMap<String, Object> map,
                                        FileUploadRequestBody.UploadListener uploadListener,
                                        ModelCallback modelCallback) {
        RxBus.Companion.getInstance().uploadFile(url, createHttpHeader(), map, uploadListener, modelCallback, "");
    }

    /**
     * upload上传文件 更换HostUrl
     */
    public synchronized void uploadFile(String url,
                                        HashMap<String, Object> map,
                                        FileUploadRequestBody.UploadListener uploadListener,
                                        ModelCallback modelCallback, String hostUrl) {
        RxBus.Companion.getInstance().uploadFile(url, createHttpHeader(), map, uploadListener, modelCallback, hostUrl);
    }

    /**
     * put上传文件
     */
    public synchronized void putFile(String url,
                                     HashMap<String, Object> map,
                                     FileUploadRequestBody.UploadListener uploadListener,
                                     ModelCallback modelCallback) {
        RxBus.Companion.getInstance().putFile(url, createHttpHeader(), map, uploadListener, modelCallback, "");
    }

    /**
     * put上传文件 更换HostUrl
     */
    public synchronized void putFile(String url,
                                     HashMap<String, Object> map,
                                     FileUploadRequestBody.UploadListener uploadListener,
                                     ModelCallback modelCallback, String hostUrl) {

        RxBus.Companion.getInstance().putFile(url, createHttpHeader(), map, uploadListener, modelCallback, hostUrl);
    }

    /**
     * 设置http header
     */
    public HashMap<String, String> createHttpHeader() {
        HashMap<String, String> header = new HashMap<>(2);
        header.put("token", ResourceApi.publicToken);
//        header.put("token", "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE2NzIzNTUxNjMsIm5iZiI6MTY2ODc1NTE2M30.gTNo0dELEJjyQCpD-qh4d1NwAy00NsbhwuLRHd1vmUw");
        return header;
    }

}
