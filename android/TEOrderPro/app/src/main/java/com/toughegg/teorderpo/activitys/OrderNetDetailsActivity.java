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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData.Payment;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.modle.entry.uploadOrder.Item;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderDetail;
import com.toughegg.teorderpo.mvp.mvppresenter.OrderNetDetailPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.OrderNetDetailPresenterInf;
import com.toughegg.teorderpo.mvp.mvpview.IOrderNetDetailView;
import com.toughegg.teorderpo.utils.LuaUtils;
import com.toughegg.teorderpo.utils.MyUtil;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;
import com.toughegg.teorderpo.view.dialogs.MyDialogOneBtn;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 从网络上获取订单详情
 * Created by toughegg on 15/9/25.
 */
public class OrderNetDetailsActivity extends BaseActivity implements IOrderNetDetailView, MyTopActionBar.OnTopActionBarClickListener, View
        .OnClickListener {

    private TEOrderPoApplication mTEOrderPoApplication;
    private TableResultData mTableResultData;
    private OrderNetDetailPresenterInf mOrderNetDetailPresenterImp;

    // ********** TopActionBar **********
    private MyTopActionBar mMyTopActionBar;
    // ********** 中间 **********
//    private TextView mOrderNoTextView;// 订单编号
    private TextView mTableNameTextView;// 餐桌
    private TextView mPeopleCountTextView;// 人数
    private TextView mSubTotalTextView;// 菜品合计
    private TextView mDiscTextView, mDiscShowTextView;// 折扣
    private TextView mConsumptionTaxTextView, mConsumptionTaxShowTextView;// 消费税
    private TextView mServiceTaxTextView, mServiceTaxShowTextView;// 服务税
    private TextView mRoundTextView;// round
    private TextView mTotalTextView;// 总计
    private TextView mPaidTextView;// 应付金额
    private LinearLayout mOrderDishListLayout;// 菜单列表

    // 修改订单和打印订单的按钮
    private TextView mModifyOrderTextView, mPrintOrderTextView;

    private LuaState mLuaState;
    private LuaUtils mLuaUtils;

    private Dialog mLoadingDialog;
    private boolean mIsCanBack = false;// 是否能返回，在访问网络（订单是否改变）的时候不能返回

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            if (intent.getAction ().equals (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY)) {
                finish ();
            }
        }
    };

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (msg.what == LuaUtils.HANDLER_LUA_SUCCESS) {
                String mOrderDetailStr = (String) msg.obj;
                Log.e ("aaron", "11>>>>>>>>>>>>>\n\n" + mOrderDetailStr + "\n\n11>>>>>>>>>>>>>");
                OrderDetail orderDetailLua = new Gson ().fromJson (mOrderDetailStr, OrderDetail.class);
                // 价格
                mSubTotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderDetailLua.getBasePrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 合计
                String discount = orderDetailLua.getOrderDiscount ();// 整单折扣率
                String discountAmt = orderDetailLua.getDiscountAmount ();// 整单折扣金额
                if (discount == null || discount.equals ("")) {
                    discount = "0";
                }
                if (Double.parseDouble (discount) == 0) {// 如果折扣率为0，则显示为0
                    mDiscShowTextView.setText (
                            "(" + StringUtils.getPriceString (
                                    Double.parseDouble (discount) * 100, TEOrderPoConstans.PRICE_FORMAT_TWO) + "%)");
                } else {// 不为0时，在前面加上－号
                    mDiscShowTextView.setText (
                            "(-" + StringUtils.getPriceString (
                                    Double.parseDouble (discount) * 100, TEOrderPoConstans.PRICE_FORMAT_TWO) + "%)");
                }
                if (Double.parseDouble (discountAmt) == 0) {// 如果折扣金额为0，则显示为0
                    mDiscTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                            + StringUtils.getPriceString (
                            Double.parseDouble (discountAmt), TEOrderPoConstans.PRICE_FORMAT_TWO));
                } else {// 不为0时，在前面加上－号
                    mDiscTextView.setText (TEOrderPoConstans.DOLLAR_SIGN + "-"
                            + StringUtils.getPriceString (
                            Double.parseDouble (discountAmt), TEOrderPoConstans.PRICE_FORMAT_TWO));
                }
                mConsumptionTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderDetailLua.getTotalGst ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 国家税
                mServiceTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderDetailLua.getTotalServiceTax ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 服务税
                mRoundTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderDetailLua.getRound ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
                mTotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderDetailLua.getPayPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 总计
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mTableResultData = (TableResultData) getIntent ().getSerializableExtra (TEOrderPoConstans.KEY_TABLE_INFO);
        setContentView (R.layout.activity_order_net_detail);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplication ();

        mLuaState = LuaStateFactory.newLuaState ();
        mLuaState.openLibs ();
        mLuaUtils = new LuaUtils (mLuaState, this, mHandler);

        initTopActionBar ();
        initView ();

        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY);
        registerReceiver (mReceiver, filter);

        mOrderNetDetailPresenterImp = new OrderNetDetailPresenterImp (this);
    }

    @Override
    protected void onDestroy () {
        unregisterReceiver (mReceiver);
        super.onDestroy ();
    }

    private void initTopActionBar () {
        mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
//        mMyTopActionBar.setTitleTextView (mTableResultData.getCode ());
        mMyTopActionBar.setTitleTextView (R.string.actionbar_order_detail_title_str);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
        mMyTopActionBar.setRightClick (false);
    }

    private void initView () {
//        mOrderNoTextView = (TextView) findViewById (R.id.activity_order_no_textview);
        mTableNameTextView = (TextView) findViewById (R.id.activity_order_net_detatil_tablename_textView);
        mTableNameTextView.setText (mTableResultData.getCode ());
        mPeopleCountTextView = (TextView) findViewById (R.id.activity_order_net_detatil_peoplecount_textView);
        mPeopleCountTextView.setText (mTableResultData.getCustomerNum () + "");

        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_NUMBER, mTableResultData
                .getCode ());
        //保存当前选择餐厅 Table id
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_ID, mTableResultData
                .getId ());
        // 保存选择的人数
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_PEOPLE, mTableResultData.getCustomerNum
                ());

        mSubTotalTextView = (TextView) findViewById (R.id.activity_order_net_detail_subtotal_textView);
        mDiscTextView = (TextView) findViewById (R.id.activity_order_net_detail_discountamount_textView);
        mDiscShowTextView = (TextView) findViewById (R.id.activity_order_net_detail_discountamountshow_textView);
        mConsumptionTaxTextView = (TextView) findViewById (R.id.activity_order_net_detail_consumptiontax_textView);
        mConsumptionTaxShowTextView = (TextView) findViewById (R.id.activity_order_net_detail_gstshow_textView);
        mServiceTaxTextView = (TextView) findViewById (R.id.activity_order_net_detail_servicetax_textView);
        mServiceTaxShowTextView = (TextView) findViewById (R.id.activity_order_net_detail_servicetaxshow_textView);
        mRoundTextView = (TextView) findViewById (R.id.activity_order_net_detail_round_textView);
        mTotalTextView = (TextView) findViewById (R.id.activity_order_net_detail_total_textView);
        mPaidTextView = (TextView) findViewById (R.id.activity_order_net_detail_total_str_textView);
        mOrderDishListLayout = (LinearLayout) findViewById (R.id.activity_order_net_detail_list_layout);

        mModifyOrderTextView = (TextView) findViewById (R.id.activity_order_net_detail_modify_textView);
        if (mTableResultData.getPaymentStatus () == 0) {
            mModifyOrderTextView.setOnClickListener (this);
        } else {
            mModifyOrderTextView.setVisibility (View.GONE);
            mPaidTextView.setText (R.string.order_net_detail_str_amt);
        }
        mPrintOrderTextView = (TextView) findViewById (R.id.activity_order_net_detail_reprint_textView);
        mPrintOrderTextView.setOnClickListener (this);
    }

    private OrderNetResultData mOrderNetResultData;

    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {
        // －－－－－－－－－
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.activity_order_net_detail_modify_textView:
                // 判断该订单是否失效
                if (mIsCanBack) {
                    if (SystemTool.checkNet (this)) {
                        mIsCanBack = false;
                        mOrderNetDetailPresenterImp.orderIsHave (OrderNetDetailsActivity.this, mTableResultData.getOrderNo ());
                    } else {
                        DialogUtils.createErrorDialog (this, R.string.app_network_is_not_connected);
                    }
                }
                break;
            case R.id.activity_order_net_detail_reprint_textView:
                // 此处为重新打印订单的按钮点击事件
                break;
        }
    }

    @Override
    public void orderLoad (Object object, List<ItemTax> itemTaxes) {
        mLoadingDialog.dismiss ();
        mIsCanBack = true;
        Message msg = (Message) object;
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                setOrderData (msg.obj, itemTaxes);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                loadFail ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                MyDialogOneBtn myDialogOneBtn = new MyDialogOneBtn (this);
                myDialogOneBtn.setMessage (R.string.app_service_is_not_connected);
                myDialogOneBtn.setOnDialogClickListener (new MyDialogOneBtn.OnDialogClickListener () {
                    @Override
                    public void onConfirmClicked () {
                        toBack ();
                    }
                });
                myDialogOneBtn.show ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_NET_ERROR:
                DialogUtils.createErrorDialog (this, R.string.app_net_date_error);
                break;
        }
    }

    @Override
    public void orderIsHave (Object object) {
        mIsCanBack = true;
        Message msg = (Message) object;
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                isHaveOrder (msg.obj);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                loadFail ();
                break;
            default:
                MyUtil.handlerSendMessage (this, msg);
                break;
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
        mIsCanBack = false;// 不能返回
        mLoadingDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
        mLoadingDialog.show ();
        mOrderNetDetailPresenterImp.getOrderNetData (this, mTableResultData.getOrderNo ());
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
        if (mIsCanBack) {
            finish ();
            overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

    private void setOrderData (Object object, List<ItemTax> itemTaxes) {
        Log.e ("aaron", "=========================\n" + itemTaxes.toString ());
        mOrderNetResultData = (OrderNetResultData) object;
        OrderDetail orderDetail = new OrderDetail ();
        // ====================设置数据====================
        // 订单编号
//        mOrderNoTextView.setText (mTableResultData.getOrderNo ());
        // 菜单列表
        List<OrderNetResultDataItem> itemList = mOrderNetResultData.getItem ();
        mOrderDishListLayout.removeAllViews ();
        List<Item> tempItemList = new ArrayList<> ();
        if (itemList != null && itemList.size () > 0) {
            for (int i = 0; i < itemList.size (); i++) {
                OrderNetResultDataItem dataItem = itemList.get (i);
                mOrderDishListLayout.addView (MyUtil.createOrderNetItem (this, dataItem));

                Item item = new Item ();
                item.setItemTax (itemTaxes);// 用本地的
                item.setAction (dataItem.getAction ());
                item.setName (dataItem.getName ());
                item.setAddTaxAmount (dataItem.getAddTaxAmount ());
                item.setAllowCustomDiscount (dataItem.isAllowCustomDiscount ());
                item.setBasePrice (dataItem.getBasePrice ());
                item.setCategoryId (dataItem.getCategoryId ());
                item.setCode (dataItem.getCode ());
                item.setCount (dataItem.getCount ());
                item.setDiscountAmount (dataItem.getDiscountAmount ());
                item.setDishAction (dataItem.getDishAction ());
                item.setGroupNumber (dataItem.getGroupNumber ());
                item.setIsDeleted (dataItem.isDeleted ());
                item.setItemDiscount (dataItem.getItemDiscount ());
                item.setItemPrice (dataItem.getItemPrice ());
                item.setMenuItemId (dataItem.getMenuItemId ());
                item.setMenuPrice (dataItem.getMenuPrice ());
                item.setModifierAmount (dataItem.getModifierAmount ());
                item.setOrderItemId (dataItem.getOrderItemId ());
                item.setPriceAdjustment (dataItem.getPriceAdjustment ());
                if (dataItem.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
                    dataItem.setRemark ("");
                }
                item.setRemark (dataItem.getRemark ());
                item.setModifier (dataItem.getModifier ());
                item.setModifierList (dataItem.getModifierList ());
                item.setSetNeedsPrint (dataItem.isSetNeedsPrint ());
                tempItemList.add (item);
            }
        }
        setOrderDetailUpdateOrder (orderDetail, mOrderNetResultData);

        orderDetail.setItem (tempItemList);

        // 通过lua计算返回json数据
        Gson gson = new Gson ();
        String json = gson.toJson (orderDetail);
        Log.e ("aaron", ">>>>>>>>>>>>>\n\n" + json + "\n\n>>>>>>>>>>>>>");
        mLuaUtils.executeLuaCalculate (json);
    }

    private void setOrderDetailUpdateOrder (OrderDetail orderDetail, OrderNetResultData data) {
        orderDetail.setAddress (data.getAddress ());
        orderDetail.setAddTat (data.getAddTax ());
        orderDetail.setBasePrice (data.getBasePrice ());
        orderDetail.setCancelledBy (data.getCancelledBy ());
        orderDetail.setStatus (data.getStatus ());
        orderDetail.setIsPaid (0);// ----------
        orderDetail.setUpdatedStamp (data.getUpdatedStamp ());
        orderDetail.setCustomerNum (data.getCustomerNum ());
        orderDetail.setContactName (data.getContactName ());
        orderDetail.setContactNum (data.getContactNum ());
        orderDetail.setCreatedStamp (data.getCreatedStamp ());
        orderDetail.setDiningDataTime (data.getDiningDataTime ());
        orderDetail.setDiscountAmount (data.getDiscountAmount ());
        orderDetail.setDiscountPrice (data.getDiscountPrice ());
        orderDetail.setIsReview (data.getIsReview ());
        orderDetail.setTableCode (data.getTableCode ());
        orderDetail.setTableId (data.getTableId ());
        orderDetail.setRemark (data.getRemark ());
        orderDetail.setOrderId (data.getOrderId ());
        orderDetail.setOrderDiscount (data.getOrderDiscount ());
//        orderDetail.setTotalGst (data.getTotalGst ());
//        orderDetail.setTotalServiceTax (data.getTotalServiceTax ());
//        orderDetail.setRound (data.getRound ());
        orderDetail.setPayPrice (data.getPayPrice ());
        orderDetail.setOrderNo (data.getOrderNo ());
        orderDetail.setType (data.getType ());
        orderDetail.setRestaurantId (data.getRestaurantId ());
        Payment payment = data.getPayment ();
        if (mTableResultData.getPaymentStatus () == 0) {// 未支付，不显示Round
            payment.setPaymentMethod (0);// Lua只有在现金支付时有Round
        }
        Log.e ("aaron", "payment>>>>>" + payment.toString ());
        orderDetail.setPayment (payment);
    }

    private void loadFail () {
        final MyDialogOneBtn myDialogOneBtn = new MyDialogOneBtn (this);
        myDialogOneBtn.setMessage (R.string.app_load_fail);
        myDialogOneBtn.setOnDialogClickListener (new MyDialogOneBtn.OnDialogClickListener () {
            @Override
            public void onConfirmClicked () {
                toBack ();
                myDialogOneBtn.dismiss ();
            }
        });
        myDialogOneBtn.show ();
    }

    private void isHaveOrder (Object object) {
        boolean isCanModify = false;// 能否修改
        if (object != null) {
            OrderNetResultData data = (OrderNetResultData) object;
            if (data.getTableId () != null && !data.getTableId ().equals ("")) {
                isCanModify = true;
            }
        }

        if (isCanModify) {
            Intent intent = new Intent (OrderNetDetailsActivity.this, OrderActivity.class);
            // 将网络菜单存到全局变量，因为他将由多个界面改变数据
//            mTEOrderPoApplication.orderNetResultData = mOrderNetResultData;
            startActivity (intent);
            overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
        } else {// 订单已经失效
            DialogUtils.createErrorDialog (OrderNetDetailsActivity.this, R.string.order_net_detail_str_order_nvalid);
        }
    }
}