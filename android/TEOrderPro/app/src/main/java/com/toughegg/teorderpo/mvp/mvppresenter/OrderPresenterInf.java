package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

import java.util.List;

/**
 * Created by toughegg on 15/8/11.
 * 点菜
 */
public interface OrderPresenterInf {
    void showDishAndCategoryView (Context context, Handler handler);

    void saveShoppingCartList (List<ShoppingCart> shoppingCarts, Handler handler);

    void deleteShoppingCartData (Handler handler);
}
