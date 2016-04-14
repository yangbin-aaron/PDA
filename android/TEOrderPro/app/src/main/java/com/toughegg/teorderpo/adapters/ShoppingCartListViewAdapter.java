package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.andytools.view.swipemenu.BaseSwipListAdapter;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.mvp.mvppresenter.ShoppingCartPresenterInf;

import java.util.List;

/**
 * Created by toughegg on 15/9/11.
 */
public class ShoppingCartListViewAdapter extends BaseSwipListAdapter {

    private TEOrderPoApplication mTEOrderPoApplication;

    private List<ShoppingCart> mShoppingCarts;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Integer> positionList;

    private ShoppingCartPresenterInf mShoppingCartPresenterImp;

    public void setShoppingCartPresenterImp (ShoppingCartPresenterInf shoppingCartPresenterImp) {
        mShoppingCartPresenterImp = shoppingCartPresenterImp;
    }

    public ShoppingCartListViewAdapter (Context context, List<ShoppingCart> shoppingCarts) {
        mContext = context;
        mTEOrderPoApplication = (TEOrderPoApplication) context.getApplicationContext ();
        mShoppingCarts = shoppingCarts;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setShoppingCarts (List<ShoppingCart> shoppingCarts) {
        mShoppingCarts = shoppingCarts;
        notifyDataSetChanged ();
    }

    public void setPosition (List<Integer> positionList) {
        this.positionList = positionList;
    }

    @Override
    public int getCount () {
        return mShoppingCarts.size ();
    }

    @Override
    public ShoppingCart getItem (int position) {
        return mShoppingCarts.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_shoppingcart_listview, null);
            holder = new ViewHolder ();
            holder.codeTextView = (TextView) convertView.findViewById (R.id.adapter_shoppingcart_listview_dishcode_textView);
            holder.disnNameTextView = (TextView) convertView.findViewById (R.id.adapter_shoppingcart_listview_dishname_textView);
            holder.priceTextView = (TextView) convertView.findViewById (R.id.adapter_shoppingcart_listview_price_textView);
            holder.copiseTextView = (TextView) convertView.findViewById (R.id.adapter_shoppingcart_listview_copies_textView);
            holder.remarkTextView = (TextView) convertView.findViewById (R.id.adapter_shoppingcart_listview_remark_textview);
            holder.minusLinearLayout = (LinearLayout) convertView.findViewById (R.id.adapter_shoppingcart_listview_minus_layout);
            holder.plusLinearLayout = (LinearLayout) convertView.findViewById (R.id.adapter_shoppingcart_listview_plus_layout);
            holder.labelLinearLayout = (LinearLayout) convertView.findViewById (R.id.adapter_shoppingcart_listview_label_linearlayout);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }
        final ShoppingCart shoppingCart = getItem (position);
        // code
        holder.codeTextView.setText (shoppingCart.getCode ());
        // name
        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            holder.disnNameTextView.setText (shoppingCart.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            holder.disnNameTextView.setText (shoppingCart.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            holder.disnNameTextView.setText (shoppingCart.getName ().getEn_US ());
        }
        // price
        holder.priceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                + StringUtils.getPriceString (
                Double.parseDouble (shoppingCart.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        // copies
        if (shoppingCart.getCopies () > 0) {
            holder.copiseTextView.setText ("" + shoppingCart.getCopies ());
            holder.minusLinearLayout.setVisibility (View.VISIBLE);
        } else {
            holder.copiseTextView.setText ("");
            holder.minusLinearLayout.setVisibility (View.INVISIBLE);
        }
        // remark
        holder.remarkTextView.setText (shoppingCart.getRemark ());
        if (!shoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
            holder.remarkTextView.setVisibility (View.VISIBLE);
            holder.remarkTextView.setText (shoppingCart.getRemark ());
        } else {
            holder.remarkTextView.setVisibility (View.GONE);
        }
        // label
        List<Option> optionList = shoppingCart.getOptionList ();
        holder.labelLinearLayout.removeAllViews ();
        if (optionList != null && optionList.size () > 0) {
            for (Option option : optionList) {
                // 动态添加（需要一个布局）
                holder.labelLinearLayout.addView (createLabelLayout (option, shoppingCart.getCopies ()));
            }
        }
        // -===================-
        if (shoppingCart.getIsDeleted ()) {
            convertView.findViewById (R.id.adapter_shoppingcart_listview_linear_view).setVisibility (View.VISIBLE);
        } else {
            convertView.findViewById (R.id.adapter_shoppingcart_listview_linear_view).setVisibility (View.GONE);
        }
        if (mTEOrderPoApplication.orderNetResultData != null) {
            if (position >= mTEOrderPoApplication.orderNetResultData.getItem ().size ()) {// 本地订单
                holder.minusLinearLayout.setVisibility (View.VISIBLE);
                holder.plusLinearLayout.setVisibility (View.VISIBLE);
                convertView.setBackgroundResource (R.color.color_white);
            } else {// 网络订单
                holder.minusLinearLayout.setVisibility (View.INVISIBLE);
                holder.plusLinearLayout.setVisibility (View.INVISIBLE);
                convertView.setBackgroundResource (R.color.color_gray_light);
            }
        }
        // -===================-

        // －－－－设置监听事件－－－－
        holder.plusLinearLayout.setOnClickListener (new View.OnClickListener () {// +
            @Override
            public void onClick (View v) {
                if (shoppingCart.getCopies () < TEOrderPoConstans.DISH_MAX_COPISE) {
                    shoppingCart.setCopies (shoppingCart.getCopies () + 1);
                    mShoppingCarts.set (position, shoppingCart);
                    notifyDataSetChanged ();
                    mShoppingCartPresenterImp.updateShoppingCartCopies (shoppingCart);
                    // 总份数＋1
                    mTEOrderPoApplication.shoppingCartCount++;
                    // 在价格上加上一份
                    mTEOrderPoApplication.shoppingCartPrice = CountUtils.add (mTEOrderPoApplication.shoppingCartPrice, Double.parseDouble
                            (shoppingCart.getTotalPrice ()));
                    mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
                }
            }
        });

        holder.minusLinearLayout.setOnClickListener (new View.OnClickListener () {// -
            @Override
            public void onClick (View v) {
                if (shoppingCart.getCopies () > 0) {
                    shoppingCart.setCopies (shoppingCart.getCopies () - 1);
                    mShoppingCarts.set (position, shoppingCart);
                    if (shoppingCart.getCopies () == 0) {
                        mShoppingCarts.remove (position);
                    }
                    if (mShoppingCarts.size () == 0) {
                        if (mTEOrderPoApplication.orderNetResultData != null) {
                            if (mTEOrderPoApplication.orderNetResultData.getItem ().size () == 0) {
                                mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_CLOSE_SHOPPINGCAR_ACTIVITY));
                            }
                        } else {
                            mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_CLOSE_SHOPPINGCAR_ACTIVITY));
                        }
                    }
                    notifyDataSetChanged ();
                    mShoppingCartPresenterImp.updateShoppingCartCopies (shoppingCart);
                    // 总份数-1
                    mTEOrderPoApplication.shoppingCartCount--;
                    // 在价格上减去一份
                    mTEOrderPoApplication.shoppingCartPrice = CountUtils.sub (mTEOrderPoApplication.shoppingCartPrice, Double.parseDouble
                            (shoppingCart.getTotalPrice ()));
                    mContext.sendBroadcast (new Intent (TEOrderPoConstans.ACTION_UPDATE_SHOPPINGCAR_DATA));
                }
            }
        });
        return convertView;
    }

    private View createLabelLayout (Option option, int shopCopies) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView nameTextView;
        TextView copiesTextView;
        TextView priceTextView;
        View view = LayoutInflater.from (mContext).inflate (R.layout.view_add_shoppingcart_label, null);
        nameTextView = (TextView) view.findViewById (R.id.view_add_shoppingcart_label_name_textview);
        copiesTextView = (TextView) view.findViewById (R.id.view_add_shoppingcart_label_copies_textview);
        priceTextView = (TextView) view.findViewById (R.id.view_add_shoppingcart_label_price_textview);

        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            nameTextView.setText (option.getName ().getZh_CN ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            nameTextView.setText (option.getName ().getEn_US ());
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            nameTextView.setText (option.getName ().getEn_US ());
        }
        if (Double.parseDouble (option.getPrice ()) == 0) {
            priceTextView.setVisibility (View.INVISIBLE);
        } else {
            priceTextView.setVisibility (View.VISIBLE);
            priceTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                    + StringUtils.getPriceString (
                    Double.parseDouble (option.getPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
        }
        copiesTextView.setText (option.getCount () * shopCopies + "");
        view.setLayoutParams (lp);
        return view;
    }

    @Override
    public boolean getSwipEnableByPosition (int position) {
        boolean flag = true;
        if (positionList == null) {
            return true;
        }
        for (Integer getposition : positionList) {
            if (getposition == position) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    class ViewHolder {
        TextView codeTextView;
        TextView disnNameTextView;
        TextView priceTextView;
        TextView copiseTextView;
        TextView remarkTextView;
        LinearLayout minusLinearLayout, plusLinearLayout, labelLinearLayout;
    }
}
