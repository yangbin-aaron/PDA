package com.toughegg.teorderpo.asynserver;

import android.os.AsyncTask;

/**
 * Created by Andy on 15/9/7.
 */
public class TEAsyncTask<T> extends AsyncTask<String, Void, T> {
    private ITEAsyncTaskCallBack<T> mITETiteAsyncTaskCallBack;

    public TEAsyncTask(ITEAsyncTaskCallBack<T> mITETiteAsyncTaskCallBack) {
        this.mITETiteAsyncTaskCallBack = mITETiteAsyncTaskCallBack;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected T doInBackground(String... params) {
        return mITETiteAsyncTaskCallBack.doInBackground();
//        return T;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        mITETiteAsyncTaskCallBack.onPostExecute(t);
    }
}
