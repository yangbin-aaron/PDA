package com.toughegg.teorderpo.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.view.swipemenu.SwipeMenu;
import com.toughegg.andytools.view.swipemenu.SwipeMenuCreator;
import com.toughegg.andytools.view.swipemenu.SwipeMenuItem;
import com.toughegg.andytools.view.swipemenu.SwipeMenuListView;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.adapters.ShoppingCartListViewAdapter;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;
import com.toughegg.teorderpo.mvp.mvppresenter.ShoppingCartPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.ShoppingCartPresenterInf;
import com.toughegg.teorderpo.view.MyBottomBar;
import com.toughegg.teorderpo.view.MyBottomBar.OnBottomBarListener;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by toughegg on 15/9/11.
 * 购物车
 */
public class ShoppingCartActivity extends BaseActivity implements OnTopActionBarClickListener,
        OnItemClickListener, OnBottomBarListener {

    private TEOrderPoApplication mTEOrderPoApplication;

    private MyTopActionBar mMyTopActionBar;
    private MyBottomBar myBottomBar;
    private SwipeMenuListView mSwipeMenuListView;
    private ShoppingCartListViewAdapter mShoppingCartListViewAdapter;
    private List<ShoppingCart> mShoppingCartList;

    private ShoppingCartPresenterInf mShoppingCartPresenterImp;
    private int mOrderNetSize = 0;// 网络菜单的数量

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_SHOPPINGCARTLIST:
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
                    mShoppingCartList = (List<ShoppingCart>) hashMap.get ("shoppingcartlist");
                    showShoppingCartList ();
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA)) {
                myBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (mTEOrderPoApplication.shoppingCartPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
            } else if (action.equals (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY)) {
                finish ();
            } else if (action.equals (TEOrderPoConstans.ACTION_CLOSE_SHOPPINGCAR_ACTIVITY)) {
                toBack ();
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_shoppingcart);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplicationContext ();
        if (mTEOrderPoApplication.orderNetResultData != null) {
            mOrderNetSize = mTEOrderPoApplication.orderNetResultData.getItem ().size ();
        }
        mShoppingCartPresenterImp = new ShoppingCartPresenterImp ();
        initTopActionBar ();
        initView ();

        // 注册广播 改变菜品份数和价格
        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA);
        filter.addAction (TEOrderPoConstans.ACTION_CLOSE_SHOPPINGCAR_ACTIVITY);
        filter.addAction (TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY);
        registerReceiver (mReceiver, filter);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        mShoppingCartPresenterImp.showShoppingCartList (mHandler);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);
    }

    private void initTopActionBar () {
        mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.actionbar_shoppingcart_title_str);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initView () {
        myBottomBar = (MyBottomBar) findViewById (R.id.myBottomBar);
        myBottomBar.setNumber (false, 0);
        myBottomBar.showLeftImg (false);
        myBottomBar.showMiddleLayout (false);
        myBottomBar.setRightText (true, R.string.order_button_str_next);
        myBottomBar.setOnBottomBarListener (this);
        mSwipeMenuListView = (SwipeMenuListView) findViewById (R.id.activity_shoppingcart_listview);
        myBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN +
                StringUtils.getPriceString (mTEOrderPoApplication.shoppingCartPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
    }

    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {

    }

    private void showShoppingCartList () {
        orderNetToShoppingCart ();
        if (mShoppingCartListViewAdapter == null) {
            mShoppingCartListViewAdapter = new ShoppingCartListViewAdapter (this, mShoppingCartList);
            mShoppingCartListViewAdapter.setShoppingCartPresenterImp (mShoppingCartPresenterImp);
            mShoppingCartListViewAdapter.setPosition (mIntegerList);
            mSwipeMenuListView.setAdapter (mShoppingCartListViewAdapter);
            mSwipeMenuListView.setOnItemClickListener (this);
            setDeleteMenu ();
        } else {
            mShoppingCartListViewAdapter.setShoppingCarts (mShoppingCartList);
        }
    }

    /**
     * 将网络订单数据变为ShoppingCart
     */
    private void orderNetToShoppingCart () {
        if (mTEOrderPoApplication.orderNetResultData != null && mTEOrderPoApplication.orderNetResultData.getItem ().size () > 0) {
            List<ShoppingCart> tempCarts = new ArrayList<> ();
            for (int i = 0; i < mTEOrderPoApplication.orderNetResultData.getItem ().size (); i++) {
                List<OrderNetResultDataItem> itemList = mTEOrderPoApplication.orderNetResultData.getItem ();
                OrderNetResultDataItem item = itemList.get (i);
                // 重点＊＊＊＊＊＊＊＊＊＊＊＊***************＊＊＊＊＊＊＊＊＊＊＊
                if (item.getAction () == 3) {// 之前删除的菜品还原成2，只有本次删除的才能是3
                    item.setAction (2);
                    itemList.set (i, item);
                    mTEOrderPoApplication.orderNetResultData.setItem (itemList);
                }
                ShoppingCart shoppingCart = new ShoppingCart ();
                shoppingCart.setId (-2);
                shoppingCart.setDishId (item.getMenuItemId ());
                shoppingCart.setPrice (item.getMenuPrice ());
                shoppingCart.setCopies (item.getCount ());
                shoppingCart.setCode (item.getCode ());
                shoppingCart.setRemark (item.getRemark ());
                shoppingCart.setName (item.getName ());
                shoppingCart.setIsDeleted (item.isDeleted ());
                if (item.isDeleted ()) {
                    mIntegerList.add (i);
                }
                List<Option> labels = new ArrayList<> ();
                double totalPrice = Double.parseDouble (item.getMenuPrice ());
                if (item.getModifier () != null && item.getModifier ().size () > 0) {
                    for (OrderNetResultItemModifier modifier : item.getModifier ()) {
                        double price = CountUtils.mul (modifier.getUnitPrice (), modifier.getCount () + "");
                        totalPrice = CountUtils.add (totalPrice, price);
                        Option option = new Option ();
                        option.setShoppingCartId (-2);
                        option.setId (modifier.getModifierOptionId ());
                        option.setItemModifierId (modifier.getModifierId ());
                        option.setCount (modifier.getCount ());
                        option.setName (modifier.getName ());
                        option.setPrice (modifier.getUnitPrice ());
                        labels.add (option);
                    }
                }
                shoppingCart.setTotalPrice (StringUtils.getPriceString (totalPrice, "#0.00"));
                shoppingCart.setOptionList (labels);
                tempCarts.add (shoppingCart);
            }
            mShoppingCartList.addAll (0, tempCarts);
        }
    }

    private List<Integer> mIntegerList = new ArrayList<> ();

    private void setDeleteMenu () {
        final SwipeMenuCreator creator = new SwipeMenuCreator () {

            @Override
            public void create (SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem (ShoppingCartActivity.this);
                // set item background
                deleteItem.setBackground (new ColorDrawable (Color.rgb (253, 91, 0)));
                // set item width
                deleteItem.setWidth ((int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, 80, getResources ().getDisplayMetrics ()));
                // set item title
                deleteItem.setTitle (R.string.activity_add_printer_delete);
                // set item title fontsize
                deleteItem.setTitleSize (18);
                // set item title font color
                deleteItem.setTitleColor (Color.WHITE);
                menu.addMenuItem (deleteItem);
            }
        };

        // set creator
        mSwipeMenuListView.setMenuCreator (creator);
        mSwipeMenuListView.setmOnLeftItemClickListener (new SwipeMenuListView.OnLeftItemClickListener () {
            @Override

            public boolean onLeftItemClick (int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ShoppingCart cart = mShoppingCartList.get (position);
                        double deleteTotalPrice = CountUtils.mul (cart.getTotalPrice (), cart.getCopies () + "");
                        mTEOrderPoApplication.shoppingCartPrice = CountUtils.sub (mTEOrderPoApplication.shoppingCartPrice, deleteTotalPrice);
                        myBottomBar.setLeftPrice (true, TEOrderPoConstans.DOLLAR_SIGN
                                + StringUtils.getPriceString (mTEOrderPoApplication.shoppingCartPrice, TEOrderPoConstans.PRICE_FORMAT_TWO));
                        mTEOrderPoApplication.shoppingCartCount -= cart.getCopies ();
                        if (position >= mOrderNetSize) {// 本地数据，删除本地数据库
                            // 在数据库删除
                            mShoppingCartPresenterImp.deleteShoppingCart (cart);
                            // 删除该数据
                            mShoppingCartList.remove (position);
                            mShoppingCartListViewAdapter.setShoppingCarts (mShoppingCartList);
                        } else {
                            if (!cart.getIsDeleted ()) {
                                OrderNetResultData data = mTEOrderPoApplication.orderNetResultData;
                                List<OrderNetResultDataItem> itemList = data.getItem ();
                                OrderNetResultDataItem item = itemList.get (position);
                                item.setIsDeleted (true);
                                item.setAction (3);
                                itemList.set (position, item);
                                data.setItem (itemList);
                                mTEOrderPoApplication.orderNetResultData = data;

                                cart.setIsDeleted (true);
                                mShoppingCartList.set (position, cart);
                                mShoppingCartListViewAdapter.setShoppingCarts (mShoppingCartList);

                                mIntegerList.add (position);
                                mShoppingCartListViewAdapter.setPosition (mIntegerList);
                            }
                        }
                        if (mShoppingCartList.size () == 0 && mOrderNetSize == 0) {
                            toBack ();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        if (mSwipeMenuListView.isOpen ()) {
            return;
        } else {
            Intent intent = new Intent (getApplication (), DishDetailsActivity.class);
            Bundle bundle = new Bundle ();
            bundle.putSerializable (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_SHOPPINGCART, mShoppingCartList.get (position));
            int from = DishDetailsActivity.FROM_SHOP;
            boolean isCanClick = true;
            if (mTEOrderPoApplication.orderNetResultData != null) {
                if (position < mTEOrderPoApplication.orderNetResultData.getItem ().size ()) {
                    from = DishDetailsActivity.FROM_NET;
                    if (mTEOrderPoApplication.orderNetResultData.getItem ().get (position).isDeleted ()) {
                        isCanClick = false;
                    }
                }
            }
            bundle.putInt (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_FROM, from);
            intent.putExtras (bundle);
            intent.putExtra ("position", position);
            if (isCanClick) {
                startActivity (intent);
                overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
            }
        }
    }

    @Override
    public void onBottomBarLeftListener () {

    }

    @Override
    public void onBottomBarRightListener () {
        if (!flag ()) {
            DialogUtils.createErrorDialog (this, R.string.order_dialog_message_donnotorder);
        } else {
            Intent intent = new Intent (this, OrderDetailActivity.class);
            startActivity (intent);
            overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
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
