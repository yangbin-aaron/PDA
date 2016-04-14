package com.toughegg.andytools.netUtil;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by Andy on 15/11/3.\
 *  加载网络链接接口
 */
public interface IAndyAsyncTackCallBack <T>{
    /**
     * 预加载
     */
    public void preLoad ();

    /**
     * 加载数据
     *
     * @param url
     * @param pairs
     * @return
     * @return
     */
    public T doload (String url, List<NameValuePair> pairs);

    /**
     * 读取进度
     *
     * @param value
     */
    public void progressUpdate (Void[] value);

    /**
     * 加载完成返回结果更新view
     *
     * @param result
     */
    public void updateView (T result);
}
