package com.toughegg.andytools.netUtil;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Objects;

/**
 * Created by Andy on 15/11/3.
 *
 * 异步网络加载
 */
public class AndyAsyncTask<T> extends AsyncTask<Objects,Void ,T>{
    private Context mContext;

    private IAndyAsyncTackCallBack <T> mIAndyAsyncTackCallBack;

    private HttpPost mHttpPost;

    private List<NameValuePair> pairs;

    private String url=null;

    public AndyAsyncTask(HttpPost mHttpPost, List<NameValuePair> pairs,IAndyAsyncTackCallBack <T> mIAndyAsyncTackCallBack){
        this.mHttpPost=mHttpPost;
        this.pairs=pairs;
        this.mIAndyAsyncTackCallBack=mIAndyAsyncTackCallBack;
    }

    public AndyAsyncTask(Context mContext,String url,List<NameValuePair> pairs,IAndyAsyncTackCallBack <T> mIAndyAsyncTackCallBack){
        this.mContext=mContext;
        this.url=url;
        this.pairs=pairs;
        this.mIAndyAsyncTackCallBack=mIAndyAsyncTackCallBack;
    }

    @Override
    protected void onPreExecute() {
        mIAndyAsyncTackCallBack.preLoad();
    }

    @Override
    protected T doInBackground(Objects... params) {

        mIAndyAsyncTackCallBack.doload(url,pairs);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        mIAndyAsyncTackCallBack.progressUpdate(values);
    }

    @Override
    protected void onPostExecute(T result) {
        mIAndyAsyncTackCallBack.updateView(result);
    }
}
