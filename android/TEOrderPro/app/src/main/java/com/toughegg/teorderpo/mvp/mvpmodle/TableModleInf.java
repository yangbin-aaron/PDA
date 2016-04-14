package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;

import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.mvp.OnFinishedListener;

import java.util.List;

/**
 * Created by Andy on 15/8/10.
 * Table Modle 接口
 */
public interface TableModleInf {
    // 获取网络数据
    void getNetData (Context context, OnFinishedListener onFinishedListener);

    // 根据餐桌状态查找餐桌信息
    List<TableResultData> findTableInfoListBySpinnerIndex (int spinnerIndex);

    void deleteShoppingCartData ();

    void merge (Context context, List<String> tableIds);

    void unMerge (Context context, String tableId);
}

