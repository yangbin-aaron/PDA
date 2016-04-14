package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;

/**
 * Created by lidan on 15/8/28.
 */
public interface SettingModleInf {
    void loginOut (Context context);

    void syncInfoAndTemplate (Context context, boolean isTableSync);// 同步数据和更新模版

    void syncInfo (Context context, Handler ActHandler);//同步数据

    void syncTemplate (Context context, Handler ActHandler);// 更新模版

    void checkVersion(Handler handler);
}
