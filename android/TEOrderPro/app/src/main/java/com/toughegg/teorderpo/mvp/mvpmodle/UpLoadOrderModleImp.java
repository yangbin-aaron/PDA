package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.network.TEProNetWorkService;

/**
 * Created by Andy on 15/9/19.
 */
public class UpLoadOrderModleImp implements UpLoadOrderModleInf {

    OnFinishedListener onFinishedListener;
    public Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            // 处理完网络数据发送消息，通过findItems方法取出数据库中的数据，放到onFinishedListener接口中
            onFinishedListener.onFinished (msg);
        }
    };

    @Override
    public void orderNewData (Context context, String orderDetail, OnFinishedListener
            onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
        TEProNetWorkService.getInstance ().orderNew (context, orderDetail, mHandler);
    }

    @Override
    public void updateOrder (Context context, String orderDetail, OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
        TEProNetWorkService.getInstance ().updateOrder (context, orderDetail, mHandler);
    }

    @Override
    public void deleteShoppingCart () {
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME);
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME);
    }

    @Override
    public void printSuccess (Context context, String orderNo, Handler handler) {
        TEProNetWorkService.getInstance ().orderPrinted (context, orderNo, handler);
    }
}
