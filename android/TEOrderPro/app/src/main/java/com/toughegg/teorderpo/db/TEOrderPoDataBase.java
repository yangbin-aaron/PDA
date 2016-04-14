package com.toughegg.teorderpo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Andy on 15/8/10.
 * 数据库创建
 */
public class TEOrderPoDataBase {
    public Context mContext;
    private static TEOrderPoDataBase database = null;
    private SQLiteDatabase mSQLiteDatabase;
    private static final int DB_VERSION_NUMBER = 2; //数据库版本

    public static final String AUTOINCREMENT_ID = "id";
    // 数据库名称常量
    private static final String DATABASE_NAME = "TEOrderPo.db";

    // null常量
    public static final String DB_NULL = "db_null";


    //------------------Table Info  餐桌---------------------------
    //            "_id": "5649ce9a72c68923e",    //餐桌id
//            "capacity": 5,      //餐桌容量
//            "code": "T1",       //餐桌桌号
//            "customerNum": 0,   //当前入座人数
//            "enabled": true,    //餐桌可用状态
//            "maximum": 7,       //餐桌最大容量
//            "minimum": 3,       //餐桌最小入座数
//            "occupied": false,  //是否被占用
//            "order": 1,         //餐桌排序序号
//            "orderNo": "",      //此餐桌当前对应的订单id
//            "orderStatus": 1,   //订单状态
//            "payPrice": "",     //订单价格
//            "paymentStatus": 0, //订单支付状态 0表示未支付， 1表示已支付
//            "restId": "5649569e72c6892bf008df49"  //餐桌所在餐厅id

    public static final String TABLE_INFO_TABLE_NAME = "table_info_table";
    public static final String TABLE_ID = "table_id";
    public static final String TABLE_CAPACITY = "table_capacity";
    public static final String TABLE_CODE = "table_code";
    public static final String TABLE_CUSTOMER_NUMBER = "table_customer_number";
    public static final String TABLE_ENABLED = "table_enabled";
    public static final String TABLE_MAXIMUM = "table_maximum";
    public static final String TABLE_MINIMUM = "table_minimum";
    public static final String TABLE_OCCUPIED = "table_occupied";
    public static final String TABLE_ORDER = "table_order";
    public static final String TABLE_ORDER_NO = "table_order_no";
    public static final String TABLE_ORDER_STATUS = "table_order_status";
    public static final String TABLE_PAY_PRICE = "table_payprice";
    public static final String TABLE_PAYMENT_STATUS = "table_payment_status";
    public static final String TABLE_REST_ID = "table_rest_id";

    // ------------------Table_TableList 餐桌拼桌---------------------------
    public static final String TABLE_TABLELIST_TABLE_NAME = "table_tablelist_table_name";
    public static final String TABLE_TABLELIST_ID = "table_tablelist_id";
    public static final String TABLE_TABLELIST_CODE = "table_tablelist_code";
    public static final String TABLE_TABLELIST_ORDERNO = "table_tablelist_orderno";
    public static final String TABLE_TABLELIST_PAYPRICE = "table_tablelist_payprice";
    public static final String TABLE_TABLELIST_ORDERSTATUS = "table_tablelist_orderstatus";
    public static final String TABLE_TABLELIST_PAYMENTSTATUS = "table_tablelist_paymentstatus";

    //------------------menu_classify Info菜品分类信息----------------------------------------

    /**
     * "_id": "563accf6f04ada66e729d391",
     * "name": {
     * "zh": "类别 A",
     * "en": "category A"
     * },
     * "order": 1   排序
     */
    public static final String MENU_CATEGORY_TABLE = "menu_category_table";
    public static final String MENU_CATEGORY_ID = "menu_category_id";
    public static final String MENU_CATEGORY_TITLE_ZH = "menu_category_title_zh";
    public static final String MENU_CATEGORY_TITLE_EN = "menu_category_title_en";
    public static final String MENU_CATEGORY_ORDER = "menu_category_order";


    //------------------dish Info 菜品信息----------------------------------------
    public static final String DISH_INFO_TABLE_NAME = "dish_info_table";
    public static final String DISH_ID = "dish_id";
    public static final String DISH_CATEGORY_ID = "dish_category_id";
    public static final String DISH_NAME_ZH = "dish_name_zh";
    public static final String DISH_NAME_EN = "dish_name_en";
    public static final String DISH_CODE = "dish_code";
    public static final String DISH_PRICE = "dish_price";
    public static final String DISH_IS_SET = "dish_is_set";
    public static final String DISH_IS_HOT = "dish_is_hot";
    public static final String DISH_IS_REC = "dish_is_rec";
    public static final String DISH_HOT_FACTOR = "dish_hot_factor";
    public static final String DISH_DISPLAY = "dish_display";
    public static final String DISH_DELETED = "dish_deleted";
    public static final String DISH_ALLOW_CUSTOM_DISCOUNT = "dish_allow_custom_discount";
    public static final String DISH_CAN_TAKE_AWAY = "dish_can_take_away";
    public static final String DISH_ORDER = "dish_order";
    public static final String DISH_FROM_DATE = "dish_from_date";
    public static final String DISH_TO_DATE = "dish_to_date";
    public static final String DISH_DESCRIPTION_ZH = "dish_description_zh";
    public static final String DISH_DESCRIPTION_EN = "dish_description_en";
    public static final String DISH_MODIFIER = "dish_modifier";
    public static final String DISH_IMAGE = "dish_image";


    //------------------ItemModifier Info 菜品标签分类信息----------------------------------------
//    _id	:	563acd45f04ada66e729d39a
//
//    zh	:	modifier A
//
//    en	:	加料 A
//
//    isSingle	:	false
//
//    isOptional	:	false
//
//    onlyForRest	:	false


    public static final String ITEM_MODIFIER_TABLE_NAME = "item_modifier_table_name";
    public static final String ITEM_MODIFIER_ID = "item_modifier_id";
    public static final String ITEM_MODIFIER_TITLE_ZH = "item_modifier_title_zh";
    public static final String ITEM_MODIFIER_TITLE_EN = "item_modifier_title_en";
    public static final String ITEM_MODIFIER_IS_SINGLE = "item_modifier_is_single";
    public static final String ITEM_MODIFIER_IS_OPTIONAL = "item_modifier_is_optional";
    public static final String ITEM_MODIFIER_ONLY_FOR_REST = "item_modifier_only_for_rest";


    //------------------option Info 菜品标签信息----------------------------------------
//    _id	:	563acd88f04ada66e729d3a4
//    zh	:	选项 1
//    en	:	option 1
//    selected	:	false
//    price	:	10.2
//    order	:	1

    public static final String OPTION_TABLE_NAME = "option_table_name";
    public static final String OPTION_ID = "option_id";
    public static final String OPTION_ITEM_MODIFIER_ID = "option_item_modifier_id";
    public static final String OPTION_NAME_ZH = "option_name_zh";
    public static final String OPTION_NAME_EN = "option_name_en";
    public static final String OPTION_PRICE = "option_price";
    public static final String OPTION_SELECTED = "option_selected";
    public static final String OPTION_ORDER = "option_order";


    //------------------ShoppingCart 购物车 ----------------------------------------
    public static final String SHOPPINGCART_TABLE_NAME = "shoppingcart_table";
    public static final String SHOPPINGCART_DISH_ID = "shoppingcart_dish_id";
    public static final String SHOPPINGCART_DISH_CODE = "shoppingcart_dish_code";
    public static final String SHOPPINGCART_DISH_NAME_EN = "shoppingcart_dish_name_en";
    public static final String SHOPPINGCART_DISH_NAME_ZH = "shoppingcart_dish_name_zh";
    public static final String SHOPPINGCART_DISH_COPIES = "shoppingcart_dish_copies";
    public static final String SHOPPINGCART_DISH_PRICE = "shoppingcart_dish_price";
    public static final String SHOPPINGCART_DISH_TOTALPRICE = "shoppingcart_dish_totalprice";
    public static final String SHOPPINGCART_DISH_REMARK = "shoppingcart_dish_remark";

    //------------------ShoppingCart LabelList 购物车菜品label ----------------------------------------
    public static final String SHOPPINGCART_LABEL_TABLE_NAME = "shoppingcart_label_table";
    public static final String SHOPPINGCART_ID = "shoppingcart_id";
    public static final String SHOPPINGCART_LABEL_DISHTAG_ID = "shoppingcart_label_dishtag_id";
    public static final String SHOPPINGCART_LABEL_DISHMODIFIER_ID = "shoppingcart_label_dishmodifier_id";
    public static final String SHOPPINGCART_LABEL_DISHTAG_NAME_ZH = "shoppingcart_label_dishtag_name_zh";
    public static final String SHOPPINGCART_LABEL_DISHTAG_NAME_EN = "shoppingcart_label_dishtag_name_en";
    public static final String SHOPPINGCART_LABEL_PRICE = "shoppingcart_label_price";
    public static final String SHOPPINGCART_LABEL_COUNT = "shoppingcart_label_count";


    //---------------------item tax----------
//    _id	:	563acbd7f04ada66e729d374
//    name		{2}
//    zh	:	GST
//    en	:	GST
//    enable	:	true
//    rate	:	0.07
//    isInclude	:	false
//    fixed	:	true
//    order	:	2
//    code	:	gst
    public static final String ITEM_TAX_TABLE = "item_tax_table";
    public static final String ITEM_TAX_ID = "item_tax_id";
    public static final String ITEM_TAX_NAME_EN = "item_tax_name_en";
    public static final String ITEM_TAX_NAME_ZH = "item_tax_name_zh";
    public static final String ITEM_TAX_ENABLE = "item_tax_enable";
    public static final String ITEM_TAX_RATE = "item_tax_rate";
    public static final String ITEM_TAX_IS_INCLUDE = "item_tax_is_include";
    public static final String ITEM_TAX_FIXED = "item_tax_fixed";
    public static final String ITEM_TAX_ORDER = "item_tax_order";
    public static final String ITEM_TAX_CODE = "item_tax_code";
    public static final String ITEM_TAX_EFFECTORDERTYPES = "item_tax_effectOrderTypes";
    public static final String ITEM_TAX_AFTERDISCOUNT = "item_tax_afterDiscount";
    public static final String ITEM_TAX_PRECONDITION = "item_tax_precondition";
    public static final String ITEM_TAX_AMOUNT = "item_tax_amount";


    public TEOrderPoDataBase (Context mContext) {
        this.mContext = mContext;
        database = this;
        try {
            mSQLiteDatabase = mContext.openOrCreateDatabase (DATABASE_NAME, Context.MODE_PRIVATE, null);
            createOrUpgradeTable ();
        } catch (SQLiteException se) {
            se.printStackTrace ();
        }
    }


    public void createOrUpgradeTable () {
        if (mSQLiteDatabase != null) {
            int version = mSQLiteDatabase.getVersion ();
            if (version < 1) {

                //----Create TableInfo Table 餐桌信息--------------------

                String createTableInfoSql = "CREATE TABLE " + TABLE_INFO_TABLE_NAME + " (" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT," +
                        " " + TABLE_ID + " TEXT, " + TABLE_CAPACITY + " INTEGER , " + TABLE_CODE + " TEXT, " + TABLE_CUSTOMER_NUMBER +
                        " INTEGER," + TABLE_ENABLED + " BLOB," + TABLE_MAXIMUM + " INTEGER," + TABLE_MINIMUM + " INTEGER, " +
                        TABLE_OCCUPIED + " BLOB, " + TABLE_ORDER + " INTEGER , " + TABLE_ORDER_NO + " TEXT , " + TABLE_ORDER_STATUS + " INTEGER ," +
                        TABLE_PAY_PRICE + " TEXT, " + TABLE_PAYMENT_STATUS + " INTEGER, " + TABLE_REST_ID + " TEXT)";


                //----Create MenuCATEGORY Table  菜品分类--------------------

                String createMenuCategorySql = "CREATE TABLE " + MENU_CATEGORY_TABLE + " (" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT," + MENU_CATEGORY_ID + " TEXT, " + MENU_CATEGORY_TITLE_ZH + " TEXT , " + MENU_CATEGORY_TITLE_EN +
                        " TEXT, " + MENU_CATEGORY_ORDER + " INTEGER )";


                //----Create DishInfo Table  菜品信息--------------------
                String createDishInfoSql = "CREATE TABLE " + DISH_INFO_TABLE_NAME + " (" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        + " " + DISH_ID + "  TEXT, " + DISH_CATEGORY_ID + " TEXT," + DISH_NAME_ZH + " TEXT, " + DISH_NAME_EN + " TEXT,"
                        + DISH_CODE + " TEXT," + DISH_PRICE + " TEXT," + DISH_IS_REC + " BLOB," + DISH_IS_SET + " BLOB," + DISH_IS_HOT + " BLOB,"
                        + DISH_HOT_FACTOR + " INTEGER, " + DISH_DISPLAY + " BLOB," + DISH_DELETED + " BLOB," + DISH_ALLOW_CUSTOM_DISCOUNT + " BLOB," +
                        "" + DISH_CAN_TAKE_AWAY + " BLOB, " + DISH_ORDER + " INTEGER," + DISH_FROM_DATE + " TEXT," + DISH_TO_DATE + " TEXT," +
                        DISH_DESCRIPTION_ZH + " TEXT," + DISH_DESCRIPTION_EN + " TEXT," + DISH_MODIFIER + " TEXT," + DISH_IMAGE + " TEXT)";

                // ----Create DishOption Table  菜品标签分类-------------------

                String createDishOptionSql = "CREATE TABLE " + ITEM_MODIFIER_TABLE_NAME + " (" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT, " + ITEM_MODIFIER_ID + "  TEXT," + ITEM_MODIFIER_TITLE_EN + " TEXT," + ITEM_MODIFIER_TITLE_ZH + " TEXT," +
                        ITEM_MODIFIER_IS_SINGLE + " BLOB," + ITEM_MODIFIER_IS_OPTIONAL + " BLOB," + ITEM_MODIFIER_ONLY_FOR_REST + " BLOB)";

                //----Create DishLabel Table  菜品标签--------------------

                String createDishLableSql = "CREATE TABLE " + OPTION_TABLE_NAME + " (" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT, " + OPTION_ID + " TEXT, " + OPTION_ITEM_MODIFIER_ID + " TEXT," + OPTION_NAME_ZH +
                        " TEXT, " + OPTION_NAME_EN + " TEXT," + OPTION_SELECTED + " BLOB," + OPTION_PRICE + " TEXT," +
                        OPTION_ORDER + " INTEGER)";

                //------------------ShoppingCart 购物车 ----------------------------------------
                String createShoppingCartSql = "CREATE TABLE " + SHOPPINGCART_TABLE_NAME + "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT ," + SHOPPINGCART_DISH_ID + " TEXT," + SHOPPINGCART_DISH_CODE + " TEXT," + SHOPPINGCART_DISH_NAME_EN + " " +
                        "TEXT," + SHOPPINGCART_DISH_NAME_ZH + " TEXT," + SHOPPINGCART_DISH_COPIES + " INTEGER," + SHOPPINGCART_DISH_PRICE + " TEXT," +
                        SHOPPINGCART_DISH_TOTALPRICE + " TEXT," + SHOPPINGCART_DISH_REMARK + " TEXT)";

                //------------------ShoppingCart LabelList 购物车菜品label ----------------------------------------
                String createShoppingCartLabelSql = "CREATE TABLE " + SHOPPINGCART_LABEL_TABLE_NAME + "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY " +
                        "KEY AUTOINCREMENT ," + SHOPPINGCART_ID + " TEXT," + SHOPPINGCART_LABEL_DISHTAG_ID + " TEXT," +
                        SHOPPINGCART_LABEL_DISHMODIFIER_ID + " TEXT," + SHOPPINGCART_LABEL_DISHTAG_NAME_ZH + " TEXT," +
                        SHOPPINGCART_LABEL_DISHTAG_NAME_EN + " TEXT," + SHOPPINGCART_LABEL_COUNT + " INTEGER," + SHOPPINGCART_LABEL_PRICE + " TEXT)";

                //------------------ITEM_TAX 税率表格-----------------------------------------------------------------------

                String createTaxSql = "CREATE TABLE " + ITEM_TAX_TABLE + "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT ," + ITEM_TAX_ID + " TEXT," + ITEM_TAX_NAME_EN + " TEXT," + ITEM_TAX_NAME_ZH + " TEXT," + ITEM_TAX_ENABLE +
                        " BLOB," + ITEM_TAX_FIXED + " BLOB," + ITEM_TAX_RATE + " INTEGER," + ITEM_TAX_IS_INCLUDE + " BLOB," + ITEM_TAX_CODE + " " +
                        "TEXT," + ITEM_TAX_ORDER + " INTEGER )";


                mSQLiteDatabase.execSQL (createTableInfoSql);
                mSQLiteDatabase.execSQL (createMenuCategorySql);
                mSQLiteDatabase.execSQL (createDishInfoSql);
                mSQLiteDatabase.execSQL (createDishOptionSql);
                mSQLiteDatabase.execSQL (createDishLableSql);
                mSQLiteDatabase.execSQL (createShoppingCartSql);
                mSQLiteDatabase.execSQL (createShoppingCartLabelSql);
                mSQLiteDatabase.execSQL (createTaxSql);
            }

            if (version < 2) {
                //------------------Table_TableList 餐桌拼桌-----------------------------------------------------------------------
                String createTableTableListSql = "CREATE TABLE " + TABLE_TABLELIST_TABLE_NAME + "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT ," + TABLE_ID + " TEXT," + TABLE_TABLELIST_ID + " TEXT," + TABLE_TABLELIST_CODE + " TEXT," +
                        TABLE_TABLELIST_ORDERNO + " TEXT," + TABLE_TABLELIST_PAYPRICE + " TEXT," + TABLE_TABLELIST_PAYMENTSTATUS + " INTEGER," +
                        TABLE_TABLELIST_ORDERSTATUS + " INTEGER)";
                mSQLiteDatabase.execSQL (createTableTableListSql);

                // 添加字段 itemTax
                String alter1 = "alter table " + ITEM_TAX_TABLE + " add " + ITEM_TAX_AFTERDISCOUNT + "  BLOB;";
                mSQLiteDatabase.execSQL (alter1);
                String alter2 = "alter table " + ITEM_TAX_TABLE + " add " + ITEM_TAX_EFFECTORDERTYPES + " TEXT;";
                mSQLiteDatabase.execSQL (alter2);
                String alter3 = "alter table " + ITEM_TAX_TABLE + " add " + ITEM_TAX_PRECONDITION + " TEXT;";
                mSQLiteDatabase.execSQL (alter3);
                String alter4 = "alter table " + ITEM_TAX_TABLE + " add " + ITEM_TAX_AMOUNT + " TEXT;";
                mSQLiteDatabase.execSQL (alter4);

                mSQLiteDatabase.setVersion (DB_VERSION_NUMBER);
            }
        }
    }


    public static TEOrderPoDataBase getInstance () {
        return database;
    }

    public SQLiteDatabase getSQLiteDatabase () {
        return mSQLiteDatabase;
    }

    public Cursor rawQuery (String sql) {
        return mSQLiteDatabase.rawQuery (sql, null);
    }

    public void execSQL (String sql) {
        mSQLiteDatabase.execSQL (sql);
    }

    public long insert (String table, ContentValues values) {
        return mSQLiteDatabase.insert (table, null, values);
    }

    public void closeSQLiteDatabase () {
        mSQLiteDatabase.close ();
    }

}
