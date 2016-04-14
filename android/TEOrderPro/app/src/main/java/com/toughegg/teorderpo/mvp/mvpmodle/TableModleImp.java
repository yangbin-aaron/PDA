package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.network.TEProNetWorkService;

import java.util.List;

//import com.toughegg.teorderpo.network.TENetworkService;

/**
 * Created by Andy on 15/8/10.
 * tableModle 具体逻辑数据处理
 */

public class TableModleImp implements TableModleInf {
    OnFinishedListener onFinishedListener;
    public Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            //HANDLER_WHAT_TABLE_SUCCESS
            // HANDLER_WHAT_TABLE_FAIL
            // HANDLER_WHAT_NET_CONNECTION_FAIL
            // 处理完网络数据发送消息，通过findItems方法取出数据库中的数据，放到onFinishedListener接口中
            onFinishedListener.onFinished (msg);
        }
    };

    @Override
    public void getNetData (Context context, OnFinishedListener onFinishedListener) {
        //获取网络数据 餐桌列表（解析出来后已进行数据的存储）
        this.onFinishedListener = onFinishedListener;
        TEProNetWorkService.getInstance ().getTableListing (context, mHandler);
    }

    @Override
    public List<TableResultData> findTableInfoListBySpinnerIndex (int spinnerIndex) {
        List<TableResultData> tableResultDatas = TEOrderPoDataBaseHelp.getInstants ().findTableInfoListBySpinner (spinnerIndex);
        for (TableResultData data : tableResultDatas) {
            data.setTableList (TEOrderPoDataBaseHelp.getInstants ().findTableTableList (data.getId ()));
        }
        return tableResultDatas;
    }

    @Override
    public void deleteShoppingCartData () {
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME);
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME);
    }

    @Override
    public void merge (Context context, List<String> tableIds) {
        TEProNetWorkService.getInstance ().mergeTable (context, tableIds, mHandler);
    }

    @Override
    public void unMerge (Context context, String tableId) {
        TEProNetWorkService.getInstance ().unMergeTable (context, tableId, mHandler);
    }
}
