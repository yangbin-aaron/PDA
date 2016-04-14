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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderLuaDetail;
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
    private RelativeLayout mTipsRelativeLayout;
    private TextView mTipsTextView;// 小费
    private TextView mTotalTextView;// 总计
    private TextView mPaidTextView;// 应付金额
    private LinearLayout mOrderDishListLayout;// 菜单列表

    // 修改订单和打印订单的按钮
    private TextView mModifyOrderTextView, mPrintOrderTextView;

    private LuaState mLuaState;
    private LuaUtils mLuaUtils;

    private Dialog mLoadingDialog;
//    private boolean mIsCanBack = false;// 是否能返回，在访问网络（订单是否改变）的时候不能返回

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
                Log.e ("aaron1", "11>>>>>>>>>>>>>\n" + mOrderDetailStr + "\n11>>>>>>>>>>>>>");
                OrderLuaDetail orderLuaDetail = new Gson ().fromJson (mOrderDetailStr, OrderLuaDetail.class);
                // 价格
                mSubTotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderLuaDetail.getBasePrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 合计
                String discount = orderLuaDetail.getOrderDiscount ();// 整单折扣率
                String discountAmt = orderLuaDetail.getDiscountAmount ();// 整单折扣金额
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
                        Double.parseDouble (orderLuaDetail.getTotalGst ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 国家税
                mServiceTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderLuaDetail.getTotalService ()), TEOrderPoConstans.PRICE_FORMAT_TWO));// 服务税
                mRoundTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        Double.parseDouble (orderLuaDetail.getRound ()), TEOrderPoConstans.PRICE_FORMAT_TWO));

                double payPrice = Double.parseDouble (orderLuaDetail.getPayPrice ());
                if (mTableResultData.getPaymentStatus () == 0) {
                    mModifyOrderTextView.setOnClickListener (OrderNetDetailsActivity.this);
                } else {// 已经付款
                    double tips = Double.parseDouble (orderLuaDetail.getTips ());
                    payPrice = payPrice + tips;// 将小费加入
                    mTipsRelativeLayout.setVisibility (View.VISIBLE);
                    mTipsTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                            + StringUtils.getPriceString (
                            Double.parseDouble (orderLuaDetail.getTips ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
                    mModifyOrderTextView.setVisibility (View.GONE);
                    mPaidTextView.setText (R.string.order_net_detail_str_amt);
                }
                mTotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (payPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));// 总计
            }
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss ();
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

//        mIsCanBack = false;// 不能返回
        mLoadingDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
        mLoadingDialog.show ();
        mOrderNetDetailPresenterImp.getOrderNetData (this, mTableResultData.getOrderNo ());
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
        mTipsRelativeLayout = (RelativeLayout) findViewById (R.id.tips_layout);
        mTipsTextView = (TextView) findViewById (R.id.activity_order_net_detail_tips_textView);
        mTotalTextView = (TextView) findViewById (R.id.activity_order_net_detail_total_textView);
        mPaidTextView = (TextView) findViewById (R.id.activity_order_net_detail_total_str_textView);
        mOrderDishListLayout = (LinearLayout) findViewById (R.id.activity_order_net_detail_list_layout);

        mModifyOrderTextView = (TextView) findViewById (R.id.activity_order_net_detail_modify_textView);
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
//                if (mIsCanBack) {
                if (SystemTool.checkNet (this)) {
//                        mIsCanBack = false;
                    mLoadingDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
                    mLoadingDialog.show ();
                    mOrderNetDetailPresenterImp.orderIsHave (OrderNetDetailsActivity.this, mTableResultData.getOrderNo ());
                } else {
                    DialogUtils.createErrorDialog (this, R.string.app_network_is_not_connected);
                }
//                }
                break;
            case R.id.activity_order_net_detail_reprint_textView:
                // 此处为重新打印订单的按钮点击事件
                break;
        }
    }

    @Override
    public void orderLoad (Object object, List<ItemTax> itemTaxes) {
//        mIsCanBack = true;
        Message msg = (Message) object;
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                setOrderData (msg.obj, itemTaxes);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                mLoadingDialog.dismiss ();
                loadFail ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                mLoadingDialog.dismiss ();
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
                mLoadingDialog.dismiss ();
                DialogUtils.createErrorDialog (this, R.string.app_net_date_error);
                break;
        }
    }

    @Override
    public void orderIsHave (Object object) {
        mLoadingDialog.dismiss ();
//        mIsCanBack = true;
        Message msg = (Message) object;
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                isHaveOrder (msg.obj);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                loadFail ();
                break;
            case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                DialogUtils.createErrorDialog (this, R.string.app_service_is_not_connected);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_NET_ERROR:
                DialogUtils.createErrorDialog (this, R.string.app_net_date_error);
                break;
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
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
//        if (mIsCanBack) {
        finish ();
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
//        }
    }

    private void setOrderData (Object object, List<ItemTax> itemTaxes1) {
        Log.e ("aaron", "============itemTaxes1=============\n" + itemTaxes1.toString ());
        List<ItemTax> itemTaxes = new ArrayList<> ();
        for (ItemTax itemTax : itemTaxes1) {// 暂时－－－－－－－－－－－－－－－
            itemTax.setEffectOrderTypes ("1");
            itemTaxes.add (itemTax);
        }
        Log.e ("aaron", "============itemTaxes=============\n" + itemTaxes.toString ());
        mOrderNetResultData = (OrderNetResultData) object;
        OrderLuaDetail orderLuaDetail = new OrderLuaDetail ();
        // ====================设置数据====================
        // 订单编号
//        mOrderNoTextView.setText (mTableResultData.getOrderNo ());
        // 菜单列表
        List<OrderNetResultDataItem> itemList = mOrderNetResultData.getItem ();

        mOrderDishListLayout.removeAllViews ();
        List<OrderNetResultDataItem> tempItemList = new ArrayList<> ();
        for (int i = 0; i < itemList.size (); i++) {
            OrderNetResultDataItem dataItem = itemList.get (i);
            mOrderDishListLayout.addView (MyUtil.createOrderNetItem (this, dataItem));
            dataItem.setItemTax (itemTaxes);
//            Log.e ("aaron", ">>>>>>>item>>>>>>" + dataItem.toString ());
            tempItemList.add (dataItem);
        }

        orderLuaDetail.setItem (tempItemList);
        setOrderDetailUpdateOrder (orderLuaDetail, mOrderNetResultData);

        // 通过lua计算返回json数据
        String json = new Gson ().toJson (orderLuaDetail);
        Log.e ("aaron", ">>>>>>>>>>>>>\n" + json + "\n>>>>>>>>>>>>>");
        mLuaUtils.executeLuaCalculate (json);
    }

    private void setOrderDetailUpdateOrder (OrderLuaDetail orderLuaDetail, OrderNetResultData data) {
        orderLuaDetail.setId (data.getId ());// 默认0，来自AWS的订单id
        orderLuaDetail.setAddTax (data.getAddTax ());// 默认为"",总税额，sum(item.addTaxAmount * count)
        orderLuaDetail.setAddress (data.getAddress ());// 默认为"",联系人地址
        orderLuaDetail.setBasePrice (data.getBasePrice ());// 默认为"",原始价格，所有item的 basePrice * count 的总和
        orderLuaDetail.setCancelRemark (data.getCancelRemark ());// 取消说明
        orderLuaDetail.setCancelledBy (data.getCancelledBy ());//取消人id （0表示是顾客，其它表示店员)
        // 构建默认的Cancel
        OrderLuaDetail.Cancel cancel = new OrderLuaDetail.Cancel ();
        cancel.setId (0);
        cancel.setOperatorId (0);
        cancel.setOperatorName ("");
        cancel.setCancelReason ("");
        cancel.setCreatedStamp ("");
        orderLuaDetail.setCancel (cancel);//取消操作信息
        orderLuaDetail.setContactName (data.getContactName ());//联系人
        orderLuaDetail.setContactNum (data.getContactNum ());// 联系电话
        orderLuaDetail.setCreatedStamp (data.getCreatedStamp ());// 创建时间
        orderLuaDetail.setDiningDateTime (data.getDiningDataTime ());// 用餐时间
        orderLuaDetail.setDiscountAmount (data.getDiscountAmount ());//折扣优惠金额，sum(item.discountAmount * count)
        orderLuaDetail.setDiscountPrice (data.getDiscountPrice ());//不含税的折后价格，sum(item.itemPrice * count)
        orderLuaDetail.setFromYami (data.isFromYami ());//给默认值，是否来自yami的订单(手机下的单)
        orderLuaDetail.setIsRead (data.isRead ());//餐厅是否已经读过此单(仅LMS使用)
        orderLuaDetail.setMaxGroupNumber (data.getMaxGroupNumber ());//当前最大GroupNumber(仅LMS使用)
        orderLuaDetail.setMaxOrderItemId (data.getMaxOrderItemId ());////当前最大OrderItemId(仅LMS使用)
        orderLuaDetail.setMemberId (data.getMemberId ());//会员ID
        orderLuaDetail.setMenuId (data.getMenuId ());// 菜单ID
        orderLuaDetail.setMerchantConfirmed (data.isMerchantConfirmed ());//餐厅是否已经确认接收此订单
        orderLuaDetail.setOperatorId (data.getOperatorId ());// 操作店员账号id－－－－－－－－－－－－－－－－
        orderLuaDetail.setOperatorName (data.getContactName ());// 操作店员账号名字－－－－－－－－－－－－－－－
        orderLuaDetail.setOrderDiscount (data.getOrderDiscount ());//整单折扣率
        orderLuaDetail.setOrderNo (data.getOrderNo ());//订单号
        orderLuaDetail.setPayPrice (data.getPayPrice ());//订单应付金额
        orderLuaDetail.setRemark (data.getRemark ()); //备注
        orderLuaDetail.setRestId (data.getRestId ());//餐厅id
        orderLuaDetail.setRound (data.getRound ());//现金付款为了取整而调整的金额
        orderLuaDetail.setStatus (data.getStatus ());//订单状态 0 新增订单,1 已经取消, 2 已付款, 3 已经完成, 4 已经退款, 5 商家已经确认接收订单, 6 订单过期失效(目前商家拒收也会置为此状态)
        orderLuaDetail.setSetNeedsUpload (data.isSetNeedsUpload ());//是否需要提交以生成报表, true表示尚未提交, false表示已提交
        orderLuaDetail.setTakeAwayNo (data.getTakeAwayNo ());//打包单单号
        orderLuaDetail.setTotalGst (data.getTotalGst ());//商户端自用的gst数额显示
        orderLuaDetail.setTotalService ("");// 总的Service Charge 服务税
        orderLuaDetail.setDidTableCode (data.getTableCode ());// 换桌旧桌号；  默认为空字符 ''
        orderLuaDetail.setType (data.getType ());//订单类型（1堂食，2打包，3预定）
        orderLuaDetail.setCustomerNum (data.getCustomerNum ());// 用餐人数
        orderLuaDetail.setTableCode (data.getTableCode ());//桌号
        orderLuaDetail.setTableId (data.getTableId ());//餐桌id
        orderLuaDetail.setUpdatedStamp (data.getUpdatedStamp ());//更新时间
        orderLuaDetail.setSetNeedsPrint (true);//是否需要打印//以前使用的是    isChangeForDish: TESchema.TEBoolean, // 是否有菜品改变
        orderLuaDetail.setTips (data.getTips ());//小费
        orderLuaDetail.setIsTotalOrder (false);//判断是否是总单（程序里面用来使用的字段，打印需要调用）
        orderLuaDetail.setCashRounding ("");//判断到底要不要取整－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
        orderLuaDetail.setRoundingRule ("");//取整规则 [key]:[value],[key]:[value]... key是付款尾数,value是取整动作－－－－－－－－－－－－－－－－－－－－－－－－－
        // 构建默认的payment
        OrderNetResultData.Payment payment = new OrderNetResultData.Payment ();
        if (mTableResultData.getPaymentStatus () == 0) {// 未支付，不显示Round
            payment.setPaymentMethod (0);// Lua只有在现金支付时有Round
        } else {
            payment.setPaymentMethod (data.getPayment ().getPaymentMethod ());
        }
        payment.setId (0);
        payment.setOperatorId (0);
        payment.setInvoiceNo ("");
        payment.setAmount ("");
        payment.setReceived ("");
        payment.setChange ("");
        payment.setStatus (0);
        payment.setCreatedStamp ("");
        payment.setUpdatedStamp ("");
        orderLuaDetail.setPayment (payment);// 付款信息
        // 构建默认的refund
        OrderNetResultData.Refund refund = new OrderNetResultData.Refund ();
        refund.setId (0);
        refund.setOperatorId (0);
        refund.setRefundMethod (0);
        refund.setStatus (0);
        refund.setAmount ("");
        refund.setRefundedBy (0);
        refund.setRefundeason ("");
        refund.setCreatedStamp ("");
        refund.setUpdatedStamp ("");
        orderLuaDetail.setRefund (refund);//退款信息
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
            if (data.getTableId () != null && !data.getTableId ().equals ("")) {// -------------------------此处需要进一步确认----------------
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