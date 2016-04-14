package com.toughegg.teorderpo.mvp.mvpview;

import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;

import java.util.List;

/**
 * Created by toughegg on 15/9/25.
 */
public interface IOrderNetDetailView {
    void orderIsHave(Object object);

    void orderLoad(Object object,List<ItemTax> itemTaxes);
}
