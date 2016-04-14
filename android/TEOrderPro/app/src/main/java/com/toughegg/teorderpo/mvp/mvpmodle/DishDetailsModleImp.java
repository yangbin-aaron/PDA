package com.toughegg.teorderpo.mvp.mvpmodle;

import android.util.Log;

import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lidan on 15/8/14.
 * 菜品详情  逻辑处理
 */
public class DishDetailsModleImp implements DishDetailsModleInf {

    @Override
    public HashMap<String, Object> findDishOptionAndLabelList (String modifierString) {
        HashMap<String, Object> hashMap = new HashMap<String, Object> ();
        hashMap.put ("dishOptions", TEOrderPoDataBaseHelp.findDishItemModifier (modifierString));
        hashMap.put ("dishLabels", TEOrderPoDataBaseHelp.findDishOption (modifierString));
        return hashMap;
    }

    @Override
    public void saveShoppingCart (ShoppingCart shoppingCart) {
        Log.e ("aaron", "save new dish >>>" + shoppingCart.toString ());
        ShoppingCart shoppingCartNew = getSameShoppingCart (shoppingCart);
        if (shoppingCartNew == null) {
            long id = TEOrderPoDataBaseHelp.saveShoppingCart (shoppingCart);
            for (Option label : shoppingCart.getOptionList ()) {
                label.setShoppingCartId ((int) id);
                TEOrderPoDataBaseHelp.saveShoppingCartLabel (label);
            }
        } else {
            shoppingCart.setId (shoppingCartNew.getId ());
            shoppingCart.setCopies (shoppingCartNew.getCopies () + shoppingCart.getCopies ());// 将原有的份数＋新增的份数
            TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (shoppingCart);// 进行修改
        }
//        // 根据菜品的ID从购物车里面查到所有的菜品列表
//        List<ShoppingCart> shoppingCarts = TEOrderPoDataBaseHelp.findShoppingCartListByDishId (shoppingCart.getDishId ());
//        // 比较remark是否有相同的
//        List<ShoppingCart> cartList1 = new ArrayList<> ();// 作为筛选的容器
//        for (ShoppingCart cart : shoppingCarts) {
//            if (cart.getRemark ().equals (shoppingCart.getRemark ())) {// 如果remark相同，则可能
//                cartList1.add (cart);// 将相同remark的购物车数据暂存起来
//            }
//        }
//        if (cartList1.size () == 0) {// 没有相同的remark，证明该cart为新数据，可以直接save到数据库
//            saveShoppingCartToDB (shoppingCart);
//        } else {// 有相同remark的数据,需要继续匹配label
//            // 如果该数据没有标签
//            if (shoppingCart.getOptionList () == null || shoppingCart.getOptionList ().size () == 0) {
//                ShoppingCart tempCart = null;
//                for (ShoppingCart cart : cartList1) {
//                    List<Option> labels = TEOrderPoDataBaseHelp.findShopLabelByShopId (cart.getId ());
//                    if (labels == null || labels.size () == 0) {// 足以证明该数据存在相同的数据，只需修改份数即可
//                        tempCart = cart;
//                        break;
//                    }
//                }
//                if (tempCart == null) {// 没有找到相同的，保存
//                    saveShoppingCartToDB (shoppingCart);
//                } else {// 有相同的 ，修改即可
//                    shoppingCart.setId (tempCart.getId ());
//                    shoppingCart.setCopies (tempCart.getCopies () + shoppingCart.getCopies ());// 将原有的份数＋新增的份数
//                    TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (shoppingCart);// 进行修改
//                }
//            } else {// 如果该数据有标签
//                ShoppingCart tempCart = null;
//                for (ShoppingCart cart : cartList1) {
//                    List<Option> labels = TEOrderPoDataBaseHelp.findShopLabelByShopId (cart.getId ());
//                    if (labels.size () == shoppingCart.getOptionList ().size ()) {// 如果标签的长度相同，继续匹配;不相同则不需要继续啦
//                        int count = 0;
//                        // 轮循比较
//                        for (Option label1 : shoppingCart.getOptionList ()) {// 该数据的labels
//                            for (Option label2 : labels) {// 长度相同的labels
//                                if (label1.getId () == label2.getId () && label1.getCount () == label2.getCount ()) {
//                                    count++;
//                                }
//                            }
//                        }
//                        if (count == labels.size ()) {// 则完全匹配
//                            tempCart = cart;
//                            break;
//                        }
//                    }
//                }
//                if (tempCart == null) {// 没有找到相同的，保存
//                    saveShoppingCartToDB (shoppingCart);
//                } else {// 有相同的 ，修改即可
//                    shoppingCart.setId (tempCart.getId ());
//                    shoppingCart.setCopies (tempCart.getCopies () + shoppingCart.getCopies ());// 将原有的份数＋新增的份数
//                    TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (shoppingCart);// 进行修改
//                }
//            }
//        }
    }

    @Override
    public void updateShoppingCart (ShoppingCart shoppingCart) {
        ShoppingCart shoppingCartNew = getSameShoppingCart (shoppingCart);
        if (shoppingCartNew == null) {// 没有相同的,修改即可
            TEOrderPoDataBaseHelp.updateShoppingCart (shoppingCart);
            TEOrderPoDataBaseHelp.deleteShoppingCartLabelByShopId (shoppingCart.getId ());
            for (Option label : shoppingCart.getOptionList ()) {
                label.setShoppingCartId (shoppingCart.getId ());
                TEOrderPoDataBaseHelp.saveShoppingCartLabel (label);
            }
            Log.e ("aaron", "null-----null-----null-----null-----null-----");
        } else {// 有相同的，修改相同的cart份数，删除shoppingcart
            shoppingCartNew.setCopies (shoppingCartNew.getCopies () + shoppingCart.getCopies ());
            TEOrderPoDataBaseHelp.updateShoppingCartCopiesById (shoppingCartNew);// 进行修改
            // 再删除多余的数据
            TEOrderPoDataBaseHelp.deleteShoppingCartById (shoppingCart);
            Log.e ("aaron", "--相同---" + shoppingCartNew.toString ());
        }
    }

    /**
     * 根据菜品信息，获取购物车里面相同的shoppingcart
     *
     * @param shoppingCart
     * @return
     */
    private ShoppingCart getSameShoppingCart (ShoppingCart shoppingCart) {
        ShoppingCart tempCart = null;
        // 根据菜品的ID从购物车里面查到所有的菜品列表
        List<ShoppingCart> shoppingCarts = TEOrderPoDataBaseHelp.findShoppingCartListByDishId (shoppingCart.getDishId ());
        // 比较remark是否有相同的
        List<ShoppingCart> cartList1 = new ArrayList<> ();// 作为筛选的容器
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getRemark ().equals (shoppingCart.getRemark ())) {// 如果remark相同，则可能
                cartList1.add (cart);// 将相同remark的购物车数据暂存起来
            }
        }
        if (cartList1.size () == 0) {// 没有相同的remark，证明该cart为新数据，可以直接save到数据库
            tempCart = null;
        } else {// 有相同remark的数据,需要继续匹配label
            // 如果该数据没有标签
            if (shoppingCart.getOptionList () == null || shoppingCart.getOptionList ().size () == 0) {
                for (ShoppingCart cart : cartList1) {
                    List<Option> labels = TEOrderPoDataBaseHelp.findShopLabelByShopId (cart.getId ());
                    if (labels == null || labels.size () == 0) {// 足以证明该数据存在相同的数据，只需修改份数即可
                        tempCart = cart;
                        break;
                    }
                }
            } else {// 如果该数据有标签
                for (ShoppingCart cart : cartList1) {
                    List<Option> labels = TEOrderPoDataBaseHelp.findShopLabelByShopId (cart.getId ());
                    if (labels.size () == shoppingCart.getOptionList ().size ()) {// 如果标签的长度相同，继续匹配;不相同则不需要继续啦
                        int count = 0;
                        // 轮循比较
                        for (Option label1 : shoppingCart.getOptionList ()) {// 该数据的labels
                            for (Option label2 : labels) {// 长度相同的labels
                                if (label1.getId ().equals (label2.getId ())) {
                                    count++;
                                }
                            }
                        }
                        if (count == labels.size ()) {// 则完全匹配
//                            cart.setOptionList (labels);
                            tempCart = cart;
                            break;
                        }
                    }
                }
            }
        }
        return tempCart;
    }

}
