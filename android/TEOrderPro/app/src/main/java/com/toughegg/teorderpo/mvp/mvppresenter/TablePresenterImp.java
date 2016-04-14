package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.mvp.mvpmodle.TableModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.TableModleInf;
import com.toughegg.teorderpo.mvp.mvpview.ITableView;

import java.util.List;

/**
 * Created by Andy on 15/8/10.
 * 关联管理 view 和 modle 数据间传输
 */
public class TablePresenterImp implements TablePresenterInf, OnFinishedListener {
    private TableModleInf mITableModle;
    private ITableView mITableView;

    public TablePresenterImp (ITableView tableView) {
        this.mITableView = tableView;
        mITableModle = new TableModleImp ();
    }

    @Override
    public void showLocalTableView (final int spinnerIndex, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                List<TableResultData> tableResultDataList = mITableModle.findTableInfoListBySpinnerIndex (spinnerIndex);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_TABLELIST_DATA, tableResultDataList).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void getNetData (Context context) {
        mITableModle.getNetData (context, this);
    }

    @Override
    public void onFinished (Object object) {
        Message message = (Message) object;
        mITableView.notifyUpdateTableListData (message);
        mITableView.getDatefinish ();
    }

    @Override
    public void deleteShoppingCartData () {
        mITableModle.deleteShoppingCartData ();
    }

    @Override
    public void merge (Context context, List<String> tableIds) {
        mITableModle.merge (context, tableIds);
    }

    @Override
    public void unMerge (Context context, String tableId) {
        mITableModle.unMerge (context, tableId);
    }
}

