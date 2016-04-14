package com.toughegg.teorderpo.mvp.mvpmodle;

import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;

import java.util.List;

/**
 * Created by Andy on 15/9/10.
 */
public interface SearchDishModleInf {

    //查找所有中文菜品列表
    List<DishItems> findAllDishInfoList();

}
