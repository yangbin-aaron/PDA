package com.toughegg.teorderpo.mvp.mvppresenter;

import android.content.Context;
import android.os.Message;

import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;
import com.toughegg.teorderpo.mvp.OnFinishedListener;
import com.toughegg.teorderpo.mvp.mvpmodle.LoginModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.LoginModleInf;
import com.toughegg.teorderpo.mvp.mvpview.ILoginView;

/**
 * Created by toughegg on 15/8/25.
 */
public class LoginPresenterImp implements LoginPresenterInf,OnFinishedListener {

    private LoginModleInf mLoginModleModle;
    private ILoginView mILoginView;

    public LoginPresenterImp(ILoginView iLoginView){
        mILoginView = iLoginView;
        mLoginModleModle = new LoginModleImp ();
    }


    @Override
    public void login (Context context, UserInfo userInfo) {
        mLoginModleModle.login (context,this,userInfo);
    }
    @Override
    public void onFinished (Object object) {
        mILoginView.setLoginState ((Message)object);
    }
}
