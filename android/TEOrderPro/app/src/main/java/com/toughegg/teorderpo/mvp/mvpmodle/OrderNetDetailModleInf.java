package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;

import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.mvp.OnFinishedListener;

import java.util.List;

/**
 * Created by toughegg on 15/9/25.
 */
public interface OrderNetDetailModleInf {
    void getNetData (Context context, OnFinishedListener onFinishedListener, String orderNo);

    List<ItemTax> findItemTaxList ();
}
