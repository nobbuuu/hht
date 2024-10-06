package com.booyue.media;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * @author: wangxinhua
 * @date: 2019/11/1
 * @description : http代理缓存管理
 */
public class HHTHttpProxyCacheServer {
    /**
     * 创建缓存
     */
    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        return HHTHttpProxyCacheServer.proxy == null
                ? (HHTHttpProxyCacheServer.proxy = HHTHttpProxyCacheServer.newProxy(context))
                : HHTHttpProxyCacheServer.proxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
                .maxCacheFilesCount(20)
                .build();
    }
}
