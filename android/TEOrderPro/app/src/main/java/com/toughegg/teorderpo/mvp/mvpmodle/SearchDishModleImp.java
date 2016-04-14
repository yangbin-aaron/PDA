package com.toughegg.teorderpo.mvp.mvpmodle;

import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;

import java.util.List;

/**
 * Created by Andy on 15/9/10.
 */
public class SearchDishModleImp implements SearchDishModleInf{

    @Override
    public List<DishItems> findAllDishInfoList() {
        return TEOrderPoDataBaseHelp.getInstants().findAllDishInfoList();
//        return null;
    }
}
