package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

/**
 * Created by lidan on 15/8/28.
 */
public interface SettingPresenterInf {
    void loginOut (Context context);

    void syncInfoAndTemplate (Context context, boolean isTableSync);// 同步数据和更新模版

    void syncInfo (Context context, Handler handler);//同步数据

    void syncTemplate (Context context, Handler handler);// 更新模版

    void checkVersion (Handler handler);// 检测新版本
}
