package com.toughegg.teorderpo.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.update.UpdateVersion;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.network.TEProNetWorkService;

/**
 * Created by toughegg on 15/11/24.
 */
public class WelcomeActivity extends Activity {

    Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);

            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                    final UpdateVersion updateVersion = (UpdateVersion) (msg.obj);// 将版本信息保存到全局变量
                    TEOrderPoApplication application = (TEOrderPoApplication) WelcomeActivity.this.getApplicationContext ();
                    application.updateVersion = updateVersion;
                    break;
            }
            gotoMainView ();
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_welcome);
        // 设置字体
        TextView textView = (TextView) findViewById (R.id.activity_welcome_textview);
        Typeface typeface = Typeface.createFromAsset (getAssets (), "font/FZYBKSJW.TTF");
        textView.setTypeface (typeface);

        mCountDownTimer.start ();
    }

    // checkout https://api.fir.im/apps/latest/com.toughegg.teorderpo?api_token=e103f9d18df32d73dec4335cf8b5d610&type=android
    // 计时器
    private CountDownTimer mCountDownTimer = new CountDownTimer (2000, 2000) {
        @Override
        public void onTick (long millisUntilFinished) {

        }

        @Override
        public void onFinish () {
            TEProNetWorkService.getInstance ().checkUpdateVersion (mHandler);
//            gotoMainView ();
        }
    };

    @Override
    protected void onDestroy () {
        mCountDownTimer.cancel ();
        super.onDestroy ();
    }

    public void gotoMainView () {
        finish ();
        String str = SharePrefenceUtils.readString (WelcomeActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans
                .SP_LOGIN_RESTAURANT_USER_ID);
        if (str == null || str.equals ("")) {
            // 跳到登陆界面
            startActivity (new Intent (WelcomeActivity.this, LoginActivity.class));
        } else {
            // 餐桌界面
            startActivity (new Intent (WelcomeActivity.this, TableActivity.class));
        }
    }
}
