package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.DensityUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import java.util.List;

/**
 * Created by toughegg on 15/9/10.
 */
public class DishLabelGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private TEOrderPoApplication mTEOrderPoApplication;
    private List<Option> optionList;
    private LayoutInflater mLayoutInflater;
    private int densityW;
    private int dishCopies;// 菜品份数

    public void setDishCopies (int dishCopies) {
        this.dishCopies = dishCopies;
        notifyDataSetChanged ();
    }

    public DishLabelGridViewAdapter (Context context, List<Option> optionList) {
        this.mContext = context;
        mTEOrderPoApplication = (TEOrderPoApplication) context.getApplicationContext ();
        this.optionList = optionList;
        mTEOrderPoApplication.shoppingCartDishLabelList = optionList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        densityW = DensityUtils.getScreenW (mContext);


    }

    public void setDishLabels (List<Option> optionList) {
        this.optionList = optionList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return optionList.size ();
    }

    @Override
    public Option getItem (int position) {
        return optionList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_dishdetails_gridview, null);

            AbsListView.LayoutParams param = new AbsListView.LayoutParams (
                    android.view.ViewGroup.LayoutParams.FILL_PARENT,
                    ((densityW - 16) / 3) * 3 / 4);
            convertView.setLayoutParams (param);

            holderView = new HolderView ();
            holderView.layout = (RelativeLayout) convertView.findViewById (R.id.adapter_dishdetails_gridview_layout);
            holderView.nameTextView = (TextView) convertView.findViewById (R.id.adapter_dishdetails_gridview_name_textview);
            holderView.priceTextView = (TextView) convertView.findViewById (R.id.adapter_dishdetails_gridview_price_textview);
            holderView.copiesTextView = (TextView) convertView.findViewById (R.id.adapter_dishdetails_gridview_copies_textview);
            holderView.selectImageView = (ImageView) convertView.findViewById (R.id.adapter_dishdetails_gridview_select_imageview);
            convertView.setTag (holderView);
        } else {
            holderView = (HolderView) convertView.getTag ();
        }

        final Option option = getItem (position);
        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            holderView.nameTextView.setText (option.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            holderView.nameTextView.setText (option.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            holderView.nameTextView.setText (option.getName ().getEn_US ());
        }
        holderView.priceTextView.setText ("$ " + option.getPrice ());
        if (Float.parseFloat (option.getPrice ()) > 0) {// 收费的
            holderView.layout.setBackgroundResource (R.color.color_blue_deep);
            holderView.priceTextView.setVisibility (View.VISIBLE);
            holderView.selectImageView.setBackgroundResource (R.drawable.dishdetails_minus);// 减号
            holderView.copiesTextView.setText ("x" + option.getCount () * dishCopies);
            if (option.getCount () > 0) {
                holderView.copiesTextView.setVisibility (View.VISIBLE);
                holderView.selectImageView.setVisibility (View.VISIBLE);
            } else {
                holderView.copiesTextView.setVisibility (View.INVISIBLE);
                holderView.selectImageView.setVisibility (View.INVISIBLE);
            }
        } else {// 免费的
            holderView.layout.setBackgroundResource (R.color.color_green_deep);
            holderView.copiesTextView.setVisibility (View.INVISIBLE);
            holderView.priceTextView.setVisibility (View.INVISIBLE);
            holderView.selectImageView.setBackgroundResource (R.drawable.dishdetails_gou);// 对勾
            if (option.getCount () > 0) {
                holderView.selectImageView.setVisibility (View.VISIBLE);
            } else {
                holderView.selectImageView.setVisibility (View.INVISIBLE);
            }
        }
        holderView.layout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Option label = option;
                if (Float.parseFloat (label.getPrice ()) > 0) {
                    Log.v ("ll", "    ++++" + (label.getCount () + 1));
                    // 直接添加
                    option.setCount (label.getCount () + 1);
                    mTEOrderPoApplication.addPrice = CountUtils.add (mTEOrderPoApplication.addPrice, Double.parseDouble (label.getPrice ()));
                    mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_DISHINFO_PRICE));
                } else {
                    if (label.getCount () > 0) {
                        // 删除
                        label.setCount (0);
                    } else {
                        // 添加
                        label.setCount (1);
                    }
                }
                mTEOrderPoApplication.shoppingCartDishLabelList.set (position, label);
                optionList.set (position, label);
                notifyDataSetChanged ();
            }
        });
        holderView.selectImageView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Option label = option;
                if (label.getCount () > 0) {

                    label.setCount (label.getCount () - 1);
                    Log.v ("ll", "" + (label.getCount () - 1));
                    mTEOrderPoApplication.addPrice = CountUtils.sub (mTEOrderPoApplication.addPrice, Double.parseDouble (label.getPrice ()));
                    mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_DISHINFO_PRICE));
                    optionList.set (position, label);
                    mTEOrderPoApplication.shoppingCartDishLabelList.set (position, label);
                    notifyDataSetChanged ();
                }
            }
        });
        return convertView;
    }

    class HolderView {
        RelativeLayout layout;
        TextView nameTextView, priceTextView, copiesTextView;
        ImageView selectImageView;
    }
}
