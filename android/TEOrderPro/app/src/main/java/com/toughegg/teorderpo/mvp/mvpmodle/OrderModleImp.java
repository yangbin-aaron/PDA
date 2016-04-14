package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;

import com.toughegg.andytools.systemUtil.CountUtils;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by toughegg on 15/8/11.
 * 点菜界面
 */
public class OrderModleImp implements OrderModleInf {

    @Override
    public HashMap<String, Object> findAllData (Context context) {
        TEOrderPoApplication teOrderPoApplication = (TEOrderPoApplication) context.getApplicationContext ();
        // 先将全局数据清空
        teOrderPoApplication.shoppingCartList.clear ();
        teOrderPoApplication.shoppingCartCount = 0;
        teOrderPoApplication.shoppingCartPrice = 0;
        HashMap<String, Object> hashMap = new HashMap<String, Object> ();
        List<DishCategory> dishCategories = findAllDishCategoryList ();// 查询分类列表
        hashMap.put ("category", dishCategories);
        List<DishItems> dishItemses = new ArrayList<DishItems> ();
        List<Integer> integers = new ArrayList<Integer> ();
        for (DishCategory category : dishCategories) {
            List<DishItems> tempList = findDishInfoByCategoryId (category.getId ());
            category.setDishItemsList (tempList);
            dishItemses.addAll (dishItemses.size (), tempList);
            integers.add (tempList.size ());
        }

        List<DishItems> dishs = new ArrayList<> ();// 记录已经查询过的菜品

        for (int i = 0; i < dishItemses.size (); i++) {
            DishItems dishItems = dishItemses.get (i);
            int index = -1;
            for (int j = 0; j < dishs.size (); j++) {
                if (dishs.get (j).getId ().equals (dishItems.getId ())) {
                    index = j;
                    break;
                }
            }
            // 根据菜品ID在购物车里面查找是否有加入购物车
            List<ShoppingCart> shoppingCarts = TEOrderPoDataBaseHelp.findShoppingCartListByDishId (dishItems.getId ());

            int copies = 0;
            double prices = 0;

            if (shoppingCarts != null && shoppingCarts.size () > 0) {
                for (ShoppingCart cart : shoppingCarts) {
                    copies += cart.getCopies ();
                    double price = CountUtils.mul (cart.getTotalPrice (), cart.getCopies () + "");
                    prices = CountUtils.add (prices, price);
                    if (cart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {// 如果remark为空则有可能是没有条件的数据
                        if (TEOrderPoDataBaseHelp.findShopLabelByShopId (cart.getId ()).size () == 0) {// label数据也没有，则确定该数据肯定没有条件
                            // 将数据加到没有条件的全局变量里面
                            teOrderPoApplication.shoppingCartList.add (cart);// 带shoppingcartId
                            //  break;// 如果跳出循环了，那么份数会少算
                        }
                    }
                }
            }
            dishItems.setCopies (copies);
            if (index == -1) {
                teOrderPoApplication.shoppingCartCount += copies;
                teOrderPoApplication.shoppingCartPrice = CountUtils.add (teOrderPoApplication.shoppingCartPrice, prices);
            }
            dishItemses.set (i, dishItems);
            dishs.add (dishItems);
        }
        hashMap.put ("dish", dishItemses);
        hashMap.put ("interger", integers);
        return hashMap;
    }

    private List<DishCategory> findAllDishCategoryList () {
        // 查找数据库中的菜品分类
        return TEOrderPoDataBaseHelp.getInstants ().findDishCategoryList ();
    }

    private List<DishItems> findDishInfoByCategoryId (String categoryId) {
        return TEOrderPoDataBaseHelp.getInstants ().findDishInfoListByCategoryId (categoryId);
    }

    @Override
    public void saveShoppingCartList (List<ShoppingCart> shoppingCarts) {
        for (ShoppingCart cart : shoppingCarts) {
            // 先判断有没有shoppingcartid
            if (cart.getId () > 0) {// 有ID，购物车存在该数据，直接修改即可
                // 根据ID修改份数
                if (cart.getCopies () == 0) {
                    TEOrderPoDataBaseHelp.deleteShoppingCartById (cart);
                } else {
                    TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (cart);
                }
            } else {// 没有ID，需要在购物车数据库匹配决定是添加还是修改
                // 先根据菜品ID和remark（为空）来查询
                ShoppingCart cart1 = TEOrderPoDataBaseHelp.findShoppingCartByShoppingCart (cart);
                if (cart1 == null) {// 足以证明购物车数据库里面没有（无条件的）数据，所以直接添加
                    if (cart.getCopies () > 0) {
                        TEOrderPoDataBaseHelp.getInstants ().saveShoppingCart (cart);
                    }
                } else {// 还需要看看有没有label数据
                    List<Option> dishLabels = TEOrderPoDataBaseHelp.findShopLabelByShopId (cart1.getId ());
                    if (dishLabels == null || dishLabels.size () == 0) {// 证明该数据没有任何条件，需要修改份数即可
                        if (cart.getCopies () == 0) {
                            TEOrderPoDataBaseHelp.deleteShoppingCartById (cart);
                        } else {
                            TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (cart);
                        }
                    } else {// 该购物车数据存在条件，不是同类型数据，需要新增
                        if (cart.getCopies () > 0) {
                            TEOrderPoDataBaseHelp.getInstants ().saveShoppingCart (cart);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deleteShoppingCartData () {
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME);
        TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME);
    }
}
