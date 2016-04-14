package com.toughegg.andytools.view.fixedlistheader;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andy on 15/8/26.
 */
public interface IPinnedSectionedHeaderAdapter {
    public boolean isSectionHeader(int position);

    public int getSectionForPosition(int position);

    public View getSectionHeaderView(int section, View convertView, ViewGroup parent);

    public int getSectionHeaderViewType(int section);

    public int getCount();
}
