package com.toughegg.andytools.http.method;

import android.os.Handler;
import android.os.Looper;

import com.toughegg.andytools.AndyConstants;
import com.toughegg.andytools.http.cache.DiskCache;
import com.toughegg.andytools.http.cache.ICache;
import com.toughegg.andytools.systemUtil.FileUtils;

import java.io.File;

/**
 * Created by Andy on 15/7/8.
 */
public class HttpConfig {

    /**
     * 缓存器 *
     */
    public ICache mICache;
    public static boolean DEBUG = AndyConstants.DEBUG;
    public Network mNetwork;
    public IDelivery mIDelivery;

    /**
     * 在Http请求中，如果服务器也声明了对缓存时间的控制，那么是否优先使用服务器设置: 默认false
     */
    public static boolean useServerControl = false;

    /**
     * 缓存有效时间: 默认5分钟
     */
    public int cacheTime = 5 * 60000;

    /**
     * 全局的cookie，如果每次Http请求都需要传递固定的cookie，可以设置本项
     */
    public static String sCookie;

    /**
     * 下载控制器队列，对每个下载任务都有一个控制器负责控制下载
     */
    public DownloadTaskQueue mController;

    /**
     * 初始化集合
     */
    public HttpConfig() {
        this(AndyConstants.CACHEPATH, AndyConstants.DISK_CACHE_SIZE, AndyConstants.MAX_DOWNLOAD_TASK_SIZE);
    }

    /**
     * 初始化集合
     * @param cachePath 缓存路径
     * @param disk_cache_size 缓存大小
     * @param max_downLoad_task 下载任务个数
     */
    public HttpConfig(String cachePath, int disk_cache_size, int max_downLoad_task) {
        File folder = FileUtils.getSaveFolder(cachePath);
        mICache = new DiskCache(folder, disk_cache_size);
        mNetwork = new Network(httpStackFactory());
        //响应分发
        mIDelivery = new DeliveryExecutor(new Handler(Looper.getMainLooper()));
        //下载队列控制
        mController = new DownloadTaskQueue(max_downLoad_task);
    }


    public IHttpStack httpStackFactory() {
        return new HttpConnectStack();
    }
}
