package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;

import java.util.List;

/**
 * Created by Andy on 15/9/10.
 */
public class SearchDishInfoListAdapter extends BaseAdapter {

    private List<DishItems> mAllDishInfoList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public SearchDishInfoListAdapter (Context context, List<DishItems> mAllDishInfoList) {
        this.context = context;
        this.mAllDishInfoList = mAllDishInfoList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView (List<DishItems> mAllDishInfoList) {
        this.mAllDishInfoList = mAllDishInfoList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mAllDishInfoList.size ();
    }

    @Override
    public Object getItem (int position) {
        return mAllDishInfoList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        DishItems dishItems = mAllDishInfoList.get (position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.view_search_result_layout, null);
            holder = new ViewHolder ();
            holder.dishName = (TextView) convertView.findViewById (R.id.dish_name);
            holder.dishCode = (TextView) convertView.findViewById (R.id.dish_code);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            holder.dishName.setText (dishItems.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            holder.dishName.setText (dishItems.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            holder.dishName.setText (dishItems.getName ().getEn_US ());
        }
        holder.dishCode.setText (dishItems.getCode ());
        return convertView;
    }

    class ViewHolder {
        TextView dishCode;
        TextView dishName;
    }
}
