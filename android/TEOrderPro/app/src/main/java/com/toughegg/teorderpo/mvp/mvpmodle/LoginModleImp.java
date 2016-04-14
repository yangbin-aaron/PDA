package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.network.TEProNetWorkService;

/**
 * Created by toughegg on 15/8/25.
 */
public class LoginModleImp implements LoginModleInf {
    OnFinishedListener onFinishedListener;

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            onFinishedListener.onFinished (msg);
        }
    };

    @Override
    public void login (Context context, OnFinishedListener onFinishedListener, UserInfo userInfo) {
        this.onFinishedListener = onFinishedListener;
        TEProNetWorkService.getInstance().login (context, userInfo,mHandler);
    }
}
