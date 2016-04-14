package com.toughegg.andytools.http.method;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Http响应的分发器，这里用于把异步线程中的响应分发到UI线程中执行
 * Created by Andy on 15/7/8.
 */
public class DeliveryExecutor  implements  IDelivery{

    private final Executor mResponsePoster;

    public DeliveryExecutor(Executor executor) {
        mResponsePoster = executor;
    }

    public DeliveryExecutor(final Handler handler) {
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }
    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        Log.d("hcc","postResponse--1--------");
        postResponse(request, response, null);
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        request.markDelivered();
        Log.d("hcc", "postResponse--2--------" + response.isSuccess());
        Log.d("hcc", "postResponse--2--------" + response.error);
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable));
    }

    @Override
    public void postError(Request<?> request, SKYunHttpException error) {
        Log.d("hcc", "postResponse--3--------" + Response.error(error));
        Response<?> response = Response.error(error);
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
    }

    @Override
    public void postDownloadProgress(Request<?> request, long fileSize, long downloadedSize) {
        request.mCallback.onLoading(fileSize, downloadedSize);
        Log.d("hcc", "postDownloadProgress----------" + fileSize);
    }

    @Override
    public void postCancel(Request<?> request) {

    }


    /**
     * 一个Runnable，将网络请求响应分发到UI线程中
     */
    @SuppressWarnings("rawtypes")
    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Response mResponse;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, Response response, Runnable runnable) {
            mRequest = request;
            mResponse = response;
            mRunnable = runnable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if (mRequest.isCanceled()) {
                mRequest.finish("request已经取消，在分发时finish");
                return;
            }
            Log.d("hcc","mResponse ---isSuccess-->>"+mResponse.isSuccess());
            Log.d("hcc","mResponse ---error-->>"+mResponse.error);
            if (mResponse.isSuccess()) {
                mRequest.deliverResponse(mResponse.headers, mResponse.result);
            } else {
                mRequest.deliverError(mResponse.error);
            }
            mRequest.requestFinish();
            mRequest.finish("done");
            if (mRunnable != null) { // 执行参数runnable
                mRunnable.run();
            }
        }
    }
}
