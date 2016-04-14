package com.toughegg.andytools.http.method;


import com.toughegg.andytools.http.cache.Entry;

import java.util.Map;

/**
 * Http响应封装类，包含了本次响应的全部信息
 * Created by Andy on 15/7/8.
 */
public class Response<T> {

    public SKYunHttpException error = null;
    public Map<String, String> headers;
    public  T result;
    public Entry cacheEntry;

    public Response(T result, Map<String, String> headers, Entry cacheEntry) {
        this.result = result;
        this.headers = headers;
        this.cacheEntry = cacheEntry;
    }

    public Response(SKYunHttpException error) {
        this.result = null;
        this.cacheEntry = null;
        this.headers = null;
        this.error = error;
    }

    // 判断是否响应
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * 返回一个成功的HttpRespond
     *
     * @param result     Http响应的类型
     * @param cacheEntry 缓存对象
     */
    public static <T> Response<T> success(T result, Map<String, String> headers, Entry cacheEntry) {
        return new Response<T>(result, headers, cacheEntry);
    }

    /**
     * 返回一个失败的HttpRespond
     *
     * @param error 失败原因
     */
    public static <T> Response<T> error(SKYunHttpException error) {
        return new Response<T>(error);
    }


}
