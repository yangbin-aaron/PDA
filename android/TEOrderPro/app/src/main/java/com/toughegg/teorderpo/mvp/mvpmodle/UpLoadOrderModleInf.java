package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;

import com.toughegg.teorderpo.mvp.OnFinishedListener;

/**
 * Created by Andy on 15/9/19.
 */
public interface UpLoadOrderModleInf {
    void orderNewData(Context context, String orderDetail, OnFinishedListener onFinishedListener);

    void updateOrder(Context context, String orderDetail, OnFinishedListener onFinishedListener);

    void deleteShoppingCart();

    void printSuccess(Context context,String orderNo,Handler handler);
}
