package com.toughegg.teorderpo;

/**
 * Created by toughegg on 15/8/6.
 */
public class TEOrderPoConstans {
    // ==================== API ====================
    // 3.0
    public static final String API_HTTP = "http://";
    public static final String API_DK = ":3000/";

    //test: https://toughegg-test.s3-ap-southeast-1.amazonaws.com/
//    public static final String URL_LUA = "https://toughegg-test.s3-ap-southeast-1.amazonaws.com/"; // Lua 下载路径

    //dev: https://toughegg-development.s3.cn-north-1.amazonaws.com.cn/
//    public static final String URL_LUA = "https://toughegg-development.s3.cn-north-1.amazonaws.com.cn/"; // Lua 下载路径

    //live: https://toughegg-production.s3-ap-southeast-1.amazonaws.com/
    public static final String URL_LUA = "https://toughegg-production.s3-ap-southeast-1.amazonaws.com/"; // Lua 下载路径


    public static final String URL_LOGIN = "api/lms/login";// 登录
    public static final String URL_RESTAURANT_INFO = "api/lms/restaurant-info";// 店内服务器获取餐厅Restaurant信息
    public static final String URL_SYNC_INFO = "api/lms/sync-info"; // 同步数据
    public static final String URL_MENU_LIST = "api/lms/menu";
    public static final String URL_TABLE_LIST = "api/lms/table-list";// 获取餐桌信息列表
    public static final String URL_ORDER_NEW = "api/lms/order/order-new";  // 创建订单
    public static final String URL_ORDER_UPDATE = "api/lms/order/order-update";  // 更新订单
    public static final String URL_ORDER_DETAIL = "api/lms/order/order-detail";// 获取网络订单详情
    public static final String URL_ORDER_CHANGE_TABLE = "api/lms/order/order-change-table";// 换桌
    public static final String URL_ORDER_PRINTED = "api/lms/order/order-printed";// 确认打印
    public static final String URL_MERGE_TABLE = "api/lms/table-merge";// 拼桌
    public static final String URL_UNMERGE_TABLE = "api/lms/table-unmerge";// 拆桌


    // 炸鸡店
    public static final String TE_ORDER_HTTP_NET = "http://api-uat.toughegg.sg/restaurantapi/";
    public static final String TE_ORDER_HTTP_NET1 = "http://api-uat.toughegg.sg/";
    public static final String TE_ORDER_HTTP_NET2 = "http://api-uat.toughegg.sg";
    // 记得吃
//    public static final String TE_ORDER_HTTP_NET = "http://app-uat.toughegg.sg/restaurantapi/";
//    public static final String TE_ORDER_HTTP_NET1 = "http://app-uat.toughegg.sg/";
//    public static final String TE_ORDER_HTTP_NET2 = "http://app-uat.toughegg.sg";

    // ==================== 广播机制 ====================
    public static final String ACTION_UPDATE_SHOPPINGCAR_DATA = "com.toughegg.teorderpo.ACTION_UPDATE_SHOPPINGCAR_DATA";// 改变菜品份数
    public static final String ACTION_CLOSE_SHOPPINGCAR_ACTIVITY = "com.toughegg.teorderpo.ACTION_CLOSE_SHOPPINGCAR_ACTIVITY";// 关闭购物车界面
    public static final String ACTION_CLOSE_APP = "com.toughegg.teorderpo.ACTION_CLOSE_APP";// 关闭应用
    public static final String ACTION_UPDATE_DISHINFO_PRICE = "com.toughegg.teorderpo.ACTION_UPDATE_DISHINFO_PRICE";// 加价广播
    public static final String ACTION_TO_TABLE_ACTIVITY = "com.toughegg.teorderpo.ACTION_TO_TABLE_ACTIVITY";// 回到主页（餐桌列表）
    public static final String ACTION_CHANGE_LANG = "com.toughegg.teorderpo.ACTION_CHANGE_LANG";// 改变语言（餐桌列表）

    // ==================== 消息机制what ====================
    public static final int HANDLER_WHAT_NET_CONNECTION_FAIL = 0x00;// 网络连接失败
    public static final int HANDLER_WHAT_SERVICE_CONNECTION_FAIL = 0x01;// 服务器连接失败
    public static final int HANDLER_WHAT_POST_SUCCESS = 0x02;// 成功
    public static final int HANDLER_WHAT_POST_FAIL = 0x03;// 失败
    public static final int HANDLER_WHAT_NET_ERROR = 0x04; // 网络数据异常

    public static final int HANDLER_WHAT_TABLELIST_DATA = 0x05; // 获取餐桌列表
    public static final int HANDLER_WHAT_DISHDETAIL_SETDATA = 0x06; // 设置菜品详情信息
    public static final int HANDLER_WHAT_DISHDETAIL_SAVE_OK = 0x07; // 保存数据成功
    public static final int HANDLER_WHAT_DISHDETAIL_UPDATE_OK = 0x08; // 修改数据成功
    public static final int HANDLER_WHAT_ORDER_DISHLIST = 0x09; // 设置菜品列表
    public static final int HANDLER_WHAT_ORDER_SAVE_OK = 0x10; // 保存到购物车成功
    public static final int HANDLER_WHAT_ORDER_DELETE_OK = 0x11; // 成功删除购物车数据
    public static final int HANDLER_WHAT_SHOPPINGCARTLIST = 0x12; // 设置购物车列表
    public static final int HANDLER_WHAT_SEARCHLIST = 0x13; // 设置搜索列表


    // ==================== 传递数据到KEY ====================
    public static final String KEY_OPEN_DISH_DEFAIL_ACTIVITY_DISHINFO = "key_open_dish_defail_activity_dishinfo";// 进入菜品详情时传递菜品信息的KEY
    public static final String KEY_OPEN_DISH_DEFAIL_ACTIVITY_SHOPPINGCART = "key_open_dish_defail_activity_shoppingcart";// 进入菜品详情时传递购物车信息的KEY
    public static final String KEY_OPEN_DISH_DEFAIL_ACTIVITY_FROM = "key_open_dish_defail_activity_from";// 从哪里进入菜品详情时的KEY
    public static final String KEY_TABLE_INFO = "key_table_info";// 餐桌信息
    public static final String KEY_FROM_LOGINACTIVITY_STYLE = "key_from_loginactivity_style";// 进入loginactivity的方式

    // =====================sharepreference 文件name========================
    public static final String SHAREPREFERENCE_NAME = "teOrderPo";

    //======================sharepreference key==============
    public static final String SP_CHANGE_LAN = "sp_change_lan";// 改变语言
    public static final String SP_SELECT_LAN = "sp_select_lan";// 选择语言:en,zh,zh_tw
    public static final String SP_RESTAURANT_ID = "sp_restaurant_id"; // 餐厅ID；
    public static final String SP_MERCHANT_ID = "sp_merchant_id";// 商户ID
    public static final String SP_GST_TAX = "sp_gst_tax";// 国家税
    public static final String SP_SERVICE_TAX = "sp_service_tax";// 服务税
    public static final String SP_LOGIN_RESTAURANT_USER_ID = "sp_login_restaurant_user_id";
    public static final String SP_SELECT_PEOPLE = "sp_select_people";// 保存选择的人数
    public static final String SP_SELECT_TABLE_NUMBER = "sp_select_table_number"; //保存选择table number
    public static final String SP_SELECT_TABLE_ID = "sp_select_table_id"; //保存选择table id
    public static final String SP_COUNT_PRICE = "sp_count_price";// 总价格
    public static final String SP_ORDER_TYPE = "sp_order_type";// 点单方式：1堂食2打包
    // ------logind sharePreference key------
    public static final String SP_LOGIN_TOKEN = "sp_login_token";
    public static final String SP_LOGIN_USER_ID = "sp_login_user_id";
    public static final String SP_LOGIN_REST_ID = "sp_login_rest_id";
    public static final String SP_LOGIN_EMAIL = "sp_login_email";
    public static final String SP_LOGIN_GROUP_ID = "sp_login_group_id";
    public static final String SP_LOGIN_STATUS = "sp_login_status";
    public static final String SP_LOGIN_REST_CODE = "sp_login_rest_code";
    public static final String SP_LOGIN_EMPLOYEE_ID = "sp_login_employee_id";
    public static final String SP_LOGIN_USERNAME = "sp_login_username";
    public static final String SP_LOGIN_PASSWORD = "sp_login_password";
    public static final String SP_LOGIN_STATE = "sp_login_state";  //登陆状态
    public static final String SP_SERVICE_IP = "sp_service_ip";  //服务器IP


    //=====================固定string=========================
    public static final String RESTAURANTID = "RestaurantId";
    public static final String LOGINRESTAURANTUSERID = "LoginRestaurantUserId";
    public static final String LAN = "lan";
    public static final String KEY = "key";

    // ==================== 其他 ====================
    // ********** 点单方式:0堂食1打包 **********
    public static final int ORDER_TYPE_HOUSE = 1;// 堂食
    public static final int ORDER_TYPE_TAKEAWAY = 2;// 打包
    // ********** 每个菜品最多只能点的份数 **********
    public static final int DISH_MAX_COPISE = 999;
    // ********** 中英文标示 **********
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_CHINESE = "zh";
    public static final String LANGUAGE_CHINESE_TW = "zh_tw";
    // ********** 金额符号 **********
    public static final String DOLLAR_SIGN = "$";
    // ********** 金额的格式 **********
    public static final String PRICE_FORMAT_ONE = "#0.0";// 价格按一位小数显示
    public static final String PRICE_FORMAT_TWO = "#0.00";// 价格按两位小数显示
    // ********** 进入Login Activity的方式 **********
    public static final int STYLE_DEFAULT = 0;// 其他
    public static final int STYLE_LOGOUT = 1;// 从设置里面退出登陆
    // ********** 时间的各种形式 **********
    public static final String FORMAT_DATE_YY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_YY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_YMDHMSSSS = "yyyy-MM-dd'T'HH:mm:ss.sss";
    public static final String DATA_FORMAT_YMDHMSSSSZZZZZ = "yyyy-MM-dd'T'HH:mm:ss.sssZZZZZ"; //包括时区解析


    //=======================打印ip设置=======================
    public static final int PRINT_PORT = 9100;// 端口

    public static final String CASH = "cash";// 厨房2
    public static final String KITCHEN = "kitchen";// 厨房1
    public static final String RECEIPT = "receipt";// 收据
    public static final String BAR = "bar";// 吧台
    public static final String PRINT_DETALS_IP = "1.1.1.1";
}
