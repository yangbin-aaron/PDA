package com.toughegg.teorderpo.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;

import java.util.List;

//import com.skyun.skyunandroid.utils.SharePrefenceUtils;

/**
 * Created by toughegg on 15/8/7.
 */
public class MyUtil {
    /**
     * 订单详情checkout界面显示菜单
     *
     * @param shoppingCart
     * @return
     */
    public static View createTextView (Context context, ShoppingCart shoppingCart, boolean isNewOrUpdate) {
        List<Option> optionList = shoppingCart.getOptionList ();
        LinearLayout labelLayout;
        TextView dishCode;
        TextView dishName;
        TextView dishRemark;
        TextView dishPrice;
        TextView dishPart;
        View dishListView = LayoutInflater.from (context).inflate (R.layout.view_add_order_dish_list_item, null);
        dishCode = (TextView) dishListView.findViewById (R.id.dish_code);
        dishName = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_name_textView);
        dishRemark = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_remak_textView);
        dishPrice = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_price_textView);
        dishPart = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_part_textView);
        labelLayout = (LinearLayout) dishListView.findViewById (R.id.label_layout_id);
        if (optionList != null && optionList.size () > 0) {
            for (int i = 0; i < shoppingCart.getOptionList ().size (); i++) {
                Option option = optionList.get (i);
                labelLayout.addView (createLabelLayout (context, option, shoppingCart.getCopies (), shoppingCart.getId (), isNewOrUpdate));
            }
        }
        dishCode.setText (shoppingCart.getCode ());
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            dishName.setText (shoppingCart.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            dishName.setText (shoppingCart.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            dishName.setText (shoppingCart.getName ().getEn_US ());
        }
        if (shoppingCart.getRemark () == null || shoppingCart.getRemark ().equals ("")) {
            shoppingCart.setRemark (TEOrderPoDataBase.DB_NULL);// 网络获取时会出现的问题
        }
        if (shoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
            dishListView.findViewById (R.id.view_add_order_dish_list_item_remak_layout).setVisibility (View.GONE);
        } else {
            dishRemark.setVisibility (View.VISIBLE);
            dishRemark.setText (shoppingCart.getRemark ());
        }
        if (isNewOrUpdate) {
            if (shoppingCart.getId () == -2) {// new
                if (shoppingCart.getIsDeleted ()) {
                    dishListView.findViewById (R.id.dish_delete_line).setVisibility (View.VISIBLE);
                }
            } else {// net
                dishCode.setTextColor (context.getResources ().getColor (R.color.orange));
                dishName.setTextColor (context.getResources ().getColor (R.color.orange));
                dishPrice.setTextColor (context.getResources ().getColor (R.color.orange));
                dishPart.setTextColor (context.getResources ().getColor (R.color.orange));
                dishRemark.setTextColor (context.getResources ().getColor (R.color.orange));
                ((TextView) dishListView.findViewById (R.id.x_textview)).setTextColor (context.getResources ().getColor (R.color.orange));
                ((TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_remak_str)).setTextColor (context.getResources ()
                        .getColor (R.color.orange));
            }
        }
        dishPrice.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (Double.parseDouble (shoppingCart.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        dishPart.setText ("" + shoppingCart.getCopies ());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dishListView.setLayoutParams (lp);
        return dishListView;
    }

    private static View createLabelLayout (Context context, Option option, int shopCopies, int shopId, boolean isNewOrUpdate) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView nameTextView;
        TextView moneyTextView;
        TextView copiesTextView;
        View view = LayoutInflater.from (context).inflate (R.layout.view_add_order_detail_label_item, null);
        nameTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_name_textview);
        moneyTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_price_textview);
        copiesTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_copies_textview);
        if (shopId != -2 && isNewOrUpdate) {
            ((TextView) view.findViewById (R.id.x_textview)).setTextColor (context.getResources ().getColor (R.color.orange));
            nameTextView.setTextColor (context.getResources ().getColor (R.color.orange));
            moneyTextView.setTextColor (context.getResources ().getColor (R.color.orange));
            copiesTextView.setTextColor (context.getResources ().getColor (R.color.orange));
        }
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            nameTextView.setText (option.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            nameTextView.setText (option.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            nameTextView.setText (option.getName ().getEn_US ());
        }
        if (Double.parseDouble (option.getPrice ()) == 0) {
            moneyTextView.setVisibility (View.INVISIBLE);
        } else {
            moneyTextView.setVisibility (View.VISIBLE);
            moneyTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                    + StringUtils.getPriceString (Double.parseDouble (option.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        }
        copiesTextView.setText ("" + option.getCount () * shopCopies);
        view.setLayoutParams (lp);
        return view;
    }


    // ====================Add=====================

    /**
     * 订单详情net界面显示菜单
     *
     * @param item
     * @return
     */
    public static View createOrderNetItem (Context context, OrderNetResultDataItem item) {
        List<OrderNetResultItemModifier> dishLabels = item.getModifier ();
        Name name = item.getName ();
        LinearLayout labelLayout;
        TextView dishCode;
        TextView dishName;
        TextView dishRemark;
        TextView dishPrice;
        TextView dishPart;
        View dishListView = LayoutInflater.from (context).inflate (R.layout.view_add_order_dish_list_item, null);
        dishCode = (TextView) dishListView.findViewById (R.id.dish_code);
        dishName = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_name_textView);
        dishRemark = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_remak_textView);
        dishPrice = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_price_textView);
        dishPart = (TextView) dishListView.findViewById (R.id.view_add_order_dish_list_item_part_textView);
        labelLayout = (LinearLayout) dishListView.findViewById (R.id.label_layout_id);
        if (dishLabels != null && dishLabels.size () > 0) {
            for (int i = 0; i < dishLabels.size (); i++) {
                OrderNetResultItemModifier modifier = dishLabels.get (i);
                labelLayout.addView (createLabelLayout (context, modifier, item.getCount ()));
            }
        }
        // 加价
        if (item.getPriceAdjustment () != null && !item.getPriceAdjustment ().equals ("") && Double.parseDouble (item.getPriceAdjustment ()) > 0) {
            labelLayout.addView (createLabelLayout (context, null, item.getPriceAdjustment ()));
            Log.e ("aaron", "加价   " + item.getPriceAdjustment ());
        }
        // 折扣
        if (item.getItemDiscount () != null && !item.getItemDiscount ().equals ("") && Double.parseDouble (item.getItemDiscount ()) > 0) {
            Log.e ("aaron", "折扣   " + CountUtils.mul (item.getItemDiscount (), item.getMenuPrice ()));
            labelLayout.addView (createLabelLayout (context, item.getItemDiscount (),
                    "" + CountUtils.mul (item.getItemDiscount (), item.getMenuPrice ())));// 计算单品折扣金额
        }

        dishCode.setText (item.getCode ());
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            dishName.setText (name.getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            dishName.setText (name.getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            dishName.setText (name.getEn_US ());
        }
        if (item.getRemark () == null || item.getRemark ().equals ("")) {
            item.setRemark (TEOrderPoDataBase.DB_NULL);// 网络获取时会出现的问题
        }
        if (item.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
            dishListView.findViewById (R.id.view_add_order_dish_list_item_remak_layout).setVisibility (View.GONE);
        } else {
            dishRemark.setVisibility (View.VISIBLE);
            dishRemark.setText (item.getRemark ());
        }
        dishPrice.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (Double.parseDouble (item.getMenuPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        dishPart.setText ("" + item.getCount ());
        if (item.isDeleted ()) {// 删除显示横线
            dishListView.findViewById (R.id.dish_delete_line).setVisibility (View.VISIBLE);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dishListView.setLayoutParams (lp);
        return dishListView;
    }

    /**
     * 订单详情界面显示菜单标签
     *
     * @param context
     * @param modifier
     * @param shopDopies
     * @return
     */
    private static View createLabelLayout (Context context, OrderNetResultItemModifier modifier, int shopDopies) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView nameTextView;
        TextView moneyTextView;
        TextView copiesTextView;
        View view = LayoutInflater.from (context).inflate (R.layout.view_add_order_detail_label_item, null);
        nameTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_name_textview);
        moneyTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_price_textview);
        copiesTextView = (TextView) view.findViewById (R.id.view_add_order_detail_label_copies_textview);
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            nameTextView.setText (modifier.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            nameTextView.setText (modifier.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            nameTextView.setText (modifier.getName ().getEn_US ());
        }
        if (Double.parseDouble (modifier.getUnitPrice ()) == 0) {
            moneyTextView.setVisibility (View.INVISIBLE);
        } else {
            moneyTextView.setVisibility (View.VISIBLE);
            moneyTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                    + StringUtils.getPriceString (Double.parseDouble (modifier.getUnitPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        }
        copiesTextView.setText ("" + modifier.getCount () * shopDopies);
        view.setLayoutParams (lp);
        return view;
    }

    /**
     * 订单详情界面显示加价或者折扣
     *
     * @param context
     * @param discTax 折扣的时候显示折扣率
     * @param amount
     * @return
     */
    private static View createLabelLayout (Context context, String discTax, String amount) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView nameTextView;
        TextView moneyTextView;
        View view = LayoutInflater.from (context).inflate (R.layout.view_add_order_detail_addprice_item, null);
        nameTextView = (TextView) view.findViewById (R.id.view_add_order_detail_addprice_name_textview);
        moneyTextView = (TextView) view.findViewById (R.id.view_add_order_detail_addprice_price_textview);
        moneyTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (Double.parseDouble (amount), TEOrderPoConstans.PRICE_FORMAT_TWO));
        if (discTax != null) {// 折扣,为null时默认是加价
            nameTextView.setText ("(-"
                    + StringUtils.getPriceString (CountUtils.mul (discTax, 100 + ""), TEOrderPoConstans.PRICE_FORMAT_TWO) + "%)  ");
            nameTextView.append (context.getResources ().getString (R.string.order_detail_str_discount));
            moneyTextView.setText (TEOrderPoConstans.DOLLAR_SIGN + "-"
                    + StringUtils.getPriceString (Double.parseDouble (amount), TEOrderPoConstans.PRICE_FORMAT_TWO));
        }

        view.setLayoutParams (lp);
        return view;
    }

    /**
     * 下载打印模板
     *
     * @param context
     * @param ActHandler
     */
    public static void downTemplate (Context context, Handler ActHandler) {
        if (ActHandler == null) {
            ActHandler = new Handler ();
        }
        DownLoadFile downLoadFile = new DownLoadFile (ActHandler);
        TEOrderPoApplication application = (TEOrderPoApplication) context.getApplicationContext ();
        Log.e ("hcc", "TEOrderPoConstans.URL_LUA + application.restaurantDetailDataResult.getSetting ().getCalculatePolicy ()-->>\n"
                + TEOrderPoConstans.URL_LUA + "\n" + application.restaurantDetailDataResult.getSetting ().getCalculatePolicy ());
        downLoadFile.execute ("https://toughegg-development.s3.cn-north-1.amazonaws.com.cn/files/cd92890ccc07458ed6324556bfa9a4d0/4444.zip");
//        downLoadFile.execute (TEOrderPoConstans.URL_LUA + application.restaurantDetailDataResult.getSetting ().getCalculatePolicy ());
//        downLoadFile.execute (application.restaurantDetailDataResult.getSetting ().getCalculatePolicy ());
    }
}

