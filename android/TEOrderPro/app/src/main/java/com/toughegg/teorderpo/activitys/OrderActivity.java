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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.view.fixedlistheader.PinnedHeaderListView;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.search.TESearchActivity;
import com.toughegg.teorderpo.adapters.DishCategoryListViewAdapter;
import com.toughegg.teorderpo.adapters.DishInfoListViewAdapter;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;
import com.toughegg.teorderpo.mvp.mvppresenter.OrderPresenterImp;
import com.toughegg.teorderpo.view.MyBottomBar;
import com.toughegg.teorderpo.view.MyBottomBar.OnBottomBarListener;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;
import com.toughegg.teorderpo.view.dialogs.MyDialogOneBtn;
import com.toughegg.teorderpo.view.dialogs.MyDialogTwoBtn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by toughegg on 15/8/6.
 * 点菜界面
 */
public class OrderActivity extends BaseActivity implements OnTopActionBarClickListener, OnItemClickListener, OnScrollListener,
        OnBottomBarListener {
    private TEOrderPoApplication mTEOrderPoApplication;
    private MyBottomBar mMyBottomBar;
    // 中间
    private ListView mCategoryListView;
    private PinnedHeaderListView mDishListView;//菜品分类列表和所有菜品列表
    private DishCategoryListViewAdapter mDishCategoryListViewAdapter;
    private List<DishCategory> mDishCategories;
    private DishInfoListViewAdapter mDishInfoListViewAdapter;
    private List<DishItems> mDishItemsList = new ArrayList<> ();//菜品信息 List
    private List<Integer> mCountList = new ArrayList<> ();
    private DishItems mDishItems;          //菜品信息

    private boolean isFirst = true;// 是否第一次加载菜单
    private int isToShopOrDetails = 0;//1 为去购物车 0 为去菜品详情  2 为去订单详情

    // 接口
    private OrderPresenterImp mOrderPresenterImp;
    private Dialog mLoadingDialog;

    private boolean isScroll = true;

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_ORDER_DISHLIST:
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
                    setCategoryAndDIshList (hashMap);
                    mLoadingDialog.dismiss ();
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_ORDER_SAVE_OK:
                    notifySaveSuccess ();
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_ORDER_DELETE_OK:
                    notifyBack ();
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA)) {
                mMyBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (mTEOrderPoApplication.shoppingCartPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
                mMyBottomBar.setNumber (true, mTEOrderPoApplication.shoppingCartCount);
            } else if (action.equals (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY)) {
                finish ();
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_order);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplicationContext ();
        initTopActionBar ();//初始化TopBar
        initBottomBar ();//初始化BottomBar
        initView ();//初始化界面
        // 注册广播 改变菜品份数
        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA);
        filter.addAction (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY);
        registerReceiver (mReceiver, filter);
    }


    @Override
    protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);//销毁广播
    }


    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        if (mTEOrderPoApplication.orderNetResultData != null) {
            mMyTopActionBar.setTitleTextView (mTEOrderPoApplication.orderNetResultData.getTableCode ());
        } else {
            mMyTopActionBar.setTitleTextView (R.string.actionbar_order_title_str);
        }
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setRightImageView (R.drawable.fangda);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initBottomBar () {
        mMyBottomBar = (MyBottomBar) findViewById (R.id.myBottomBar);
        mMyBottomBar.setLeftText (false);
        mMyBottomBar.setOnBottomBarListener (this);
    }

    private void initView () {
        mCategoryListView = (ListView) findViewById (R.id.action_order_category_listView);
        mDishListView = (PinnedHeaderListView) findViewById (R.id.action_order_dish_listView);
        mOrderPresenterImp = new OrderPresenterImp ();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        mLoadingDialog = DialogUtils.createLoadingDialog (this, R.string.app_please_wait);
        mLoadingDialog.show ();
        mOrderPresenterImp.showDishAndCategoryView (this, mHandler);
    }

    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {
        Intent intent = new Intent ();
        intent.setClass (OrderActivity.this, TESearchActivity.class);
        startActivity (intent);
        overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
    }

    private void toShoppingCartActivity () {
        if (!flag ()) {
            DialogUtils.createErrorDialog (this, R.string.order_dialog_message_donnotorder);
        } else {
            // 将数据保存到购物车
            isToShopOrDetails = 1;
            mOrderPresenterImp.saveShoppingCartList (mTEOrderPoApplication.shoppingCartList, mHandler);
        }
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        updateData (position);

    }

    private void updateData (int position) {
        mDishCategoryListViewAdapter.setSelectIndex (position);
        int rightSection = 0;
        for (int i = 0; i < position; i++) {
            rightSection += mDishInfoListViewAdapter.getCountForSection (i) + 1;
        }
        mDishListView.setSelection (rightSection);  // 设置右边设置置顶
    }

    @Override
    public void onScrollStateChanged (AbsListView view, int scrollState) {
        Log.e ("hcc", "onScrollStateChanged-->>>");
    }

    @Override
    public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isScroll) {
            mDishCategoryListViewAdapter.setSelectIndex (mDishInfoListViewAdapter.getSectionForPosition (firstVisibleItem));
            isScroll = false;
        } else {
            isScroll = true;
        }
    }

    private void setCategoryAndDIshList (HashMap<String, Object> hashMap) {
        List<DishCategory> dishCategories = (List<DishCategory>) hashMap.get ("category");
        final List<DishItems> mDishItemsList = (List<DishItems>) hashMap.get ("dish");
        List<Integer> countList = (List<Integer>) hashMap.get ("interger");

//        Log.e("aaron", "setCategoryAndDIshList-->>>"+mDishItemsList.toString ());
        if (dishCategories == null) {
            final MyDialogOneBtn myDialogOneBtn = new MyDialogOneBtn (this);
            myDialogOneBtn.setMessage (R.string.order_get_catrgory_fail);
            myDialogOneBtn.setOnDialogClickListener (new MyDialogOneBtn.OnDialogClickListener () {
                @Override
                public void onConfirmClicked () {
                    notifyBack ();
                    myDialogOneBtn.dismiss ();
                }
            });
            myDialogOneBtn.show ();
            return;
        }

        // -==================-
        double orderNetPrice = 0;// 网络订单总价格
        int mOrderNetCopies = 0;
        if (mTEOrderPoApplication.orderNetResultData != null) {
            for (int i = 0; i < mTEOrderPoApplication.orderNetResultData.getItem ().size (); i++) {
                OrderNetResultDataItem item = mTEOrderPoApplication.orderNetResultData.getItem ().get (i);
                if (!item.isDeleted ()) {
                    int copies = item.getCount ();
                    mOrderNetCopies += copies;
                    double totalPrice = Double.parseDouble (item.getMenuPrice ());
                    if (item.getModifier () != null && item.getModifier ().size () > 0) {
                        for (OrderNetResultItemModifier modifier : item.getModifier ()) {
                            double subPrice = CountUtils.mul (modifier.getUnitPrice (), modifier.getCount () + "");
                            totalPrice = CountUtils.add (totalPrice, subPrice);
                        }
                    }
                    double prices = CountUtils.mul (totalPrice, copies);
                    orderNetPrice = CountUtils.add (orderNetPrice, prices);
                }
            }
        }
        mTEOrderPoApplication.shoppingCartCount += mOrderNetCopies;
        mTEOrderPoApplication.shoppingCartPrice = CountUtils.add (mTEOrderPoApplication.shoppingCartPrice, orderNetPrice);
        // -==================-

        mMyBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (mTEOrderPoApplication.shoppingCartPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
        mMyBottomBar.setNumber (true, mTEOrderPoApplication.shoppingCartCount);

        this.mDishCategories = dishCategories;
        this.mDishItemsList = mDishItemsList;
        mCountList = countList;


        if (isFirst) {
            // 设置分类数据
            mDishCategoryListViewAdapter = new DishCategoryListViewAdapter (this, mDishCategories);
            mCategoryListView.setAdapter (mDishCategoryListViewAdapter);
            mCategoryListView.setOnItemClickListener (this);
            // 设置菜品数据
            mDishInfoListViewAdapter = new DishInfoListViewAdapter (this, mDishCategories, mDishItemsList, mCountList, mDishCategoryListViewAdapter);
            mDishListView.setAdapter (mDishInfoListViewAdapter);
            mDishListView.setOnScrollListener (this);
            mDishListView.setOnItemClickListener (new PinnedHeaderListView.OnItemClickListener () {
                @Override
                public void onItemClick (AdapterView<?> adapterView, View view, int section, int position, long id) {
                    // 区分每个分类下的每个item
                    if (section < mDishCategories.size ()) {
                        int countNumber = 0;
                        mCountList.get (section); // 当前分类下菜品个数；
                        for (int i = 0; i < section; i++) {
                            countNumber += mCountList.get (i);
                        }
                        mDishItems = mDishItemsList.get (position + countNumber);
                    }
                    // 将数据保存到购物车
                    isToShopOrDetails = 0;
                    mOrderPresenterImp.saveShoppingCartList (mTEOrderPoApplication.shoppingCartList, mHandler);
                }

                @Override
                public void onSectionClick (AdapterView<?> adapterView, View view, int section, long id) {

                }
            });
            isFirst = false;
        } else {
            mDishCategoryListViewAdapter.setDishCategories (mDishCategories);
            mDishInfoListViewAdapter.setData (mDishCategories, mDishItemsList, mCountList, mDishCategoryListViewAdapter);
        }
    }

    /**
     * 将数据保存到购物车，清空数据
     */
    private void notifySaveSuccess () {
        if (isToShopOrDetails == 1) {// 跳转到购物车界面
            startActivity (new Intent (OrderActivity.this, ShoppingCartActivity.class));
            overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
        } else if (isToShopOrDetails == 0) {// 跳转到菜品详情界面
            Intent intent = new Intent (getApplication (), DishDetailsActivity.class);
            Bundle bundle = new Bundle ();
            bundle.putInt (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_FROM, DishDetailsActivity.FROM_ORDER);
            bundle.putSerializable (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_DISHINFO, mDishItems);
            intent.putExtras (bundle);
            startActivity (intent);
            overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
        } else if (isToShopOrDetails == 2) {// 跳转到订单详情界面
            Intent intent = new Intent (this, OrderDetailActivity.class);
            startActivity (intent);
            overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
        }
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
//        int count = 0;
//        if (mTEOrderPoApplication.shoppingCartList != null) {
//            for (int i = 0; i < mTEOrderPoApplication.shoppingCartList.size (); i++) {
//                count = mTEOrderPoApplication.shoppingCartList.get (i).getCopies ();
//                if (count > 0) {
//                    break;
//                }
//            }
//        }
//        if (count == 0) {
//            notifyBack ();
//            return;
//        }

        if (mTEOrderPoApplication.shoppingCartPrice==0){
            notifyBack ();
            return;
        }

        // 是否删除购物车数据（是否退出点菜）
        final MyDialogTwoBtn myDialogTwoBtn = new MyDialogTwoBtn (this);
        myDialogTwoBtn.setMessage (R.string.order_dialog_message_cancelorder);
        myDialogTwoBtn.setLeftRightBtnId (R.string.app_confirm, R.string.app_cancel);
        myDialogTwoBtn.setOnDialogClickListener (new MyDialogTwoBtn.OnDialogClickListener () {
            @Override
            public void onLeftClicked () {// 退出
                mOrderPresenterImp.deleteShoppingCartData (mHandler);
                myDialogTwoBtn.dismiss ();
            }

            @Override
            public void onRightClicked () {// 取消
                myDialogTwoBtn.dismiss ();
            }
        });
        myDialogTwoBtn.show ();
    }

    private void notifyBack () {
        finish ();
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onBottomBarLeftListener () {
        toShoppingCartActivity ();
    }

    //bottomBar  确认订单
    @Override
    public void onBottomBarRightListener () {
        if (!flag ()) {
            DialogUtils.createErrorDialog (this, R.string.order_dialog_message_donnotorder);
        } else {
            // 将数据保存到购物车
            isToShopOrDetails = 2;
            mOrderPresenterImp.saveShoppingCartList (mTEOrderPoApplication.shoppingCartList, mHandler);
        }
    }

    // 是否有点单
    private boolean flag () {
        boolean b = false;
        if (mTEOrderPoApplication.orderNetResultData != null) {
            b = true;
        } else {
            if (mTEOrderPoApplication.shoppingCartCount > 0) {
                b = true;
            }
        }
        return b;
    }
}
