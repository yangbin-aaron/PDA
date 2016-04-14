package com.toughegg.andytools.http.method;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Andy on 15/7/8.
 */
public interface IHttpStack {
    /**
     * 让Http请求端去发起一个Request
     *
     * @param request           一次实际请求集合
     * @param additionalHeaders Http请求头
     * @return 一个Http响应
     */
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException;

}
