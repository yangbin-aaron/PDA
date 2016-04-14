package com.toughegg.teorderpo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.adapters.SelectPCountDialogAdapeter;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.view.MyTopActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toughegg on 15/12/11.
 */
public class SelectPeopleActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, MyTopActionBar
        .OnTopActionBarClickListener {

    private TableResultData mTableResultData;

    private MyTopActionBar mMyTopActionBar;

    private GridView mGridView;
    private SelectPCountDialogAdapeter mSelectPCountDialogAdapeter;
    private List<Integer> mIntegers;

    private EditText mEditText;
    private TextView mConfirmTextView, mTableTextView, mMaxPTextView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mTableResultData = (TableResultData) getIntent ().getSerializableExtra ("table");
        setContentView (R.layout.activity_select_people);
        initTopBar ();
        initView ();
    }

    private void initTopBar () {
        mMyTopActionBar = (MyTopActionBar) findViewById (R.id.activity_select_people_topbar);
        mMyTopActionBar.setTitleTextView(R.string.select_people_title_str);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
        mMyTopActionBar.setRightClick(false);
    }

    private void initView () {
        mTableTextView = (TextView) findViewById (R.id.activity_select_people_table_textview);
        mTableTextView.setText (mTableResultData.getCode ());
        mMaxPTextView = (TextView) findViewById (R.id.activity_select_people_maxp_textview);
        mMaxPTextView.setText (mTableResultData.getMaximum () + "");

        mGridView = (GridView) findViewById (R.id.activity_select_people_gridview);
        mIntegers = new ArrayList<> ();
        for (int i = 1; i <= 12; i++) {
            mIntegers.add (i);
        }
        mSelectPCountDialogAdapeter = new SelectPCountDialogAdapeter (this, mIntegers, mTableResultData.getMaximum ());
        mGridView.setAdapter (mSelectPCountDialogAdapeter);
        mGridView.setOnItemClickListener (this);

        mEditText = (EditText) findViewById (R.id.activity_select_people_edittext);
        mConfirmTextView = (TextView) findViewById (R.id.activity_select_people_confirm_textview);
        mConfirmTextView.setOnClickListener (this);
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        toOrderActivity (mIntegers.get (position));
    }

    private void toOrderActivity (int peopleCount) {
        //保存当前选择餐厅 Table number
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_NUMBER, mTableResultData
                .getCode ());
        //保存当前选择餐厅 Table id
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_TABLE_ID, mTableResultData
                .getId ());
        // 保存选择的人数
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_PEOPLE, peopleCount);
        finish ();
        Intent intent = new Intent (this, OrderActivity.class);
        SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_ORDER_TYPE, TEOrderPoConstans.ORDER_TYPE_HOUSE);
        startActivity (intent);
        overridePendingTransition (R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.activity_select_people_confirm_textview:
                if (mEditText.getText ().toString ().equals ("")) {
                    mEditText.setText ("1");
                    toOrderActivity (1);// 默认为1位
                } else {
                    if (mEditText.getText ().toString ().equals ("0")){
                        mEditText.setText ("1");
                    }
                    toOrderActivity (Integer.parseInt (mEditText.getText ().toString ()));
                }
                break;
        }
    }

    @Override
    public void onTopActionBarLeftClicked () {
        finish ();
    }

    @Override
    public void onTopActionBarRightClicked () {

    }
}
