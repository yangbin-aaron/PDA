package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tough egg on 15/8/11.
 */
public interface OrderModleInf {
    HashMap<String, Object> findAllData(Context context);

    void saveShoppingCartList(List<ShoppingCart> shoppingCarts);

    void deleteShoppingCartData();
}
