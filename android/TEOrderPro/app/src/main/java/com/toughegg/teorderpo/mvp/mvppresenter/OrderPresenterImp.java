package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.mvp.mvpmodle.OrderModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.OrderModleInf;

import java.util.HashMap;
import java.util.List;

/**
 * Created by toughegg on 15/8/11.
 * 点菜界面
 */
public class OrderPresenterImp implements OrderPresenterInf, OnFinishedListener {
    private OrderModleInf mIOrderModle;

    public OrderPresenterImp () {
        mIOrderModle = new OrderModleImp ();
    }

    @Override
    public void showDishAndCategoryView (final Context context, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                HashMap<String, Object> hashMap = mIOrderModle.findAllData (context);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_ORDER_DISHLIST, hashMap).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void saveShoppingCartList (final List<ShoppingCart> shoppingCarts, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mIOrderModle.saveShoppingCartList (shoppingCarts);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_ORDER_SAVE_OK).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void deleteShoppingCartData (final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mIOrderModle.deleteShoppingCartData ();
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_ORDER_DELETE_OK).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void onFinished (Object object) {
    }
}
