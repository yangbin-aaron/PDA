package com.toughegg.andytools.http.method;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Map;

/**
 * Http请求回调类<br>
 * 
 * <b>创建时间</b> 2014-8-7
 * 
 */
public abstract class HttpCallBack {

    /**
     * 请求开始之前回调
     */
    public void onPreStart() {
    }

    /**
     * Http请求成功时回调
     * 
     * @param t
     *            HttpRequest返回信息
     */
    public void onSuccess(String t) {}

    /**
     * Http请求成功时回调
     * 
     * @param t
     *            HttpRequest返回信息
     */
    public void onSuccess(byte[] t) {
        if (t != null) {
            onSuccess(new String(t));
            Log.d("result","result--->>>"+new String(t));
        }
    }

    /**
     * Http请求成功时回调
     * 
     * @param headers
     *            HttpRespond头
     * @param t
     *            HttpRequest返回信息
     */
    public void onSuccess(Map<String, String> headers, byte[] t) {
        onSuccess(t);
    }

    /**
     * 仅在Bitmap中可用，图片加载完成时回调
     * 
     * @param t
     */
    public void onSuccess(Bitmap t) {}

    /**
     * Http请求失败时回调
     * 
     * @param errorNo
     *            错误码
     * @param strMsg
     *            错误原因
     */
    public void onFailure(int errorNo, String strMsg) {}

    /**
     * Http请求结束后回调
     */
    public void onFinish() {}

    /**
     * 进度回调，仅支持Download时使用
     * 
     * @param count
     *            总数
     * @param current
     *            当前进度
     */
    public void onLoading(long count, long current) {}
}
