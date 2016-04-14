package com.toughegg.teorderpo.activitys.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.AppUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.BaseActivity;
import com.toughegg.teorderpo.modle.entry.restaurantdetail.RestaurantDetailDataResult;
import com.toughegg.teorderpo.view.MyTopActionBar;

/**
 * Created by toughegg on 16/3/8.
 */
public class UserInfoActivity extends BaseActivity implements MyTopActionBar.OnTopActionBarClickListener {

    private TEOrderPoApplication mTEOrderPoApplication;

    private MyTopActionBar mMyTopActionBar;
    private TextView mRestNameTV, mRestAdressTV, mRestTimeTV, mRestUserIdTV, mServiceIpTV, mVersionTextView;

    private RestaurantDetailDataResult mDataResult;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplication ();
        mDataResult = mTEOrderPoApplication.restaurantDetailDataResult;
        setContentView (R.layout.activity_userinfo);
        initTopActionBar ();
        initView ();
    }

    private void initTopActionBar () {
        mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.activity_setting_userinfo);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initView () {
        mRestNameTV = (TextView) findViewById (R.id.rest_name_textview);
        mRestAdressTV = (TextView) findViewById (R.id.rest_address_textview);
        String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            mRestNameTV.setText (mDataResult.getName ().getZh_CN ());
            mRestAdressTV.setText (mDataResult.getAddress ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            mRestNameTV.setText (mDataResult.getName ().getEn_US ());
            mRestAdressTV.setText (mDataResult.getAddress ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            mRestNameTV.setText (mDataResult.getName ().getEn_US ());
            mRestAdressTV.setText (mDataResult.getAddress ().getEn_US ());
        }

        mRestTimeTV = (TextView) findViewById (R.id.rest_time_textview);
        mRestTimeTV.setText (mDataResult.getSetting ().getOpeningTime () + "-" + mDataResult.getSetting ().getClosingTime ());

        mRestUserIdTV = (TextView) findViewById (R.id.rest_userid_textview);
        String userId = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_EMPLOYEE_ID);
        mRestUserIdTV.setText (userId);

        mServiceIpTV = (TextView) findViewById (R.id.service_ip_textview);
        String ip = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, "");
        mServiceIpTV.setText (ip);

        mVersionTextView = (TextView) findViewById (R.id.app_version_textview);
        String version = AppUtils.getAppVersionName (this);
        mVersionTextView.setText (version);
    }

    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {

    }

    /**
     * 返回按钮
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toBack ();
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    private void toBack () {
        finish ();
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
