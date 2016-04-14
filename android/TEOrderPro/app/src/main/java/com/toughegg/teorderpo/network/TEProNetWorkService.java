package com.toughegg.teorderpo.network;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.toughegg.andytools.okhttp.callback.ResultCallback;
import com.toughegg.andytools.okhttp.request.OkHttpRequest;
import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.update.UpdateVersion;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.db.TEOrderPoDataBaseHelp;
import com.toughegg.teorderpo.modle.entry.APIStatusResult;
import com.toughegg.teorderpo.modle.entry.JsonEntry;
import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishMenuResult;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemModifier;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResult;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;
import com.toughegg.teorderpo.modle.entry.restaurantdetail.RestaurantDetailResult;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResult;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultDataListItem;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderNewData;
import com.toughegg.teorderpo.modle.entry.uploadOrder.OrderNewResult;
import com.toughegg.teorderpo.modle.entry.userlogin.UserInfo;
import com.toughegg.teorderpo.modle.entry.userlogin.UserLoginResult;
import com.toughegg.teorderpo.view.dialogs.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 15/11/27.
 */
public class TEProNetWorkService {

    private static final String TAG = "TEProNetWorkService---";

    private SharedPreferences mPreferences;

    private static TEProNetWorkService mTEProNetWorkService;

    public static TEProNetWorkService getInstance () {
        if (mTEProNetWorkService == null) {
            mTEProNetWorkService = new TEProNetWorkService ();
        }
        return mTEProNetWorkService;
    }

    private HashMap getSession (Context context) {
        HashMap session = new HashMap ();
        String lang = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN);
        if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE)) {
            session.put ("lang", "zh_CH");
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_ENGLISH)) {
            session.put ("lang", "en_US");
        } else if (lang.equals (TEOrderPoConstans.LANGUAGE_CHINESE_TW)) {
            session.put ("lang", "en_US");
        }
        session.put ("restId", SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_REST_ID));
        session.put ("token", SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_TOKEN));
        session.put ("userId", SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_LOGIN_USER_ID));
        return session;
    }


    /**
     * 登陆
     */
    public void login (final Context mContext, UserInfo userInfo, final Handler mHandler) {
        final Dialog dialog = DialogUtils.createLoadingDialog (mContext, R.string.activity_login_loading);
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_LOGIN;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("restaurantCode", userInfo.getRestCode ());
        agrs.put ("employeeId", userInfo.getEmployeeId ());
        agrs.put ("password", userInfo.getPassword ());
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);
//        Log.d ("hcc", "logint-->>" + json);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {

            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
                dialog.show ();
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
                Log.d ("aaron", "login-->>>" + request.toString () + "\n" + e.getMessage ());
            }

            @Override
            public void onResponse (String response) {
                Log.d ("aaron", "login-->>>" + response);

                try {
                    //解析登陆
                    UserLoginResult mUserLoginResult = gson.fromJson (response, UserLoginResult.class);
                    if (mUserLoginResult.getStatus () == 200) {
                        // 保存登陆数据
                        SharedPreferences preference = mContext.getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preference.edit ();
                        editor.putString (TEOrderPoConstans.SP_LOGIN_TOKEN, mUserLoginResult.getToken ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_REST_ID, mUserLoginResult.getData ().getRestId ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_EMPLOYEE_ID, mUserLoginResult.getData ().getEmployeeId ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_EMAIL, mUserLoginResult.getData ().getEmail ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_GROUP_ID, mUserLoginResult.getData ().getGroupId ());
                        editor.putInt (TEOrderPoConstans.SP_LOGIN_STATUS, mUserLoginResult.getData ().getStatus ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_USER_ID, mUserLoginResult.getData ().getId ());
                        editor.putString (TEOrderPoConstans.SP_LOGIN_USERNAME, mUserLoginResult.getData ().getUsername ());
                        editor.commit ();
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, mUserLoginResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }

            }

            @Override
            public void onAfter () {
                super.onAfter ();
                dialog.dismiss ();
            }
        });
    }

    /**
     * 获取TableList 列表
     */
    public void getTableListing (Context mContext, final Handler mHandler) {
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_TABLE_LIST;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (final String response) {
                Log.e ("aaron", "response-->>>getTableListing-->>" + response);
                new Thread () {
                    @Override
                    public void run () {
                        super.run ();
                        try {
                            TableResult mTableResult = gson.fromJson (response, TableResult.class);
                            if (mTableResult.getStatus () == 200) {
                                updateTableListOfLocal (mTableResult);
                                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                            } else {
                                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
                            }
                        } catch (Exception e) {
                            mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                            e.printStackTrace ();
                        }
                    }
                }.start ();
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

    /**
     * 将网络餐桌信息更新到本地
     *
     * @param mTableResult
     */
    private void updateTableListOfLocal (TableResult mTableResult) {
        // 删除所有餐桌信息
        TEOrderPoDataBaseHelp.getInstants ().deleteAllDataFromTable (TEOrderPoDataBase.TABLE_INFO_TABLE_NAME);
        TEOrderPoDataBaseHelp.getInstants ().deleteAllDataFromTable (TEOrderPoDataBase.TABLE_TABLELIST_TABLE_NAME);
        for (TableResultData tableResultData : mTableResult.getData ()) {
            TEOrderPoDataBaseHelp.getInstants ().saveOrUpdateTableInfo (tableResultData);
            List<TableResultDataListItem> dataListItems = tableResultData.getTableList ();
            if (dataListItems != null && dataListItems.size () > 0) {
                for (TableResultDataListItem item : dataListItems) {
                    item.setTableId (tableResultData.getId ());
                    TEOrderPoDataBaseHelp.getInstants ().saveTableTableList (item);
                }
            }
        }
    }

    /**
     * 拼桌
     *
     * @param context
     * @param tableIdList
     * @param mHandler
     */
    public void mergeTable (Context context, List<String> tableIdList, final Handler mHandler) {
        mPreferences = context.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_MERGE_TABLE;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("tableList", tableIdList.toArray ());
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (context));

        String json = gson.toJson (jsonEntry);
        Log.e ("aaron", "json>>>>" + json);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", TAG + "mergeTable>>>>>>" + response);
                try {
                    TableResult tableResult = gson.fromJson (response, TableResult.class);
                    if (tableResult.getStatus () == 200) {
                        updateTableListOfLocal (tableResult);
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, tableResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

    /**
     * 拆桌
     *
     * @param context
     * @param tableId
     * @param mHandler
     */
    public void unMergeTable (Context context, String tableId, final Handler mHandler) {
        mPreferences = context.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_UNMERGE_TABLE;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("tableId", tableId);
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (context));

        String json = gson.toJson (jsonEntry);
        Log.e ("aaron", "json>>>>" + json);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", TAG + "mergeTable>>>>>>" + response);
                try {
                    TableResult tableResult = gson.fromJson (response, TableResult.class);
                    if (tableResult.getStatus () == 200) {
                        updateTableListOfLocal (tableResult);
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, tableResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

    /**
     * 换桌API
     *
     * @param context
     * @param orderNo
     * @param tableId
     * @param mHandler
     */
    public void orderChangeTable (Context context, String orderNo, String tableId, final Handler mHandler) {
        final Dialog dialog = DialogUtils.createLoadingDialog (context, R.string.app_please_wait);
        mPreferences = context.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_ORDER_CHANGE_TABLE;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("orderNo", orderNo);
        agrs.put ("tableId", tableId);
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (context));

        String json = gson.toJson (jsonEntry);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
                dialog.show ();
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
//                Log.e ("aaron", TAG + "getOrderNetDetatil>>>>>>" + response);
                try {
                    APIStatusResult apiStatusResult = gson.fromJson (response, APIStatusResult.class);
                    if (apiStatusResult.getStatus () == 200) {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, apiStatusResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
                dialog.dismiss ();
            }
        });
    }

    /**
     * 获取菜品数据
     */
    public void getMenuData (Context mContext, final Handler mHandler) {
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_MENU_LIST;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);

        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (final String response) {
                Log.e ("aaron", "response-->>>getMenuData-->>" + response);
                new Thread () {
                    @Override
                    public void run () {
                        super.run ();
                        try {
                            DishMenuResult mDishMenuResult = gson.fromJson (response, DishMenuResult.class);
                            if (mDishMenuResult.getStatus () == 200) {
                                // 先删除之前的数据再添加，防止换号登录时出现数据重复
                                TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.MENU_CATEGORY_TABLE);
                                TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.DISH_INFO_TABLE_NAME);
                                TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.ITEM_TAX_TABLE);
                                TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.ITEM_MODIFIER_TABLE_NAME);
                                TEOrderPoDataBaseHelp.deleteAllDataFromTable (TEOrderPoDataBase.OPTION_TABLE_NAME);

                                // 获取菜品分类
                                for (DishCategory dishCategory : mDishMenuResult.getData ().getItemCategory ()) {
                                    TEOrderPoDataBaseHelp.saveOrUpdateDishCategory (dishCategory);
                                }


                                //获取菜品信息
                                for (DishItems dishItems : mDishMenuResult.getData ().getHotDishItems ()) {
                                    TEOrderPoDataBaseHelp.saveOrUpdateDishInfo (dishItems);
//                                    Log.e ("aaron", dishItems.toString () + "\n\n");
                                }

                                //获取菜品信息
                                for (DishItems dishItems : mDishMenuResult.getData ().getDishItems ()) {
                                    TEOrderPoDataBaseHelp.saveOrUpdateDishInfo (dishItems);
//                                    Log.e ("aaron", dishItems.toString () + "\n\n");
                                }

                                // 保存税率信息
                                for (ItemTax itemTax : mDishMenuResult.getData ().getItemTax ()) {
                                    TEOrderPoDataBaseHelp.saveOrUpdateTax (itemTax);
                                }

                                // 保存菜品加料分类
                                for (ItemModifier itemModifier : mDishMenuResult.getData ().getItemModifier ()) {
//                                    Log.e ("aaron", itemModifier.toString () + "\n\n");
                                    TEOrderPoDataBaseHelp.saveOrUpdateDishItemModifier (itemModifier);
                                    // 保存加料详情
                                    for (Option option : itemModifier.getOption ()) {
                                        option.setItemModifierId (itemModifier.getId ());
                                        TEOrderPoDataBaseHelp.saveOrUpdateDishOption (option);
                                    }
                                }
                                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                            } else {
                                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
                            }
                        } catch (Exception e) {
                            mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                            e.printStackTrace ();
                        }
                    }
                }.start ();
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }


    /**
     * 提交创建订单
     */

    public void orderNew (Context mContext, String orderDetail, final Handler mHandler) {
//        Log.e("aaron", orderDetail);
//        final Dialog dialog = DialogUtils.createLoadingDialog (mContext, R.string.app_please_wait);
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_ORDER_NEW;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();

        Map<String, Object> map = new HashMap<> ();
        map = (Map<String, Object>) gson.fromJson (orderDetail, map.getClass ());
        agrs.put ("orderDetail", map);

        jsonEntry.setArgs (agrs);

        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);
        Log.d ("hcc", "orderNew json-->>" + json);

        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
//                dialog.show ();
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", "orderNew-->>>" + response);
                try {
                    OrderNewResult mOrderNewResult = gson.fromJson (response, OrderNewResult.class);
                    if (mOrderNewResult.getStatus () == 200) {
                        OrderNewData mOrderNewData = mOrderNewResult.getData ();
                        Log.e ("aaron", mOrderNewData.getTakeAwayNo () + "    -------------------------");
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS, mOrderNewData).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, mOrderNewResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
//                dialog.dismiss ();
            }
        });
    }

    /**
     * 更新菜品
     */
    public void updateOrder (Context mContext, String orderDetail, final Handler mHandler) {
//        final Dialog dialog = DialogUtils.createLoadingDialog (mContext, R.string.app_please_wait);
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_ORDER_UPDATE;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();

        Map<String, Object> map = new HashMap<> ();
        map = (Map<String, Object>) gson.fromJson (orderDetail, map.getClass ());
        agrs.put ("orderDetail", map);

        jsonEntry.setArgs (agrs);

        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);
//        Log.d("aaron", "orderNew json-->>" + json);

        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
//                dialog.show ();
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.d ("aaron", "orderUpdate-->>>" + response);
                try {
                    OrderNewResult mOrderNewResult = gson.fromJson (response, OrderNewResult.class);
                    if (mOrderNewResult.getStatus () == 200) {
                        OrderNewData mOrderNewData = mOrderNewResult.getData ();
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS, mOrderNewData).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, mOrderNewResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
//                dialog.dismiss ();
            }
        });
    }

    /**
     * 获取网络订单详情
     *
     * @param mContext
     * @param orderNo
     * @param mHandler
     */
    public void getOrderNetDetatil (Context mContext, String orderNo, final Handler mHandler) {
        final TEOrderPoApplication application = (TEOrderPoApplication) mContext.getApplicationContext ();
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        final String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_ORDER_DETAIL;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("orderNo", orderNo);
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", TAG + "getOrderNetDetatil>>>>>>" + response);
                try {
                    OrderNetResult orderNetResult = gson.fromJson (response, OrderNetResult.class);
                    if (orderNetResult.getStatus () == 200) {
                        OrderNetResultData data = orderNetResult.getData ();
                        OrderNetResultData dataHandler = orderNetResult.getData ();
                        List<OrderNetResultDataItem> items = data.getItem ();
                        List<OrderNetResultDataItem> tempItems = new ArrayList<OrderNetResultDataItem> ();
                        for (OrderNetResultDataItem item : items) {
                            // 从数据库查询出菜名
                            DishItems dishItems = TEOrderPoDataBaseHelp.findDishInfoByDishId (item.getMenuItemId ());
                            // 从数据库查询菜品的所属分类
                            String categoryIds = null;
                            for (int i = 0; i < dishItems.getCategoryId ().size (); i++) {
                                if (i == dishItems.getCategoryId ().size () - 1) {
                                    categoryIds += dishItems.getCategoryId ().get (i);
                                } else {
                                    categoryIds += dishItems.getCategoryId ().get (i) + ",";
                                }
                            }
                            item.setCategoryId (categoryIds);
                            if (dishItems != null) {
                                Name diahName = dishItems.getName ();
                                item.setName (diahName);
                                List<OrderNetResultItemModifier> modifiers = item.getModifier ();
                                List<OrderNetResultItemModifier> tempModifiers = new ArrayList<OrderNetResultItemModifier> ();
                                for (OrderNetResultItemModifier modifier : modifiers) {
                                    // 从数据库查询出加料的名称
                                    Option option = TEOrderPoDataBaseHelp.findOptionById (modifier.getModifierId (), modifier.getModifierOptionId ());
                                    if (option != null) {
                                        modifier.setName (option.getName ());
                                        tempModifiers.add (modifier);
                                    }
                                }
                                item.setModifier (tempModifiers);
                                tempItems.add (item);
                            }
                        }
                        data.setItem (tempItems);
                        application.orderNetResultData = data;
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS, dataHandler).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, orderNetResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

    /**
     * 获取店铺信息
     */
    public void getRestaurantDetail (final Context mContext, final Handler mHandler) {
        final TEOrderPoApplication application = (TEOrderPoApplication) mContext.getApplicationContext ();
        application.restaurantDetailDataResult = null;
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_RESTAURANT_INFO;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        jsonEntry.setArgs (new HashMap ());
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);

        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
//                Log.e ("aaron", "response-->>>getRestaurantDetail-->>" + response);
                try {
                    RestaurantDetailResult detailResult = gson.fromJson (response, RestaurantDetailResult.class);
                    if (detailResult.getStatus () == 200) {
                        application.restaurantDetailDataResult = detailResult.getData ();
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }


    /**
     * 同步数据
     *
     * @param mContext
     * @param mHandler
     */
    public void syncInfo (final Context mContext, final Handler mHandler) {
        mPreferences = mContext.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_SYNC_INFO;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        jsonEntry.setArgs (new HashMap ());
        jsonEntry.setSession (getSession (mContext));

        String json = gson.toJson (jsonEntry);

        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                try {
//                    Log.e ("aaron", "json-->>>" + response);
                    APIStatusResult statusResult = gson.fromJson (response, APIStatusResult.class);
                    if (statusResult.getStatus () == 200) {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

    /**
     * 确定打印
     *
     * @param context
     * @param orderNo
     * @param mHandler
     */
    public void orderPrinted (Context context, String orderNo, final Handler mHandler) {
        final Dialog dialog = DialogUtils.createLoadingDialog (context, R.string.app_please_wait);
        mPreferences = context.getApplicationContext ().getSharedPreferences (TEOrderPoConstans.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        String ip = mPreferences.getString (TEOrderPoConstans.SP_SERVICE_IP, "");
        String url = TEOrderPoConstans.API_HTTP + ip + TEOrderPoConstans.API_DK + TEOrderPoConstans.URL_ORDER_PRINTED;
        final Gson gson = new Gson ();
        JsonEntry jsonEntry = new JsonEntry ();
        HashMap agrs = new HashMap ();
        agrs.put ("orderNo", orderNo);
//        Log.e ("aaron", ">>>>>>>orderNo>>>>>>" + orderNo);
        jsonEntry.setArgs (agrs);
        jsonEntry.setSession (getSession (context));

        String json = gson.toJson (jsonEntry);
        new OkHttpRequest.Builder ().url (url).content (json).post (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
                dialog.show ();
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", TAG + "orderPrinted>>>>>>" + response);
                try {
                    APIStatusResult apiStatusResult = gson.fromJson (response, APIStatusResult.class);
                    if (apiStatusResult.getStatus () == 200) {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
                    } else {
                        mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL, apiStatusResult.getMessage ()).sendToTarget ();
                    }
                } catch (Exception e) {
                    mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_NET_ERROR).sendToTarget ();
                    e.printStackTrace ();
                }
            }

            @Override
            public void onAfter () {
                super.onAfter ();
                dialog.dismiss ();
            }
        });
    }


    // 更新数据网络
    public void checkUpdateVersion (final Handler mHandler) {
        final Gson gson = new Gson ();
        String url = "http://api.fir.im/apps/latest/com.toughegg.teorderpo?api_token=e103f9d18df32d73dec4335cf8b5d610&type=android";
        new OkHttpRequest.Builder ().url (url).get (new ResultCallback<String> () {
            @Override
            public void onBefore (Request request) {
                super.onBefore (request);
                Log.e ("aaron", TAG + "orderPrinted>>>>>>");
            }

            @Override
            public void onError (Request request, Exception e) {
                // 服务器连接失败
                Log.e ("aaron", TAG + "orderPrinted>>>>>>" + e.toString ());
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_SERVICE_CONNECTION_FAIL).sendToTarget ();
            }

            @Override
            public void onResponse (String response) {
                Log.e ("aaron", TAG + "orderPrinted>>>>>>" + response);
                UpdateVersion updateVersion = gson.fromJson (response, UpdateVersion.class);
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS, updateVersion).sendToTarget ();
            }

            @Override
            public void onAfter () {
                super.onAfter ();
            }
        });
    }

}
