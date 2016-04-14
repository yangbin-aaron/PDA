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
import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.setting.AddPrinterActivity;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderDetail;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderLuaDetail;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderNewData;
import com.toughegg.teorderpo.mvp.mvppresenter.ShoppingCartPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.UpLoadOrderPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.UpLoadOrderPresenterInf;
import com.toughegg.teorderpo.mvp.mvpview.IUpLoadDataView;
import com.toughegg.teorderpo.print.NetPrintServer;
import com.toughegg.teorderpo.utils.LuaUtils;
import com.toughegg.teorderpo.utils.MyUtil;
import com.toughegg.teorderpo.view.MyBottomBar;
import com.toughegg.teorderpo.view.MyBottomBar.OnBottomBarListener;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;
import com.toughegg.teorderpo.view.dialogs.MyDialogOneBtn;
import com.toughegg.teorderpo.view.dialogs.MyDialogTwoBtn;
import com.toughegg.teorderpo.view.dialogs.RePrintDialog;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ld on 15/8/6.
 * 订单详情界面（提交的时候需要）
 */
public class OrderDetailActivity extends BaseActivity implements MyTopActionBar.OnTopActionBarClickListener,
        IUpLoadDataView, OnBottomBarListener {

    private TEOrderPoApplication mTEOrderPoApplication;

    private TextView mTableNameTextView;// 餐桌
    private TextView mPeopleCountTextView;// 人数
    private TextView mSubtotalTextView;// 菜品合计
    private TextView mConsumptionTaxTextView;
    private TextView mServiceTaxTextView;
    private TextView mRoundTextView;// Round
    private LinearLayout mOrderDishListLayout;// 菜单列表

    private String mOrderNo;
    private int mType;// 订单类型
    private LuaState mLuaState;

    // -=====================-
    private boolean mIsNewOrUpdate = false;// 默认为新增菜单，true为修改菜单
    // -=====================-

    // ********** BottomActionBar **********
    private MyBottomBar myBottomBar;

    //****table number ,people number *********
    private String tableNumber;
    private int peopleNumber;
    private String tableId;
    private String restId;

    private UpLoadOrderPresenterInf mUpLoadOrderPresenterInf;

    private LuaUtils mLuaUtils;
    private OrderLuaDetail mOrderLuaDetail;
    private OrderDetail mOrderJsonDetail;

    private Iterator mIpIterator;// 从lua获取IP的迭代器
    private boolean mIsSubmitSuccess = false;

    private Dialog mLoadingDialog;// 加载对话框

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (msg.what == TEOrderPoConstans.HANDLER_WHAT_SHOPPINGCARTLIST) {// 设置购物车数据
                HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
                showShoppingCartList (hashMap);
            } else if (msg.what == LuaUtils.HANDLER_LUA_SUCCESS) {// 购物车数据 通过lua计算成功
                String orderDetailStr = (String) msg.obj;
                Log.e ("aaron1", "mOrderDetailStr-->>" + orderDetailStr);
                setLuaData (orderDetailStr);
            } else if (msg.what == NetPrintServer.HANDLER_PRINT_COM) {
                printCom ();// 完成打印
            } else if (msg.what == TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS) {
                startOrderStateActivity ();
            } else if (msg.what == TEOrderPoConstans.HANDLER_WHAT_POST_FAIL || msg.what == TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL) {
                // mUpLoadOrderPresenterInf.printSuccess失败
                mLoadingDialog.dismiss ();
                final MyDialogTwoBtn myDialogTwoBtn = new MyDialogTwoBtn (OrderDetailActivity.this);
                myDialogTwoBtn.setMessage (R.string.app_load_fail);
                myDialogTwoBtn.setOnDialogClickListener (new MyDialogTwoBtn.OnDialogClickListener () {
                    @Override
                    public void onLeftClicked () {
                        myDialogTwoBtn.dismiss ();
                        startOrderStateActivity ();
                    }

                    @Override
                    public void onRightClicked () {
                        myDialogTwoBtn.dismiss ();
                        mLoadingDialog = DialogUtils.createLoadingDialog (OrderDetailActivity.this, R.string.app_please_wait, false);
                        mLoadingDialog.show ();
                        mUpLoadOrderPresenterInf.printSuccess (OrderDetailActivity.this, mOrderNo, mHandler);
                    }
                });
                myDialogTwoBtn.show ();
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            if (intent.getAction ().equals (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY)) {
                finish ();
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_order_detail);
        mType = SharePrefenceUtils.readInt (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_ORDER_TYPE);
        initTopActionBar ();
        initBottomBar ();
        initView ();

        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY);
        registerReceiver (mReceiver, filter);

//        mLuaState = TEOrderPoApplication.getInstance ().getLuastate ();
        mLuaState = LuaStateFactory.newLuaState ();
        mLuaState.openLibs ();
        mLuaUtils = new LuaUtils (mLuaState, this, mHandler);

        tableNumber = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_NUMBER);
        tableId = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_ID, null);
        peopleNumber = SharePrefenceUtils.readInt (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_PEOPLE);
        restId = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_RESTAURANT_ID, null);
        mTableNameTextView.setText (tableNumber);
        mPeopleCountTextView.setText (peopleNumber + "");

        mTEOrderPoApplication = (TEOrderPoApplication) getApplicationContext ();
        if (mTEOrderPoApplication.orderNetResultData != null) {
            mIsNewOrUpdate = true;
        }
        ShoppingCartPresenterImp mShoppingCartPresenterImp = new ShoppingCartPresenterImp ();
        mUpLoadOrderPresenterInf = new UpLoadOrderPresenterImp (this);
        mShoppingCartPresenterImp.showShoppingCartList (mHandler);
    }

    @Override
    protected void onDestroy () {
        unregisterReceiver (mReceiver);
        super.onDestroy ();
    }

    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.actionbar_order_detail_title_str);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
        mMyTopActionBar.setRightClick (false);
    }

    private void initBottomBar () {
        myBottomBar = (MyBottomBar) findViewById (R.id.myBottomBar);
        myBottomBar.setNumber (false, 0);
        myBottomBar.showLeftImg (false);
        myBottomBar.showMiddleLayout (true);
//        myBottomBar.setLeftPrice(true, "$ 1222");
        myBottomBar.setRightText (true, R.string.order_detail_str_print_order);
        myBottomBar.setOnBottomBarListener (this);

        myBottomBar.setLeftText (true);
    }

    private void initView () {
        mTableNameTextView = (TextView) findViewById (R.id.activity_order_detatil_tablename_textView);
        mPeopleCountTextView = (TextView) findViewById (R.id.activity_order_detatil_peoplecount_textView);
        mSubtotalTextView = (TextView) findViewById (R.id.activity_order_detail_subtotal_textView);
        mConsumptionTaxTextView = (TextView) findViewById (R.id.activity_order_detail_consumptiontax_textView);
        mServiceTaxTextView = (TextView) findViewById (R.id.activity_order_detail_servicetax_textView);
        mRoundTextView = (TextView) findViewById (R.id.activity_order_detail_round_textView);

        TextView mConsumptionTaxShowTextView = (TextView) findViewById (R.id.activity_order_detail_gstshow_textView);
        TextView mServiceTaxShowTextView = (TextView) findViewById (R.id.activity_order_detail_servicetaxshow_textView);
        String gstStr = "";
        String serviceStr = "";
        mConsumptionTaxShowTextView.setText (gstStr);
        mServiceTaxShowTextView.setText (serviceStr);

        mOrderDishListLayout = (LinearLayout) findViewById (R.id.activity_order_detail_list_layout);

        // 在堂食和打包的时候，部分组件时需要隐藏的，如：桌号信息，税率
        if (SharePrefenceUtils.readInt (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_ORDER_TYPE) == TEOrderPoConstans
                .ORDER_TYPE_TAKEAWAY) {
            // 隐藏餐桌信息
            findViewById (R.id.activity_order_detail_table_layout).setVisibility (View.GONE);
            findViewById (R.id.activity_order_detail_table_textview).setVisibility (View.GONE);
            // 隐藏税率
            myBottomBar.setMiddleText (false, null);
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
    }


    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {

    }

    private void showShoppingCartList (HashMap<String, Object> hashMap) {
        List<ShoppingCart> shoppingCarts = (List<ShoppingCart>) hashMap.get ("shoppingcartlist");
        List<ItemTax> itemTaxes = (List<ItemTax>) hashMap.get ("itemtaxlist");
        List<ItemTax> itemTaxList = new ArrayList<> ();
        for (ItemTax itemTax : itemTaxes) {// 暂时－－－－－－－－－－－－－－－
            itemTax.setEffectOrderTypes ("1");
            itemTaxList.add (itemTax);
        }
        Log.e ("aaron", "OrderDetailActivity>>>\n\n" + itemTaxList.toString () + "\n\n");
        List<OrderNetResultDataItem> itemList = new ArrayList<> ();
        List<OrderNetResultDataItem> tempItemList = new ArrayList<> ();
        mOrderLuaDetail = new OrderLuaDetail ();
        for (ShoppingCart shoppingCart : shoppingCarts) {// 设置shopping_cart里面的数据
            mOrderDishListLayout.addView (MyUtil.createTextView (this, shoppingCart, mIsNewOrUpdate));// 显示菜单
            OrderNetResultDataItem item = new OrderNetResultDataItem ();
            item.setItemTax (itemTaxList);
            item.setAction (0);
            if (mIsNewOrUpdate) {
                item.setAction (1);// 新添加的菜品
            }
            item.setName (shoppingCart.getName ());
            item.setAddTaxAmount ("0");
            item.setAllowCustomDiscount (false);
            item.setBasePrice ("");
            item.setCategoryId (TEOrderPoDataBaseHelp.findDishInfoCategoryIdsByDishId (shoppingCart.getDishId ()));
            item.setCode (shoppingCart.getCode ());
            item.setCount (shoppingCart.getCopies ());
            item.setDiscountAmount ("");
//            item.setDishAction ("");
            item.setGroupNumber (0);
            item.setIsDeleted (shoppingCart.getIsDeleted ());
            item.setItemDiscount ("0");
            item.setItemPrice ("0");
            item.setMenuItemId (shoppingCart.getDishId ());
            item.setMenuPrice (shoppingCart.getPrice ());
            item.setModifierAmount ("0");
//            item.setOrderItemId (i + "0");
            item.setPriceAdjustment ("0");
            if (shoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
                shoppingCart.setRemark ("");
            }
            item.setRemark (shoppingCart.getRemark ());
            String modifierListStr = "";
            List<OrderNetResultItemModifier> uploadModifierList = new ArrayList<> ();
            for (Option option : shoppingCart.getOptionList ()) {
                OrderNetResultItemModifier uploadModifier = new OrderNetResultItemModifier ();
                String modifierId = TEOrderPoDataBaseHelp.findItemModifierByOptionId (option.getId ()).getId ();
                uploadModifier.setModifierId (modifierId);
                uploadModifier.setModifierOptionId (option.getId ());
                uploadModifier.setCount (option.getCount ());
                uploadModifier.setUnitPrice (option.getPrice ());
                uploadModifier.setName (option.getName ());
                uploadModifierList.add (uploadModifier);
                modifierListStr = modifierId + "," + modifierListStr;
            }
            String str = modifierListStr.substring (0, modifierListStr.length ());
            String[] strArray = str.split (",");
            List<String> stringList = new ArrayList<> ();
            Collections.addAll (stringList, strArray);

            item.setModifier (uploadModifierList);
            item.setModifierList (stringList);
            item.setSetNeedsPrint (true);
            //            Log.e ("aaron", item.getName ().toString ());
            tempItemList.add (item);
        }
        OrderNetResultData data = mTEOrderPoApplication.orderNetResultData;
        if (mIsNewOrUpdate) {// 修改订单时需要的设置
            setUpdateListData (data, itemList, itemTaxList);
        }
        // 因为需要显示是新单在前，json数据是新单在后，所以添加这个临时变量
        itemList.addAll (tempItemList);
        mOrderLuaDetail.setItem (itemList);
        if (mIsNewOrUpdate) {// 更新
            setOrderDetailUpdateOrder (mOrderLuaDetail, data);
        } else {// 新建订单
            setOrderDetailNewOrder (mOrderLuaDetail);
        }

        // 通过lua计算返回json数据
        Gson gson = new Gson ();
        final String json = gson.toJson (mOrderLuaDetail);
        Log.d ("aaron", "json-->>" + json);
        mLuaUtils.executeLuaCalculate (json);
    }

    private Bundle mBundle = null;

    @Override
    public void getFinishResult (Message msg) {
        switch (msg.what) {
            case TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS:
                mIsSubmitSuccess = true;// 订单提交成功，不能返回了
                mBundle = new Bundle ();
                mBundle.putSerializable ("orderNewData", (OrderNewData) msg.obj);
                Log.e ("hcc", "mTEOrderPoApplication.printSuccessCount-->>>" + mTEOrderPoApplication.printSuccessCount);
                if (mTEOrderPoApplication.printSuccessCount == 0) {// 不需要打印
                    startOrderStateActivity ();
                } else {// 需要打印
                    printMethod ((OrderNewData) msg.obj);
                }
                break;
            case TEOrderPoConstans.HANDLER_WHAT_POST_FAIL:
                mLoadingDialog.dismiss ();
                DialogUtils.createErrorDialog (this, (String) msg.obj);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL:
                mLoadingDialog.dismiss ();
                DialogUtils.createErrorDialog (this, R.string.app_service_is_not_connected);
                break;
            case TEOrderPoConstans.HANDLER_WHAT_NET_ERROR:
                mLoadingDialog.dismiss ();
                DialogUtils.createErrorDialog (this, R.string.app_net_date_error);
                break;
        }
    }

    @Override
    public void onBottomBarLeftListener () {

    }

    @Override
    public void onBottomBarRightListener () {
        if (mIsSubmitSuccess) {
            return;// 不能重复提交订单
        }
        // 打印订单
        HashMap<String, String> printNameAndIp = NetPrintServer.getInstance ().hashMapIp (OrderDetailActivity.this, "print_order");
        mIpIterator = printNameAndIp.entrySet ().iterator ();
        Log.e ("aaron", ">>>>" + printNameAndIp.toString ());

        mTEOrderPoApplication.printSuccessCount = printNameAndIp.entrySet ().size ();
        boolean flag = true;
        while (mIpIterator.hasNext ()) {
            HashMap.Entry entry = (HashMap.Entry) mIpIterator.next ();
            String key = (String) entry.getKey ();
            String ip = (String) entry.getValue ();
            if (ip.equals (TEOrderPoConstans.PRINT_DETALS_IP)) {
                flag = false;
                break;
            }
        }
        mIpIterator = printNameAndIp.entrySet ().iterator ();

        if (!flag) {
            // 请设置IP地址
            final MyDialogOneBtn myDialogOneBtn = new MyDialogOneBtn (this);
            myDialogOneBtn.setMessage (R.string.order_detail_str_setip);
            myDialogOneBtn.setOnDialogClickListener (new MyDialogOneBtn.OnDialogClickListener () {
                @Override
                public void onConfirmClicked () {
                    myDialogOneBtn.dismiss ();
                    startActivity (new Intent (OrderDetailActivity.this, AddPrinterActivity.class));
                    overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
                }
            });
            myDialogOneBtn.show ();
            return;
        }

//        String orderDetailStr = mLuaUtils.getOrderDetailStr ();
        String orderDetailStr = new Gson ().toJson (mOrderJsonDetail);
        Log.e ("aaron", ">>>>==============" + orderDetailStr);
        mLoadingDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait, false);
        mLoadingDialog.show ();
        if (mIsNewOrUpdate) {
            mUpLoadOrderPresenterInf.updateOrder (this, orderDetailStr);
        } else {
            mUpLoadOrderPresenterInf.orderNewData (this, orderDetailStr);
        }
    }

    /**
     * 将网络菜单设置到lua
     */
    private void setUpdateListData (OrderNetResultData data, List<OrderNetResultDataItem> itemList, List<ItemTax> itemTaxes) {
        List<OrderNetResultDataItem> dataItemList = data.getItem ();
        for (OrderNetResultDataItem dataItem : dataItemList) {// 设置,网络菜单数据
//            Log.e ("aaron", TAG + "  >>>" + dataItem.toString () + "\n\n");
            mOrderDishListLayout.addView (MyUtil.createTextView (this, getShoppingCart (dataItem), mIsNewOrUpdate));// 显示菜单
            OrderNetResultDataItem item = new OrderNetResultDataItem ();
//            item.setItemTax (dataItem.getItemTax ());
            item.setItemTax (itemTaxes);
            item.setAction (dataItem.getAction ());
            item.setName (dataItem.getName ());
            item.setAddTaxAmount (dataItem.getAddTaxAmount ());
            item.setAllowCustomDiscount (dataItem.isAllowCustomDiscount ());
            item.setBasePrice (dataItem.getBasePrice ());
            item.setCategoryId (dataItem.getCategoryId ());
            item.setCode (dataItem.getCode ());
            item.setCount (dataItem.getCount ());
            item.setDiscountAmount (dataItem.getDiscountAmount ());
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
            itemList.add (item);
        }
    }

    /**
     * // 已经设置itemList
     *
     * @param orderLuaDetail
     */
    private void setOrderDetailNewOrder (OrderLuaDetail orderLuaDetail) {
        orderLuaDetail.setId (0);// 默认0，来自AWS的订单id
        orderLuaDetail.setAddTax ("");// 默认为"",总税额，sum(item.addTaxAmount * count)
        orderLuaDetail.setAddress ("");// 默认为"",联系人地址
        orderLuaDetail.setBasePrice ("");// 默认为"",原始价格，所有item的 basePrice * count 的总和
        orderLuaDetail.setCancelRemark ("");// 取消说明
        orderLuaDetail.setCancelledBy (0);//取消人id （0表示是顾客，其它表示店员)
        // 构建默认的Cancel
        OrderLuaDetail.Cancel cancel = new OrderLuaDetail.Cancel ();
        cancel.setId (0);
        cancel.setOperatorId (0);
        cancel.setOperatorName ("");
        cancel.setCancelReason ("");
        cancel.setCreatedStamp ("");
        orderLuaDetail.setCancel (cancel);//取消操作信息
        orderLuaDetail.setContactName ("");//联系人
        orderLuaDetail.setContactNum ("");// 联系电话
        orderLuaDetail.setCreatedStamp ("");// 创建时间
        orderLuaDetail.setDiningDateTime ("");// 用餐时间
        orderLuaDetail.setDiscountAmount ("");//折扣优惠金额，sum(item.discountAmount * count)
        orderLuaDetail.setDiscountPrice ("");//不含税的折后价格，sum(item.itemPrice * count)
        orderLuaDetail.setFromYami (false);//给默认值，是否来自yami的订单(手机下的单)
        orderLuaDetail.setIsRead (false);//餐厅是否已经读过此单(仅LMS使用)
        orderLuaDetail.setMaxGroupNumber (0);//当前最大GroupNumber(仅LMS使用)
        orderLuaDetail.setMaxOrderItemId (0);////当前最大OrderItemId(仅LMS使用)
        orderLuaDetail.setMemberId (0);//会员ID
        orderLuaDetail.setMenuId ("");// 菜单ID
        orderLuaDetail.setMerchantConfirmed (false);//餐厅是否已经确认接收此订单
        orderLuaDetail.setOperatorId (0);// 操作店员账号id－－－－－－－－－－－－－－－－
        orderLuaDetail.setOperatorName ("");// 操作店员账号名字－－－－－－－－－－－－－－－
        orderLuaDetail.setOrderDiscount ("");//整单折扣率
        orderLuaDetail.setOrderNo ("");//订单号
        orderLuaDetail.setPayPrice ("");//订单应付金额
        orderLuaDetail.setRemark (""); //备注
        orderLuaDetail.setRestId (restId);//餐厅id
        orderLuaDetail.setRound ("");//现金付款为了取整而调整的金额
        orderLuaDetail.setStatus (0);//订单状态 0 新增订单,1 已经取消, 2 已付款, 3 已经完成, 4 已经退款, 5 商家已经确认接收订单, 6 订单过期失效(目前商家拒收也会置为此状态)
        orderLuaDetail.setSetNeedsUpload (false);//是否需要提交以生成报表, true表示尚未提交, false表示已提交
        orderLuaDetail.setTakeAwayNo ("");//打包单单号
        orderLuaDetail.setTotalGst ("");//商户端自用的gst数额显示
        orderLuaDetail.setTotalService ("");// 总的Service Charge 服务税
        orderLuaDetail.setDidTableCode ("");// 换桌旧桌号；  默认为空字符 ''
        orderLuaDetail.setType (mType);//订单类型（1堂食，2打包，3预定）
        if (mType == TEOrderPoConstans.ORDER_TYPE_HOUSE) {// 堂食才会有用餐人数、餐桌信息
            orderLuaDetail.setCustomerNum (peopleNumber);// 用餐人数
            orderLuaDetail.setTableCode (tableNumber);//桌号
            orderLuaDetail.setTableId (tableId);//餐桌id
        } else if (mType == TEOrderPoConstans.ORDER_TYPE_TAKEAWAY) {
            orderLuaDetail.setCustomerNum (1);// 用餐人数
            orderLuaDetail.setTableCode ("");//桌号
            orderLuaDetail.setTableId ("");//餐桌id
        }
        orderLuaDetail.setUpdatedStamp ("");//更新时间
        orderLuaDetail.setSetNeedsPrint (true);//是否需要打印//以前使用的是    isChangeForDish: TESchema.TEBoolean, // 是否有菜品改变
        orderLuaDetail.setTips ("");//小费
        orderLuaDetail.setIsTotalOrder (false);//判断是否是总单（程序里面用来使用的字段，打印需要调用）
        orderLuaDetail.setCashRounding ("");//判断到底要不要取整－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
        orderLuaDetail.setRoundingRule ("");//取整规则 [key]:[value],[key]:[value]... key是付款尾数,value是取整动作－－－－－－－－－－－－－－－－－－－－－－－－－
        // 构建默认的payment
        OrderNetResultData.Payment payment = new OrderNetResultData.Payment ();
        payment.setId (0);
        payment.setOperatorId (0);
        payment.setInvoiceNo ("");
        payment.setPaymentMethod (0);
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
        orderLuaDetail.setTotalService (data.getTotalGst ());// 总的Service Charge 服务税
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
        payment.setId (0);
        payment.setOperatorId (0);
        payment.setInvoiceNo ("");
        payment.setPaymentMethod (data.getPayment ().getPaymentMethod ());
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

    // 将网络菜单的数据转化为ShoppingCart方便显示
    private ShoppingCart getShoppingCart (OrderNetResultDataItem item) {
        ShoppingCart shoppingCart = new ShoppingCart ();
        shoppingCart.setId (-2);
        shoppingCart.setDishId (item.getMenuItemId ());
        shoppingCart.setPrice (item.getMenuPrice ());
        double totalPrice = Double.parseDouble (item.getMenuPrice ());
        if (item.getModifier () != null && item.getModifier ().size () > 0) {
            for (OrderNetResultItemModifier modifier : item.getModifier ()) {
                double price = CountUtils.mul (modifier.getUnitPrice (), modifier.getCount () + "");
                totalPrice = CountUtils.add (totalPrice, price);
            }
        }
        shoppingCart.setTotalPrice (StringUtils.getPriceString (totalPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        shoppingCart.setCopies (item.getCount ());
        shoppingCart.setCode (item.getCode ());
        shoppingCart.setRemark (item.getRemark ());
        shoppingCart.setName (item.getName ());
        shoppingCart.setIsDeleted (item.isDeleted ());
        List<Option> labels = new ArrayList<> ();
        if (item.getModifier () != null && item.getModifier ().size () > 0) {
            for (int j = 0; j < item.getModifier ().size (); j++) {
                OrderNetResultItemModifier modifier = item.getModifier ().get (j);
                Option option = new Option ();
                option.setShoppingCartId (-2);
                option.setItemModifierId (modifier.getModifierId ());
                option.setCount (modifier.getCount ());
                option.setName (modifier.getName ());
                option.setPrice (modifier.getUnitPrice ());
                labels.add (option);
            }
        }
        shoppingCart.setOptionList (labels);
        return shoppingCart;
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
        if (mIsSubmitSuccess == false) {
            finish ();
            overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

    private String json;

    /**
     * 执行打印操作
     *
     * @param orderNewData
     */
    public void printMethod (OrderNewData orderNewData) {
        mOrderNo = orderNewData.getOrderNo ();

        mOrderLuaDetail.setTakeAwayNo (orderNewData.getTakeAwayNo ());
        mOrderLuaDetail.setSetNeedsPrint (true);

        json = new Gson ().toJson (mOrderLuaDetail);

        while (mIpIterator.hasNext ()) {
            mLuaState = LuaStateFactory.newLuaState ();
            mLuaState.openLibs ();
            HashMap.Entry entry = (HashMap.Entry) mIpIterator.next ();
            String key = (String) entry.getKey ();
            String ip = (String) entry.getValue ();
            Log.e ("aaron", "key-->>" + key + " val-->>" + ip);
            NetPrintServer.getInstance ().executePrint
                    ("print_order", key, ip, TEOrderPoConstans.PRINT_PORT, mLuaState, OrderDetailActivity.this, json, mHandler);
        }
    }

    /**
     * 设置lua计算后的数据
     * ＊＊＊＊＊＊＊＊＊＊＊＊需要构建一个OrderJsonDetail对象来提交订单＊＊＊＊＊＊＊＊＊＊＊＊
     *
     * @param json
     */
    private void setLuaData (String json) {
        mOrderLuaDetail = new Gson ().fromJson (json, OrderLuaDetail.class);
        // 菜品合计
        double basePrice = Double.parseDouble (mOrderLuaDetail.getBasePrice ());
        mSubtotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (basePrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        // 税收
        double gstPrice = Double.parseDouble (mOrderLuaDetail.getTotalGst ());
        mConsumptionTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (gstPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        double servicePrice = Double.parseDouble (mOrderLuaDetail.getTotalService ());
        mServiceTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (servicePrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        double roundPrice = Double.parseDouble (mOrderLuaDetail.getRound ());
        mRoundTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (roundPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        // 总计
        double totalPrice = Double.parseDouble (mOrderLuaDetail.getPayPrice ());
        myBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (totalPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));

        createOrderJsonDetail ();
    }

    private void createOrderJsonDetail () {
        mOrderJsonDetail = new OrderDetail ();
        mOrderJsonDetail.setAddTax (mOrderLuaDetail.getAddTax ());
        mOrderJsonDetail.setAddress (mOrderLuaDetail.getAddress ());
        mOrderJsonDetail.setBasePrice (mOrderLuaDetail.getBasePrice ());
        mOrderJsonDetail.setCancelRemark (mOrderLuaDetail.getCancelRemark ());
        mOrderJsonDetail.setCancelledBy (mOrderLuaDetail.getCancelledBy ());
        mOrderJsonDetail.setContactName (mOrderLuaDetail.getContactName ());
        mOrderJsonDetail.setContactNum (mOrderLuaDetail.getContactNum ());
        mOrderJsonDetail.setCreatedStamp (mOrderLuaDetail.getCreatedStamp ());
        mOrderJsonDetail.setCustomerNum (mOrderLuaDetail.getCustomerNum ());
        mOrderJsonDetail.setDiscountAmount (mOrderLuaDetail.getDiscountAmount ());
        mOrderJsonDetail.setDiscountPrice (mOrderLuaDetail.getDiscountPrice ());
        mOrderJsonDetail.setRemark (mOrderLuaDetail.getRemark ());
        mOrderJsonDetail.setRestId (mOrderLuaDetail.getRestId ());
        mOrderJsonDetail.setRound (mOrderLuaDetail.getRound ());
        mOrderJsonDetail.setTableCode (mOrderLuaDetail.getTableCode ());
        mOrderJsonDetail.setTableId (mOrderLuaDetail.getTableId ());
        mOrderJsonDetail.setTotalGst (mOrderLuaDetail.getTotalGst ());
        mOrderJsonDetail.setType (mOrderLuaDetail.getType ());
        mOrderJsonDetail.setUpdatedStamp (mOrderLuaDetail.getUpdatedStamp ());
        mOrderJsonDetail.setOperatorId (mOrderLuaDetail.getOperatorId ());
        mOrderJsonDetail.setOrderDiscount (mOrderLuaDetail.getOrderDiscount ());
        mOrderJsonDetail.setOrderNo (mOrderLuaDetail.getOrderNo ());
        mOrderJsonDetail.setPayPrice (mOrderLuaDetail.getPayPrice ());
        mOrderJsonDetail.setItem (mOrderLuaDetail.getItem ());
    }

    private void printCom () {
        if (mTEOrderPoApplication.printFail) {// 有打印失败的，需要显示
            final RePrintDialog rePrintDialog = new RePrintDialog (OrderDetailActivity.this);
            rePrintDialog.setOnDialogClickListener (new RePrintDialog.OnDialogClickListener () {
                @Override
                public void onLeftClicked () {
                    rePrintDialog.dismiss ();
                    mTEOrderPoApplication.printFail = false;
                    mTEOrderPoApplication.printLog = null;
                    startOrderStateActivity ();
                }

                @Override
                public void onRightClicked () {

                    mLoadingDialog = DialogUtils.createLoadingDialog (OrderDetailActivity.this, R.string.app_please_wait, false);
                    mLoadingDialog.show ();

                    List<HashMap<String, Object>> printLog = new ArrayList<> ();
                    for (int i = 0; i < mTEOrderPoApplication.printLog.size (); i++) {
                        HashMap<String, Object> hashMap = mTEOrderPoApplication.printLog.get (i);
                        boolean select = (boolean) hashMap.get ("select");
                        if (select) {
                            printLog.add (hashMap);
                        }
                    }
                    mTEOrderPoApplication.printLog = null;
                    mTEOrderPoApplication.printFail = false;
                    mTEOrderPoApplication.printSuccessCount = printLog.size ();
                    for (int i = 0; i < printLog.size (); i++) {
                        HashMap<String, Object> hashMap = printLog.get (i);
                        String key = (String) hashMap.get ("key");
                        String ip = (String) hashMap.get ("ip");
                        Log.e ("key", "key-->>" + key + " val-->>" + ip);
                        NetPrintServer.getInstance ().executePrint
                                ("print_order", key, ip, TEOrderPoConstans.PRINT_PORT, mLuaState, OrderDetailActivity.this, json, mHandler);
                    }
                    rePrintDialog.dismiss ();
                    if (printLog.size () == 0) {
                        // 调用API，通知打印完成
                        mUpLoadOrderPresenterInf.printSuccess (OrderDetailActivity.this, mOrderNo, mHandler);
                    }
                }
            });
            mLoadingDialog.dismiss ();
            rePrintDialog.show ();
        } else {// 打印成功
            Log.e ("aaron1","--打印成功--打印成功--打印成功--打印成功--打印成功--打印成功--打印成功--");
            mTEOrderPoApplication.printFail = false;
            mTEOrderPoApplication.printLog = null;
            // 跳用API，通知打印完成
            mUpLoadOrderPresenterInf.printSuccess (OrderDetailActivity.this, mOrderNo, mHandler);
        }
    }

    private void startOrderStateActivity () {
        Intent intent = new Intent ();
        intent.setClass (this, OrderStateActivity.class);
        intent.putExtras (mBundle);
        startActivity (intent);
        overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
        mLoadingDialog.dismiss ();
    }
}
