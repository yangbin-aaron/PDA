package com.toughegg.teorderpo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.modle.bean.Language;

import java.util.List;

/**
 * Created by toughegg on 16/2/26.
 */
public class SelectLangAdapter extends BaseAdapter {

    private Context mContext;
    private List<Language> mLanguageList;
    private LayoutInflater mLayoutInflater;
    private ViewHolder holder;

    public SelectLangAdapter (Context context, List<Language> languageList) {
        this.mContext = context;
        this.mLanguageList = languageList;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setLanguageList (List<Language> languageList) {
        mLanguageList = languageList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mLanguageList.size ();
    }

    @Override
    public Language getItem (int position) {
        return mLanguageList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate (R.layout.adapter_select_lang, null);
            holder = new ViewHolder (convertView);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        Language language = mLanguageList.get (position);
        holder.titleTV.setText (language.getTitleLang ());
        holder.subTV.setText (language.getSubLang ());
        if (language.isSelect ()) {
            holder.selectIV.setVisibility (View.VISIBLE);
        } else {
            holder.selectIV.setVisibility (View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView titleTV, subTV;
        ImageView selectIV;

        public ViewHolder (View view) {
            titleTV = (TextView) view.findViewById (R.id.title_textview);
            subTV = (TextView) view.findViewById (R.id.sub_textview);
            selectIV = (ImageView) view.findViewById (R.id.select_imageview);
            view.setTag (this);
        }
    }
}
