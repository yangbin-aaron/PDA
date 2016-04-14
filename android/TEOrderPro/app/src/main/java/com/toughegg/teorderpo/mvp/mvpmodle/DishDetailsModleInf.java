package com.toughegg.teorderpo.mvp.mvpmodle;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

import java.util.HashMap;

/**
 * Created by lidan on 15/8/14.
 * 菜品详情 Model 接口
 */
public interface DishDetailsModleInf {
    HashMap<String, Object> findDishOptionAndLabelList (String modifierString);//获取网络数据

    void saveShoppingCart (ShoppingCart shoppingCart);

    void updateShoppingCart (ShoppingCart shoppingCart);
}
