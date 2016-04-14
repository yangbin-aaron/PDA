package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toughegg.teorderpo.R;

import java.util.List;

/**
 * Created by toughegg on 15/9/24.
 */
public class SelectPCountDialogAdapeter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    List<Integer> mPeopleCounts;
    private int mMaxPeople;// 最大人数

    private Context mContext;

    public SelectPCountDialogAdapeter (Context context, List<Integer> peopleCounts, int maxPeople) {
        mContext = context;
        this.mMaxPeople = maxPeople;
        this.mPeopleCounts = peopleCounts;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount () {
        return mPeopleCounts.size ();
    }

    @Override
    public Integer getItem (int position) {
        return mPeopleCounts.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_dialog_select_p_count, null);
            holderView = new HolderView ();
            holderView.countTextView = (TextView) convertView.findViewById (R.id.adapter_dialog_select_p_count_textview);
            convertView.setTag (holderView);
        } else {
            holderView = (HolderView) convertView.getTag ();
        }

        holderView.countTextView.setText (getItem (position) + "");
        if (getItem (position) > mMaxPeople) {
            // 背景为灰色
            convertView.setBackgroundResource (R.drawable.selectpeople_blue_btn_color1);
        }

        return convertView;
    }

    class HolderView {
        TextView countTextView;
    }
}
