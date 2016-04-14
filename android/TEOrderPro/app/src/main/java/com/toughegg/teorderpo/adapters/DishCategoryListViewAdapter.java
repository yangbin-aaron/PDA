package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;

import java.util.List;

/**
 * Created by toughegg on 15/8/6.
 */
public class DishCategoryListViewAdapter extends BaseAdapter {

    private TEOrderPoApplication mTEOrderPoApplication;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DishCategory> mDishCategories;
    private int mSelectIndex = 0;

    public DishCategoryListViewAdapter (Context context, List<DishCategory> dishCategories) {
        this.mContext = context;
        mTEOrderPoApplication = (TEOrderPoApplication) context.getApplicationContext ();
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        this.mDishCategories = dishCategories;
    }

    public void setDishCategories (List<DishCategory> dishCategories) {
        mDishCategories = dishCategories;
        notifyDataSetChanged ();
    }

    public void setSelectIndex (int selectIndex) {
        mSelectIndex = selectIndex;
//        Log.e ("hcc", "setSelectIndex-->>>>" + selectIndex);
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mDishCategories.size ();
    }

    @Override
    public DishCategory getItem (int position) {
        return mDishCategories.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_dishtype_listview, parent, false);
            holder = new ViewHolder ();
            holder.DishTagNameTextView = (TextView) convertView.findViewById (R.id.adapter_disgtype_listview_dishname_textView);
            holder.MessageShowTextView = (TextView) convertView.findViewById (R.id.message_show);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        LayoutParams layoutParams = holder.DishTagNameTextView.getLayoutParams ();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = 200;
        holder.DishTagNameTextView.setLayoutParams (layoutParams);

        if (mSelectIndex == position) {
            convertView.setBackgroundColor (Color.RED);
            holder.DishTagNameTextView.setBackgroundColor (mContext.getResources ().getColor (R.color.white));
        } else {
            convertView.setBackgroundColor (mContext.getResources ().getColor (R.color.color_gray_bright));
            holder.DishTagNameTextView.setBackgroundColor (mContext.getResources ().getColor (R.color.color_gray_bright));
        }
        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            holder.DishTagNameTextView.setText (getItem (position).getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            holder.DishTagNameTextView.setText (getItem (position).getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            holder.DishTagNameTextView.setText (getItem (position).getName ().getEn_US ());
        }
        int count = 0;
        for (int i = 0; i < mDishCategories.get (position).getDishItemsList ().size (); i++) {
            count += mDishCategories.get (position).getDishItemsList ().get (i).getCopies ();
        }
        // -=======================-
        if (mTEOrderPoApplication.orderNetResultData != null) {
            for (int i = 0; i < mTEOrderPoApplication.orderNetResultData.getItem ().size (); i++) {
                OrderNetResultDataItem item = mTEOrderPoApplication.orderNetResultData.getItem ().get (i);
//                Log.e ("aaron", item.getCategoryId () + "------" + mDishCategories.get (position).getId ());
                if (item.getCategoryId ().contains (mDishCategories.get (position).getId ())) {
                    if (item.isDeleted () == false) {
                        count += item.getCount ();
                    }
                }
            }
        }
        // -=======================-
        if (count <= 0) {
            holder.MessageShowTextView.setVisibility (View.GONE);
        } else {
            holder.MessageShowTextView.setVisibility (View.VISIBLE);
            holder.MessageShowTextView.setText (count + "");
        }

        return convertView;
    }

    class ViewHolder {
        TextView DishTagNameTextView;
        TextView MessageShowTextView;
    }

}
