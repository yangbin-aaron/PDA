package com.toughegg.teorderpo.mvp.mvpmodle;

import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import java.util.List;

/**
 * Created by toughegg on 15/9/15.
 */
public class ShoppingCartModleImp implements ShoppingCartModleInf {
    @Override
    public List<ShoppingCart> findShoppingCartList() {
        List<ShoppingCart> shoppingCarts = TEOrderPoDataBaseHelp.findShoppingCartList();
        for (int i = 0; i < shoppingCarts.size(); i++) {
            ShoppingCart cart = shoppingCarts.get(i);
            List<Option> labels = TEOrderPoDataBaseHelp.findShopLabelByShopId(cart.getId());
            cart.setOptionList(labels);
            shoppingCarts.set(i, cart);
        }
        return shoppingCarts;
    }

    @Override
    public List<ItemTax> findItemTaxList() {
        return TEOrderPoDataBaseHelp.findItemTax();
    }

    @Override
    public void updateShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart.getCopies() == 0) {
            // 删除
            TEOrderPoDataBaseHelp.deleteShoppingCartById(shoppingCart);
        } else {
            // 修改份数
            TEOrderPoDataBaseHelp.updateShoppingCart(shoppingCart);
        }
    }

    @Override
    public void deleteShoppingCart (ShoppingCart shoppingCart) {
        TEOrderPoDataBaseHelp.deleteShoppingCartById(shoppingCart);
    }
}
