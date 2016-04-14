package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;

import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.mvp.mvpmodle.OrderNetDetailModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.OrderNetDetailModleInf;
import com.toughegg.teorderpo.mvp.mvpview.IOrderNetDetailView;

import java.util.List;

/**
 * Created by toughegg on 15/9/25.
 */
public class OrderNetDetailPresenterImp implements OrderNetDetailPresenterInf, OnFinishedListener {

    private IOrderNetDetailView mIOrderNetDetailView;
    private OrderNetDetailModleInf mOrderNetDetailModleImf;

    public OrderNetDetailPresenterImp (IOrderNetDetailView IOrderNetDetailView) {
        mIOrderNetDetailView = IOrderNetDetailView;
        mOrderNetDetailModleImf = new OrderNetDetailModleImp ();
    }

    private boolean flag = false;// ture为判断订单false为获取订单

    @Override
    public void getOrderNetData (Context context, String orderNO) {
        flag = false;
        mOrderNetDetailModleImf.getNetData (context, this, orderNO);
    }

    @Override
    public void orderIsHave (Context context, String orderNO) {
        flag = true;
        mOrderNetDetailModleImf.getNetData (context, this, orderNO);
    }

    @Override
    public void onFinished (Object object) {
        if (flag) {
            mIOrderNetDetailView.orderIsHave (object);
        } else {
            List<ItemTax> itemTaxes = mOrderNetDetailModleImf.findItemTaxList ();
            mIOrderNetDetailView.orderLoad (object, itemTaxes);
        }
    }
}
