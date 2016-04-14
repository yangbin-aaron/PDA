package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;

import com.toughegg.teorderpo.modle.entry.LocalSession;
import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;
import com.toughegg.teorderpo.mvp.OnFinishedListener;

/**
 * Created by toughegg on 15/8/25.
 */
public interface LoginModleInf {
    public void login (Context context, OnFinishedListener onFinishedListener,UserInfo userInfo);
}
