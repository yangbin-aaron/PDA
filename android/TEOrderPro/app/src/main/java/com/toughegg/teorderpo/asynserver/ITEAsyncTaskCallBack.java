package com.toughegg.teorderpo.asynserver;

/**
 * Created by Andy on 15/9/7.
 */
public interface ITEAsyncTaskCallBack<T> {
    /**
     *  prepare
     */
	public void onPreExecute();

    /**
     * 加载数据
     * @return
     * @return
     */
    public T doInBackground(String... params);

    /**
     * 读取进度
     *
     * @param value
     */
	public void progressUpdate(Void[] value);

    /**
     * 加载完成返回结果更新view
     *
     * @param result
     */
    public void onPostExecute(T result);
}
