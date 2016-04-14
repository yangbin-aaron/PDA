package com.toughegg.teorderpo;

import android.app.Application;
import android.content.Context;

import com.toughegg.andytools.okhttp.OkHttpClientManager;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.andytools.update.UpdateVersion;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.restaurantdetail.RestaurantDetailDataResult;
import com.toughegg.teorderpo.utils.AssetCopyer;
import com.toughegg.teorderpo.utils.CrashHandler;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.fir.sdk.FIR;

/**
 * Created by toughegg on 15/8/6.
 * Application
 */
public class TEOrderPoApplication extends Application {
    private Context mContext;
    private LuaState mLuaState;
    private static TEOrderPoApplication instance;

    public static TEOrderPoApplication getInstance () {
        return instance;
    }

    @Override
    public void onCreate () {
        super.onCreate ();
        // fir 集成
        FIR.init (this);
        instance = this;
        mLuaState = LuaStateFactory.newLuaState ();
        mLuaState.openLibs ();


        //初始化加载https
        OkHttpClientManager.getInstance ().setCertificates ();

        // 捕获APP崩溃异常
        CrashHandler crashHandler = CrashHandler.getInstance ();
//        crashHandler.init (getApplicationContext ());

        mContext = getApplicationContext ();
        new TEOrderPoDataBase (mContext);

        // 设置打印Ip地址默认值
        initSetIpList (mContext);

        initLanguage ();

        new Thread (new Runnable () {
            @Override
            public void run () {
                AssetCopyer assetCopyer = new AssetCopyer (getApplicationContext ());
                try {
                    assetCopyer.copy ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }).start ();
    }

    public Context getAppContext () {
        return mContext;
    }

    // 点单时存的点菜(没有条件的)列表
    public List<ShoppingCart> shoppingCartList = new ArrayList<ShoppingCart> ();
    // 份数
    public int shoppingCartCount = 0;
    // 价格
    public double shoppingCartPrice = 0;
    // 标签
    public List<Option> shoppingCartDishLabelList = new ArrayList<Option> ();
    // 加价
    public double addPrice = 0;

    public OrderNetResultData orderNetResultData = null;// 网络订单信息

    public LuaState getLuastate () {
        return mLuaState;
    }

    public RestaurantDetailDataResult restaurantDetailDataResult = null;// 店铺详情

    private void initSetIpList (Context context) {
        // 收据
        String strReceiptList = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.RECEIPT);
        if (strReceiptList == null || strReceiptList.equals ("")) {
            List<String> ipList = new ArrayList<> ();
            ipList.add (TEOrderPoConstans.PRINT_DETALS_IP);
            try {
                String ipListString = SharePrefenceUtils.sceneList2String (ipList);
                SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.RECEIPT, ipListString);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        // 厨房1
        String strKitchenList = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.KITCHEN);
        if (strKitchenList == null || strKitchenList.equals ("")) {
            List<String> ipList = new ArrayList<> ();
            ipList.add (TEOrderPoConstans.PRINT_DETALS_IP);
            try {
                String ipListString = SharePrefenceUtils.sceneList2String (ipList);
                SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.KITCHEN, ipListString);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        // 厨房2
        String strCashList = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.CASH);
        if (strCashList == null || strCashList.equals ("")) {
            List<String> ipList = new ArrayList<> ();
            ipList.add (TEOrderPoConstans.PRINT_DETALS_IP);
            try {
                String ipListString = SharePrefenceUtils.sceneList2String (ipList);
                SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.CASH, ipListString);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        // 吧台
        String strBarList = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.BAR);
        if (strBarList == null || strBarList.equals ("")) {
            List<String> ipList = new ArrayList<> ();
            ipList.add (TEOrderPoConstans.PRINT_DETALS_IP);
            try {
                String ipListString = SharePrefenceUtils.sceneList2String (ipList);
                SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.BAR, ipListString);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    }

    /**
     * 初始化语言
     */
    private void initLanguage () {
        boolean isHongKongLang = false;// 为香港店定制
        String langs = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_LAN, "");
        if (langs.equals ("")) {
            if (isHongKongLang) {
                langs = TEOrderPoConstans.LANGUAGE_ENGLISH + "," + TEOrderPoConstans.LANGUAGE_CHINESE_TW;
            } else {
                langs = TEOrderPoConstans.LANGUAGE_ENGLISH + "," + TEOrderPoConstans.LANGUAGE_CHINESE;
            }
            // 默认选择英文和简体中文
            SharePrefenceUtils.write (
                    mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_LAN, langs);
            if (isHongKongLang) {
                SharePrefenceUtils.write (
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN, TEOrderPoConstans.LANGUAGE_CHINESE_TW);
            } else {
                SharePrefenceUtils.write (
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN, TEOrderPoConstans.LANGUAGE_ENGLISH);
            }
        }

        String lang = SharePrefenceUtils.readString (mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        SystemTool.switchLanguage (mContext, lang);
    }

    public List<HashMap<String, Object>> printLog = null;// 记录打印机打印日志
    public boolean printFail = false;// 记录打印机打印日志(有失败的)
    public int printSuccessCount = 0;// 记录打印机打印成功的数量(递减)

    public UpdateVersion updateVersion = null;
}
