package com.toughegg.teorderpo.activitys.setting;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.andytools.update.UpdateChecker;
import com.toughegg.andytools.update.UpdateVersion;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.BaseActivity;
import com.toughegg.teorderpo.activitys.LoginActivity;
import com.toughegg.teorderpo.mvp.mvppresenter.SettingPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.SettingPresenterInf;
import com.toughegg.teorderpo.mvp.mvpview.ISettingView;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;
import com.toughegg.teorderpo.view.dialogs.MyDialogProgress;
import com.toughegg.teorderpo.view.dialogs.MyDialogTwoBtn;

/**
 * Created by lidan on 15/8/21.
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements OnTopActionBarClickListener, OnClickListener, ISettingView {

    private TEOrderPoApplication mTEOrderPoApplication;
    private UpdateVersion mUpdateVersion;
    private MyTopActionBar myTopActionBar;
    private Button mLoginOutBtn;
    //添加打印机布局,同步数据,更新模板,选择语言,账户信息
    private RelativeLayout mAddPrinterLayout, mSyncInfoLayout, mSyncTemplateLayout, mSelectLangLayout, mUserInfoLayout, mCheckForUpdate;
    private LinearLayout mHaveUpdateLinearLayout;
    private SettingPresenterInf settingPrinterInf;
    private Dialog mLoadingDialog;

    private int mClickWhichBtn; // 点击的哪个按钮
    private static final int SYNC_INFO = 0x01;// 同步数据
    private static final int SYNC_TEMPLATE = 0x02;// 不同模板
    private static final int CHECK_FOR_UPDATE = 0x03; // 检查更新

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss ();
            }
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:// 检测更新成功
                    if (mClickWhichBtn == SYNC_INFO) {
                        Toast.makeText (SettingActivity.this, R.string.activity_setting_syncinfo_success, Toast.LENGTH_SHORT).show ();
                    } else if (mClickWhichBtn == SYNC_TEMPLATE) {
                        Toast.makeText (SettingActivity.this, R.string.activity_setting_synctemplate_success, Toast.LENGTH_SHORT).show ();
                    } else if (mClickWhichBtn == CHECK_FOR_UPDATE) {
                        mTEOrderPoApplication.updateVersion = (UpdateVersion) (msg.obj);
                        mUpdateVersion = (UpdateVersion) (msg.obj);
                        downloadApk ();
                    }
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                    if (mClickWhichBtn == SYNC_INFO) {
                        DialogUtils.createErrorDialog (SettingActivity.this, R.string.activity_setting_syncinfo_fail);
                    } else if (mClickWhichBtn == SYNC_TEMPLATE) {
                        DialogUtils.createErrorDialog (SettingActivity.this, R.string.activity_setting_synctemplate_fail);
                    } else {
                        DialogUtils.createErrorDialog (SettingActivity.this, R.string.app_load_fail);
                    }
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_NET_CONNECTION_FAIL:
                    DialogUtils.createErrorDialog (SettingActivity.this, R.string.app_network_is_not_connected);
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                    DialogUtils.createErrorDialog (SettingActivity.this, R.string.app_service_is_not_connected);
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_NET_ERROR:
                    DialogUtils.createErrorDialog (SettingActivity.this, R.string.app_net_date_error);
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (TEOrderPoConstans.ACTION_CHANGE_LANG)) {
                myTopActionBar.setTitleTextView (R.string.activity_setting_top_bar_title);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_add_printer_tv)).setText (R.string.activity_setting_add_printer);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_sync_info_tv)).setText (R.string.activity_setting_sync_info);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_sync_template_tv)).setText (R.string
                        .activity_setting_sync_template);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_select_lang_tv)).setText (R.string.activity_setting_select_lang);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_userinfo_tv)).setText (R.string.activity_setting_userinfo);
                ((TextView) SettingActivity.this.findViewById (R.id.activity_setting_checkUpdate_tv)).setText (R.string.activity_setting_checkUpdate);
                mLoginOutBtn.setText (R.string.activity_setting_login_out_btn);
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_setting);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplication ();
        mUpdateVersion = mTEOrderPoApplication.updateVersion;
        initTopActionBar ();
        initView ();

        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_CHANGE_LANG);
        registerReceiver (mReceiver, filter);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);
    }

    private void initTopActionBar () {
        myTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        myTopActionBar.setTitleTextView (R.string.activity_setting_top_bar_title);
        myTopActionBar.setLeftImageView (R.drawable.back_btn);
        myTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initView () {
        mAddPrinterLayout = (RelativeLayout) findViewById (R.id.activity_setting_add_printer_layout);
        mAddPrinterLayout.setOnClickListener (this);
        mSyncInfoLayout = (RelativeLayout) findViewById (R.id.activity_setting_sync_info_layout);
        mSyncInfoLayout.setOnClickListener (this);
        mSyncTemplateLayout = (RelativeLayout) findViewById (R.id.activity_setting_sync_template_layout);
        mSyncTemplateLayout.setOnClickListener (this);
        mSelectLangLayout = (RelativeLayout) findViewById (R.id.activity_setting_select_lang_layout);
        mSelectLangLayout.setOnClickListener (this);
        mUserInfoLayout = (RelativeLayout) findViewById (R.id.activity_setting_userinfo_layout);
        mUserInfoLayout.setOnClickListener (this);

        mCheckForUpdate = (RelativeLayout) findViewById (R.id.activity_setting_checkUpdate_layout);
        mCheckForUpdate.setOnClickListener (this);

        mHaveUpdateLinearLayout = (LinearLayout) findViewById (R.id.activity_setting_haveupdate_LL);
        if (mUpdateVersion != null
                && mUpdateVersion.getVersionShort ().compareTo (SystemTool.getAppVersionName (this)) > 0) {
            mHaveUpdateLinearLayout.setVisibility (View.VISIBLE);
        } else {
            mHaveUpdateLinearLayout.setVisibility (View.GONE);
        }

        mLoginOutBtn = (Button) findViewById (R.id.activity_seeting_login_out_btn);
        mLoginOutBtn.setOnClickListener (this);

        settingPrinterInf = new SettingPresenterImp (this);
    }

    @Override
    public void onTopActionBarLeftClicked () {
        finish ();
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onTopActionBarRightClicked () {

    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            //添加打印机
            case R.id.activity_setting_add_printer_layout:// 添加打印机
                startActivity (new Intent (this, AddPrinterActivity.class));
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.activity_setting_sync_info_layout:// 同步数据
                mClickWhichBtn = SYNC_INFO;
                // 同步？？－－同步餐厅数据－－同步菜品数据
                if (SystemTool.checkNet (this)) {
                    mLoadingDialog = DialogUtils.createLoadingDialog (SettingActivity.this, R.string.app_please_wait);
                    mLoadingDialog.show ();
                    settingPrinterInf.syncInfo (this, mHandler);
                } else {
                    Toast.makeText (this, R.string.app_service_is_not_connected, Toast.LENGTH_SHORT).show ();
                }
                break;
            case R.id.activity_setting_sync_template_layout:// 更新模版
                mClickWhichBtn = SYNC_TEMPLATE;
                // 同步？？－－同步餐厅数据－－下载模版
                if (SystemTool.checkNet (this)) {
                    mLoadingDialog = DialogUtils.createLoadingDialog (SettingActivity.this, R.string.app_please_wait);
                    mLoadingDialog.show ();
                    settingPrinterInf.syncTemplate (this, mHandler);
                } else {
                    Toast.makeText (this, R.string.app_service_is_not_connected, Toast.LENGTH_SHORT).show ();
                }
                break;
            case R.id.activity_setting_select_lang_layout:// 语言选择
                startActivity (new Intent (this, SelectLanguageActivity.class));
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.activity_setting_userinfo_layout:// 账户信息
                if (mTEOrderPoApplication.restaurantDetailDataResult == null) {
                    DialogUtils.createErrorDialog (this, R.string.activity_setting_please_sync_info);
                    return;
                }
                startActivity (new Intent (this, UserInfoActivity.class));
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.activity_setting_checkUpdate_layout: // 检查版本更新
                mClickWhichBtn = CHECK_FOR_UPDATE;
                downloadApk ();
                break;
            case R.id.activity_seeting_login_out_btn:// 退出登录
                final MyDialogTwoBtn dialogTwoBtn = new MyDialogTwoBtn (this);
                dialogTwoBtn.setMessage (R.string.app_logout);// 确定退出该用户？
                dialogTwoBtn.setOnDialogClickListener (new MyDialogTwoBtn.OnDialogClickListener () {
                    @Override
                    public void onLeftClicked () {
                        dialogTwoBtn.dismiss ();
                    }

                    @Override
                    public void onRightClicked () {
                        dialogTwoBtn.dismiss ();
                        settingPrinterInf.loginOut (SettingActivity.this);
                    }
                });
                dialogTwoBtn.show ();
                break;
        }
    }

    @Override
    public void notifyLoginOut () {
        toBack ();
        SharedPreferences preference = getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit ();
        editor.putBoolean (TEOrderPoConstans.SP_LOGIN_STATE, false);
        editor.commit ();

        Intent intent = new Intent ();
        intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass (this, LoginActivity.class);
        intent.putExtra (TEOrderPoConstans.KEY_FROM_LOGINACTIVITY_STYLE, TEOrderPoConstans.STYLE_LOGOUT);
        startActivity (intent);
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
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

    /**
     * 下载APK
     */
    private void downloadApk () {
        // 判断版本号，是否有更新
        if (mUpdateVersion != null
                && mUpdateVersion.getVersionShort ().compareTo (SystemTool.getAppVersionName (SettingActivity.this)) > 0) {// 有新版本
            //下载处理；
            final MyDialogTwoBtn myDialogTwoBtn = new MyDialogTwoBtn (this);
            myDialogTwoBtn.setHeight (200);
            String message = getResources ().getString (R.string.activity_userinfo_version) + mUpdateVersion.getVersionShort ()
                    + "\n" + mUpdateVersion.getChangelog ();
            myDialogTwoBtn.setMessageList (message);
            String title = getResources ().getString (R.string.activity_update_app);
            String leftBtn = getResources ().getString (R.string.activity_update_later);// 以后更新
            String rightBtn = getResources ().getString (R.string.activity_update_now);// 马上更新
            myDialogTwoBtn.setTitleLeftRightBtnStr (title, leftBtn, rightBtn);
            myDialogTwoBtn.setOnDialogClickListener (new MyDialogTwoBtn.OnDialogClickListener () {
                @Override
                public void onLeftClicked () {
                    myDialogTwoBtn.dismiss ();
                }

                @Override
                public void onRightClicked () {
                    myDialogTwoBtn.dismiss ();
                    //进行下载
                    UpdateChecker.getInstance ().goToDownload (SettingActivity.this, mUpdateVersion);

                    MyDialogProgress dialogProgress = new MyDialogProgress (SettingActivity.this);
                    dialogProgress.setUpdateVersion (mUpdateVersion);
                    dialogProgress.show ();
                }
            });
            myDialogTwoBtn.show ();
        } else {// 没有新版本
            final MyDialogTwoBtn myDialogTwoBtn = new MyDialogTwoBtn (this);
            myDialogTwoBtn.setTitleLeftRightBtnStr (
                    getResources ().getString (R.string.activity_update_app),
                    getResources ().getString (R.string.app_cancel),
                    getResources ().getString (R.string.activity_refresh));
            myDialogTwoBtn.setMessage (R.string.activity_havenot_newversion);
            myDialogTwoBtn.setOnDialogClickListener (new MyDialogTwoBtn.OnDialogClickListener () {
                @Override
                public void onLeftClicked () {
                    myDialogTwoBtn.dismiss ();
                }

                @Override
                public void onRightClicked () {
                    if (SystemTool.checkNet (SettingActivity.this)) {
                        mLoadingDialog = DialogUtils.createLoadingDialog (SettingActivity.this, R.string.activity_update_app);
                        mLoadingDialog.show ();
                        settingPrinterInf.checkVersion (mHandler);
                    } else {// 没有网络
                        DialogUtils.createErrorDialog (SettingActivity.this, R.string.app_service_is_not_connected);
                    }
                    myDialogTwoBtn.dismiss ();
                }
            });
            myDialogTwoBtn.show ();
        }
    }
}
