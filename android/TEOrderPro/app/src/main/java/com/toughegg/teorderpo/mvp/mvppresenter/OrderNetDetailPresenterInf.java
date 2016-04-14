package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;

/**
 * Created by toughegg on 15/9/25.
 */
public interface OrderNetDetailPresenterInf {

    public void getOrderNetData(Context context,String orderNO);

    void orderIsHave(Context context,String orderNO);
}
