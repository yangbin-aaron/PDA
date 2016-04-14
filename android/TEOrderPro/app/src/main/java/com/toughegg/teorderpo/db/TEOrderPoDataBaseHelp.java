package com.toughegg.teorderpo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishCategory;
import com.toughegg.teorderpo.modle.entry.dishMenu.DishItems;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemModifier;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultData;
import com.toughegg.teorderpo.modle.entry.tablelist.TableResultDataListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 15/8/11.
 * 数据库帮助类
 */
public class TEOrderPoDataBaseHelp {
    private static TEOrderPoDataBaseHelp mTEOrderPoDataBaseHelp;

    public TEOrderPoDataBaseHelp () {

    }

    public static TEOrderPoDataBaseHelp getInstants () {
        if (mTEOrderPoDataBaseHelp == null) {
            mTEOrderPoDataBaseHelp = new TEOrderPoDataBaseHelp ();
        }
        return mTEOrderPoDataBaseHelp;
    }

    // =========================== PUBLIC ===========================

    /**
     * 删除表数据
     *
     * @param tableName
     */
    public static void deleteAllDataFromTable (String tableName) {
        String sql = "delete from " + tableName;
        TEOrderPoDataBase.getInstance ().getSQLiteDatabase ().execSQL (sql);
    }

    // =========================== 餐桌表格相关 ===========================

    /**
     * 保存餐桌信息
     *
     * @param tableResultData
     */
    public static void saveOrUpdateTableInfo (TableResultData tableResultData) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.TABLE_ID, tableResultData.getId ());
        values.put (TEOrderPoDataBase.TABLE_CAPACITY, tableResultData.getCapacity ());
        values.put (TEOrderPoDataBase.TABLE_CODE, tableResultData.getCode ());
        values.put (TEOrderPoDataBase.TABLE_CUSTOMER_NUMBER, tableResultData.getCustomerNum ());
        values.put (TEOrderPoDataBase.TABLE_ENABLED, tableResultData.isEnable ());
        values.put (TEOrderPoDataBase.TABLE_MAXIMUM, tableResultData.getMaximum ());
        values.put (TEOrderPoDataBase.TABLE_MINIMUM, tableResultData.getMinimum ());
        values.put (TEOrderPoDataBase.TABLE_OCCUPIED, tableResultData.isOccupied ());
        values.put (TEOrderPoDataBase.TABLE_ORDER, tableResultData.getOrder ());
        values.put (TEOrderPoDataBase.TABLE_ORDER_NO, tableResultData.getOrderNo ());
        values.put (TEOrderPoDataBase.TABLE_ORDER_STATUS, tableResultData.getOrderStatus ());
        values.put (TEOrderPoDataBase.TABLE_PAY_PRICE, tableResultData.getPayPrice ());
        values.put (TEOrderPoDataBase.TABLE_PAYMENT_STATUS, tableResultData.getPaymentStatus ());
        values.put (TEOrderPoDataBase.TABLE_REST_ID, tableResultData.getRestId ());

        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();

        db.insert (TEOrderPoDataBase.TABLE_INFO_TABLE_NAME, null, values);
    }

    /**
     * 根据餐桌的状态查找餐桌
     *
     * @param spinnerIndex 指定的Spinner条件：0为全部，1为付款，2为入座，3为空座
     * @return
     */
    public List<TableResultData> findTableInfoListBySpinner (int spinnerIndex) {

        String sql;
        List<TableResultData> mTableResultDataList = new ArrayList<> ();
        if (spinnerIndex == 1) {
            sql = "select * from " + TEOrderPoDataBase.TABLE_INFO_TABLE_NAME + " where " + TEOrderPoDataBase.TABLE_PAYMENT_STATUS + " = 1 and " +
                    TEOrderPoDataBase.TABLE_OCCUPIED + "= 1 order by " + TEOrderPoDataBase.TABLE_ORDER;
        } else if (spinnerIndex == 2) {
            sql = "select * from " + TEOrderPoDataBase.TABLE_INFO_TABLE_NAME + " where " + TEOrderPoDataBase.TABLE_OCCUPIED + " = 1 and " +
                    TEOrderPoDataBase.TABLE_PAYMENT_STATUS + " =0 order by " + TEOrderPoDataBase.TABLE_ORDER;
        } else if (spinnerIndex == 3) {
            sql = "select * from " + TEOrderPoDataBase.TABLE_INFO_TABLE_NAME + " where " + TEOrderPoDataBase.TABLE_OCCUPIED + " = 0 order by " +
                    TEOrderPoDataBase.TABLE_ORDER;
        } else {
            sql = "select * from " + TEOrderPoDataBase.TABLE_INFO_TABLE_NAME + " order by " + TEOrderPoDataBase.TABLE_ORDER;
        }
        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            TableResultData tableResultData = new TableResultData ();
            tableResultData.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_ID)));
            tableResultData.setCapacity (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_CAPACITY)));
            tableResultData.setCode (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_CODE)));
            tableResultData.setCustomerNum (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_CUSTOMER_NUMBER)));
            tableResultData.setEnable (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_ENABLED)) == 1);
            tableResultData.setMaximum (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_MAXIMUM)));
            tableResultData.setMinimum (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_MINIMUM)));
            tableResultData.setOccupied (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_OCCUPIED)) == 1);
            tableResultData.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_ORDER)));
            tableResultData.setOrderNo (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_ORDER_NO)));
            tableResultData.setOrderStatus (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_ORDER_STATUS)));
            tableResultData.setPayPrice (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_PAY_PRICE)));
            tableResultData.setPaymentStatus (c.getInt (c.getColumnIndex (TEOrderPoDataBase.TABLE_PAYMENT_STATUS)));
            tableResultData.setRestId (c.getString (c.getColumnIndex (TEOrderPoDataBase.TABLE_REST_ID)));
            mTableResultDataList.add (tableResultData);
        }
        c.close ();
        return mTableResultDataList;
    }

    // =========================== 餐桌拼桌表格相关 ===========================

    /**
     * 保存餐桌信息
     *
     * @param item
     */
    public static void saveTableTableList (TableResultDataListItem item) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.TABLE_ID, item.getTableId ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_ID, item.getId ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_CODE, item.getCode ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_ORDERNO, item.getOrderNo ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_ORDERSTATUS, item.getOrderStatus ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_PAYMENTSTATUS, item.getPaymentStatus ());
        values.put (TEOrderPoDataBase.TABLE_TABLELIST_PAYPRICE, item.getPayPrice ());

        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();

        db.insert (TEOrderPoDataBase.TABLE_TABLELIST_TABLE_NAME, null, values);
    }

    /**
     * 根据所属餐桌ID，查询拼桌信息
     *
     * @param tableId
     * @return
     */
    public static List<TableResultDataListItem> findTableTableList (String tableId) {
        List<TableResultDataListItem> dataListItems = new ArrayList<> ();
        String sql = "select * from " + TEOrderPoDataBase.TABLE_TABLELIST_TABLE_NAME + " where "
                + TEOrderPoDataBase.TABLE_ID + " = '" + tableId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (cursor.moveToNext ()) {
            TableResultDataListItem item = new TableResultDataListItem ();
            item.setTableId (tableId);
            item.setId (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_ID)));
            item.setCode (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_CODE)));
            item.setOrderNo (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_ORDERNO)));
            item.setPayPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_PAYPRICE)));
            item.setOrderStatus (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_ORDERSTATUS)));
            item.setPaymentStatus (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.TABLE_TABLELIST_PAYMENTSTATUS)));
            dataListItems.add (item);
        }
        cursor.close ();
        return dataListItems;
    }

    // =========================== 菜品分类表格相关 ===========================

    /**
     * 保存/更新 菜品分类信息
     *
     * @param dishCategory
     */
    public static void saveOrUpdateDishCategory (DishCategory dishCategory) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.MENU_CATEGORY_ID, dishCategory.getId ());
        values.put (TEOrderPoDataBase.MENU_CATEGORY_TITLE_EN, dishCategory.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.MENU_CATEGORY_TITLE_ZH, dishCategory.getName ().getZh_CN ());
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
//        int id = -1;
//        String sql = "select * from " + TEOrderPoDataBase.MENU_CATEGORY_TABLE + " where " + TEOrderPoDataBase.MENU_CATEGORY_ID + " = '" +
//                dishCategory.getId () + "'";
//        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
//        if (c.moveToFirst ()) {
//            id = c.getInt (c.getColumnIndex (TEOrderPoDataBase.MENU_CATEGORY_ID));
//        }
//        //  判断是进行保存还是进行更新
//        if (id == -1) {
        db.insert (TEOrderPoDataBase.MENU_CATEGORY_TABLE, null, values);
//        } else {
//            db.update (TEOrderPoDataBase.MENU_CATEGORY_TABLE, values, TEOrderPoDataBase.MENU_CATEGORY_ID + "=?", new String[]{String
//                    .valueOf (dishCategory.getId ())});
//        }
//        c.close ();
    }

    /**
     * 获取该餐厅所有菜品分类
     *
     * @return
     */
    public static List<DishCategory> findDishCategoryList () {
        List<DishCategory> dishCategoryList = new ArrayList<> ();

        String sql = "select * from " + TEOrderPoDataBase.MENU_CATEGORY_TABLE;

        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            DishCategory dishCategory = new DishCategory ();
            Name name = new Name ();
            name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.MENU_CATEGORY_TITLE_EN)));
            name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.MENU_CATEGORY_TITLE_ZH)));
            dishCategory.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.MENU_CATEGORY_ID)));
            dishCategory.setName (name);
            dishCategoryList.add (dishCategory);
            dishCategory.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.MENU_CATEGORY_ORDER)));
        }
        c.close ();
        return dishCategoryList;
    }
    // =========================== 菜品相关表格相关 ===========================

    //            "_id": "563b1a52f04ada66e729d918",
//            "categoryId": "563acd21f04ada66e729d396",
//            "name": {
//                  "zh": "菜品 20",
//                  "en": "DISH 20"
//              },
//            "code": "B9",
//            "price": "12.0",
//            "isRec": true,
//            "isSet": true,
//            "isHot": false,
//            "hotFactor": 20,
//            "display": true,
//            "deleted": false,
//            "allowCustomDiscount": true,
//            "canTakeAway": true,
//            "order": 20,
//            "fromDate": "2015-11-11T00:00:00.000Z",
//            "toDate": "2015-11-13T23:59:59.000Z",
//            "description": {
//        "zh": "中文中文中文中文",
//                "en": "englishenglishenglishenglishenglishenglishenglishenglishenglishenglishenglish"
//    },
//            "modifier": [],
//            "img": "images/3dc120d81acd4039a9b594c9463909b2/reward-img.jpg"
//}

    /**
     * 保存/更新 菜品信息
     *
     * @param dishItems
     */
    public static void saveOrUpdateDishInfo (DishItems dishItems) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.DISH_ID, dishItems.getId ());
        List<String> categoryIds = dishItems.getCategoryId ();
        String cateId = categoryIds.get (0);
        for (int i = 1; i < categoryIds.size (); i++) {
            cateId += "," + categoryIds.get (i);
        }
        values.put (TEOrderPoDataBase.DISH_CATEGORY_ID, cateId);
        values.put (TEOrderPoDataBase.DISH_NAME_EN, dishItems.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.DISH_NAME_ZH, dishItems.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.DISH_CODE, dishItems.getCode ());
        values.put (TEOrderPoDataBase.DISH_PRICE, dishItems.getPrice ());
        values.put (TEOrderPoDataBase.DISH_IS_SET, dishItems.isSet ());
        values.put (TEOrderPoDataBase.DISH_IS_HOT, dishItems.isHot ());
        values.put (TEOrderPoDataBase.DISH_IS_REC, dishItems.isRec ());
        values.put (TEOrderPoDataBase.DISH_HOT_FACTOR, dishItems.getHotFactor ());
        values.put (TEOrderPoDataBase.DISH_DISPLAY, dishItems.isDisplay ());
        values.put (TEOrderPoDataBase.DISH_DELETED, dishItems.isDeleted ());
        values.put (TEOrderPoDataBase.DISH_ALLOW_CUSTOM_DISCOUNT, dishItems.isAllowCustomDiscount ());
        values.put (TEOrderPoDataBase.DISH_CAN_TAKE_AWAY, dishItems.isCanTakeAway ());
        values.put (TEOrderPoDataBase.DISH_ORDER, dishItems.getOrder ());
        values.put (TEOrderPoDataBase.DISH_FROM_DATE, dishItems.getFromDate ());
        values.put (TEOrderPoDataBase.DISH_TO_DATE, dishItems.getToDate ());
        if (dishItems.getDescription () == null) {
            values.put (TEOrderPoDataBase.DISH_DESCRIPTION_EN, "");
            values.put (TEOrderPoDataBase.DISH_DESCRIPTION_ZH, "");
        } else {
            values.put (TEOrderPoDataBase.DISH_DESCRIPTION_EN, dishItems.getDescription ().getEn_US ());
            values.put (TEOrderPoDataBase.DISH_DESCRIPTION_ZH, dishItems.getDescription ().getZh_CN ());
        }
        String ModifierStr = "";
        if (dishItems.getModifier () != null && dishItems.getModifier ().size () > 0) {
            ModifierStr = dishItems.getModifier ().get (0);
            for (int i = 1; i < dishItems.getModifier ().size (); i++) {
                ModifierStr += "," + dishItems.getModifier ().get (i);
            }
        }

        values.put (TEOrderPoDataBase.DISH_MODIFIER, ModifierStr);
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();


        db.insert (TEOrderPoDataBase.DISH_INFO_TABLE_NAME, null, values);
    }

    /**
     * 该菜品对应的加料等选项ID集合
     *
     * @param dishId
     * @return
     */
    public static String findModifierListByDishId (String dishId) {
        String sql = "select " + TEOrderPoDataBase.DISH_MODIFIER + " from " + TEOrderPoDataBase.DISH_INFO_TABLE_NAME + " where " +
                TEOrderPoDataBase.DISH_ID + " = '" + dishId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        String str = null;
        while (cursor.moveToNext ()) {
            str = cursor.getString (0);
        }
        cursor.close ();
        // 查数据时，字符串需要加引号
        if (str != null && !str.equals ("")) {
            String[] strings = str.split (",");
            str = "'" + strings[0] + "'";
            for (int i = 0; i < strings.length; i++) {
                str += ",'" + strings[i] + "'";
            }
        }
        return str;
    }

    /**
     * 查询所有的菜品
     */
    public static List<DishItems> findAllDishInfoList () {
        List<DishItems> dishInfoList = new ArrayList<> ();
        String sql = "select * from " + TEOrderPoDataBase.DISH_INFO_TABLE_NAME;
        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            DishItems dishItems = new DishItems ();
            dishItems.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_ID)));
            Name name = new Name ();
            name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_NAME_ZH)));
            name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_NAME_EN)));
            dishItems.setName (name);
            String str = c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_CATEGORY_ID));
            String[] strs = str.split (",");
            List<String> cateIds = new ArrayList<> ();
            for (int i = 0; i < strs.length; i++) {
                cateIds.add (strs[i]);
            }
            dishItems.setCategoryId (cateIds);
            dishItems.setCode (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_CODE)));
            dishItems.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_ORDER)));
            dishItems.setAllowCustomDiscount (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_ALLOW_CUSTOM_DISCOUNT)) == 1);
            dishItems.setCanTakeAway (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_CAN_TAKE_AWAY)) == 1);
            dishItems.setDeleted (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_CAN_TAKE_AWAY)) == 1);
            DishItems.Description description = new DishItems.Description ();
            description.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_DESCRIPTION_EN)));
            description.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_DESCRIPTION_ZH)));
            dishItems.setDescription (description);
            dishItems.setDisplay (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_DISPLAY)) == 1);
            dishItems.setFromDate (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_FROM_DATE)));
            dishItems.setToDate (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_TO_DATE)));
            dishItems.setHotFactor (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_HOT_FACTOR)));
            dishItems.setImg (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_IMAGE)));
            dishItems.setIsHot (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_HOT)) == 1);
            dishItems.setIsRec (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_REC)) == 1);
            dishItems.setIsSet (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_SET)) == 1);
            dishItems.setPrice (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_PRICE)));
            String modifierStr = c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_MODIFIER));
            List<String> modifierList = new ArrayList<> ();
            if (modifierList != null && !modifierList.equals ("")) {
                String[] tempStr = modifierStr.split (",");
                for (int i = 0; i < tempStr.length; i++) {
                    modifierList.add (tempStr[i]);
                }
            }
            dishItems.setModifier (modifierList);
            dishInfoList.add (dishItems);
        }
        c.close ();
        return dishInfoList;
    }


    /**
     * 根据餐厅菜品分类查找菜品列表
     *
     * @param categoryId
     * @return
     */
    public static List<DishItems> findDishInfoListByCategoryId (String categoryId) {
        List<DishItems> dishInfoList = new ArrayList<> ();
        String sql = "select * from " + TEOrderPoDataBase.DISH_INFO_TABLE_NAME + " where " + TEOrderPoDataBase.DISH_CATEGORY_ID + " like '%" +
                categoryId + "%'";
        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            DishItems dishItems = new DishItems ();
            dishItems.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_ID)));
            Name name = new Name ();
            name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_NAME_ZH)));
            name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_NAME_EN)));
            dishItems.setName (name);
            String str = c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_CATEGORY_ID));
            String[] strs = str.split (",");
            List<String> cateIds = new ArrayList<> ();
            for (int i = 0; i < strs.length; i++) {
                cateIds.add (strs[i]);
            }
            dishItems.setCategoryId (cateIds);
            dishItems.setCode (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_CODE)));
            dishItems.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_ORDER)));
            dishItems.setAllowCustomDiscount (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_ALLOW_CUSTOM_DISCOUNT)) == 1);
            dishItems.setCanTakeAway (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_CAN_TAKE_AWAY)) == 1);
            dishItems.setDeleted (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_CAN_TAKE_AWAY)) == 1);
            DishItems.Description description = new DishItems.Description ();
            description.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_DESCRIPTION_EN)));
            description.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_DESCRIPTION_ZH)));
            dishItems.setDescription (description);
            dishItems.setDisplay (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_DISPLAY)) == 1);
            dishItems.setFromDate (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_FROM_DATE)));
            dishItems.setToDate (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_TO_DATE)));
            dishItems.setHotFactor (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_HOT_FACTOR)));
            dishItems.setImg (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_IMAGE)));
            dishItems.setIsHot (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_HOT)) == 1);
            dishItems.setIsRec (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_REC)) == 1);
            dishItems.setIsSet (c.getInt (c.getColumnIndex (TEOrderPoDataBase.DISH_IS_SET)) == 1);
            dishItems.setPrice (c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_PRICE)));
            String modifierStr = c.getString (c.getColumnIndex (TEOrderPoDataBase.DISH_MODIFIER));
            List<String> modifierList = new ArrayList<> ();
            String[] tempStr = modifierStr.split (",");
            for (int i = 0; i < tempStr.length; i++) {
                modifierList.add (tempStr[i]);
            }
            dishItems.setModifier (modifierList);
            dishInfoList.add (dishItems);
        }
        c.close ();
        return dishInfoList;
    }

    /**
     * 根据菜品ID查询改菜品所属分类
     *
     * @param dishId
     * @return
     */
    public static String findDishInfoCategoryIdsByDishId (String dishId) {
        String sql = "select " + TEOrderPoDataBase.DISH_CATEGORY_ID + " from " + TEOrderPoDataBase.DISH_INFO_TABLE_NAME + " where " +
                TEOrderPoDataBase.DISH_ID + "='" + dishId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        if (cursor.moveToFirst ()) {
            return cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.DISH_CATEGORY_ID));
        }
        cursor.close ();
        return null;
    }

    /**
     * 根据菜品ID查询菜品
     *
     * @param dishId
     * @return
     */
    public static DishItems findDishInfoByDishId (String dishId) {
        DishItems dishItems = null;
        String sql = "select * from " + TEOrderPoDataBase.DISH_INFO_TABLE_NAME + " where " + TEOrderPoDataBase.DISH_ID + "='" + dishId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        if (cursor.moveToFirst ()) {
            dishItems = new DishItems ();
            dishItems.setId (dishId);
            dishItems.setPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.DISH_PRICE)));
            String str = cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.DISH_CATEGORY_ID));
            String[] strs = str.split (",");
            List<String> cateIds = new ArrayList<> ();
            for (int i = 0; i < strs.length; i++) {
                cateIds.add (strs[i]);
            }
            dishItems.setCategoryId (cateIds);
            Name name = new Name ();
            name.setEn_US (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.DISH_NAME_EN)));
            name.setZh_CN (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.DISH_NAME_ZH)));
            dishItems.setName (name);
        }
        cursor.close ();
        return dishItems;
    }

    // =========================== 菜品标签属性表格相关 ===========================
    public static void saveOrUpdateDishItemModifier (ItemModifier itemModifier) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_ID, itemModifier.getId ());
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_TITLE_EN, itemModifier.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_TITLE_ZH, itemModifier.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_ONLY_FOR_REST, itemModifier.isOnlyForRest ());
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_IS_SINGLE, itemModifier.isSingle ());
        values.put (TEOrderPoDataBase.ITEM_MODIFIER_IS_OPTIONAL, itemModifier.isOptional ());
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();

        db.insert (TEOrderPoDataBase.ITEM_MODIFIER_TABLE_NAME, null, values);
    }

    public static List<ItemModifier> findDishItemModifier (String modifierString) {
        List<ItemModifier> itemModifierList = new ArrayList<> ();
        if (modifierString == null || modifierString.equals ("")) {
            return itemModifierList;
        } else {
            String sql = "select * from " + TEOrderPoDataBase.ITEM_MODIFIER_TABLE_NAME + " where " + TEOrderPoDataBase.ITEM_MODIFIER_ID + " in(" +
                    modifierString + ")";
            Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
            while (c.moveToNext ()) {
                ItemModifier itemModifier = new ItemModifier ();
                itemModifier.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_ID)));
                Name name = new Name ();
                name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_TITLE_ZH)));
                name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_TITLE_EN)));
                itemModifier.setName (name);
                itemModifier.setIsOptional (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_IS_OPTIONAL)) == 1);
                itemModifier.setIsSingle (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_IS_SINGLE)) == 1);
                itemModifier.setOnlyForRest (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_MODIFIER_ONLY_FOR_REST)) == 1);
                itemModifierList.add (itemModifier);
            }
            return itemModifierList;
        }
    }

    // =========================== 菜品标签表格相关 ===========================

    /**
     * 保存菜品标签选项
     *
     * @param option
     */
    public static void saveOrUpdateDishOption (Option option) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.OPTION_ITEM_MODIFIER_ID, option.getItemModifierId ());
        values.put (TEOrderPoDataBase.OPTION_ID, option.getId ());
        values.put (TEOrderPoDataBase.OPTION_NAME_EN, option.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.OPTION_NAME_ZH, option.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.OPTION_ORDER, option.getOrder ());
        values.put (TEOrderPoDataBase.OPTION_PRICE, option.getPrice ());
        values.put (TEOrderPoDataBase.OPTION_SELECTED, option.isSelected ());

        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();

        db.insert (TEOrderPoDataBase.OPTION_TABLE_NAME, null, values);
    }

    /**
     * 根据菜品ID获取菜品标签
     *
     * @return
     */
    public static List<Option> findDishOption (String modifierString) {
        List<Option> optionList = new ArrayList<> ();
        if (modifierString == null || modifierString.equals ("")) {
            return optionList;
        } else {
            String sql = "select * from " + TEOrderPoDataBase.OPTION_TABLE_NAME + " where " + TEOrderPoDataBase.OPTION_ITEM_MODIFIER_ID + " in(" +
                    modifierString + ")";
            Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
            while (c.moveToNext ()) {
                Option option = new Option ();
                option.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.OPTION_ID)));
                option.setItemModifierId (c.getString (c.getColumnIndex (TEOrderPoDataBase.OPTION_ITEM_MODIFIER_ID)));
                Name name = new Name ();
                name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.OPTION_NAME_EN)));
                name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.OPTION_NAME_ZH)));
                option.setName (name);
                option.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.OPTION_ORDER)));
                option.setPrice (c.getString (c.getColumnIndex (TEOrderPoDataBase.OPTION_PRICE)));
                option.setSelected (c.getInt (c.getColumnIndex (TEOrderPoDataBase.OPTION_SELECTED)) == 1);
                optionList.add (option);
            }
            return optionList;
        }
    }

    /**
     * 根据modifierId和optionId获取加料信息（Name）
     *
     * @param modifierId
     * @param modifierOptionId
     * @return
     */
    public static Option findOptionById (String modifierId, String modifierOptionId) {
        Option option = null;
        String sql = "select * from " + TEOrderPoDataBase.OPTION_TABLE_NAME
                + " where " + TEOrderPoDataBase.OPTION_ITEM_MODIFIER_ID + " = '" + modifierId + "'"
                + " and " + TEOrderPoDataBase.OPTION_ID + " = '" + modifierOptionId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        if (cursor.moveToFirst ()) {
            option = new Option ();
            Name name = new Name ();
            name.setEn_US (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.OPTION_NAME_EN)));
            name.setZh_CN (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.OPTION_NAME_ZH)));
            option.setName (name);
        }
        return option;
    }

    /**
     * 暂时只需要一个字段
     *
     * @param option_id
     * @return
     */
    public static ItemModifier findItemModifierByOptionId (String option_id) {
        ItemModifier itemModifier = new ItemModifier ();
        String sql = "select * from " + TEOrderPoDataBase.OPTION_TABLE_NAME + " where " + TEOrderPoDataBase.OPTION_ID + " ='" + option_id + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (cursor.moveToNext ()) {
            itemModifier.setId (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.OPTION_ITEM_MODIFIER_ID)));
        }
        cursor.close ();
        return itemModifier;
    }


    // =========================== 购物车表格相关 ===========================

    /**
     * 添加购物车信息
     *
     * @param shoppingCart
     * @return
     */
    public static long saveShoppingCart (ShoppingCart shoppingCart) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_ID, shoppingCart.getDishId ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_CODE, shoppingCart.getCode ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES, shoppingCart.getCopies ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_TOTALPRICE, shoppingCart.getTotalPrice ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE, shoppingCart.getPrice ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_EN, shoppingCart.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_ZH, shoppingCart.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK, shoppingCart.getRemark ());
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
        return db.insert (TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME, null, values);
    }

    /**
     * 根据购物车ID修改购物车信息
     *
     * @param shoppingCart
     */
    public static void updateShoppingCart (ShoppingCart shoppingCart) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_ID, shoppingCart.getDishId ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_CODE, shoppingCart.getCode ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES, shoppingCart.getCopies ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE, shoppingCart.getPrice ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_TOTALPRICE, shoppingCart.getTotalPrice ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_EN, shoppingCart.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_ZH, shoppingCart.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK, shoppingCart.getRemark ());
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
        db.update (TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME, values, TEOrderPoDataBase.AUTOINCREMENT_ID + " =? ", new String[]{String.valueOf
                (shoppingCart.getId ())});
    }

    /**
     * 根据ID修改份数
     *
     * @param shoppingCart
     */
    public static void updateShoppingCartCopiesById (ShoppingCart shoppingCart) {
        String sql = "update " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME + " set " + TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES + "=" +
                shoppingCart.getCopies () + " where " + TEOrderPoDataBase.AUTOINCREMENT_ID + "='" + shoppingCart.getId () + "'";
        TEOrderPoDataBase.getInstance ().getSQLiteDatabase ().execSQL (sql);
    }

    /**
     * 根据购物车ID删除购物车数据
     *
     * @param shoppingCart
     */
    public static void deleteShoppingCartById (ShoppingCart shoppingCart) {
        String sql = "delete from " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME + " where " + TEOrderPoDataBase.AUTOINCREMENT_ID + " = '" +
                shoppingCart.getId () + "'";
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
        db.execSQL (sql);
    }

    /**
     * 根据菜品ID查询购物车份数
     *
     * @param dishId
     * @return
     */
    public static int getShoppingCartCopiesByDishId (String dishId) {
        String sql = "select sum(" + TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES + ") from " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME + " where "
                + TEOrderPoDataBase.SHOPPINGCART_DISH_ID + " = '" + dishId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        int copies = 0;
        if (cursor.moveToFirst ()) {
            copies = cursor.getInt (0);
        }
        cursor.close ();
        return copies;
    }

    public static ShoppingCart findShoppingCartByShoppingCart (ShoppingCart cart) {
        ShoppingCart shoppingCart = null;
        String sql = "select * from " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME + " where " + TEOrderPoDataBase.SHOPPINGCART_DISH_ID + " = '" +
                cart.getDishId () + "' and " + TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK + " ='" + TEOrderPoDataBase.DB_NULL + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        if (cursor.moveToNext ()) {
            shoppingCart = new ShoppingCart ();
            shoppingCart.setId (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.AUTOINCREMENT_ID)));
            shoppingCart.setDishId (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_ID)));
            shoppingCart.setPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE)));
            shoppingCart.setTotalPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_TOTALPRICE)));
            shoppingCart.setCopies (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES)));
            shoppingCart.setCode (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_CODE)));
            shoppingCart.setRemark (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK)));
            Name name = new Name ();
            name.setEn_US (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_EN)));
            name.setZh_CN (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_ZH)));
            shoppingCart.setName (name);
        }
        cursor.close ();
        return shoppingCart;
    }

    /**
     * 查询购物车列表
     *
     * @return
     */
    public static List<ShoppingCart> findShoppingCartList () {
        String sql = "select * from " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME;
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        List<ShoppingCart> shoppingCarts = new ArrayList<> ();
        while (cursor.moveToNext ()) {
            ShoppingCart shoppingCart = new ShoppingCart ();
            shoppingCart.setId (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.AUTOINCREMENT_ID)));
            shoppingCart.setDishId (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_ID)));
            shoppingCart.setTotalPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_TOTALPRICE)));
            shoppingCart.setPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE)));
            shoppingCart.setCopies (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES)));
            shoppingCart.setCode (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_CODE)));
            shoppingCart.setRemark (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK)));
            Name name = new Name ();
            name.setEn_US (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_EN)));
            name.setZh_CN (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_ZH)));
            shoppingCart.setName (name);
            shoppingCarts.add (shoppingCart);
        }
        cursor.close ();
        return shoppingCarts;
    }

    /**
     * 根据菜品ID查询购物车列表
     *
     * @return
     */
    public static List<ShoppingCart> findShoppingCartListByDishId (String dishId) {
        String sql = "select * from " + TEOrderPoDataBase.SHOPPINGCART_TABLE_NAME + " where " + TEOrderPoDataBase.SHOPPINGCART_DISH_ID + "='" +
                dishId + "'";
        Cursor cursor = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        List<ShoppingCart> shoppingCarts = new ArrayList<> ();
        while (cursor.moveToNext ()) {
            ShoppingCart shoppingCart = new ShoppingCart ();
            shoppingCart.setId (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.AUTOINCREMENT_ID)));
            shoppingCart.setDishId (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_ID)));
            shoppingCart.setTotalPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_TOTALPRICE)));
            shoppingCart.setPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE)));
            shoppingCart.setPrice (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_PRICE)));
            shoppingCart.setCopies (cursor.getInt (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_COPIES)));
            shoppingCart.setCode (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_CODE)));
            shoppingCart.setRemark (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_REMARK)));
            Name name = new Name ();
            name.setEn_US (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_EN)));
            name.setZh_CN (cursor.getString (cursor.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_DISH_NAME_ZH)));
            shoppingCart.setName (name);
            shoppingCarts.add (shoppingCart);
        }
        cursor.close ();
        return shoppingCarts;
    }

    // =========================== 购物车标签表格相关 ===========================
    public static long saveShoppingCartLabel (Option option) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.SHOPPINGCART_ID, option.getShoppingCartId ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_ID, option.getId ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHMODIFIER_ID, option.getItemModifierId ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_NAME_EN, option.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_NAME_ZH, option.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_PRICE, option.getPrice ());
        values.put (TEOrderPoDataBase.SHOPPINGCART_LABEL_COUNT, option.getCount ());
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
        return db.insert (TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME, null, values);
    }

    /**
     * 根据购物车ID删除标签
     *
     * @param shoppingCartId
     */
    public static void deleteShoppingCartLabelByShopId (int shoppingCartId) {
        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();
        db.delete (TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME, TEOrderPoDataBase.SHOPPINGCART_ID + "=?", new String[]{String.valueOf
                (shoppingCartId)});
    }

    /**
     * 根据购物车ID查询标签列表
     *
     * @param shoppingCartId
     * @return
     */
    public static List<Option> findShopLabelByShopId (int shoppingCartId) {
        List<Option> optionList = new ArrayList<> ();
        String sql = "select * from " + TEOrderPoDataBase.SHOPPINGCART_LABEL_TABLE_NAME + " where " + TEOrderPoDataBase.SHOPPINGCART_ID + "=" +
                shoppingCartId;
        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            Option option = new Option ();
            option.setShoppingCartId (shoppingCartId);
            option.setItemModifierId (c.getString (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHMODIFIER_ID)));
            option.setCount (c.getInt (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_COUNT)));
            Name name = new Name ();
            name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_NAME_ZH)));
            name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_NAME_EN)));
            option.setName (name);
            option.setPrice (c.getString (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_PRICE)));
            option.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.SHOPPINGCART_LABEL_DISHTAG_ID)));
            optionList.add (option);
        }
        c.close ();
        return optionList;
    }

    //=========================税务相关==========================
    public static void saveOrUpdateTax (ItemTax itemTax) {
        ContentValues values = new ContentValues ();
        values.put (TEOrderPoDataBase.ITEM_TAX_ID, itemTax.getId ());
        values.put (TEOrderPoDataBase.ITEM_TAX_CODE, itemTax.getCode ());
        values.put (TEOrderPoDataBase.ITEM_TAX_ENABLE, itemTax.isEnable ());
        values.put (TEOrderPoDataBase.ITEM_TAX_FIXED, itemTax.isFixed ());
        values.put (TEOrderPoDataBase.ITEM_TAX_IS_INCLUDE, itemTax.isInclude ());
        values.put (TEOrderPoDataBase.ITEM_TAX_RATE, itemTax.getRate ());
        values.put (TEOrderPoDataBase.ITEM_TAX_NAME_EN, itemTax.getName ().getEn_US ());
        values.put (TEOrderPoDataBase.ITEM_TAX_NAME_ZH, itemTax.getName ().getZh_CN ());
        values.put (TEOrderPoDataBase.ITEM_TAX_ORDER, itemTax.getOrder ());

        SQLiteDatabase db = TEOrderPoDataBase.getInstance ().getSQLiteDatabase ();

//        int id = -1;
//        String sql = "select * from " + TEOrderPoDataBase.ITEM_TAX_TABLE + " where " + TEOrderPoDataBase.ITEM_TAX_ID + " = '" + itemTax
//                .getId () + "'";
//        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
//        if (c.moveToFirst ()) {
//            id = c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_ID));
//        }
//        //  判断是进行保存还是进行更新
//        if (id == -1) {
        db.insert (TEOrderPoDataBase.ITEM_TAX_TABLE, null, values);
//        } else {
//            db.update (TEOrderPoDataBase.ITEM_TAX_TABLE, values, TEOrderPoDataBase.ITEM_TAX_ID + "=?", new String[]{String.valueOf
//                    (itemTax.getId ())});
//        }
//        c.close ();
    }

    //  获取税率
    public static List<ItemTax> findItemTax () {
        List<ItemTax> itemTaxList = new ArrayList<> ();
        String sql = "select * from " + TEOrderPoDataBase.ITEM_TAX_TABLE;
        Cursor c = TEOrderPoDataBase.getInstance ().rawQuery (sql);
        while (c.moveToNext ()) {
            ItemTax mItemTax = new ItemTax ();
            mItemTax.setId (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_ID)));
            mItemTax.setTaxId (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_ID)));
            mItemTax.setOrder (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_ORDER)));
            mItemTax.setCode (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_CODE)));
            mItemTax.setEnable (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_ENABLE)) == 1);
            mItemTax.setFixed (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_FIXED)) == 1);
            mItemTax.setIsInclude (c.getInt (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_IS_INCLUDE)) == 1);
            mItemTax.setRate (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_RATE)));
            Name name = new Name ();
            name.setEn_US (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_NAME_EN)));
            name.setZh_CN (c.getString (c.getColumnIndex (TEOrderPoDataBase.ITEM_TAX_NAME_ZH)));
            mItemTax.setName (name);
            itemTaxList.add (mItemTax);
        }
        c.close ();
        return itemTaxList;
    }

}