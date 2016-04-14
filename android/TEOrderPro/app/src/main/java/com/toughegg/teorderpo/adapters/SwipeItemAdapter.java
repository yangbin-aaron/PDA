package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.teorderpo.R;

import java.util.List;

/**
 * Created by lidan on 15/8/27.
 */
public class SwipeItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> ipStringList;
    private LayoutInflater mLayoutInflater;
    private ViewHolder holder;

    public SwipeItemAdapter (Context context, List<String> ipStringList) {
        this.mContext = context;
        this.ipStringList = ipStringList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setIpStringList (List<String> ipStringList) {
        this.ipStringList = ipStringList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return ipStringList.size ();
    }

    @Override
    public Object getItem (int position) {
        return ipStringList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_add_printer_swipeitem, null);
            holder = new ViewHolder (convertView);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

//        LayoutParams params =new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,40);
//        holder.swipe_layout.setLayoutParams (params);

        holder.swipe_item.setText (ipStringList.get (position));
        if (position == 0) {
            holder.swipe_item_select.setVisibility (View.VISIBLE);
        } else {
            holder.swipe_item_select.setVisibility (View.INVISIBLE);
        }
        return convertView;
    }

//    @Override
//    public boolean getSwipEnableByPosition (int position) {
//        boolean flag = true;
//        if (ipStringList.size () <= 1) {
//            flag = false;
//        }
//        return flag;
//    }

    class ViewHolder {
        RelativeLayout swipe_layout;
        TextView swipe_item;
        LinearLayout swipe_item_select;

        public ViewHolder (View view) {
            swipe_layout = (RelativeLayout) view.findViewById (R.id.activity_swipe_item_layout);
            swipe_item = (TextView) view.findViewById (R.id.activity_add_printer_swipe_item);
            swipe_item_select = (LinearLayout) view.findViewById (R.id.activity_swipe_item_img_layout);
            view.setTag (this);
        }
    }
}
