package com.toughegg.andytools.view.swipemenu;

import android.widget.BaseAdapter;

/**
 * Created by Andy on 15/12/9.
 *
 */

public abstract class BaseSwipListAdapter extends BaseAdapter {

    public boolean getSwipEnableByPosition(int position) {
        return true;
    }


}