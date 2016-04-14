package com.toughegg.teorderpo.mvp.mvppresenter;

import android.os.Handler;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.mvp.mvpmodle.ShoppingCartModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.ShoppingCartModleInf;

import java.util.HashMap;

/**
 * Created by toughegg on 15/9/15.
 */
public class ShoppingCartPresenterImp implements ShoppingCartPresenterInf {

    private ShoppingCartModleInf mShoppingCartModle;

    public ShoppingCartPresenterImp () {
        mShoppingCartModle = new ShoppingCartModleImp ();
    }

    @Override
    public void showShoppingCartList (final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                HashMap<String, Object> hashMap = new HashMap<String, Object> ();
                hashMap.put ("shoppingcartlist", mShoppingCartModle.findShoppingCartList ());
                hashMap.put ("itemtaxlist", mShoppingCartModle.findItemTaxList ());
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SHOPPINGCARTLIST, hashMap).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void updateShoppingCartCopies (final ShoppingCart shoppingCart) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mShoppingCartModle.updateShoppingCart (shoppingCart);
            }
        }.start ();
    }

    @Override
    public void deleteShoppingCart (final ShoppingCart shoppingCart) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mShoppingCartModle.deleteShoppingCart (shoppingCart);
            }
        }.start ();
    }
}
