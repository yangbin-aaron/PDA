package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Handler;

import com.toughegg.teorderpo.mvp.mvpmodle.SettingModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.SettingModleInf;
import com.toughegg.teorderpo.mvp.mvpview.ISettingView;

/**
 * Created by lidan on 15/8/28.
 */
public class SettingPresenterImp implements SettingPresenterInf {
    private ISettingView iSettingView;
    private SettingModleInf settingModleInf;

    public SettingPresenterImp () {
        settingModleInf = new SettingModleImp ();
    }

    public SettingPresenterImp (ISettingView iSettingView) {
        this.iSettingView = iSettingView;
        settingModleInf = new SettingModleImp ();
    }

    @Override
    public void loginOut (Context context) {
        settingModleInf.loginOut (context);
        iSettingView.notifyLoginOut ();
    }

    @Override
    public void syncInfoAndTemplate (Context context, boolean isTableSync) {
        settingModleInf.syncInfoAndTemplate (context, isTableSync);
    }

    /**
     * @param context
     * @param ActHandler Activity传过来的Handler
     */
    @Override
    public void syncInfo (Context context, Handler ActHandler) {
        settingModleInf.syncInfo (context, ActHandler);
    }

    /**
     * @param context
     * @param ActHandler Activity传过来的Handler
     */
    @Override
    public void syncTemplate (Context context, Handler ActHandler) {
        settingModleInf.syncTemplate (context, ActHandler);
    }

    @Override
    public void checkVersion (Handler handler) {
        settingModleInf.checkVersion (handler);
    }
}
