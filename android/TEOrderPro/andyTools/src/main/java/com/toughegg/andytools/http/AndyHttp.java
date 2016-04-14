package com.toughegg.andytools.http;

import android.util.Log;

import com.toughegg.andytools.AndyConstants;
import com.toughegg.andytools.http.cache.Entry;
import com.toughegg.andytools.http.cache.ICache;
import com.toughegg.andytools.http.method.CacheDispatcher;
import com.toughegg.andytools.http.method.DownloadController;
import com.toughegg.andytools.http.method.DownloadTaskQueue;
import com.toughegg.andytools.http.method.HttpCallBack;
import com.toughegg.andytools.http.method.HttpConfig;
import com.toughegg.andytools.http.method.HttpMethod;
import com.toughegg.andytools.http.method.NetworkDispatcher;
import com.toughegg.andytools.http.method.Request;
import com.toughegg.andytools.systemUtil.AndyLoger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Andy on 15/7/8.
 */
public class AndyHttp {

    private HttpConfig mHttpConfig;

    // 请求缓冲区
    private final Map<String, Queue<Request<?>>> mWaitingRequests = new HashMap<String, Queue<Request<?>>>();
    // 请求的序列化生成器
    private final AtomicInteger mSequenceGenerator = new AtomicInteger();

    // 当前正在执行请求的线程集合
    private final Set<Request<?>> mCurrentRequests = new HashSet<Request<?>>();
    // 执行缓存任务的队列.
    private final PriorityBlockingQueue<Request<?>> mCacheQueue = new PriorityBlockingQueue<Request<?>>();
    // 需要执行网络请求的工作队列
    private final PriorityBlockingQueue<Request<?>> mNetworkQueue = new PriorityBlockingQueue<Request<?>>();

    // 请求任务执行池
    private final NetworkDispatcher[] mTaskThreads;

    // 缓存队列调度器
    private CacheDispatcher mCacheDispatcher;

    public AndyHttp() {
        this(new HttpConfig());
    }

    public AndyHttp(HttpConfig mHttpConfig) {
        this.mHttpConfig = mHttpConfig;
        mHttpConfig.mController.setRequestQueue(this);
        mTaskThreads = new NetworkDispatcher[AndyConstants.NETWORK_POOL_SIZE];
        start();
    }


    //－－－－－－－－－－－－－－－－－get  请求 －－－－－－－－－

    /**
     * 发起get请求
     *
     * @param url      地址
     * @param callback 请求中的回调方法
     */
    public Request<byte[]> get(String url, HttpCallBack callback) {
        return get(url, new HttpParams(), callback);
    }

    /**
     * 发起get请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     */
    public Request<byte[]> get(String url, HttpParams params,
                               HttpCallBack callback) {
        return get(url, params, true, callback);
    }

    /**
     * 发起get请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     * @param useCache 是否缓存本条请求
     */
    public Request<byte[]> get(String url, HttpParams params, boolean useCache,
                               HttpCallBack callback) {
        if (params != null) {
            url += params.getUrlParams();
        }
        Request<byte[]> request = new FormRequest(HttpMethod.GET, url, params,
                callback);
        request.setShouldCache(useCache);
        doRequest(request);
        return request;
    }


    //---------------------download ----------------------

    /**
     * 下载 队列
     *
     * @param storeFilePath
     *            文件保存路径。注，必须是一个file路径不能是folder
     * @param url
     *            下载地址
     * @param callback
     *            请求中的回调方法
     */
    public DownloadTaskQueue download(String storeFilePath, String url, HttpCallBack callback) {
        FileRequest request = new FileRequest(storeFilePath, url, callback);
        mHttpConfig.mController.add(request);
        request.setShouldCache(false);
        doRequest(request);
        return mHttpConfig.mController;
    }

    /**
     * 返回下载总控制器
     *
     * @return
     */
    public DownloadController getDownloadController(String storeFilePath, String url) {
        return mHttpConfig.mController.get(storeFilePath, url);
    }

    public void cancleAll() {
        mHttpConfig.mController.clearAll();
    }







//－－－－－－－－－－－－－－－－－－post 请求－－－－－－－－－－－－－－－－－－－－－－

    /**
     * 发起post请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     */
    public Request<byte[]> post(String url, HttpParams params,
                                HttpCallBack callback) {
        return post(url, params, true, callback);
    }

    /**
     * 发起post请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     * @param useCache 是否缓存本条请求
     */
    public Request<byte[]> post(String url, HttpParams params,
                                boolean useCache, HttpCallBack callback) {
        if (params != null) {
            url += params.getUrlParams();
        }
        Request<byte[]> request = new FormRequest(HttpMethod.POST, url, params, callback);
        request.setShouldCache(useCache);
        doRequest(request);
        return request;
    }


    /**
     * json post 请求
     *
     * @param url      链接
     * @param params   http参数集
     * @param callBack 请求方法回调
     * @return
     */
    public Request<byte[]> jsonPost(String url, HttpParams params, HttpCallBack callBack) {
        return jsonPost(url, params, true, callBack);

    }

    /**
     * json post 请求 ，，有缓存参数
     *
     * @param url
     * @param params
     * @param userCache 是否缓存请求
     * @param callBack
     * @return
     */
    public Request<byte[]> jsonPost(String url, HttpParams params, boolean userCache, HttpCallBack callBack) {
        Request<byte[]> request = new JsonReques(HttpMethod.POST, url, params, callBack);
        request.setShouldCache(userCache);
        doRequest(request);
        return request;
    }

    /**
     * 使用JSON传参的get请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     * @param useCache 是否缓存本条请求
     */
    public Request<byte[]> jsonGet(String url, HttpParams params,
                                   boolean useCache, HttpCallBack callback) {
        Request<byte[]> request = new JsonReques(HttpMethod.GET, url, params,
                callback);
        request.setShouldCache(useCache);
        doRequest(request);
        return request;
    }


    /**
     * 执行一个自定义请求
     *
     * @param request
     */
    public void doRequest(Request<?> request) {
        request.setConfig(mHttpConfig);
        add(request);
    }


    /**
     * 只有你确定cache是一个String时才可以使用这个方法，否则还是应该使用getCache(String);
     *
     * @param url    url
     * @param params http请求中的参数集(KJHttp的缓存会连同请求参数一起作为一个缓存的key)
     */
    public String getStringCache(String url, HttpParams params) {
        if (params != null) {
            url += params.getUrlParams();
        }
        return new String(getCache(url));
    }

    /**
     * 只有你确定cache是一个String时才可以使用这个方法，否则还是应该使用getCache(String);
     *
     * @param url
     * @return
     */
    public String getStringCache(String url) {
        return new String(getCache(url));
    }

    /**
     * 获取缓存数据
     *
     * @param url 哪条url的缓存
     * @return
     */
    public byte[] getCache(String url) {
        ICache cache = mHttpConfig.mICache;
        cache.initialize();
        Entry entry = cache.get(url);
        if (entry != null) {
            return entry.data;
        } else {
            return new byte[0];
        }
    }


    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    public void setHttpConfig(HttpConfig mHttpConfig) {
        this.mHttpConfig = mHttpConfig;
    }


    /**
     * 移除一个缓存
     *
     * @param url 哪条url的缓存
     */
    public void removeCache(String url) {
        mHttpConfig.mICache.remove(url);
    }

    /**
     * 清空缓存
     */
    public void cleanCache() {
        mHttpConfig.mICache.clear();
    }


    // -------------------------主要代码－－－－－－－－－－－－－－－－－－
    public void start() {
        stop();// 首先关闭之前的运行，不管是否存在
        mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue,
                mHttpConfig.mICache, mHttpConfig.mIDelivery, mHttpConfig);
        mCacheDispatcher.start();
        // 构建线程池
        for (int i = 0; i < mTaskThreads.length; i++) {
            NetworkDispatcher tasker = new NetworkDispatcher(mNetworkQueue, mHttpConfig.mNetwork, mHttpConfig.mICache, mHttpConfig.mIDelivery);
            mTaskThreads[i] = tasker;
            tasker.start();
        }
    }

    public void stop() {
        if (mCacheDispatcher != null) {
            mCacheDispatcher.quit();
        }
        for (int i = 0; i < mTaskThreads.length; i++) {
            if (mTaskThreads[i] != null) {
                mTaskThreads[i].quit();
                mTaskThreads[i]=null;
            }
        }
    }

    /**
     * 向请求队列加入一个请求
     * Note:此处工作模式是这样的：SKYunHttp可以看做是一个队列类，而本方法不断的向这个队列添加request；另一方面，
     * TaskThread不停的从这个队列中取request并执行。类似的设计可以参考Handle...Looper...MessageQueue的关系
     */
    public <T> Request<T> add(Request<T> request) {
        if (request.getCallback() != null) {
            request.getCallback().onPreStart();
        }
        Log.d("hcc","request-->>add-->>"+request.getCacheEntry());
        // 标记该请求属于该队列，并将它添加到该组当前的请求。
        request.setRequestQueue(this);
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }
        // 设置进程优先序列
        request.setSequence(mSequenceGenerator.incrementAndGet());

        // 如果请求不可缓存，跳过缓存队列，直接进入网络。
        if (!request.shouldCache()) {
            mNetworkQueue.add(request);
            return request;
        }

        // 缓存队列添加，如果已经在mWaitingRequests中有本请求，则替换
        synchronized (mWaitingRequests) {
            String cacheKey = request.getCacheKey();
            if (mWaitingRequests.containsKey(cacheKey)) {
                // There is already a request in flight. Queue up.
                Queue<Request<?>> stagedRequests = mWaitingRequests
                        .get(cacheKey);
                if (stagedRequests == null) {
                    stagedRequests = new LinkedList<Request<?>>();
                }
                stagedRequests.add(request);
                mWaitingRequests.put(cacheKey, stagedRequests);
                if (HttpConfig.DEBUG) {
                    AndyLoger.debug(
                            "Request for cacheKey=%s is in flight, putting on hold.",
                            cacheKey);
                }
            } else {
                mWaitingRequests.put(cacheKey, null);
                mCacheQueue.add(request);
            }
            return request;
        }
    }


    /**
     * 将一个请求标记为已完成
     */
    public void finish(Request<?> request) {
        synchronized (mCurrentRequests) {
            mCurrentRequests.remove(request);
        }

        if (request.shouldCache()) {
            synchronized (mWaitingRequests) {
                String cacheKey = request.getCacheKey();
                Queue<Request<?>> waitingRequests = mWaitingRequests
                        .remove(cacheKey);
                if (waitingRequests != null) {
                    if (AndyConstants.DEBUG) {
                        AndyLoger.debug("Releasing %d waiting requests for cacheKey=%s.", waitingRequests.size(), cacheKey);
                    }
                    mCacheQueue.addAll(waitingRequests);
                }
            }
        }
    }

    public void destroy() {
        cancelAll();
        stop();
    }


    /**
     * 取消指定url请求
     *
     * @param url
     */
    public void cancel(String url) {
        synchronized (mCurrentRequests) {
            for (Request<?> request : mCurrentRequests) {
                if (url.equals(request.getTag())) {
                    request.cancel();
                }
            }
        }
    }

    /**
     * 取消全部请求
     */
    public void cancelAll() {
        synchronized (mCurrentRequests) {
            for (Request<?> request : mCurrentRequests) {
                request.cancel();
            }
        }
    }
}
