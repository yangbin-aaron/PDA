package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toughegg.andytools.systemUtil.DensityUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultDataListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toughegg on 15/8/5.
 */
public class TableGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<TableResultData> mTableResultDataList;
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    private int mSelectIndex = -1;

    private boolean isShowCheckBox = false;// 是否显示选择box
    private boolean isMergeOrUnmerge = false;// true为拼桌，false为拆桌

    public void setIsShowCheckBox (boolean isShowCheckBox, boolean isMergeOrUnmerge) {
        this.isShowCheckBox = isShowCheckBox;
        this.isMergeOrUnmerge = isMergeOrUnmerge;
        notifyDataSetChanged ();
    }

    public void setTableInfos (List<TableResultData> mTableResultDataList) {
        this.mTableResultDataList = mTableResultDataList;
        notifyDataSetChanged ();
    }

    public TableGridViewAdapter (Context context, List<TableResultData> tableInfos) {
        this.mContext = context;
        mTableResultDataList = tableInfos;
        if (mTableResultDataList == null) {
            mTableResultDataList = new ArrayList<TableResultData> ();
        }
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        mWidth = DensityUtils.getScreenW (mContext);
    }

    public void setSelectIndex (int selectIndex) {
        mSelectIndex = selectIndex;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mTableResultDataList.size ();
    }

    @Override
    public TableResultData getItem (int position) {
        return mTableResultDataList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        TableResultData mTableResultData = getItem (position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_table_gridview, null);
            holder = new ViewHolder ();
            holder.tableNameTextView = (TextView) convertView.findViewById (R.id.gridview_table_name_textView);
            holder.tableToNameTextView = (TextView) convertView.findViewById (R.id.gridview_table_to_name_textView);
            holder.tableToNameImageView = (ImageView) convertView.findViewById (R.id.gridview_table_to_name_imageView);
            holder.peopleTextView = (TextView) convertView.findViewById (R.id.gridview_people_textView);
            holder.moneyTextView = (TextView) convertView.findViewById (R.id.gridview_money_textView);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById (R.id.gridview_layout);
            holder.relativeLayoutSub = (RelativeLayout) convertView.findViewById (R.id.gridview_layout_sub);
            holder.showSelectLayout = (RelativeLayout) convertView.findViewById (R.id.show_select_imageview);
            holder.showSelectImageView = (ImageView) convertView.findViewById (R.id.check_select_imageview);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        if (isShowCheckBox) {
            holder.showSelectLayout.setVisibility (View.VISIBLE);
            if (mTableResultData.isSelected ()) {
                holder.showSelectImageView.setImageResource (R.drawable.select_selected);
            } else {
                holder.showSelectImageView.setImageResource (R.drawable.select_normal);
            }
        } else {
            holder.showSelectLayout.setVisibility (View.GONE);
        }

        /**
         * 记录选中的选项
         */
        if (mSelectIndex == position) {
            holder.relativeLayout.setBackgroundColor (mContext.getResources ().getColor (R.color.color_black));
        } else {
            if (mTableResultData.getPaymentStatus () == 1) {
                holder.relativeLayout.setBackgroundColor (mContext.getResources ().getColor (R.color.color_blue_deep));
            } else {
                holder.relativeLayout.setBackgroundColor (mContext.getResources ().getColor (R.color.color_orange_deep));
            }
        }

        LayoutParams params = new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (mWidth - 28) / 3);
        convertView.setLayoutParams (params);

        if (!isMergeOrUnmerge) {// 全部隐藏
            holder.showSelectImageView.setVisibility (View.GONE);
        }
        holder.tableNameTextView.setText (mTableResultData.getCode ());
        List<TableResultDataListItem> dataListItems = mTableResultData.getTableList ();
        if (dataListItems != null && dataListItems.size () > 0) {
            String strToName = "";
            if (mTableResultData.getCode ().equals (dataListItems.get (0).getCode ())) {// 拼桌主桌
                for (int i = 0; i < dataListItems.size (); i++) {
                    if (i == dataListItems.size () - 1) {
                        strToName += dataListItems.get (i).getCode ();
                    } else {
                        strToName += dataListItems.get (i).getCode () + "-";
                    }
                }
                holder.tableToNameTextView.setTextSize (12);
                holder.tableToNameImageView.setVisibility (View.GONE);
                if (!isMergeOrUnmerge) {
                    holder.showSelectImageView.setVisibility (View.VISIBLE);
                }
            } else {// 附桌
                strToName = dataListItems.get (0).getCode ();
                holder.tableToNameImageView.setVisibility (View.VISIBLE);
                holder.tableToNameTextView.setTextSize (20);
            }
            holder.tableToNameTextView.setText (strToName);
            if (isMergeOrUnmerge) {
                holder.showSelectImageView.setVisibility (View.GONE);// 不显示选择按钮
            }
        } else {
            holder.tableToNameTextView.setText ("");
            holder.tableToNameImageView.setVisibility (View.GONE);
            if (isMergeOrUnmerge) {
                holder.showSelectImageView.setVisibility (View.VISIBLE);
            }
        }
        holder.peopleTextView.setText (mTableResultData.getCustomerNum () + "/" + mTableResultData.getMaximum ());
        if (!mTableResultData.isOccupied () && mTableResultData.getCustomerNum () == 0) {// 空座
            holder.tableNameTextView.setTextColor (mContext.getResources ().getColor (R.color.color_orange_deep));
            holder.tableToNameTextView.setTextColor (mContext.getResources ().getColor (R.color.color_orange_deep));
            holder.tableToNameImageView.setImageResource (R.drawable.table_arrow_orange);
            holder.peopleTextView.setTextColor (mContext.getResources ().getColor (R.color.color_orange_deep));
            holder.relativeLayoutSub.setBackgroundColor (mContext.getResources ().getColor (R.color.color_white));
        } else {// 入座
            // 金额
            holder.moneyTextView.setText (TEOrderPoConstans.DOLLAR_SIGN
                    + StringUtils.getPriceString (Double.parseDouble (mTableResultData.getPayPrice ()), TEOrderPoConstans.PRICE_FORMAT_TWO));
            holder.peopleTextView.setTextColor (mContext.getResources ().getColor (R.color.color_gray_bright));
            holder.tableNameTextView.setTextColor (mContext.getResources ().getColor (R.color.color_white));
            holder.tableToNameTextView.setTextColor (mContext.getResources ().getColor (R.color.color_white));
            holder.tableToNameImageView.setImageResource (R.drawable.table_arrow_white);
            holder.peopleTextView.setTextColor (mContext.getResources ().getColor (R.color.color_white));
            if (mTableResultData.getPaymentStatus () == 1) {// 已经付款
                holder.relativeLayoutSub.setBackgroundColor (mContext.getResources ().getColor (R.color.color_blue_deep));
            } else {// 未付款
                holder.relativeLayoutSub.setBackgroundColor (mContext.getResources ().getColor (R.color.color_orange_deep));
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tableNameTextView;
        TextView tableToNameTextView;
        ImageView tableToNameImageView;
        TextView peopleTextView;
        TextView moneyTextView;
        RelativeLayout relativeLayoutSub;
        RelativeLayout relativeLayout;

        private RelativeLayout showSelectLayout;
        private ImageView showSelectImageView;
    }
}
