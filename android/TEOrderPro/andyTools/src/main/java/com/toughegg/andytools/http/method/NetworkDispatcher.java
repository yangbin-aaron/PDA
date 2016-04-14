
package com.toughegg.andytools.http.method;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.toughegg.andytools.http.cache.ICache;
import com.toughegg.andytools.systemUtil.AndyLoger;

import java.util.concurrent.BlockingQueue;

/**
 * 网络请求任务的调度器，负责不停的从RequestQueue中取Request并交给NetWork执行，
 * 执行完成后分发执行结果到UI线程的回调并缓存结果到缓存器
 */
public class NetworkDispatcher extends Thread {
    private final BlockingQueue<Request<?>> mQueue; // 正在发生请求的队列
    private final Network mNetwork; // 网络请求执行器
    private final ICache mICache; // 缓存器
    private final IDelivery mIDelivery;
    private volatile boolean mQuit = false; // 标记是否退出本线程

    public NetworkDispatcher(BlockingQueue<Request<?>> queue, Network network, ICache cache, IDelivery delivery) {
        mQueue = queue;
        mNetwork = network;
        this.mICache = cache;
        this.mIDelivery = delivery;

        Log.d("hcc", "mNetworkQueue.take().getCacheEntry()----->>>>" + mQueue.size());
    }

    /**
     * 强制退出本线程
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void addTrafficStatsTag(Request<?> request) {
        // Tag the request (if API >= 14)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    /**
     * 阻塞态工作，不停的从队列中获取任务，直到退出。并把取出的request使用Network执行请求，然后NetWork返回一个NetWork响应
     */
    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            Request<?> request;
            try {
                request = mQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                } else {
                    continue;
                }
            }
            try {
                if (request.isCanceled()) {
                    request.finish("任务已经取消");
                    continue;
                }
                Log.d("hcc", "reques-->>>>" + request.getCacheEntry());
                addTrafficStatsTag(request);
                NetworkResponse networkResponse = mNetwork.performRequest(request);
                // 如果这个响应已经被分发，则不会再次分发
                if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                    request.finish("已经分发过本响应");
                    continue;
                }
                Response<?> response = request.parseNetworkResponse(networkResponse);

                if (request.shouldCache() && response.cacheEntry != null) {
                    mICache.put(request.getCacheKey(), response.cacheEntry);
                }

                request.markDelivered();
                mIDelivery.postResponse(request, response);
            } catch (SKYunHttpException volleyError) {
                parseAndDeliverNetworkError(request, volleyError);
            } catch (Exception e) {
                AndyLoger.debug("Unhandled exception %s", e.getMessage());
                mIDelivery.postError(request, new SKYunHttpException(e));
            }
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request,
                                             SKYunHttpException error) {
        error = request.parseNetworkError(error);
        mIDelivery.postError(request, error);
    }
}
