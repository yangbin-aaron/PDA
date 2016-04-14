package com.toughegg.teorderpo.activitys;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.andytools.view.pullrefresh.ILoadingLayout;
import com.toughegg.andytools.view.pullrefresh.PullToRefreshBase;
import com.toughegg.andytools.view.pullrefresh.PullToRefreshGridView;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.setting.SettingActivity;
import com.toughegg.teorderpo.adapters.TableGridViewAdapter;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.mvp.mvppresenter.SettingPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.SettingPresenterInf;
import com.toughegg.teorderpo.mvp.mvppresenter.TablePresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.TablePresenterInf;
import com.toughegg.teorderpo.mvp.mvpview.ITableView;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarTitleItemListener;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TableActivity extends BaseActivity implements OnTopActionBarClickListener, OnTopActionBarTitleItemListener, OnClickListener,
        OnItemClickListener, ITableView {

    private TEOrderPoApplication mTEOrderPoApplication;
    // 上面的ACTIONBAR
    private MyTopActionBar mMyTopActionBar;// TopActionBar
    private static String[] mArrayString = null;
    private ArrayAdapter<String> mArrayAdapter;
    private int mSelectSpinnerItemIndex = 0;
    // 下面的按钮(打包，消息，设置)
    private LinearLayout mTakeAwayLinearLayout, mMessageLinearLayout, mSettingLinearLayout;
    private TextView mTableTextView, mTakeAwayTextView, mMessageTextView, mSettingTextView;
    // 中间的餐桌
    private PullToRefreshGridView mTableGridView;
    private TableGridViewAdapter mTableGridViewAdapter;
    private List<TableResultData> mTableResultDataList;

    //presenter
    private TablePresenterInf mITablePresenter;
    private SettingPresenterInf mSettingPresenter;
    private boolean isFirstLoad = true;
    private final static int LOAD_TIME_INTERVAL = 10000;// 获取数据时间间隔

    private boolean isOpenMergeFunction = false;// 是否开放拼桌功能
    private boolean isShowCheckBox = false;// false为访问餐桌，true为选择餐桌
    private LinearLayout mSelectBtnLayout;// 显示拼桌确定按钮
    private TextView mCancelSelectTV, mConfirmSelectTV;
    private List<String> mMergeTableIdList = new ArrayList<> ();// 存放拼桌的餐桌ID

    private String mUnmergeTableId = "";// 存放拆桌的餐桌ID

    private boolean isMergeOrUnmerge;// true为拼桌，false为拆桌

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_TABLELIST_DATA:
                    if (mDialog != null) {
                        mDialog.dismiss ();
                    }
                    if (mTableResultDataList != null) {
                        mTableResultDataList.clear ();
                    }
                    mTableResultDataList = (List<TableResultData>) msg.obj;
                    mIsLoading = false;
                    if (isFirstLoad) {
                        mTableGridViewAdapter = new TableGridViewAdapter (TableActivity.this, mTableResultDataList);
                        mTableGridView.setAdapter (mTableGridViewAdapter);
                        mTableGridView.setOnItemClickListener (TableActivity.this);
//                        mDialog.dismiss ();
                    } else {
                        mTableGridViewAdapter.setTableInfos (mTableResultDataList);
                    }
                    isFirstLoad = false;
                    break;
            }
        }
    };

    /**
     * 定时器，实时获取桌面信息
     */
    private boolean mIsLoading = false;// 正在加载
    private Dialog mDialog = null;
    private Runnable runnable = new Runnable () {
        @Override
        public void run () {
            if (!mIsLoading) {
                if (SystemTool.checkNet (TableActivity.this)) {
                    mITablePresenter.getNetData (TableActivity.this);
                    if (isFirstLoad) {
                        mDialog = DialogUtils.createLoadingDialog (TableActivity.this, R.string.app_please_wait);
                        mDialog.show ();
                    }
                }
                mIsLoading = true;
            }
            mHandler.postDelayed (runnable, LOAD_TIME_INTERVAL);// 10秒钟获取一次数据
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (TEOrderPoConstans.ACTION_CLOSE_APP)) {
                finish ();
            } else if (action.equals (TEOrderPoConstans.ACTION_CHANGE_LANG)) {
                // 重新获取string资源
                reInitRes ();
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplicationContext ();

        mSettingPresenter = new SettingPresenterImp ();// 实例化接口
        mSettingPresenter.syncInfoAndTemplate (this, true);// 同步数据和更新模版一起执行
        mITablePresenter = new TablePresenterImp (this);// 实例化接口

        initTopActionBar ();
        initBottomBar ();
        initView ();
        IntentFilter intentFilter = new IntentFilter ();
        intentFilter.addAction (TEOrderPoConstans.ACTION_CLOSE_APP);// 关闭程序
        intentFilter.addAction (TEOrderPoConstans.ACTION_CHANGE_LANG);// 切换语言
        this.registerReceiver (mReceiver, intentFilter);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        // 开启线程，获取实时数据
        mHandler.post (runnable);

        // 清空购物车数据
        mITablePresenter.deleteShoppingCartData ();

        // 数据归零
        mTEOrderPoApplication.shoppingCartPrice = 0;
        mTEOrderPoApplication.shoppingCartCount = 0;
        mTEOrderPoApplication.shoppingCartList.clear ();
        mTEOrderPoApplication.orderNetResultData = null;
    }

    @Override
    protected void onPause () {
        super.onPause ();
        // 关闭线程
        mHandler.removeCallbacks (runnable);
    }

    @Override
    protected void onStop () {
        super.onStop ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);
    }

    private void initTopActionBar () {
        mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        if (isOpenMergeFunction) {
            mMyTopActionBar.setRightTextView (R.string.main_right_top_str);
        } else {
            mMyTopActionBar.setRightClick (false);
        }
        mMyTopActionBar.setLeftImageView (R.drawable.change_lan_en);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
        //将可选内容与ArrayAdapter连接起来
        initArrayAdapter ();
    }

    private void initBottomBar () {
        mTakeAwayLinearLayout = (LinearLayout) findViewById (R.id.activity_main_takeaway_linearLayout);
        mMessageLinearLayout = (LinearLayout) findViewById (R.id.activity_main_message_linearLayout);
        mSettingLinearLayout = (LinearLayout) findViewById (R.id.activity_main_setting_linearLayout);
        mTakeAwayLinearLayout.setOnClickListener (this);
        mMessageLinearLayout.setOnClickListener (this);
        mSettingLinearLayout.setOnClickListener (this);
    }

    private void initView () {
        mSelectBtnLayout = (LinearLayout) findViewById (R.id.show_select_btn_layout);
        mCancelSelectTV = (TextView) findViewById (R.id.cancel_select_textview);
        mCancelSelectTV.setOnClickListener (this);
        mConfirmSelectTV = (TextView) findViewById (R.id.confirm_select_textview);
        mConfirmSelectTV.setOnClickListener (this);

        mTableTextView = (TextView) findViewById (R.id.activity_main_table_textView);
        mTakeAwayTextView = (TextView) findViewById (R.id.activity_main_takeaway_textView);
        mMessageTextView = (TextView) findViewById (R.id.activity_main_message_textView);
        mSettingTextView = (TextView) findViewById (R.id.activity_main_setting_textView);
        mTableGridView = (PullToRefreshGridView) findViewById (R.id.table_gridView);

        mTableGridView.setOnRefreshListener (new PullToRefreshBase.OnRefreshListener2<GridView> () {
            // 下拉刷新
            @Override
            public void onPullDownToRefresh (PullToRefreshBase<GridView> refreshView) {
                String label = SystemTool.getDataTime (new Date (), TEOrderPoConstans.FORMAT_DATE_YY_MM_DD_HH_MM_SS);
//                String label = DateUtils.formatDateTime (getApplication (), System.currentTimeMillis (), DateUtils.FORMAT_SHOW_TIME
//                        | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy ().setLastUpdatedLabel (label);
                refreshNetData ();
            }

            // 上拉加载更多
            @Override
            public void onPullUpToRefresh (PullToRefreshBase<GridView> refreshView) {
                refreshNetData ();
            }
        });
        initIndicator ();
    }

    // 设置top 提示到文字
    private void initIndicator () {
        ILoadingLayout startLabels = mTableGridView.getLoadingLayoutProxy (true, false);
        startLabels.setPullLabel (getResources ().getString (R.string.refresh_remain));// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel (getResources ().getString (R.string.refresh_start));// 刷新时
        startLabels.setReleaseLabel (getResources ().getString (R.string.refresh_end));// 下来达到一定距离时，显示的提示
    }

    /**
     * 刷新加载网络数据
     */
    private void refreshNetData () {
        if (SystemTool.checkNet (this)) {
            // 加载店铺
            mITablePresenter.getNetData (TableActivity.this);
        } else {
            mTableGridView.onRefreshComplete ();
            // 弹出请链接网络dialog
        }
    }


    @Override
    public void onTopActionBarLeftClicked () {
        String langs = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_LAN);
        String[] langList = langs.split (",");
        String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (langList[0])) {
            lang = langList[1];
        } else {
            lang = langList[0];
        }
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN, lang);
        SystemTool.switchLanguage (getApplication (), lang);

        // 重新获取string资源
        reInitRes ();
    }

    @Override
    public void onTopActionBarRightClicked () {
        isMergeOrUnmerge = true;
        if (!isShowCheckBox) {
            isShowCheckBox = true;
            showShowCheckBox ();
        }
    }

    /**
     * 按条件查找餐桌信息
     *
     * @param position
     */
    @Override
    public void onTopActionBarTitleItemSelected (int position) {
        mSelectSpinnerItemIndex = position;
        if (!isFirstLoad) {
            mITablePresenter.showLocalTableView (position, mHandler);
        }
        mArrayAdapter.notifyDataSetChanged ();
    }

    private void initArrayAdapter () {
        mArrayString = new String[]{getResources ().getString (R.string.spinner_str_0), getResources ().getString (R.string.spinner_str_1),
                getResources ().getString (R.string.spinner_str_2), getResources ().getString (R.string.spinner_str_3)};
        mArrayAdapter = new ArrayAdapter<String> (this, R.layout.adapter_mytopactionbar_spinner, mArrayString) {
            @Override
            public View getDropDownView (int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater ().inflate (R.layout.adapter_mytopactionbar_spinner_item, parent, false);
                }
                TextView textView = (TextView) convertView.findViewById (R.id.spinner_textView);
                textView.setText (getItem (position));
                if (mSelectSpinnerItemIndex == position) {
                    convertView.setBackgroundColor (TableActivity.this.getResources ().getColor (R.color.color_blue_deep));
                    textView.setTextColor (TableActivity.this.getResources ().getColor (R.color.color_white));
                } else {
                    convertView.setBackgroundColor (TableActivity.this.getResources ().getColor (R.color.color_white));
                    textView.setTextColor (TableActivity.this.getResources ().getColor (R.color.color_blue_deep));
                }
                return convertView;
            }
        };
        mMyTopActionBar.setTitleSpinnerAdapter (mArrayAdapter, mSelectSpinnerItemIndex);
        mMyTopActionBar.setOnTopActionBarTitleItemListener (this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.activity_main_takeaway_linearLayout:// 打包
                Intent intent = new Intent (TableActivity.this, OrderActivity.class);
                SharePrefenceUtils.write (TableActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_ORDER_TYPE,
                        TEOrderPoConstans.ORDER_TYPE_TAKEAWAY);
                startActivity (intent);//
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.activity_main_message_linearLayout:// 暂时用作拆桌
//                isMergeOrUnmerge = false;
//                if (!isShowCheckBox) {
//                    isShowCheckBox = true;
//                    showShowCheckBox ();
//                }
                break;
            case R.id.activity_main_setting_linearLayout:
                startActivity (new Intent (this, SettingActivity.class));
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.cancel_select_textview:
                isShowCheckBox = false;
                mMergeTableIdList.clear ();// 清空之后就不会merge了
                mUnmergeTableId = "";
                showShowCheckBox ();
                break;
            case R.id.confirm_select_textview:
                isShowCheckBox = false;
                showShowCheckBox ();
                break;
        }
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        if (isShowCheckBox) {
            TableResultData tableResultData = mTableResultDataList.get (position);
            if (isMergeOrUnmerge) {// 拼桌
                if (tableResultData.getTableList () == null || tableResultData.getTableList ().size () == 0) {
                    if (tableResultData.isSelected ()) {
                        tableResultData.setIsSelected (false);
                        for (int i = 0; i < mMergeTableIdList.size (); i++) {
                            if (mMergeTableIdList.get (i).equals (tableResultData.getId ())) {
                                mMergeTableIdList.remove (i);
                                break;
                            }
                        }
                    } else {
                        tableResultData.setIsSelected (true);
                        mMergeTableIdList.add (tableResultData.getId ());
                    }
                    mTableResultDataList.set (position, tableResultData);
                }
            } else {// 拆桌
                if (tableResultData.getTableList () != null && tableResultData.getTableList ().size () > 0) {
                    if (tableResultData.getTableList ().get (0).getId ().equals (tableResultData.getId ())) {// 主桌才能被选择
                        if (!tableResultData.isSelected ()) {
                            // 选择
                            for (int i = 0; i < mTableResultDataList.size (); i++) {
                                TableResultData data = mTableResultDataList.get (i);
                                if (data.getId ().equals (tableResultData.getId ())) {
                                    data.setIsSelected (true);
                                } else {
                                    data.setIsSelected (false);
                                }
                                mTableResultDataList.set (i, data);
                            }
                            mUnmergeTableId = tableResultData.getId ();
                        }
                    }
                }
            }
            mTableGridViewAdapter.setTableInfos (mTableResultDataList);
        } else {
            mTableGridViewAdapter.setSelectIndex (position);
            Log.e ("aaron", mTableResultDataList.get (position).toString ());
            if (SystemTool.checkNet (this)) {
                if (!mTableResultDataList.get (position).isOccupied () && mTableResultDataList.get (position).getCustomerNum () == 0) {// 未被占用
                    Intent intent = new Intent (TableActivity.this, SelectPeopleActivity.class);
                    Bundle bundle = new Bundle ();
                    bundle.putSerializable ("table", mTableResultDataList.get (position));
                    intent.putExtras (bundle);
                    startActivity (intent);
                } else {// 被占用
                    SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_ORDER_TYPE, TEOrderPoConstans
                            .ORDER_TYPE_HOUSE);
                    Intent intent = new Intent (TableActivity.this, OrderNetDetailsActivity.class);
                    Bundle bundle = new Bundle ();
                    bundle.putSerializable (TEOrderPoConstans.KEY_TABLE_INFO, mTableResultDataList.get (position));
                    intent.putExtras (bundle);
                    startActivity (intent);
                    overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                }
            } else {
                DialogUtils.createErrorDialog (this, R.string.app_network_is_not_connected);
            }
        }
    }

    @Override
    public void notifyUpdateTableListData (Message msg) {
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                mITablePresenter.showLocalTableView (mSelectSpinnerItemIndex, mHandler);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                Toast.makeText (this, R.string.app_load_fail, Toast.LENGTH_SHORT).show ();
                mITablePresenter.showLocalTableView (mSelectSpinnerItemIndex, mHandler);
                mDialog.dismiss ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_NET_CONNECTION_FAIL:
                Toast.makeText (this, R.string.app_network_is_not_connected, Toast.LENGTH_SHORT).show ();
                mITablePresenter.showLocalTableView (mSelectSpinnerItemIndex, mHandler);
                mDialog.dismiss ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                Toast.makeText (this, R.string.app_service_is_not_connected, Toast.LENGTH_SHORT).show ();
                mDialog.dismiss ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_NET_ERROR:
                Toast.makeText (this, R.string.app_net_date_error, Toast.LENGTH_SHORT).show ();
                mDialog.dismiss ();
                break;
        }
    }

    @Override
    public void getDatefinish () {
        mTableGridView.onRefreshComplete ();
    }

    /**
     * 退出应用（在后台运行）
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowCheckBox) {
                isShowCheckBox = false;
                mMergeTableIdList.clear ();// 清空之后就不会merge了
                mUnmergeTableId = "";
                showShowCheckBox ();
            } else {
                Intent intent = new Intent (Intent.ACTION_MAIN);
                intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
                intent.addCategory (Intent.CATEGORY_HOME);
                this.startActivity (intent);
            }
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    private void reInitRes () {
        initArrayAdapter ();
        mTableTextView.setText (R.string.main_button_str_table);
        mTakeAwayTextView.setText (R.string.main_button_str_takeaway);
        mMessageTextView.setText (R.string.main_button_str_message);
        mSettingTextView.setText (R.string.main_button_str_setting);
        if (isOpenMergeFunction) {
            mMyTopActionBar.setRightTextView (R.string.main_right_top_str);
        } else {
            mMyTopActionBar.setRightClick (false);
        }
        mCancelSelectTV.setText (R.string.app_cancel);
        mConfirmSelectTV.setText (R.string.app_confirm);
        initIndicator ();// 切换下拉刷新的语言
    }

    private void showShowCheckBox () {
        if (isShowCheckBox) {
            mTableGridViewAdapter.setIsShowCheckBox (isShowCheckBox, isMergeOrUnmerge);
            mSelectBtnLayout.setVisibility (View.VISIBLE);
            mHandler.removeCallbacks (runnable);// 保证数据不更新
            mMergeTableIdList.clear ();
            mUnmergeTableId = "";
        } else {
            Log.e ("aaron", mMergeTableIdList.toString ());

            for (TableResultData tableResultData : mTableResultDataList) {
                tableResultData.setIsSelected (false);// 恢复数据，如果立即点拼桌按钮，数据会更新不及时
            }
            mTableGridViewAdapter.setTableInfos (mTableResultDataList);

            boolean flag = false;
            if (isMergeOrUnmerge) {
                if (mMergeTableIdList.size () > 1) {// 拼桌
                    flag = true;
                    mDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
                    mDialog.show ();
                    mITablePresenter.merge (this, mMergeTableIdList);
                } //else {// 取消拼桌
            } else {
                if (mUnmergeTableId != null && !mUnmergeTableId.equals ("")) {
                    flag = true;
                    mDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
                    mDialog.show ();
                    mITablePresenter.unMerge (this, mUnmergeTableId);
                }
            }
            // 开启线程，获取实时数据
            if (flag) {// 为true时，merge或者unmerge后都会返回一次数据，防止数据重复，所以延时操作
                mHandler.postDelayed (runnable, LOAD_TIME_INTERVAL);
            } else {// 为false时，实则是取消操作，所以不需要延时更新数据
                mHandler.post (runnable);
            }
            mTableGridViewAdapter.setIsShowCheckBox (isShowCheckBox, isMergeOrUnmerge);
            mSelectBtnLayout.setVisibility (View.GONE);
        }
    }
}
