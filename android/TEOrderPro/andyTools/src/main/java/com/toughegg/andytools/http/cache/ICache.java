package com.toughegg.andytools.http.cache;

/**
 * Created by Andy on 15/7/8.
 */
public interface ICache {
    public Entry get(String key);

    public void put(String key, Entry entry);

    public void remove(String key);

    public void clear();

    /**
     * 执行在线程中
     */
    public void initialize();

    /**
     * 让一个缓存过期
     *
     * @param key        Cache key
     * @param fullExpire True to fully expire the entry, false to soft expire
     */
    public void invalidate(String key, boolean fullExpire);
}
