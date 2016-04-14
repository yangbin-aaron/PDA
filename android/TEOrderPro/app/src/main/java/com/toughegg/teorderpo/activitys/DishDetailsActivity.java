package com.toughegg.teorderpo.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.adapters.DishLabelGridViewAdapter;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemModifier;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;
import com.toughegg.teorderpo.mvp.mvppresenter.DishDetailsPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.DishDetailsPresenterInf;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by toughegg on 15/8/7.
 * 菜品详情界面
 */
public class DishDetailsActivity extends BaseActivity implements OnTopActionBarClickListener, OnClickListener {

    private TEOrderPoApplication mTEOrderPoApplication;
    // ********** 中间部分 **********
//    private RelativeLayout mRelativeLayout;// 整个布局
    private EditText mRemarkEditText;// 备注编辑框
    private TextView mRemarkLengthTextView;// 备注的字符数
    private int mRemarkETMaxWordCount = 10;// 备注的最大字符数
    private TextView mCopiseTextView;// 菜名，菜价，份数
    private GridView mLabelGridView;// 菜品标签
    private DishLabelGridViewAdapter mDishLabelGridViewAdapter;
    private TextView mOptionTextView;// 菜品标签属性
    private TextView mAddPriceTextView, mTotalPriceTextView;// 加价，总价

    private ShoppingCart mShoppingCart;// shopping实体
    private DishDetailsPresenterInf mIDishDetailsPresenter;

    public static final int FROM_ORDER = 100, FROM_SHOP = 101, FROM_NET = 102;
    private int fromOrderOrShop = FROM_ORDER;// 默认从菜单列表，反之从购物车
    private int mNetPosition = -1;

    private double mTotalPrice = 0;// 总价
    private double mTotalPriceN = 0;// 原有总价

    private String mModifierString;// 该菜品对应的加料等选项ID集合,以逗号隔开

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_SETDATA:
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
                    setDishDetails (hashMap);
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_SAVE_OK:
                    saveSuccess ();
                    break;
                case TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_UPDATE_OK:
                    updateSuccess ();
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (TEOrderPoConstans.ACTION_UPDATE_DISHINFO_PRICE)) {
                mAddPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        CountUtils.mul (mTEOrderPoApplication.addPrice, mShoppingCart.getCopies ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
                mTotalPrice = CountUtils.add (mTEOrderPoApplication.addPrice + "", mShoppingCart.getPrice ());
                mShoppingCart.setTotalPrice (mTotalPrice + "");
                mTotalPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                        + StringUtils.getPriceString (
                        CountUtils.mul (mTotalPrice, mShoppingCart.getCopies ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 设置软键盘弹出时屏幕自适应
        setContentView (R.layout.activity_order_dish);
        mTEOrderPoApplication = (TEOrderPoApplication) getApplicationContext ();
        mTEOrderPoApplication.shoppingCartDishLabelList.clear ();
        // 得到菜品基本信息
        fromOrderOrShop = getIntent ().getIntExtra (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_FROM, 100);
        if (fromOrderOrShop == FROM_ORDER) {// mShoppingCart为空
            DishItems dishItems = (DishItems) getIntent ().getSerializableExtra (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_DISHINFO);
            mShoppingCart = new ShoppingCart ();
            mShoppingCart.setDishId (dishItems.getId ());
            mShoppingCart.setPrice (dishItems.getPrice ());
            mShoppingCart.setTotalPrice (dishItems.getPrice ());
            // 如果从菜品列表进入则直接获取
            // 查数据时，字符串需要加引号
//            Log.e ("aaron",dishItems.getModifier ().toString ());
            if (dishItems.getModifier () != null && dishItems.getModifier ().size () > 0) {
                mModifierString = "'" + dishItems.getModifier ().get (0) + "'";
                for (int i = 1; i < dishItems.getModifier ().size (); i++) {
                    mModifierString += ",'" + dishItems.getModifier ().get (i) + "'";
                }
            }
            mShoppingCart.setName (dishItems.getName ());
            mShoppingCart.setCode (dishItems.getCode ());
            mShoppingCart.setCopies (1);
        } else if (fromOrderOrShop == FROM_SHOP) {
            mShoppingCart = (ShoppingCart) getIntent ().getSerializableExtra (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_SHOPPINGCART);
            mModifierString = TEOrderPoDataBaseHelp.findModifierListByDishId (mShoppingCart.getDishId ());// 如果从购物车列表进入则从数据库查询
            mTEOrderPoApplication.shoppingCartDishLabelList.clear ();
        } else if (fromOrderOrShop == FROM_NET) {
            mNetPosition = getIntent ().getIntExtra ("position", -1);
            mShoppingCart = (ShoppingCart) getIntent ().getSerializableExtra (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_SHOPPINGCART);
            mModifierString = TEOrderPoDataBaseHelp.findModifierListByDishId (mShoppingCart.getDishId ());// 如果从购物车列表进入则从数据库查询
            mTEOrderPoApplication.shoppingCartDishLabelList.clear ();
        }
        mTEOrderPoApplication.addPrice = CountUtils.sub (mShoppingCart.getTotalPrice (), mShoppingCart.getPrice ());
        initTopActionBar ();
        initView ();
        // 从网络获取菜单选项,用Handler接收数据并处理数据
        mIDishDetailsPresenter = new DishDetailsPresenterImp ();
        mIDishDetailsPresenter.showDishDetails (mModifierString, mHandler);

        IntentFilter filter = new IntentFilter ();
        filter.addAction (TEOrderPoConstans.ACTION_UPDATE_DISHINFO_PRICE);
        registerReceiver (mReceiver, filter);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);
    }

    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.actionbar_order_dish_title_str);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
        mMyTopActionBar.setRightClick (false);
    }

    private void initView () {
//        mRelativeLayout = (RelativeLayout) findViewById (R.id.activity_order_dish_layout);
//        mRelativeLayout.setOnClickListener (this);
        // －－－－基本信息－－－－
        TextView mDishNameTextView = (TextView) findViewById (R.id.activity_order_dish_dishname_textView);
        String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        switch (lang) {
            case TEOrderPoConstans.LANGUAGE_CHINESE:
                mDishNameTextView.setText (mShoppingCart.getName ().getZh_CN ());
                break;
            case TEOrderPoConstans.LANGUAGE_ENGLISH:
                mDishNameTextView.setText (mShoppingCart.getName ().getEn_US ());
                break;
            case TEOrderPoConstans.LANGUAGE_CHINESE_TW:
                mDishNameTextView.setText (mShoppingCart.getName ().getEn_US ());
                break;
        }
        TextView mDishCodeTextView = (TextView) findViewById (R.id.activity_order_dish_dishcode_textView);
        mDishCodeTextView.setText (mShoppingCart.getCode ());
        mCopiseTextView = (TextView) findViewById (R.id.activity_order_dish_copies_textView);
        mCopiseTextView.setText ("" + mShoppingCart.getCopies ());// 默认一份
        TextView mPriceTextView = (TextView) findViewById (R.id.activity_order_dish_price_textView);
        mPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (Double.parseDouble (mShoppingCart.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        LinearLayout mMinusLinearLayout = (LinearLayout) findViewById (R.id.activity_order_dish_minus_linearlayout);
        mMinusLinearLayout.setOnClickListener (this);
        LinearLayout mPlusLinearLayout = (LinearLayout) findViewById (R.id.activity_order_dish_plus_linearlayout);
        mPlusLinearLayout.setOnClickListener (this);
        if (fromOrderOrShop == FROM_NET) {
            mMinusLinearLayout.setVisibility (View.INVISIBLE);
            mPlusLinearLayout.setVisibility (View.INVISIBLE);
        }
        // －－－－菜单选项－－－－
        mOptionTextView = (TextView) findViewById (R.id.activity_order_dish_dishoption_textView);
        mLabelGridView = (GridView) findViewById (R.id.activity_order_dish_option_gridview);
        // －－－－备注－－－－
        mRemarkEditText = (EditText) findViewById (R.id.activity_order_dish_remark_editText);
        if (mShoppingCart.getRemark () != null && !mShoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
            mRemarkEditText.setText (mShoppingCart.getRemark ());
            mRemarkEditText.setSelection (mShoppingCart.getRemark ().length ());
        }
        mRemarkEditText.addTextChangedListener (mRemarkWatcher);
        mRemarkLengthTextView = (TextView) findViewById (R.id.activity_order_dish_wordcount_textView);
        String language = Locale.getDefault ().getLanguage ().trim ();// 获取当前语言
        if (language.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            mRemarkETMaxWordCount = 20;
        } else {
            mRemarkETMaxWordCount = 10;
        }
        mRemarkLengthTextView.setText (mRemarkEditText.getText ().toString ().length () + "/" + mRemarkETMaxWordCount);
        // －－－－总价和加价－－－－
        mAddPriceTextView = (TextView) findViewById (R.id.activity_order_dish_jiajia_textView);
        mAddPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                CountUtils.mul (mTEOrderPoApplication.addPrice, mShoppingCart.getCopies ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        mTotalPriceTextView = (TextView) findViewById (R.id.activity_order_dish_total_textView);
        mTotalPriceN = CountUtils.mul (mShoppingCart.getTotalPrice (), mShoppingCart.getCopies () + "");
        mTotalPrice = Double.parseDouble (mShoppingCart.getTotalPrice ());
        mTotalPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                CountUtils.mul (mShoppingCart.getTotalPrice (), mShoppingCart.getCopies () + ""), TEOrderPoConstans.PRICE_FORMAT_TWO));
        // －－－－保存按钮－－－－
        TextView mSaveTextView = (TextView) findViewById (R.id.activity_order_dish_save_textView);
        if (fromOrderOrShop == FROM_ORDER) {
            mSaveTextView.setText (R.string.order_dish_button_str_save);
        } else {
            mSaveTextView.setText (R.string.order_dish_button_str_update);
        }
        mSaveTextView.setOnClickListener (this);
    }

    private TextWatcher mRemarkWatcher = new TextWatcher () {
        CharSequence temp;

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            String mRemarkEnter = mRemarkEditText.getText ().toString ();
            if (mRemarkEnter.length () > mRemarkETMaxWordCount) {
                mRemarkEditText.setText (mRemarkEnter.substring (0, mRemarkETMaxWordCount));
                mRemarkEditText.setSelection (mRemarkETMaxWordCount);
            }
            mRemarkLengthTextView.setText (mRemarkEnter.length () + "/" + mRemarkETMaxWordCount);
        }

        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void afterTextChanged (Editable s) {
        }
    };

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
            case R.id.activity_order_dish_plus_linearlayout:
                if (Integer.parseInt (mCopiseTextView.getText ().toString ()) < TEOrderPoConstans.DISH_MAX_COPISE) {
                    mCopiseTextView.setText ((Integer.parseInt (mCopiseTextView.getText ().toString ()) + 1) + "");
                    setPriceTextView ();
                }
                break;
            case R.id.activity_order_dish_minus_linearlayout:
                if (Integer.parseInt (mCopiseTextView.getText ().toString ()) > 1) {// 最少需要点一份
                    mCopiseTextView.setText ((Integer.parseInt (mCopiseTextView.getText ().toString ()) - 1) + "");
                    setPriceTextView ();
                }
                break;
            case R.id.activity_order_dish_save_textView:// 保存
                mShoppingCart.setRemark (mRemarkEditText.getText ().toString ());
                if (mRemarkEditText.getText ().toString ().equals ("")) {
                    mShoppingCart.setRemark (TEOrderPoDataBase.DB_NULL);
                }

                List<Option> optionList = new ArrayList<> ();
                if (fromOrderOrShop == FROM_ORDER) {// 保存
                    for (Option option : mTEOrderPoApplication.shoppingCartDishLabelList) {
                        if (option.getCount () > 0) {
                            optionList.add (option);
                        }
                    }
                    mShoppingCart.setOptionList (optionList);
                    mIDishDetailsPresenter.saveShoppingCart (mShoppingCart, mHandler);
                } else if (fromOrderOrShop == FROM_SHOP) {// 修改
                    for (Option option : mTEOrderPoApplication.shoppingCartDishLabelList) {
                        if (option.getCount () > 0) {
                            optionList.add (option);
                        }
                    }
                    mShoppingCart.setOptionList (optionList);
                    mIDishDetailsPresenter.updateShoppingCart (mShoppingCart, mHandler);
                } else if (fromOrderOrShop == FROM_NET) {
                    // 修改全局变量里面的数据
                    List<OrderNetResultDataItem> itemList = mTEOrderPoApplication.orderNetResultData.getItem ();
                    OrderNetResultDataItem item = itemList.get (mNetPosition);
                    item.setRemark (mRemarkEditText.getText ().toString ());
                    item.setAction (2);// 修改订单
                    List<OrderNetResultItemModifier> modifiers = new ArrayList<> ();
                    for (Option option : mTEOrderPoApplication.shoppingCartDishLabelList) {
                        if (option.getCount () > 0) {
                            OrderNetResultItemModifier modifier = new OrderNetResultItemModifier ();
                            modifier.setModifierId (option.getItemModifierId ());
                            modifier.setModifierOptionId (option.getId ());
                            modifier.setCount (option.getCount ());
                            modifier.setName (option.getName ());
                            modifier.setUnitPrice (option.getPrice ());
                            modifiers.add (modifier);
                        }
                    }
                    item.setModifier (modifiers);
                    itemList.set (mNetPosition, item);
                    updateSuccess ();
                }
                break;
        }
    }

    private void setPriceTextView () {
        mShoppingCart.setCopies (Integer.parseInt (mCopiseTextView.getText ().toString ()));
        if (mDishLabelGridViewAdapter != null) {
            mDishLabelGridViewAdapter.setDishCopies (Integer.parseInt (mCopiseTextView.getText ().toString ()));
        }
        mAddPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                CountUtils.mul (mTEOrderPoApplication.addPrice, mShoppingCart.getCopies ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        mTotalPriceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                CountUtils.mul (mShoppingCart.getTotalPrice (), mShoppingCart.getCopies () + ""), TEOrderPoConstans.PRICE_FORMAT_TWO));
        mTotalPrice = Double.parseDouble (mShoppingCart.getTotalPrice ());
    }


    private void setDishDetails (HashMap<String, Object> hashMap) {
        List<ItemModifier> itemModifierList = null;
        List<Option> optionList = null;
        itemModifierList = (List<ItemModifier>) hashMap.get ("dishOptions");
        optionList = (List<Option>) hashMap.get ("dishLabels");
        if (itemModifierList != null && itemModifierList.size () > 0) {
            String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
            switch (lang) {
                case TEOrderPoConstans.LANGUAGE_CHINESE:
                    mOptionTextView.setText (itemModifierList.get (0).getName ().getZh_CN ());
                    break;
                case TEOrderPoConstans.LANGUAGE_ENGLISH:
                    mOptionTextView.setText (itemModifierList.get (0).getName ().getEn_US ());
                    break;
                case TEOrderPoConstans.LANGUAGE_CHINESE_TW:
                    mOptionTextView.setText (itemModifierList.get (0).getName ().getEn_US ());
                    break;
            }

            if (optionList != null && optionList.size () > 0) {
                if ((fromOrderOrShop == FROM_SHOP
                        || fromOrderOrShop == FROM_NET) && mShoppingCart.getOptionList () != null && mShoppingCart.getOptionList ().size () > 0) {
                    for (Option label : mShoppingCart.getOptionList ()) {
                        for (int i = 0; i < optionList.size (); i++) {
                            if (label.getItemModifierId ().equals (optionList.get (i).getItemModifierId ())
                                    && label.getId ().equals (optionList.get (i).getId ())) {
                                optionList.set (i, label);
                                break;
                            }
                        }
                    }
                }
                mTEOrderPoApplication.shoppingCartDishLabelList = optionList;
                mDishLabelGridViewAdapter = new DishLabelGridViewAdapter (this, optionList);
                mDishLabelGridViewAdapter.setDishCopies (mShoppingCart.getCopies ());
                mLabelGridView.setAdapter (mDishLabelGridViewAdapter);
            } else {
                mLabelGridView.setVisibility (View.GONE);
                mOptionTextView.setVisibility (View.GONE);
            }
        } else {
            mLabelGridView.setVisibility (View.GONE);
            mOptionTextView.setVisibility (View.GONE);
        }
    }

    private void saveSuccess () {
        mTEOrderPoApplication.shoppingCartCount += mShoppingCart.getCopies ();
        double prices = CountUtils.mul (mShoppingCart.getTotalPrice (), "" + mShoppingCart.getCopies ());
        mTEOrderPoApplication.shoppingCartPrice = CountUtils.add (mTEOrderPoApplication.shoppingCartPrice, prices);
        sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
        finish ();
    }

    private void updateSuccess () {
        mTEOrderPoApplication.shoppingCartPrice = CountUtils.sub (mTEOrderPoApplication.shoppingCartPrice, mTotalPriceN);
        mTEOrderPoApplication.shoppingCartPrice = CountUtils.add (mTEOrderPoApplication.shoppingCartPrice, CountUtils.mul (mTotalPrice + "",
                mShoppingCart.getCopies () + ""));
        sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
        finish ();
    }
}
