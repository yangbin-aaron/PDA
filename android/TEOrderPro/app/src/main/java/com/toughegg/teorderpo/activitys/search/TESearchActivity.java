package com.toughegg.teorderpo.activitys.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.BaseActivity;
import com.toughegg.teorderpo.activitys.DishDetailsActivity;
import com.toughegg.teorderpo.adapters.SearchDishInfoListAdapter;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.mvp.mvppresenter.SearchDishPresenterImp;
import com.toughegg.teorderpo.mvp.mvppresenter.SearchDishPresenterInf;
import com.toughegg.teorderpo.utils.TENetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andy on 15/9/10.
 */
public class TESearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private LinearLayout mLinearLayout;

    private EditText seachEditText;
    private RelativeLayout backLeft;
    private RelativeLayout searchRight;
    private ListView searchList;
    private List<DishItems> mAllDishInfoList;
    private SearchDishInfoListAdapter mSearchDishInfoListAdapter;

    private SearchDishPresenterInf mSearchDishPresenterInf;

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case TEOrderPoConstans.HANDLER_WHAT_SEARCHLIST:
                    mAllDishInfoList = (List<DishItems>) msg.obj;
                    break;
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search);
        initTopActionBar ();
    }

    private void initTopActionBar () {
        mLinearLayout = (LinearLayout) findViewById (R.id.activity_search_layout);
        mLinearLayout.setOnClickListener (this);

        mSearchDishPresenterInf = new SearchDishPresenterImp ();
        mSearchDishPresenterInf.getAllDishInfoList (mHandler);
        searchList = (ListView) findViewById (R.id.search_list);
        searchList.setOnItemClickListener (this);

        backLeft = (RelativeLayout) findViewById (R.id.search_left_layout);
        backLeft.setOnClickListener (this);
        searchRight = (RelativeLayout) findViewById (R.id.search_right_layout);
        searchRight.setOnClickListener (this);

        seachEditText = (EditText) findViewById (R.id.search_edit);
        //监听edittext中的事件
        seachEditText.addTextChangedListener (new TextWatcher () {

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (s.toString () != null && !s.toString ().equals ("")) {
                    searchRight.setVisibility (View.VISIBLE);
                } else {
                    searchRight.setVisibility (View.INVISIBLE);
                }
                filterData (s.toString ());
            }

            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }

    @Override
    protected void onResume () {
        super.onResume ();
        //对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
        Timer timer = new Timer ();
        timer.schedule (
                new TimerTask () {
                    public void run () {
                        InputMethodManager inputManager = (InputMethodManager) seachEditText.getContext ().getSystemService (Context
                                .INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput (seachEditText, 0);
                    }
                },
                500);
    }

    @Override
    protected void onPause () {
        super.onPause ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.search_left_layout:
                SystemTool.hideKeyBoard (this);
                toBack ();
                break;
            case R.id.search_right_layout:
                seachEditText.setText ("");
                break;
            case R.id.activity_search_layout:
                SystemTool.hideKeyBoard (this);
                break;
        }
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData (String filterStr) {
        List<DishItems> filterDateList = new ArrayList<DishItems> ();
        if (TextUtils.isEmpty (filterStr) || filterStr.equals ("")) {
            filterDateList.clear ();
        } else {
            filterStr = filterStr.toLowerCase ();
            filterDateList.clear ();
            String name = null;
            String code = null;
            for (DishItems dishItems : mAllDishInfoList) {
                code = dishItems.getCode ();
                if (code != null && code.toLowerCase ().indexOf (filterStr.toString ()) != -1) {// 先判断code，如果根据code找不到再去匹配菜名
                    filterDateList.add (dishItems);
                } else {
                    String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
                    if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE) || lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
                        name = dishItems.getName ().getZh_CN ();
                        if (name != null && name.toLowerCase ().indexOf (filterStr.toString ()) != -1
                                || TENetUtils.getPingYinShort (name).startsWith (filterStr.toString ())) {
                            filterDateList.add (dishItems);
                        }
                    } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
                        name = dishItems.getName ().getEn_US ();
                        if (name != null && name.toLowerCase ().indexOf (filterStr.toString ()) != -1
                                || StringUtils.getEnShort (name).startsWith (filterStr.toString ())) {
                            filterDateList.add (dishItems);
                        }
                    }
                }
            }
        }
        //刷新list
        mSearchDishInfoListAdapter = new SearchDishInfoListAdapter (this, filterDateList);
        searchList.setAdapter (mSearchDishInfoListAdapter);
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        toBack ();
        DishItems dishInfo = (DishItems) mSearchDishInfoListAdapter.getItem (position);
        Intent intent = new Intent (getApplication (), DishDetailsActivity.class);
        Bundle bundle = new Bundle ();
        bundle.putInt (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_FROM, DishDetailsActivity.FROM_ORDER);
        bundle.putSerializable (TEOrderPoConstans.KEY_OPEN_DISH_DEFAIL_ACTIVITY_DISHINFO, dishInfo);
        intent.putExtras (bundle);
        startActivity (intent);
        overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
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
