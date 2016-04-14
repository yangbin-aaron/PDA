package com.toughegg.teorderpo.mvp.mvpmodle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.network.TEProNetWorkService;
import com.toughegg.teorderpo.utils.MyUtil;

/**
 * Created by lidan on 15/8/28.
 */
public class SettingModleImp implements SettingModleInf {

    private boolean mIsSyncInfo = true;// true同步数据false更新模版
    private Context mContext;
    private Handler mActHandler;

    private boolean mIsTableSync = false;// 是否在桌面管理界面同步数据

    @Override
    public void loginOut (Context context) {
        // 删除登陆信息
        SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_RESTAURANT_USER_ID, "");
        SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_RESTAURANT_ID, "");
        SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_GST_TAX, "");
        SharePrefenceUtils.write (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SERVICE_TAX, "");
    }

    @Override
    public void syncInfo (Context context, Handler ActHandler) {
        mContext = context;
        mActHandler = ActHandler;
        mIsSyncInfo = true;
        // 同步餐厅数据
        Log.e ("aaron", ">>>>>>>>>>>>>>>>同步？？");
        TEProNetWorkService.getInstance ().syncInfo (context, handlerSyncInfo);
    }

    @Override
    public void syncTemplate (Context context, Handler ActHandler) {
        mContext = context;
        mActHandler = ActHandler;
        mIsSyncInfo = false;
        Log.e ("aaron", ">>>>>>>>>>>>>>>>同步？？");
        TEProNetWorkService.getInstance ().syncInfo (context, handlerSyncInfo);
    }

    @Override
    public void syncInfoAndTemplate (Context context, boolean isTableSync) {
        mContext = context;
        mIsTableSync = isTableSync;
        mActHandler = new Handler ();// 无效Handler
        Log.e ("aaron", ">>>>>>>TableActivity>>>>>>>>>同步？？");
        TEProNetWorkService.getInstance ().syncInfo (context, handlerSyncInfo);
    }

    private Handler handlerRest = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (msg.what == TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS) {
                if (mIsTableSync) {
                    Log.e ("aaron", ">>>>>>>>>>>>>>>>同步菜单数据");
                    TEProNetWorkService.getInstance ().getMenuData (mContext, mActHandler);
                    Log.e ("aaron", ">>>>>>>>>>>>>>>>更新模版");
                    MyUtil.downTemplate (mContext, mActHandler);
                } else {
                    if (mIsSyncInfo) {// 同步数据
                        Log.e ("aaron", ">>>>>>>>>>>>>>>>同步菜单数据");
                        TEProNetWorkService.getInstance ().getMenuData (mContext, mActHandler);
                    } else {// 更新模版
                        Log.e ("aaron", ">>>>>>>>>>>>>>>>更新模版");
                        MyUtil.downTemplate (mContext, mActHandler);
                    }
                }
            } else {
                mActHandler.obtainMessage (msg.what).sendToTarget ();
            }
        }
    };

    private Handler handlerSyncInfo = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (msg.what == TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS) {// 同步菜单数据
                Log.e ("aaron", ">>>>>>>>>>>>>>>>同步餐厅数据");
                TEProNetWorkService.getInstance ().getRestaurantDetail (mContext, handlerRest);
            } else {
                mActHandler.obtainMessage (msg.what).sendToTarget ();
            }
        }
    };

    @Override
    public void checkVersion (Handler handler) {
        TEProNetWorkService.getInstance ().checkUpdateVersion (handler);
    }
}
