package com.toughegg.teorderpo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.AppUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;
import com.toughegg.teorderpo.mvp.mvppresenter.LoginPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.LoginPresenterInf;
import com.toughegg.teorderpo.mvp.mvpview.ILoginView;
import com.toughegg.teorderpo.utils.MyUtil;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * Created by lidan on 15/8/21.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener, ILoginView {

    private LinearLayout mSetIpLayout;
    private EditText mSetIpEditText;
//    private TextView mSetIpTextView;

    private EditText mMerchantId, mUserId, mPassword;

    private LoginPresenterInf mLoginPresenter;

    private boolean bool;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        LuaState mLuaState = LuaStateFactory.newLuaState();
        mLuaState.openLibs();
        // 解决APK安装成功后点击"打开"再按Home键遇到的问题 判断本Activity是不是在栈底
        int mFromActivity = getIntent().getIntExtra(TEOrderPoConstans.KEY_FROM_LOGINACTIVITY_STYLE, 0);
        if (!isTaskRoot ()) {
            if (mFromActivity == TEOrderPoConstans.STYLE_DEFAULT) {
                finish ();
                return;
            }
        }

        mLoginPresenter = new LoginPresenterImp (this);
        setContentView (R.layout.activity_login);
        initTopActionBar ();
        initView ();

        // 开发时测试用
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_STATE, true);
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, "192.168.5.125");
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, "52.77.225.139");
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, "192.168.5.20");
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_REST_CODE, "0012");
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_EMPLOYEE_ID, "101");
//        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_PASSWORD, "123456");

        String ip = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, "");
        if (ip.equals ("") || ip.length () == 0) {
            mSetIpLayout.setVisibility (View.VISIBLE);
        } else {
            mSetIpLayout.setVisibility (View.GONE);
            mSetIpEditText.setText (ip);
        }

        bool = SharePrefenceUtils.readBoolean (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans
                .SP_LOGIN_STATE);
        if (bool) {
            // 改造UserInfo
            UserInfo userInfo = new UserInfo ();

            userInfo.setRestCode (SharePrefenceUtils.readString (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME,
                    TEOrderPoConstans.SP_LOGIN_REST_CODE));
            userInfo.setEmployeeId (SharePrefenceUtils.readString (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME,
                    TEOrderPoConstans.SP_LOGIN_EMPLOYEE_ID));
            userInfo.setPassword (SharePrefenceUtils.readString (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME,
                    TEOrderPoConstans.SP_LOGIN_PASSWORD));

            mMerchantId.setText (userInfo.getRestCode ());
            mUserId.setText (userInfo.getEmployeeId ());
            mPassword.setText (userInfo.getPassword ());
            mPassword.setSelection (userInfo.getPassword ().length ());

            mLoginPresenter.login (LoginActivity.this, userInfo);
        }
    }

    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById(R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView(R.string.activity_login_top_bar_title);
    }

    private void initView () {
        // 设置IP
        TextView mToSetIpTextView = (TextView) findViewById(R.id.activity_login_tosetip_textview);
        mToSetIpTextView.setOnClickListener(this);
        mSetIpLayout = (LinearLayout) findViewById (R.id.activity_login_setip_layout);
        mSetIpLayout.setOnClickListener (this);
        mSetIpEditText = (EditText) findViewById (R.id.activity_login_setip_edittext);
        mSetIpEditText.setInputType (EditorInfo.TYPE_CLASS_PHONE);
//        mSetIpTextView = (TextView) findViewById (R.id.activity_login_setip_textview);
//        mSetIpTextView.setOnClickListener (this);

        mMerchantId = (EditText) findViewById (R.id.activity_login_merchant_id);
        mMerchantId.addTextChangedListener (mMerchantWatcher);
        mUserId = (EditText) findViewById (R.id.activity_login_user_id);
        mUserId.addTextChangedListener (mUserWatcher);
        mPassword = (EditText) findViewById (R.id.activity_login_password);
        mPassword.addTextChangedListener (mPasswordWatcher);
        TextView mLoginTextView = (TextView) findViewById(R.id.activity_login_login_btn);
        mLoginTextView.setOnClickListener(this);
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_login_layout);
        mRelativeLayout.setOnClickListener(this);

        TextView mVersionTextView = (TextView) findViewById(R.id.activity_login_version_text);
        String version = AppUtils.getAppVersionName (this);
        mVersionTextView.setText(R.string.activity_login_login_version_str);
        mVersionTextView.append(version + "");
    }

    private TextWatcher mMerchantWatcher = new TextWatcher () {

        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            if (s.length () == 4) {
                mUserId.requestFocus ();
            }
        }

        @Override
        public void afterTextChanged (Editable s) {

        }
    };

    private TextWatcher mUserWatcher = new TextWatcher () {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            if (s.length () == 3) {
                mPassword.requestFocus ();
            }
        }

        @Override
        public void afterTextChanged (Editable s) {

        }
    };

    private TextWatcher mPasswordWatcher = new TextWatcher () {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            if (s.length () == 6) {
                SystemTool.hideKeyBoard (LoginActivity.this);
            }
        }

        @Override
        public void afterTextChanged (Editable s) {

        }
    };

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.activity_login_login_btn:// 登录
                // 获取输入信息
                String merchantId = mMerchantId.getText().toString();
                String userId = mUserId.getText().toString();
                String password = mPassword.getText().toString();
                // 网络
                if (!SystemTool.checkNet (this)) {// 网络无连接
                    DialogUtils.createErrorDialog (this, R.string.app_network_is_not_connected);
                    return;
                }
                // 非空判断
                if (merchantId.equals("")) {// 商户ID不能为空
                    DialogUtils.createErrorDialog (this, R.string.activity_login_merchantid_cannot_be_empty);
                    return;
                }
                if (userId.equals("")) {// 用户ID不能为空
                    DialogUtils.createErrorDialog (this, R.string.activity_login_userid_cannot_be_empty);
                    return;
                }
                if (password.equals("")) {// 密码不能为空
                    DialogUtils.createErrorDialog (this, R.string.activity_login_password_cannot_be_empty);
                    return;
                }
                if (merchantId.length() != 4) {// 商户ID必须为4位数字
                    DialogUtils.createErrorDialog (this, R.string.activity_login_merchantid_must_digits);
                    return;
                }
                if (userId.length() != 3) {// 用户ID必须为3位数字
                    DialogUtils.createErrorDialog (this, R.string.activity_login_userid_must_digits);
                    return;
                }
                if (password.length() != 6) {// 密码必须为6位数字
                    DialogUtils.createErrorDialog (this, R.string.activity_login_password_must_digits);
                    return;
                }
                // 判断输入信息的格式等

                // 改造UserInfo
                UserInfo userInfo = new UserInfo ();
                userInfo.setRestCode (merchantId);
                userInfo.setEmployeeId (userId);
                userInfo.setPassword (password);
                mLoginPresenter.login (LoginActivity.this, userInfo);
                break;
            case R.id.activity_login_layout:// 隐藏键盘
                SystemTool.hideKeyBoard (this);
                break;
            case R.id.activity_login_setip_layout:// 请设置IP地址
//            case R.id.activity_login_setip_textview:// 确认设置IP
                String ip = mSetIpEditText.getText ().toString ();
                if (ip.equals ("") || ip.length () == 0) {
                    DialogUtils.createErrorDialog (this, R.string.activity_login_setip_null);
                }else{
                    // 将IP存入
                    SharePrefenceUtils.write (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_IP, ip);
                    mSetIpLayout.setVisibility (View.GONE);
                }
                break;
            case R.id.activity_login_tosetip_textview:
                mSetIpLayout.setVisibility (View.VISIBLE);
                break;
        }
    }

    @Override
    public void setLoginState (Message msg) {
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:// 登录成功
                if (!bool) {
                    SharePrefenceUtils.write (LoginActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_STATE, true);
                }
                SharePrefenceUtils.write (LoginActivity.this,TEOrderPoConstans.SHAREPREFERENCE_NAME,
                        TEOrderPoConstans.SP_LOGIN_REST_CODE,mMerchantId.getText ().toString ());
                SharePrefenceUtils.write (LoginActivity.this,TEOrderPoConstans.SHAREPREFERENCE_NAME,
                        TEOrderPoConstans.SP_LOGIN_PASSWORD,mPassword.getText ().toString ());
                startActivity (new Intent (this, TableActivity.class));
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                finish ();
                break;
            default:
                MyUtil.handlerSendMessage (this,msg);
                break;
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 发送广播关闭应用
            sendBroadcast (new Intent (TEOrderPoConstans.ACTION_CLOSE_APP));
            finish ();
        }
        return super.onKeyDown (keyCode, event);
    }
}
