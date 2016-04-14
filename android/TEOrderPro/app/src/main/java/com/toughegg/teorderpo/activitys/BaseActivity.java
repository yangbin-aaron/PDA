package com.toughegg.teorderpo.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by toughegg on 15/8/6.
 * My BaseActivity
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        requestWindowFeature (Window.FEATURE_NO_TITLE);
//        getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 常亮
        getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate (savedInstanceState);
    }
}
