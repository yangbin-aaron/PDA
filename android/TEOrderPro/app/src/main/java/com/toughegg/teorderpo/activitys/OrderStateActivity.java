package com.toughegg.teorderpo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderNewData;
import com.toughegg.teorderpo.view.MyTopActionBar;

import java.util.Date;

/**
 * Created by ld on 15/8/6.
 * 订单状态界面 (订单提交成功)
 */
public class OrderStateActivity extends BaseActivity implements OnClickListener {

    private OrderNewData mOrderNewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_state);
        Intent intent = this.getIntent();
        mOrderNewData = (OrderNewData) intent.getSerializableExtra("orderNewData");
        initTopBar();
        initView();
    }

    private void initTopBar() {
        MyTopActionBar myTopActionBar = (MyTopActionBar) findViewById(R.id.top_action_bar);
        myTopActionBar.setTitleTextView(R.string.actionbar_order_detail_title_str);
        myTopActionBar.setRightClick(false);
        myTopActionBar.setLeftClick(false);
    }

    private void initView() {
        TextView mOrderStateTextView = (TextView) findViewById(R.id.activity_order_state_order_state);
        View dashed_horizontal_line = findViewById(R.id.dashed_horizontal_line);
        dashed_horizontal_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        Button mBackMainActivityButton = (Button) findViewById(R.id.activity_order_state_back_main_btn);
        TextView mOrderType = (TextView) findViewById(R.id.activity_order_state_order_type_text);
        TextView mOrderNumber = (TextView) findViewById(R.id.activity_order_state_order_number_text);
        TextView mOrderTime = (TextView) findViewById(R.id.activity_order_state_order_time_text);
        mOrderType.setText(mOrderNewData.getType() == TEOrderPoConstans.ORDER_TYPE_HOUSE ? R.string.activity_order_state_dinner_in : R.string
                .activity_order_state_take_away);
        //订单类型
        mOrderNumber.setText(mOrderNewData.getOrderNo());//订单编号
        // 将时间转化为需要的格式
        Date date;
        if (mOrderNewData.getCreatedStamp() == null || mOrderNewData.getCreatedStamp().equals("")) {
            date = new Date();
        } else {
            date = SystemTool.parseStringToDate(mOrderNewData.getCreatedStamp(), TEOrderPoConstans.DATA_FORMAT_YMDHMSSSSZZZZZ);
        }
        mOrderTime.setText(SystemTool.getDataTime(date, TEOrderPoConstans.FORMAT_DATE_YY_MM_DD_HH_MM));//下单时间
        mOrderStateTextView.setText(R.string.order_state_str_submitsuccess);
        mBackMainActivityButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_order_state_back_main_btn:
                backToHomePage();
                break;
        }
    }

    private void backToHomePage() {
        sendBroadcast(new Intent(TEOrderPoConstans.ACTION_TO_TABLE_ACTIVITY));
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToHomePage();
        }
        return super.onKeyDown(keyCode, event);
    }
}
