//package com.booyue.net.request.okhttp;
//
//
//import com.booyue.net.callback.ICallback;
//import com.booyue.net.request.HttpRequest;
//
//import java.io.Closeable;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okio.BufferedSink;
//
///**
// * Created by Administrator on 2018/6/15.10:00
// */
//
//public class OkHttpRequest implements HttpRequest {
//
//    private final OkHttpClient mOkHttpClient;
//
//    public OkHttpRequest() {
//        mOkHttpClient = new OkHttpClient();
//
//
//    }
//
//    @Override
//    public void get(String url, Map<String, String> params, final ICallback iCallback) {
//        //进行url的拼接
//        StringBuilder stringBuilder = new StringBuilder(url + "?");
//        if(params != null){
//            Set<Map.Entry<String, String>> entries = params.entrySet();
//            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
//            if(iterator.hasNext()){
//                Map.Entry<String, String> next = iterator.next();
//                stringBuilder.append(next.getKey() + "=" + next.getValue() + "&");
//            }
//        }
//        //去掉最后一个“&”
//        StringBuilder stringBuilderUrl = stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        String requestUrl = stringBuilderUrl.toString();//拼接之后的url
//
//        //Request对象使用建造者模式创建对象request
//        Request request = new Request.Builder()
//                .url(requestUrl)
//                .build();
//        mOkHttpClient
//                .newCall(request)
//                .enqueue(new Callback() {//异步请求，同步请求执行execute();
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if(iCallback != null){
//                    iCallback.onFailure();
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response == null){
//                    if(iCallback != null){
//                        iCallback.onError(0,"response is null");
//                    }
//                }
//                if(response.isSuccessful()){
//                    String result = response.body().toString();
//                    if(iCallback != null){
//                        iCallback.onSuccess(result);
//                    }
//                }else {
//                    if(iCallback != null){
//                        iCallback.onError(response.code(),response.message());
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void post(String url, Map<String, String> params, final ICallback iCallback) {
//        //Request对象使用建造者模式创建对象request
//        FormBody.Builder builder = new FormBody.Builder();
//        if(params != null){
//            Set<Map.Entry<String, String>> entries = params.entrySet();
//            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
//            if(iterator.hasNext()){
//                Map.Entry<String, String> next = iterator.next();
////                stringBuilder.append(next.getKey() + "=" + next.getValue() + "&");
//                builder.add(next.getKey(),next.getValue());
//            }
//        }
//        RequestBody formBody = builder.build();
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)//执行post默认执行post请求
//                .build();
//        mOkHttpClient
//                .newCall(request)
//                .enqueue(new Callback() {//异步请求，同步请求执行execute();
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        if(iCallback != null){
//                            iCallback.onFailure();
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if(response.isSuccessful()){
//                            String result = response.body().toString();
//                            if(iCallback != null){
//                                iCallback.onSuccess(result);
//                            }
//                        }else {
//                            if(iCallback != null){
//                                iCallback.onError(response.code(),response.message());
//                            }
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 创建json请求体
//     * @return json请求体对象
//     */
//    public RequestBody createJsonBody(String json){
//        MediaType jsontype = MediaType.parse("application/json; charset=utf-8");
////        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据
//        RequestBody requestBody = RequestBody.create(jsontype,json);
//        return requestBody;
//    }
//
//    /**
//     * 创建文件请求体
//     * @param filePath 文件路径
//     * @return 请求体对象
//     */
//    public RequestBody createFileBody(String filePath){
//        MediaType fileBody = MediaType.parse("file/*");
//        File file = new File(filePath);
//        RequestBody requestBody  = RequestBody.create(fileBody,file);
//        return requestBody;
//    }
//
//    /**
//     * 创建同时多部分请求体（包含文件和表单）
//     * @param filePath 文件路径
//     * @param params 参数map集合
//     * @return 请求体
//     */
//    public RequestBody createMultiPartBody(String filePath, Map<String,String> params){
//        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
//        //设置类型
//        multipartBodyBuilder.setType(MultipartBody.FORM);
//        Set<Map.Entry<String, String>> entries = params.entrySet();
//        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
//        if(iterator.hasNext()){
//            Map.Entry<String, String> next = iterator.next();
//            multipartBodyBuilder.addFormDataPart(next.getKey(),next.getValue());
//        }
//        File file = new File(filePath);
//        MediaType fileType = MediaType.parse("file/*");
//        RequestBody requestBody = RequestBody.create(fileType,file);
//        multipartBodyBuilder.addFormDataPart("file",file.getName(),requestBody);
//        return multipartBodyBuilder.build();
//    }
//
//    /**
//     * 创建自定义请求体上传文件
//     * @param filePath
//     * @return
//     */
//    public RequestBody createCustomBody(final String filePath){
//        RequestBody requestBody = new RequestBody() {
//            @Override
//            public MediaType contentType() {
//                return null;
//            }
//
//            @Override
//            public void writeTo(BufferedSink sink) throws IOException {
//                //重写此方法
//                FileInputStream fileInputStream = new FileInputStream(new File(filePath));
//                byte[] buffer = new byte[1024 * 8];
//                int len = -1;
//                while ((len = fileInputStream.read(buffer)) != -1){
//                    sink.write(buffer,0,len);
//                }
//            }
//        };
//        return requestBody;
//    }
//
//    /**
//     * 文件下载
//     * @param response
//     * @param fileDirPath
//     * @param fileName
//     */
//    public void downloadFile(Response response, String fileDirPath, String fileName){
//        if(response == null) return;
//        InputStream is = response.body().byteStream();
//        File fileDir = new File(fileDirPath);
//        if(!fileDir.exists()){
//            fileDir.mkdirs();
//        }
//        File file = new File(fileDir,fileName);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//            byte[] buffer = new byte[1024 * 8];
//            int len = -1;
//            while ((len = is.read(buffer)) !=  -1){//IOExeception
//                fos.write(buffer,0,len);
//            }
//            fos.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            close(is);
//            close(fos);
//        }
//    }
//
//    /**
//     * 流的关闭
//     * @param closeable
//     */
//    public void close(Closeable closeable){
//        if(closeable != null){
//            try {
//                closeable.close();
//                closeable = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
