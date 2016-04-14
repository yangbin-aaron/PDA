package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Andy on 15/9/19.
 */
public interface UpLoadOrderPresenterInf {

    void updateOrder (Context context, String orderDetail);

    void orderNewData (Context context, String orderDetail);

    void printSuccess (Context context, String orderNo, Handler handler);
}
