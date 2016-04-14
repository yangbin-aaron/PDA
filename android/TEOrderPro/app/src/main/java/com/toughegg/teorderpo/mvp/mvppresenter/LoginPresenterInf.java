package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;

import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;

/**
 * Created by toughegg on 15/8/25.
 */
public interface LoginPresenterInf {
    // 登录接口
    public void login(Context context,UserInfo userInfo);
}
