package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lidan on 15/8/27.
 */
public class DialogRePrintAdapter extends BaseAdapter {

    private Context mContext;
    private TEOrderPoApplication mApplication;
    private List<HashMap<String, Object>> mPrintLog;
    private LayoutInflater mLayoutInflater;
    private ViewHolder holder;

    public DialogRePrintAdapter (Context context, List<HashMap<String, Object>> printLog) {
        this.mContext = context;
        this.mPrintLog = printLog;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        mApplication = (TEOrderPoApplication) context.getApplicationContext ();
    }

    @Override
    public int getCount () {
        return mPrintLog.size ();
    }

    @Override
    public HashMap<String, Object> getItem (int position) {
        return mPrintLog.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_dialog_reprint, null);
            holder = new ViewHolder (convertView);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        HashMap<String, Object> hashMap = getItem (position);
        boolean fail = (boolean) hashMap.get ("status");
        String key = (String) hashMap.get ("key");
        if (key.equals (TEOrderPoConstans.RECEIPT)) {// 收据
            key = mContext.getResources ().getString (R.string.activity_add_printer_receipt);
        } else if (key.equals (TEOrderPoConstans.BAR)) {// 吧台
            key = mContext.getResources ().getString (R.string.activity_add_printer_counter);
        } else if (key.equals (TEOrderPoConstans.KITCHEN)) {// 厨房1
            key = mContext.getResources ().getString (R.string.activity_add_printer_kitchen);
        } else if (key.equals (TEOrderPoConstans.CASH)) {// 厨房2
            key = mContext.getResources ().getString (R.string.activity_add_printer_cash);
        }
        holder.printNameTV.setText (key);
        if (fail) {
            holder.printStateIV.setImageResource (R.drawable.dialog_reprint_ok);
        } else {
            holder.printStateIV.setImageResource (R.drawable.dialog_reprint_fail);
        }
        boolean reprint = (boolean) hashMap.get ("select");
        if (reprint) {
            holder.printSelectIV.setImageResource (R.drawable.dialog_reprint_changed);
        } else {
            holder.printSelectIV.setImageResource (R.drawable.dialog_reprint_notchanged);
        }

        holder.printLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                HashMap<String, Object> hashMap1 = mApplication.printLog.get (position);
                boolean fail1 = (boolean) hashMap1.get ("status");
                boolean reprint1 = (boolean) hashMap1.get ("select");
                if (!fail1) {// 只有失败的才能重新打印
                    reprint1 = !reprint1;
                    hashMap1.put ("select", reprint1);
                    mApplication.printLog.set (position, hashMap1);
                    notifyDataSetChanged ();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        RelativeLayout printLayout;
        ImageView printStateIV;
        TextView printNameTV;
        ImageView printSelectIV;

        public ViewHolder (View view) {
            printLayout = (RelativeLayout) view.findViewById (R.id.adapter_dialog_reprint_layout);
            printStateIV = (ImageView) view.findViewById (R.id.adapter_dialog_reprint_hint);
            printNameTV = (TextView) view.findViewById (R.id.adapter_dialog_reprint_textview);
            printSelectIV = (ImageView) view.findViewById (R.id.adapter_dialog_reprint_checkbox);
            view.setTag (this);
        }
    }
}
