package com.toughegg.teorderpo.mvp.mvpmodle;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;

import java.util.List;

/**
 * Created by toughegg on 15/9/15.
 */
public interface ShoppingCartModleInf {
    List<ShoppingCart> findShoppingCartList();

    List<ItemTax> findItemTaxList();

    void updateShoppingCart(ShoppingCart shoppingCart);

    public void deleteShoppingCart(ShoppingCart shoppingCart);
}
