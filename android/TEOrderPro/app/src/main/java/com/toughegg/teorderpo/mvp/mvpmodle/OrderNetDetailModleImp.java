package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.network.TEProNetWorkService;

import java.util.List;

/**
 * Created by toughegg on 15/9/25.
 */
public class OrderNetDetailModleImp implements OrderNetDetailModleInf {

    OnFinishedListener onFinishedListener;

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            onFinishedListener.onFinished (msg);
        }
    };

    @Override
    public void getNetData (Context context, OnFinishedListener onFinishedListener, String orderNo) {
        this.onFinishedListener = onFinishedListener;
        TEProNetWorkService.getInstance ().getOrderNetDetatil (context, orderNo, mHandler);
    }

    @Override
    public List<ItemTax> findItemTaxList () {
        return TEOrderPoDataBaseHelp.findItemTax ();
    }
}
