package com.toughegg.teorderpo.mvp.mvppresenter;

import android.os.Handler;

import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.mvp.mvpmodle.DishDetailsModleImp;
import com.toughegg.teorderpo.mvp.mvpmodle.DishDetailsModleInf;

import java.util.HashMap;

/**
 * Created by lidan on 15/8/14.
 */
public class DishDetailsPresenterImp implements DishDetailsPresenterInf {
    private DishDetailsModleInf mIDishDetailsModle;

    public DishDetailsPresenterImp () {
        mIDishDetailsModle = new DishDetailsModleImp ();
    }

    @Override
    public void showDishDetails (final String modifierString, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                HashMap<String, Object> hashMap = mIDishDetailsModle.findDishOptionAndLabelList (modifierString);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_SETDATA, hashMap).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void saveShoppingCart (final ShoppingCart shoppingCart, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mIDishDetailsModle.saveShoppingCart (shoppingCart);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_SAVE_OK).sendToTarget ();
            }
        }.start ();
    }

    @Override
    public void updateShoppingCart (final ShoppingCart shoppingCart, final Handler handler) {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                mIDishDetailsModle.updateShoppingCart (shoppingCart);
                handler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_DISHDETAIL_UPDATE_OK).sendToTarget ();
            }
        }.start ();
    }
}
