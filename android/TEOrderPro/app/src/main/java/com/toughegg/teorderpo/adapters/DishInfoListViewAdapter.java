package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.view.fixedlistheader.SectionedBaseAdapter;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;

import java.util.List;

/**
 * Created by toughegg on 15/8/6.
 */
public class DishInfoListViewAdapter extends SectionedBaseAdapter {
    private Context mContext;
    private TEOrderPoApplication mTEOrderPoApplication;
    private LayoutInflater mLayoutInflater;
    private List<DishItems> mDishItemsList;
    private List<DishCategory> mDishCategories;
    private List<Integer> mCountList;
    private DishItems mDishItems;
    private DishCategoryListViewAdapter mDishCategoryListViewAdapter;

    public DishInfoListViewAdapter (Context context, List<DishCategory> mDishCategories, List<DishItems> mDishItemsList, List<Integer> mCountList,
                                    DishCategoryListViewAdapter mDishCategoryListViewAdapter) {
        mContext = context;
        mTEOrderPoApplication = (TEOrderPoApplication) context.getApplicationContext ();
        this.mDishItemsList = mDishItemsList;
        this.mDishCategories = mDishCategories;
        this.mCountList = mCountList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        this.mDishCategoryListViewAdapter = mDishCategoryListViewAdapter;
    }

    public void setData (List<DishCategory> mDishCategories, List<DishItems> mDishItemsList, List<Integer> mCountList, DishCategoryListViewAdapter
            mDishCategoryListViewAdapter) {
        this.mDishItemsList = mDishItemsList;
        this.mDishCategories = mDishCategories;
        this.mCountList = mCountList;
        this.mDishCategoryListViewAdapter = mDishCategoryListViewAdapter;
        notifyDataSetChanged ();
    }

    @Override
    public DishItems getItem (int section, int position) {
        return mDishItemsList.get (position);
    }

    @Override
    public long getItemId (int section, int position) {
        return position;
    }

    // 当前左边条目数
    @Override
    public int getSectionCount () {
        return mDishCategories.size ();
    }

    // 当前右侧数据条数
    @Override
    public int getCountForSection (int section) {
        return mCountList.get (section);
    }

    @Override
    public View getItemView (final int section, final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_dishinfo_listview, parent, false);
            holder = new ViewHolder ();
            holder.codeTextView = (TextView) convertView.findViewById (R.id.adapter_dishinfo_listview_dishcode_textView);
            holder.disnNameTextView = (TextView) convertView.findViewById (R.id.adapter_dishinfo_listview_dishname_textView);
            holder.priceTextView = (TextView) convertView.findViewById (R.id.adapter_dishinfo_listview_price_textView);
            holder.copiseTextView = (TextView) convertView.findViewById (R.id.adapter_dishinfo_listview_copies_textView);
            holder.minusLinearLayout = (LinearLayout) convertView.findViewById (R.id.adapter_dishinfo_listview_minus_layout);
            holder.plusLinearLayout = (LinearLayout) convertView.findViewById (R.id.adapter_dishinfo_listview_plus_layout);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        // 区分每个分类下的每个item
        if (section < mDishCategories.size ()) {
            int countNumber = 0;
            mCountList.get (section); // 当前分类下菜品个数；
            for (int i = 0; i < section; i++) {
                countNumber += mCountList.get (i);
            }
            mDishItems = mDishItemsList.get (position + countNumber);
        }

        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);

        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            holder.disnNameTextView.setText (mDishItems.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            holder.disnNameTextView.setText (mDishItems.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            holder.disnNameTextView.setText (mDishItems.getName ().getEn_US ());
        }
        holder.codeTextView.setText (mDishItems.getCode ());
        holder.priceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                Double.parseDouble (mDishItems.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        // -===================-
        int copies = 0;
        if (mTEOrderPoApplication.orderNetResultData != null) {
            for (int i = 0; i < mTEOrderPoApplication.orderNetResultData.getItem ().size (); i++) {
                OrderNetResultDataItem item = mTEOrderPoApplication.orderNetResultData.getItem ().get (i);
                if (mDishItems.getId ().equals (item.getMenuItemId ())) {
                    if (item.isDeleted () == false) {
                        copies += item.getCount ();
                    }
                }
            }
        }
        // -===================-
        if (mDishItems.getCopies () == 0) {
            holder.minusLinearLayout.setVisibility (View.INVISIBLE);
            if (copies > 0) {
                holder.copiseTextView.setText (copies + "");
            } else {
                holder.copiseTextView.setText ("");
            }
        } else {
            holder.minusLinearLayout.setVisibility (View.VISIBLE);
            holder.copiseTextView.setText ((mDishItems.getCopies () + copies) + "");
        }
        holder.minusLinearLayout.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View v) {
                DishItems mDishItems = null;
                if (section < mDishCategories.size ()) {
                    int countNumber = 0;
                    mCountList.get (section); // 当前分类下菜品个数；
                    for (int i = 0; i < section; i++) {
                        countNumber += mCountList.get (i);
                    }
                    mDishItems = mDishItemsList.get (position + countNumber);
                    mDishCategoryListViewAdapter.notifyDataSetChanged ();
                }

                if (mDishItems.getCopies () > 0) {
                    int index = -1;
                    // 判断（无条件的）菜单列表里面有没有该菜品
                    for (int i = 0; i < mTEOrderPoApplication.shoppingCartList.size (); i++) {
                        if (mTEOrderPoApplication.shoppingCartList.get (i).getDishId ().equals (mDishItems.getId ())) {
                            index = i;
                            break;
                        }
                    }
                    // 有不加条件的并且份数大于0才能减少份数
                    if (index != -1 && mTEOrderPoApplication.shoppingCartList.get (index).getCopies () > 0) {
                        int copies = mDishItems.getCopies ();
                        copies--;
                        for (DishItems dishItems : mDishItemsList) {// 所以菜品ID相同的项都－1
                            if (dishItems.getId ().equals (mDishItems.getId ())) {
                                dishItems.setCopies (copies);//份数减1
                            }
                        }
                        notifyDataSetChanged ();
                        // 总份数-1
                        mTEOrderPoApplication.shoppingCartCount--;
                        // 在价格上减去一份
                        mTEOrderPoApplication.shoppingCartPrice = CountUtils.sub (mTEOrderPoApplication.shoppingCartPrice, Double.parseDouble
                                (mDishItems.getPrice ()));
                        ShoppingCart cart = mTEOrderPoApplication.shoppingCartList.get (index);
                        cart.setCopies (cart.getCopies () - 1);
                        mTEOrderPoApplication.shoppingCartList.set (index, cart);
                        mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
                    }
                }
            }
        });
        holder.plusLinearLayout.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View v) {
                DishItems mDishItems = null;
                if (section < mDishCategories.size ()) {
                    int countNumber = 0;
                    mCountList.get (section); // 当前分类下菜品个数；
                    for (int i = 0; i < section; i++) {
                        countNumber += mCountList.get (i);
                    }
                    mDishItems = mDishItemsList.get (position + countNumber);
                    mDishCategoryListViewAdapter.notifyDataSetChanged ();
                }

                if (mDishItems.getCopies () < TEOrderPoConstans.DISH_MAX_COPISE) {  // 加菜
                    int copies = mDishItems.getCopies ();
                    copies++;
                    for (DishItems dishItems : mDishItemsList) {// 所以菜品ID相同的项都＋1
                        if (dishItems.getId ().equals (mDishItems.getId ())) {
                            dishItems.setCopies (copies);//份数加1
                        }
                    }
                    notifyDataSetChanged ();//更新数据
                    // 总份数＋1
                    mTEOrderPoApplication.shoppingCartCount++;
                    // 在价格上加上一份
                    mTEOrderPoApplication.shoppingCartPrice = CountUtils.add (mTEOrderPoApplication.shoppingCartPrice, Double.parseDouble (mDishItems
                            .getPrice ()));
                    int index = -1;
                    // 判断（无条件的）菜单列表里面有没有该菜品
                    for (int i = 0; i < mTEOrderPoApplication.shoppingCartList.size (); i++) {
                        if (mTEOrderPoApplication.shoppingCartList.get (i).getDishId ().equals (mDishItems.getId ())) {
                            index = i;
                            break;
                        }
                    }
                    ShoppingCart shoppingCart = new ShoppingCart ();
                    shoppingCart.setDishId (mDishItems.getId () + "");
                    shoppingCart.setName (mDishItems.getName ());
                    shoppingCart.setPrice (mDishItems.getPrice ());
                    shoppingCart.setTotalPrice (mDishItems.getPrice ());// 没有加价
                    shoppingCart.setCode (mDishItems.getCode ());
                    shoppingCart.setRemark (TEOrderPoDataBase.DB_NULL);
                    if (index == -1) {
//                        如果购物车中没有这道菜，份数就是1
                        shoppingCart.setCopies (1);
                        mTEOrderPoApplication.shoppingCartList.add (shoppingCart);
                    } else {
                        //                        如果购物车中没有这道菜，份数加1
                        shoppingCart.setId (mTEOrderPoApplication.shoppingCartList.get (index).getId ());
                        shoppingCart.setCopies (mTEOrderPoApplication.shoppingCartList.get (index).getCopies () + 1);
                        mTEOrderPoApplication.shoppingCartList.set (index, shoppingCart);
                    }
                }
                mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
            }
        });
        return convertView;
    }

    @Override
    public View getSectionHeaderView (int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate (R.layout.view_add_order_dish_list_header, parent, false);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable (false);

        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            ((TextView) layout.findViewById (R.id.textItem)).setText (mDishCategories.get (section).getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            ((TextView) layout.findViewById (R.id.textItem)).setText (mDishCategories.get (section).getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            ((TextView) layout.findViewById (R.id.textItem)).setText (mDishCategories.get (section).getName ().getEn_US ());
        }
        return layout;
    }


    class ViewHolder {
        TextView codeTextView;
        TextView disnNameTextView;
        TextView priceTextView;
        TextView copiseTextView;
        LinearLayout minusLinearLayout, plusLinearLayout;
    }
}
