package com.toughegg.andytools.http.method;

/**
 * Created by Andy on 15/7/8.
 */
/**
 * 支持的请求方式
 */
public interface HttpMethod {
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;
}
