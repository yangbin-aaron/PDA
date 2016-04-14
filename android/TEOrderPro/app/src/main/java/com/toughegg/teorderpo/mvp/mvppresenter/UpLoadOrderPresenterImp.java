package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.mvp.mvpmodle.UpLoadOrderModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.UpLoadOrderModleInf;
import com.toughegg.teorderpo.mvp.mvpview.IUpLoadDataView;

/**
 * Created by Andy on 15/9/19.
 */
public class UpLoadOrderPresenterImp implements UpLoadOrderPresenterInf, OnFinishedListener {
    private UpLoadOrderModleInf mUpLoadOrderModleInf;
    private IUpLoadDataView mIUpLoadDataView;


    public UpLoadOrderPresenterImp (IUpLoadDataView mIUpLoadDataView) {
        this.mIUpLoadDataView = mIUpLoadDataView;
        mUpLoadOrderModleInf = new UpLoadOrderModleImp ();
    }

    @Override
    public void orderNewData (Context context, String orderDetail) {
        mUpLoadOrderModleInf.orderNewData (context, orderDetail, this);
    }

    @Override
    public void printSuccess (Context context, String orderNo, Handler handler) {
        mUpLoadOrderModleInf.printSuccess (context, orderNo, handler);
    }

    @Override
    public void updateOrder (Context context, String orderDetail) {
        mUpLoadOrderModleInf.updateOrder (context, orderDetail, this);
    }

    @Override
    public void onFinished (Object object) {
        Message message = (Message) object;
        if (message.what == TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS) {
            mUpLoadOrderModleInf.deleteShoppingCart ();
        }
        mIUpLoadDataView.getFinishResult (message);
    }
}
