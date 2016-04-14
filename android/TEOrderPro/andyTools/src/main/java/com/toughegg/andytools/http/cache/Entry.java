package com.toughegg.andytools.http.cache;

/**
 * Created by Andy on 15/7/8.
 */

import java.util.Collections;
import java.util.Map;

/**
 * cache真正缓存的数据bean，这个是会被保存的缓存对象
 */
public class Entry {
    public byte[] data;
    public String etag; // 为cache标记一个tag

    public long serverDate; // 本次请求成功时的服务器时间
    public long ttl; // 有效期,System.currentTimeMillis()

    public Map<String, String> responseHeaders = Collections.emptyMap();

    /**
     * 是否已过期
     */
    public boolean isExpired() {
        return this.ttl < System.currentTimeMillis();
    }
}