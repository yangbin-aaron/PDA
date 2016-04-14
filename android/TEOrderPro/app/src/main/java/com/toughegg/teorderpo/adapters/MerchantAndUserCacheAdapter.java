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
 * Created by toughegg on 16/4/11.
 */
public class MerchantAndUserCacheAdapter extends BaseAdapter {

    private List<String> mStringList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MerchantAndUserCacheAdapter (Context context, List<String> stringList) {
        mContext = context;
        mStringList = stringList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount () {
        return mStringList.size ();
    }

    @Override
    public String getItem (int position) {
        return mStringList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_merchant_user_cache_textview, null);
            viewHolder = new ViewHolder ();
            viewHolder.textView = (TextView) convertView.findViewById (R.id.adapter_merchant_user_cache_list_textview);
            convertView.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag ();
        }
        viewHolder.textView.setText (mStringList.get (position));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
