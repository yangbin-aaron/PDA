package com.toughegg.teorderpo.mvp.mvppresenter;

import android.os.Handler;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

/**
 * Created by lidan on 15/8/14.
 * 菜品标签  Presenter接口
 */
public interface DishDetailsPresenterInf {

    void showDishDetails (String modifierString, Handler handler);

    void saveShoppingCart (ShoppingCart shoppingCart, Handler handler);

    void updateShoppingCart (ShoppingCart shoppingCart, Handler handler);
}
