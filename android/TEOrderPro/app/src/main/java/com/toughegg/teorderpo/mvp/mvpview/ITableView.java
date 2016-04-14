package com.toughegg.teorderpo.mvp.mvpview;

import android.os.Message;

/**
 * Created by Andy on 15/8/10.
 * 定义table 显示接口
 */
public interface ITableView {

    void notifyUpdateTableListData (Message msg);

    void getDatefinish ();  //获取数据结束接口

//    void sendRestaurantDetail (Message msg);
}
