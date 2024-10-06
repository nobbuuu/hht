package com.booyue.net.request.retrofit.rxjava


import com.booyue.net.request.HttpMethod
import com.booyue.net.request.retrofit.RestCreator
import com.booyue.net.request.retrofit.upload.FileUploadRequestBody
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * Created by jett on 2018/6/6.
 */
/**
 * 构造函数
 * @param params 参数
 * @param url url
 * @param body 上传原始数据传入此参数，否则为null
 * @param file 上传文件
 */
class RxRestClient(private val PARAMS: HashMap<String, Any>?,
 private val URL: String,
 private val HEADER:HashMap<String, String>?,
 private val BODY: RequestBody?,
        //上传下载
 private val FILE: File?,
private val json:String?) {

    companion object {
        fun create(): RxRestClientBuilder {
            return RxRestClientBuilder()
        }
    }

    /**
     * 开始实现真实的网络操作
     */
    private fun realRequest(method: HttpMethod, baseUrl: String? = ""): Observable<String>? {
//        val service = RestCreator.getRxRestService() ?: return null
        val service: RxRestService = (if (baseUrl == null || baseUrl.isEmpty()) {
            RestCreator.getRxRestService()
        } else {
            RestCreator.getRxRestCustomService(baseUrl)
        }) ?: return null

        var observable: Observable<String>? = null
        when (method) {
            HttpMethod.GET -> observable = if(HEADER == null)service.get(URL, PARAMS) else service.get(HEADER,URL, PARAMS)
            HttpMethod.POST -> observable = if(HEADER == null)service.post(URL, PARAMS) else service.post(HEADER,URL, PARAMS)
            HttpMethod.PUT -> observable =if(HEADER == null)service.put(URL, PARAMS) else service.put(HEADER,URL, PARAMS)
            HttpMethod.DELETE -> observable = if(HEADER == null)service.delete(URL, PARAMS) else service.delete(HEADER,URL, PARAMS)
            HttpMethod.POST_RAW -> observable = if(HEADER == null)service.postRaw(URL, RequestBody.create(MediaType.parse("application/json"),json))
                                                else service.postRaw(HEADER,URL, RequestBody.create(MediaType.parse("application/json"),json))
        }
        return observable
    }

    /**
     * 开始实现真实的网络操作
     */
    private fun realRequest(method: HttpMethod, listener: FileUploadRequestBody.UploadListener, baseUrl: String?): Observable<String>? {
        val service: RxRestService = (if (baseUrl == null || baseUrl.isEmpty()) {
            RestCreator.getRxRestService()
        } else {
            RestCreator.getRxRestCustomService(baseUrl)
        }) ?: return null
        var observable: Observable<String>? = null
        when (method) {
            HttpMethod.UPLOAD -> {
                val builder = MultipartBody.Builder()
                val set = PARAMS!!.entries
                val iterator = set.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    if (entry.value is File) {
                        //                        final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, (File) entry.getValue());
                        val requestBody = FileUploadRequestBody(entry.value as File, listener)

                        builder.addFormDataPart(entry.key, (entry.value as File).name, requestBody)
                    } else {
                        builder.addFormDataPart(entry.key, entry.value as String)
                    }
                }
                observable = if(HEADER == null)service.upload(URL, builder.build()) else service.upload(HEADER,URL, builder.build())
            }
             HttpMethod.PUT_FILE -> {
                //                final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, FILE);
                //                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                //                observable = service.upload(URL, body);

                val builder = MultipartBody.Builder()
                val set = PARAMS!!.entries
                val iterator = set.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    if (entry.value is File) {
                        //                        final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, (File) entry.getValue());
                        val requestBody = FileUploadRequestBody(entry.value as File, listener)

                        builder.addFormDataPart(entry.key, (entry.value as File).name, requestBody)
                    } else {
                        builder.addFormDataPart(entry.key, entry.value as String)
                    }
                }
                observable = if(HEADER == null)service.put(URL, builder.build()) else service.put(HEADER,URL, builder.build())
            }
            HttpMethod.DOWNLOAD -> {
            }
            else -> {
            }
        }
        return observable
    }


    //各种请求
    fun get(baseUrl: String? = ""): Observable<String>? {
        return realRequest(HttpMethod.GET, baseUrl)
    }

    fun post(baseUrl: String? = ""): Observable<String>? {
        return realRequest(HttpMethod.POST, baseUrl)
    }

    fun put(baseUrl: String?): Observable<String>? {
        return realRequest(HttpMethod.PUT, baseUrl)
    }

     fun putFile(listener: FileUploadRequestBody.UploadListener,baseUrl: String?): Observable<String>? {
        return realRequest(HttpMethod.PUT_FILE, listener,baseUrl)
    }

    fun delete(baseUrl: String? = ""): Observable<String>? {
        return realRequest(HttpMethod.DELETE, baseUrl)
    }

    fun upload(listener: FileUploadRequestBody.UploadListener, baseUrl: String?): Observable<String>? {
        return realRequest(HttpMethod.UPLOAD, listener, baseUrl)
    }
    fun postRaw(baseUrl: String? = ""):Observable<String>?{
        return realRequest(HttpMethod.POST_RAW, baseUrl)
    }



}







