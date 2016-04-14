package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

import java.util.List;

/**
 * Created by Andy on 15/8/10.
 * TablePresenter接口
 */
public interface TablePresenterInf {
    // 根据餐桌状态从本地查找数据
    void showLocalTableView (int spinnerIndex, Handler handler);

    // 根据餐桌状态从本地查找数据(先从网络获取数据)
    void getNetData (Context context);

    // 清除购物车数据
    void deleteShoppingCartData ();

    void merge (Context context, List<String> tableIds);

    void unMerge (Context context, String tableId);
}
