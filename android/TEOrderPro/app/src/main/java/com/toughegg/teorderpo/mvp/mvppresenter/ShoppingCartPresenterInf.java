package com.toughegg.teorderpo.mvp.mvppresenter;

import android.os.Handler;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

/**
 * Created by toughegg on 15/9/15.
 */
public interface ShoppingCartPresenterInf {
    void showShoppingCartList (Handler handler);

    void updateShoppingCartCopies (ShoppingCart shoppingCart);

    void deleteShoppingCart (ShoppingCart shoppingCart);
}
