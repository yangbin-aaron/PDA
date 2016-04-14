package com.toughegg.teorderpo.activitys.setting;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toughegg.andytools.systemUtil.DensityUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.andytools.view.swipemenu.SwipeMenu;
import com.toughegg.andytools.view.swipemenu.SwipeMenuCreator;
import com.toughegg.andytools.view.swipemenu.SwipeMenuItem;
import com.toughegg.andytools.view.swipemenu.SwipeMenuListView;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.BaseActivity;
import com.toughegg.teorderpo.adapters.SwipeItemAdapter;
import com.toughegg.teorderpo.print.NetPrintServer;
import com.toughegg.teorderpo.view.MyTopActionBar;
import com.toughegg.teorderpo.view.MyTopActionBar.OnTopActionBarClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lidan on 15/8/22.
 * 添加打印机界面
 */

public class AddPrinterActivity extends BaseActivity implements OnTopActionBarClickListener, View.OnClickListener {
    private EditText receiptEditText, kitchenEditText, kitchenEditText2, counterEditText;//IP地址输入框
    private RelativeLayout receiptPrinterLayout, kitchenPrinterLayout, kitchenPrinterLayout2, counterPrinterLayout;//ip布局
    private SwipeMenuListView receiptPrinterItemsListView, kitchenPrinterItemsListView, kitchenPrinterItemsListView2, counterPrinterItemsListView;
    private SwipeItemAdapter receiptAdapter, kitchenAdapter, kitchenAdapter2, counterAdapter;//IPList  adapter
    private List<String> receiptIPStringList;
    private List<String> kitchenIPStringList;
    private List<String> kitchenIPStringList2;
    private List<String> counterIPStringList;//IP List
    private SwipeMenuCreator creator;//右边滑出的删除布局
    private int mWidth;

    private boolean mIsReadSuccess = false;// 如果没有读取到选项便不会有键盘弹出，此时不能隐藏键盘

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_printer);
        initTopActionBar ();
        initView ();
    }

    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.activity_add_printer_topbar_title);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initView () {
//        IP操作布局
        receiptPrinterLayout = (RelativeLayout) findViewById (R.id.activity_add_printer_receipt_layout);
        kitchenPrinterLayout = (RelativeLayout) findViewById (R.id.activity_add_printer_kitchen_layout);
        kitchenPrinterLayout2 = (RelativeLayout) findViewById (R.id.activity_add_printer_cash_layout);
        counterPrinterLayout = (RelativeLayout) findViewById (R.id.activity_add_printer_counter_layout);
//IP输入框
        receiptEditText = (EditText) findViewById (R.id.activity_add_printer_receipt_edit_text);
        kitchenEditText = (EditText) findViewById (R.id.activity_add_printer_kitchen_edit_text);
        kitchenEditText2 = (EditText) findViewById (R.id.activity_add_printer_cash_edit_text);
        counterEditText = (EditText) findViewById (R.id.activity_add_printer_counter_edit_text);
        // 设置数字输入
        receiptEditText.setInputType (EditorInfo.TYPE_CLASS_PHONE);
        kitchenEditText.setInputType (EditorInfo.TYPE_CLASS_PHONE);
        kitchenEditText2.setInputType (EditorInfo.TYPE_CLASS_PHONE);
        counterEditText.setInputType (EditorInfo.TYPE_CLASS_PHONE);
//IP增加按钮
        TextView receiptAdd = (TextView) findViewById (R.id.activity_add_printer_receipt_add_tv);
        TextView kitchenAdd = (TextView) findViewById (R.id.activity_add_printer_kitchen_add_tv);
        TextView cashAdd = (TextView) findViewById (R.id.activity_add_printer_cash_add_tv);
        TextView counterAdd = (TextView) findViewById (R.id.activity_add_printer_counter_add_tv);
//IP增加按钮点击监听
        receiptAdd.setOnClickListener (this);
        kitchenAdd.setOnClickListener (this);
        cashAdd.setOnClickListener (this);
        counterAdd.setOnClickListener (this);
//        右边删除布局初始化
        creator = new SwipeMenuCreator () {
            @Override
            public void create (SwipeMenu menu) {
                //				右边隐藏的每个部分都是一个SwipeMenuItem对象，如果还需要删除以外的item，直接创建SwipeMenuItem对象然后加入到menu视图即可
                SwipeMenuItem deleteItem = new SwipeMenuItem (AddPrinterActivity.this);    // 创建 "delete" item
                deleteItem.setBackground (new ColorDrawable (Color.rgb (255, 0, 0)));   // 设置右边滑出的delete背景色
                mWidth = DensityUtils.getScreenW (AddPrinterActivity.this);
                deleteItem.setWidth (mWidth / 6); // 设置右边滑出的delete的宽度
                deleteItem.setTitle (R.string.activity_add_printer_delete); // 设置右边滑出的delete文字
                deleteItem.setTitleSize (16);   // 设置右边滑出的delete字体大小
                deleteItem.setTitleColor (Color.WHITE);      // 设置右边滑出的delete颜色
                menu.addMenuItem (deleteItem);  //				将open部分添加到Menu中
            }
        };

        initReceipt ();// 初始化收银
        initKitchen ();// 初始化厨房
        initKitchen2 ();// 初始化厨房2
        initCounter ();// 初始化吧台

        setListViewHeight ();

        HashMap<String, String> printNameAndIp = NetPrintServer.getInstance ().hashMapIp (this, "print_order");
        Iterator iterator = printNameAndIp.entrySet ().iterator ();
        while (iterator.hasNext ()) {
            mIsReadSuccess = true;
            HashMap.Entry entry = (HashMap.Entry) iterator.next ();
            String key = (String) entry.getKey ();
            String ip = (String) entry.getValue ();
            Log.e ("aaron", "Addprinter>>>>>>>>>>>>>>>>>" + key + " -- " + ip);
            if (key.equals (TEOrderPoConstans.RECEIPT)) {
                findViewById (R.id.activity_add_printer_receipt_linearlayout).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_receipt_line).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_receipt_line2).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_receipt_items_layout).setVisibility (View.VISIBLE);
            } else if (key.equals (TEOrderPoConstans.KITCHEN)) {
                findViewById (R.id.activity_add_printer_kitchen_linearlayout).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_kitchen_line).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_kitchen_line2).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_kitchen_items_layout).setVisibility (View.VISIBLE);
            } else if (key.equals (TEOrderPoConstans.CASH)) {
                findViewById (R.id.activity_add_printer_cash_linearlayout).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_cash_line).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_cash_line2).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_cash_items_layout).setVisibility (View.VISIBLE);
            } else if (key.equals (TEOrderPoConstans.BAR)) {
                findViewById (R.id.activity_add_printer_counter_linearlayout).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_counter_line).setVisibility (View.VISIBLE);
                findViewById (R.id.activity_add_printer_counter_items_layout).setVisibility (View.VISIBLE);
            }
        }
    }

    private void initReceipt () {// 收据
        receiptPrinterItemsListView = (SwipeMenuListView) findViewById (R.id.activity_add_printer_receipt_items_layout);
        receiptIPStringList = new ArrayList<> ();//ip 地址数据初始化
        String strCashList = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.RECEIPT);
        if (strCashList != null) {
            try {
                receiptIPStringList = SharePrefenceUtils.string2SceneList (strCashList);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        listRemoveDetailStr (receiptIPStringList);

        receiptAdapter = new SwipeItemAdapter (AddPrinterActivity.this, receiptIPStringList);
        receiptPrinterItemsListView.setAdapter (receiptAdapter);
        IsShowAdd (receiptIPStringList.size (), receiptPrinterLayout);//是否显示ip布局
        swipeListener (receiptPrinterItemsListView, receiptIPStringList, receiptAdapter, TEOrderPoConstans.RECEIPT);
        swipeOnClickListener (receiptPrinterItemsListView, receiptIPStringList, receiptAdapter, TEOrderPoConstans.RECEIPT);
    }

    private void initKitchen () {// 厨房1
        kitchenPrinterItemsListView = (SwipeMenuListView) findViewById (R.id.activity_add_printer_kitchen_items_layout);
        kitchenIPStringList = new ArrayList<> ();
        String strKitchenList = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.KITCHEN);
        if (strKitchenList != null) {
            try {
                kitchenIPStringList = SharePrefenceUtils.string2SceneList (strKitchenList);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        listRemoveDetailStr (kitchenIPStringList);

        kitchenAdapter = new SwipeItemAdapter (AddPrinterActivity.this, kitchenIPStringList);
        kitchenPrinterItemsListView.setAdapter (kitchenAdapter);
        IsShowAdd (kitchenIPStringList.size (), kitchenPrinterLayout);
        swipeListener (kitchenPrinterItemsListView, kitchenIPStringList, kitchenAdapter, TEOrderPoConstans.KITCHEN);
        swipeOnClickListener (kitchenPrinterItemsListView, kitchenIPStringList, kitchenAdapter, TEOrderPoConstans.KITCHEN);
    }

    private void initKitchen2 () {// 厨房2
        kitchenPrinterItemsListView2 = (SwipeMenuListView) findViewById (R.id.activity_add_printer_cash_items_layout);
        kitchenIPStringList2 = new ArrayList<> ();
        String strKitchen2List = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.CASH);
        if (strKitchen2List != null) {
            try {
                kitchenIPStringList2 = SharePrefenceUtils.string2SceneList (strKitchen2List);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        listRemoveDetailStr (kitchenIPStringList2);

        kitchenAdapter2 = new SwipeItemAdapter (AddPrinterActivity.this, kitchenIPStringList2);
        kitchenPrinterItemsListView2.setAdapter (kitchenAdapter2);
        IsShowAdd (kitchenIPStringList2.size (), kitchenPrinterLayout2);
        swipeListener (kitchenPrinterItemsListView2, kitchenIPStringList2, kitchenAdapter2, TEOrderPoConstans.CASH);
        swipeOnClickListener (kitchenPrinterItemsListView2, kitchenIPStringList2, kitchenAdapter2, TEOrderPoConstans.CASH);
    }


    private void initCounter () {// 吧台
        counterPrinterItemsListView = (SwipeMenuListView) findViewById (R.id.activity_add_printer_counter_items_layout);
        counterIPStringList = new ArrayList<> ();
        String strBarList = SharePrefenceUtils.readString (AddPrinterActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.BAR);
        if (strBarList != null) {
            try {
                counterIPStringList = SharePrefenceUtils.string2SceneList (strBarList);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        listRemoveDetailStr (counterIPStringList);

        counterAdapter = new SwipeItemAdapter (this, counterIPStringList);
        counterPrinterItemsListView.setAdapter (counterAdapter);
        IsShowAdd (counterIPStringList.size (), counterPrinterLayout);
        swipeListener (counterPrinterItemsListView, counterIPStringList, counterAdapter, TEOrderPoConstans.BAR);
        swipeOnClickListener (counterPrinterItemsListView, counterIPStringList, counterAdapter, TEOrderPoConstans.BAR);
    }

    private void swipeOnClickListener (final SwipeMenuListView swipeMenuListView, final List<String> ipStringList, final SwipeItemAdapter
            arrayAdapter,
                                       final String printIpName) {
        swipeMenuListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                if (ipStringList.size () > 1) {
                    if (swipeMenuListView.isOpen ()) {
                        return;
                    } else {
                        // 将选中的项移到第一项
                        String tempIpString = ipStringList.get (position);
                        ipStringList.remove (position);
                        ipStringList.add (0, tempIpString);
                        arrayAdapter.setIpStringList (ipStringList);
                        //保存数据
                        try {
                            String ipStr = SharePrefenceUtils.sceneList2String (ipStringList);
                            SharePrefenceUtils.write (AddPrinterActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, printIpName, ipStr);
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }
                    }
                }
            }
        });
    }

    //    处理IP显示删除等监听
    private void swipeListener (final SwipeMenuListView swipeMenuListView, final List<String> ipStringList, final SwipeItemAdapter arrayAdapter,
                                final String printIpName) {
        swipeMenuListView.setMenuCreator (creator);
        // 点击删除监听
        swipeMenuListView.setmOnLeftItemClickListener (new SwipeMenuListView.OnLeftItemClickListener () {
            @Override
            public boolean onLeftItemClick (int position, SwipeMenu menu, int index) {
                ipStringList.remove (position);// 移除该项
                listRemoveDetailStr (ipStringList);
                arrayAdapter.notifyDataSetChanged ();
                setListViewHeight ();

                //保存数据
                try {
                    if (ipStringList.size () == 0) {
                        ipStringList.add (TEOrderPoConstans.PRINT_DETALS_IP);
                    }
                    String ipStr = SharePrefenceUtils.sceneList2String (ipStringList);
                    SharePrefenceUtils.write (AddPrinterActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, printIpName, ipStr);
                } catch (Exception e) {
                    e.printStackTrace ();
                }

                return false;
            }
        });
        swipeMenuListView.setOnSwipeListener (new SwipeMenuListView.OnSwipeListener () {
            @Override
            public void onSwipeStart (int position) {
            }

            @Override
            public void onSwipeEnd (int position) {
            }
        });
        if (!swipeMenuListView.isFastScrollEnabled ()) {
            if (swipeMenuListView.isOpen ()) {
                return;
            }
        }
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.activity_add_printer_receipt_add_tv:// 收据
                verifyIP (receiptEditText, receiptIPStringList, receiptAdapter, TEOrderPoConstans.RECEIPT);
                IsShowAdd (receiptIPStringList.size (), receiptPrinterLayout);
                break;
            case R.id.activity_add_printer_kitchen_add_tv:// 厨房1
                verifyIP (kitchenEditText, kitchenIPStringList, kitchenAdapter, TEOrderPoConstans.KITCHEN);
                IsShowAdd (kitchenIPStringList.size (), kitchenPrinterLayout);
                break;
            case R.id.activity_add_printer_cash_add_tv:// 厨房2
                verifyIP (kitchenEditText2, kitchenIPStringList2, kitchenAdapter2, TEOrderPoConstans.CASH);
                IsShowAdd (kitchenIPStringList2.size (), kitchenPrinterLayout2);
                break;
            case R.id.activity_add_printer_counter_add_tv:// 吧台
                verifyIP (counterEditText, counterIPStringList, counterAdapter, TEOrderPoConstans.BAR);
                IsShowAdd (counterIPStringList.size (), counterPrinterLayout);
                break;
        }

    }

    //    是否显示添加IP布局
    private void IsShowAdd (int ipListNumber, RelativeLayout isShowAddIPLayout) {
        if (ipListNumber > 100) {// 最多添加数量
            isShowAddIPLayout.setVisibility (View.GONE);
        } else {
            isShowAddIPLayout.setVisibility (View.VISIBLE);
        }
    }

    //验证能否添加ip
    private void verifyIP (EditText inputIP, List<String> ipStringList, SwipeItemAdapter adapter, String printIpName) {
        Pattern pa = Pattern.compile ("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1," +
                "2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
        String inputIpStr = inputIP.getText ().toString ().trim ();
        Matcher ma = pa.matcher (inputIpStr);
        if (inputIpStr.equals ("") || !ma.matches () || inputIpStr.equals (TEOrderPoConstans.PRINT_DETALS_IP)) {
            Toast.makeText (this, R.string.activity_login_setip_error, Toast.LENGTH_SHORT).show ();
            return;
        }

        int index = -1;
        for (int i = 0; i < ipStringList.size (); i++) {
            String ipStr = ipStringList.get (i);
            if (inputIpStr.equals (ipStr)) {
                Toast.makeText (this, R.string.activity_login_setip_re, Toast.LENGTH_SHORT).show ();
                return;
            }
            if (ipStr.equals (TEOrderPoConstans.PRINT_DETALS_IP)) {
                index = i;
            }
        }
        if (index != -1) {
            ipStringList.remove (index);
        }
        ipStringList.add (0, inputIpStr);
        listRemoveDetailStr (ipStringList);
        adapter.notifyDataSetChanged ();
        inputIP.setText ("");
        SystemTool.hideKeyBoard (this);// 隐藏键盘
        setListViewHeight ();

        //保存数据
        try {
            String ipStr = SharePrefenceUtils.sceneList2String (ipStringList);
            Log.e ("aaron", "Addprinter>>>>>>>>>>>>>>>>>" + ipStr);
            SharePrefenceUtils.write (AddPrinterActivity.this, TEOrderPoConstans.SHAREPREFERENCE_NAME, printIpName, ipStr);
        } catch (Exception e) {
            Log.e ("aaron", "Addprinter>>>>>>>>>>>>>>>>>" + e.getMessage ());
            e.printStackTrace ();
        }
    }

    @Override
    public void onTopActionBarLeftClicked () {
        if (mIsReadSuccess) {
            SystemTool.hideKeyBoard (this);
        }
        toBack ();
    }


    @Override
    public void onTopActionBarRightClicked () {
    }

    /**
     * 设置ListView的高度
     */
    private void setListViewHeight () {
        final int height = SystemTool.dip2px (this, 40 + 1);
        LinearLayout.LayoutParams params1 =
                new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, receiptIPStringList.size () * height);
        receiptPrinterItemsListView.setLayoutParams (params1);

        LinearLayout.LayoutParams params2 =
                new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, kitchenIPStringList.size () * height);
        kitchenPrinterItemsListView.setLayoutParams (params2);

        LinearLayout.LayoutParams params3 =
                new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, kitchenIPStringList2.size () * height);
        kitchenPrinterItemsListView2.setLayoutParams (params3);

        LinearLayout.LayoutParams params4 =
                new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, counterIPStringList.size () * height);
        counterPrinterItemsListView.setLayoutParams (params4);
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
     * 将1.1.1.1的默认IP地址移除
     *
     * @param ipStringList
     */
    private void listRemoveDetailStr (List<String> ipStringList) {
        for (int i = 0; i < ipStringList.size (); i++) {
            if (ipStringList.get (i).equals (TEOrderPoConstans.PRINT_DETALS_IP)) {
                ipStringList.remove (i);
            }
        }
    }

}
