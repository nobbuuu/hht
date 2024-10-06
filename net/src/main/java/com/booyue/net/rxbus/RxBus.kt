package com.booyue.net.rxbus

import com.booyue.base.app.ProjectInit
import com.booyue.base.toast.Tips
import com.booyue.base.util.LoggerUtils
import com.booyue.base.util.NetWorkUtils
import com.booyue.net.R
import com.booyue.net.callback.ModelCallback
import com.booyue.net.request.retrofit.rxjava.RxRestClient
import com.booyue.net.request.retrofit.upload.FileUploadRequestBody
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet


/**
 * 数据总线
 */
class RxBus private constructor() {
    companion object {
        private val TAG = "RxBus"
        @Volatile
        private var instance: RxBus? = null

        @Synchronized
        fun getInstance(): RxBus? {
            if (instance == null) {
                synchronized(RxBus::class.java) {
                    if (instance == null) {
                        instance = RxBus()
                    }
                }
            }
            return instance
        }
    }

    private val subscribers: MutableSet<Any>
    /**
     * 注册
     */
    @Synchronized
    fun register(subscriber: Any) {
        subscribers.add(subscriber)
    }

    /**
     * 取消注册
     */
    @Synchronized
    fun unRegister(subscriber: Any) {
        subscribers.remove(subscriber)
    }

    init {
        //读写分离的集合
        subscribers = CopyOnWriteArraySet()
        //        Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * 把处理过程包装起来
     */
    fun <T> post(url: String, params: HashMap<String, Any>, modelCallback: ModelCallback<T>, baseUrl: String = "", header: HashMap<String, String>? = null) {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .post(baseUrl)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        //data会传送到总线上
                        //                        send(type, data);//把数据送到P层
                        LoggerUtils.d(TAG, "response : $data")
                        modelCallback.onSuccess(data)
                        //                        processResult(data,modelCallback);
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }


    /**
     * 把处理过程包装起来
     */
    fun <T> uploadFile(url: String, header: HashMap<String, String>?, params: HashMap<String, Any>,
                       listener: FileUploadRequestBody.UploadListener,
                       modelCallback: ModelCallback<T>, baseUrl: String = "") {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .upload(listener, baseUrl)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        //data会传送到总线上
                        //                        send(type, data);//把数据送到P层
                        LoggerUtils.d(TAG, "response : $data")
                        modelCallback.onSuccess(data)
                        //                        processResult(data,modelCallback);
                    }

                    override fun onError(e: Throwable) {
                        LoggerUtils.d(TAG, e.message)
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }


    /**
     * 把处理过程包装起来
     */
    fun <T> get(url: String, params: HashMap<String, Any>,
                modelCallback: ModelCallback<T>, baseUrl: String = "", header: HashMap<String, String>? = null) {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .get(baseUrl)!!
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        modelCallback.onSuccess(data)
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }

    /**
     * 把处理过程包装起来
     */
    fun <T> put(url: String, header: HashMap<String, String>, params: HashMap<String, Any>,
                modelCallback: ModelCallback<T>, baseUrl: String = "") {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .put(baseUrl)!!
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        modelCallback.onSuccess(data)
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }


    /**
     * 把处理过程包装起来
     */
    fun <T> putFile(url: String, header: HashMap<String, String>, params: HashMap<String, Any>,
                    listener: FileUploadRequestBody.UploadListener, modelCallback: ModelCallback<T>, baseUrl: String = "") {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .putFile(listener, baseUrl)!!
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        modelCallback.onSuccess(data)
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }

    /**
     * 把处理过程包装起来
     */
    fun <T> delete(url: String, header: HashMap<String, String>, params: HashMap<String, Any>,
                   modelCallback: ModelCallback<T>, baseUrl: String = "") {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.params(params)
                .build()
                .delete(baseUrl)!!
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        modelCallback.onSuccess(data)
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }

    /**
     * 把处理过程包装起来
     */
    fun <T> postRaw(url: String, json: String, modelCallback: ModelCallback<T>, baseUrl: String = "", header: HashMap<String, String>? = null) {
        if (!NetWorkUtils.isNetWorkAvailable(ProjectInit.getApplicationContext())) {
            Tips.show(R.string.error_check_network)
            return
        }
        var rxRestClientBuilder = RxRestClient.create().url(url)
        if (header != null) {
            rxRestClientBuilder.header(header)
        }
        rxRestClientBuilder.json(json)
                .build()
                .postRaw(baseUrl)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        modelCallback.onPreExecute()
                    }

                    override fun onNext(data: String) {
                        //data会传送到总线上
                        //                        send(type, data);//把数据送到P层
                        modelCallback.onSuccess(data)
                        //                        processResult(data,modelCallback);
                    }

                    override fun onError(e: Throwable) {
                        modelCallback.onFailure()
                    }

                    override fun onComplete() {
                        modelCallback.onPostExecute()
                    }
                })
    }
}

//    private fun checkUserIdLegal(params: HashMap<String, Any>?): Boolean {
//        if (params != null && params.containsKey("userId")) {
//            val userId = params["userId"] as String?
//            LoggerUtils.d(TAG, "userId: " + userId!!)
//            val entries = params.entries
//            val iterator = entries.iterator()
//            LoggerUtils.d(TAG, "-----------------------^^^^^^^^^^^^--------------------------------- ")
//            while (iterator.hasNext()) {
//                val next = iterator.next()
//                LoggerUtils.d(TAG, "key = : " + next.key + ",value = " + next.value)
//            }
//            LoggerUtils.d(TAG, "-----------------------~~~~~~~~~~~~~~------------------------- ")
//
//            return if (!TextUtils.isEmpty(userId)) true else false
//        }
//        return true
//    }

//    public void send(String type, Object data) {
//        for (Object subscriber : subscribers) {
//            //扫描注解,将数据发送到注册的对象标记方法的位置
//            //subscriber表示层
//            callMethodByAnnotion(subscriber, data, type);
//        }
//    }

/**
 * @param target
 * @param data
 */
//    private void callMethodByAnnotion(Object target, Object data, String type) {
//        //1.得到presenter中写的所有的方法
//        Method[] methodArray = target.getClass().getDeclaredMethods();
//        for (int i = 0; i < methodArray.length; i++) {
//            try {
//                RegisterRxBus registerRxBus = methodArray[i].getAnnotation(RegisterRxBus.class);
//                //校验是否有注解
//                if (registerRxBus == null) continue;
//                String value = registerRxBus.value();
//                //校验注解参数
//                //如果哪个方法上用了我们写的注解
//                //把数据传上去,再执行这个方法
//                LoggerUtils.d(TAG, "callMethodByAnnotion: value = " + value + ",type = " + type);
//                if (type.equals(value)) {
//                    Class paramType = methodArray[i].getParameterTypes()[0];
//                    String name = data.getClass().getName();
//                    LoggerUtils.d(TAG, "callMethodByAnnotion: paramsType = " + paramType.getSimpleName() + ",resultType = " + name);
//                    if (name.equals(paramType.getName())) {
//                        //执行
//                        methodArray[i].invoke(target, new Object[]{data});
//                    }
//                    return;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }










