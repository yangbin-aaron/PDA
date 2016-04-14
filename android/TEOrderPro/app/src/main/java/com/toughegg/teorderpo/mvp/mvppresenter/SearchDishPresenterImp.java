package com.toughegg.teorderpo.mvp.mvppresenter;

import android.os.Handler;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.mvp.mvpmodle.SearchDishModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.SearchDishModleInf;

import java.util.List;

/**
 * Created by Andy on 15/9/10.
 */
public class SearchDishPresenterImp implements SearchDishPresenterInf {
    private SearchDishModleInf mSearchDishModleInf;

    public SearchDishPresenterImp () {
        mSearchDishModleInf = new SearchDishModleImp ();
    }

    @Override
    public void getAllDishInfoList (final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                List<DishItems> dishItemsList = mSearchDishModleInf.findAllDishInfoList ();
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SEARCHLIST, dishItemsList).sendToTarget ();
            }
        }.start ();
    }
}
