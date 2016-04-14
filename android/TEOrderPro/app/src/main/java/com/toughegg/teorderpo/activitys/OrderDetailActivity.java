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
import com.toughegg.teorderpo.modle.entry.uploadOrder.Item;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderDetail;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderNewData;
import com.toughegg.teorderpo.modle.printOrder.PrintDishList;
import com.toughegg.teorderpo.modle.printOrder.PrintOrderDetail;
import com.toughegg.teorderpo.modle.printOrder.PrintPriceOptionList;
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
                Log.d ("str", "mOrderDetailStr-->>" + orderDetailStr);
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
        List<ItemTax> itemTaxList = (List<ItemTax>) hashMap.get ("itemtaxlist");
        Log.e ("aaron", "OrderDetailActivity>>>\n\n" + itemTaxList.toString () + "\n\n");
        List<Item> itemList = new ArrayList<> ();
        List<Item> tempItemList = new ArrayList<> ();
        OrderDetail orderDetail = new OrderDetail ();
        for (ShoppingCart shoppingCart : shoppingCarts) {// 设置shopping_cart里面的数据
            mOrderDishListLayout.addView (MyUtil.createTextView (this, shoppingCart, mIsNewOrUpdate));// 显示菜单
            Item item = new Item ();
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
            item.setDishAction ("");
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
//            for (String aStrArray : strArray) {
//                stringList.add(aStrArray);
//            }

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
        orderDetail.setItem (itemList);
        if (mIsNewOrUpdate) {
            setOrderDetailUpdateOrder (orderDetail, data);
        } else {
            setOrderDetailNewOrder (orderDetail);
        }

        // 通过lua计算返回json数据
        Gson gson = new Gson ();
        final String json = gson.toJson (orderDetail);
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
            default:
                mLoadingDialog.dismiss ();
                MyUtil.handlerSendMessage (this, msg);
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
            Log.e ("aaron", "OrderDetail>>>>>>>>>>>>>>>>>" + key + " -- " + ip);
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
        String orderDetailStr = mLuaUtils.getOrderDetailStr ();
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
    private void setUpdateListData (OrderNetResultData data, List<Item> itemList, List<ItemTax> itemTaxes) {
        List<OrderNetResultDataItem> dataItemList = data.getItem ();
        for (OrderNetResultDataItem dataItem : dataItemList) {// 设置,网络菜单数据
//            Log.e ("aaron", TAG + "  >>>" + dataItem.toString () + "\n\n");
            mOrderDishListLayout.addView (MyUtil.createTextView (this, getShoppingCart (dataItem), mIsNewOrUpdate));// 显示菜单
            Item item = new Item ();
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
            itemList.add (item);
        }
    }

    private void setOrderDetailNewOrder (OrderDetail orderDetail) {
        orderDetail.setAddress ("");
        orderDetail.setAddTat ("");
        orderDetail.setBasePrice ("");
        orderDetail.setCancelledBy (0);
        orderDetail.setStatus (0);
        orderDetail.setIsPaid (0);
        orderDetail.setUpdatedStamp ("");
        orderDetail.setContactName ("");
        orderDetail.setContactNum ("");
        orderDetail.setCreatedStamp ("");
        orderDetail.setDiningDataTime ("");
        orderDetail.setDiscountAmount ("");
        orderDetail.setDiscountPrice ("");
        orderDetail.setIsReview (0);
        orderDetail.setType (mType);
        if (mType == TEOrderPoConstans.ORDER_TYPE_HOUSE) {
            orderDetail.setCustomerNum (peopleNumber);
            orderDetail.setTableCode (tableNumber);
            orderDetail.setTableId (tableId);
        } else if (mType == TEOrderPoConstans.ORDER_TYPE_TAKEAWAY) {
            orderDetail.setCustomerNum (1);
            orderDetail.setTableCode ("");
            orderDetail.setTableId ("");
        }
        orderDetail.setRemark ("");
        orderDetail.setOrderId (0);
        orderDetail.setOrderDiscount ("");
        orderDetail.setTotalGst ("");
        orderDetail.setTotalServiceTax ("");
        orderDetail.setRound ("");
        orderDetail.setPayPrice ("");
        orderDetail.setOrderNo ("");
        orderDetail.setRestaurantId (restId);
        OrderNetResultData.Payment payment = new OrderNetResultData.Payment ();
        payment.setAmount ("");
        payment.setStatus (0);
        payment.setPaymentMethod (0);
        payment.setTransactionNo ("");
        orderDetail.setPayment (payment);
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
        orderDetail.setPayment (data.getPayment ());
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
     * @param orderDetailLua
     */
    public void printMethod (OrderNewData orderDetailLua) {
        mOrderNo = orderDetailLua.getOrderNo ();

        PrintOrderDetail mPrintOrderDetail = new PrintOrderDetail ();
        mPrintOrderDetail.setTake_away_number (Integer.parseInt (orderDetailLua.getTakeAwayNo ()));
        mPrintOrderDetail.setIs_paid (false);
        mPrintOrderDetail.setTotal_price_without_fee (0);
        mPrintOrderDetail.setRemark ("");
        mPrintOrderDetail.setGst (0);
        mPrintOrderDetail.setIs_printed_invoice (0);
        mPrintOrderDetail.setTable_id (orderDetailLua.getTableId ());
        mPrintOrderDetail.setTotal_gst (orderDetailLua.getTotalGst ());
        mPrintOrderDetail.setTotal_service_tax (orderDetailLua.getTotalServiceTax ());
        mPrintOrderDetail.setInternetTotalPrice (0);
        mPrintOrderDetail.setType (orderDetailLua.getType ());
        mPrintOrderDetail.setIsChangeForDish (true);
        mPrintOrderDetail.setCustomer_number (orderDetailLua.getCustomerNum ());
        mPrintOrderDetail.setDiscount ("0");
        mPrintOrderDetail.setDiscount_price (orderDetailLua.getDiscountAmount ());
//                mPrintOrderDetail.setInvoiceNo(orderDetailLua.get);
        mPrintOrderDetail.setDidTable_number ("");
        mPrintOrderDetail.setOrder_no (orderDetailLua.getOrderNo ());
        List<PrintDishList> mPrintDishLists = new ArrayList<> ();

        for (Item item : orderDetailLua.getItem ()) {// 设置shoppingcart里面的数据
            PrintDishList mPrintDishList = new PrintDishList ();
            mPrintDishList.setIs_hot (0);
            mPrintDishList.setSortno (101);
            mPrintDishList.setRemark (item.getRemark ());
            mPrintDishList.setDish_id (item.getOrderItemId ());
            mPrintDishList.setIs_localChange (0);
            mPrintDishList.setIs_discount_select (0);
//                    mPrintDishList.setPriceOptionList();
            List<PrintPriceOptionList> mPrintPriceOptionList = new ArrayList<> ();
            for (OrderNetResultItemModifier orderNetResultItemModifier : item.getModifier ()) {
                PrintPriceOptionList printPriceOptionList = new PrintPriceOptionList ();
                List<String> name = new ArrayList<> ();
                name.add (0, orderNetResultItemModifier.getName ().getEn_US ());
                name.add (1, orderNetResultItemModifier.getName ().getZh_CN ());
                printPriceOptionList.setName (name);
                printPriceOptionList.setDefault_price (orderNetResultItemModifier.getUnitPrice ());
                printPriceOptionList.setPrice_option_id (orderNetResultItemModifier.getModifierOptionId ());
                printPriceOptionList.setDish_count (orderNetResultItemModifier.getCount ());
                mPrintPriceOptionList.add (printPriceOptionList);
            }
            mPrintDishList.setPriceOptionList (mPrintPriceOptionList);
            List<String> dishName = new ArrayList<> ();
            dishName.add (0, item.getName ().getEn_US ());
            dishName.add (1, item.getName ().getZh_CN ());
            mPrintDishList.setName (dishName);
            mPrintDishList.setDish_code (item.getCode ());
            mPrintDishList.setIs_change_forPrint (true);
            mPrintDishList.setPrice (item.getMenuPrice ());
            mPrintDishList.setCopies (item.getCount ());
            mPrintDishList.setIs_change_forPrint (item.isSetNeedsPrint ());
            mPrintDishList.setIs_deleted (item.getIsDeleted ());
            mPrintDishLists.add (mPrintDishList);
        }
        mPrintOrderDetail.setDishList (mPrintDishLists);
//                mPrintOrderDetail.setPaymentCash(orderDetailLua.getPaymentChange());
        mPrintOrderDetail.setTable_number (orderDetailLua.getTableCode ());
        mPrintOrderDetail.setDish_total_price (orderDetailLua.getBasePrice ());
        mPrintOrderDetail.setTotal_price (orderDetailLua.getPayPrice ());
        mPrintOrderDetail.setRound (orderDetailLua.getRound ());
        String userName = SharePrefenceUtils.readString (OrderDetailActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans
                .SP_LOGIN_USERNAME);
        mPrintOrderDetail.setUsername (userName);


        Gson gson = new Gson ();
        json = gson.toJson (mPrintOrderDetail);
        Log.e ("aaron", "打印数据>>>>>" + json);

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

    private void setLuaData (String json) {
        OrderDetail orderDetailLua = new Gson ().fromJson (json, OrderDetail.class);
        // 菜品合计
        double basePrice = Double.parseDouble (orderDetailLua.getBasePrice ());
        mSubtotalTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (basePrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        // 税收
        double gstPrice = Double.parseDouble (orderDetailLua.getTotalGst ());
        mConsumptionTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (gstPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        double servicePrice = Double.parseDouble (orderDetailLua.getTotalServiceTax ());
        mServiceTaxTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (servicePrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        double roundPrice = Double.parseDouble (orderDetailLua.getRound ());
        mRoundTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (roundPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        // 总计
        double totalPrice = Double.parseDouble (orderDetailLua.getPayPrice ());
        myBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (totalPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
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
