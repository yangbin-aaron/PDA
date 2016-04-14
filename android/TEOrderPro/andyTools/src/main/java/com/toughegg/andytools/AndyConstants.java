package com.toughegg.andytools;

/**
 * Created by Andy on 15/11/3.
 */
public class AndyConstants {
    /**
     * sharepreference键值对
     */
    public static final String SETTING_FILE = "skyun_sharepreference";
    public static final String ONLY_WIFI = "only_wifi";


    /**
     * 是否打开debug *\
     */
    public static boolean DEBUG = true;

    /** 为了更真实的模拟网络请求。如果启用，在读取完成以后，并不立即返回而是延迟500毫秒再返回 */
    public static boolean useDelayCache = false;
    /** 如果启用了useDelayCache，本属性才有效。单位:ms */
    public static long delayTime = 500;


    /**
     * 磁盘缓存大小
     */
    public static int DISK_CACHE_SIZE = 5 * 1024 * 1024;
    /**
     * Http请求超时时间 *
     */
    public static int TIMEOUT = 5000;

    /** 线程池大小 **/
    public static int NETWORK_POOL_SIZE = 4;

    /** 同时允许多少个下载任务，建议不要太大(注意：本任务最大值不能超过NETWORK_POOL_SIZE) */
    public static int MAX_DOWNLOAD_TASK_SIZE = 2;


    /**
     * 缓存文件夹 *
     */
    public static String CACHEPATH = "SKYunLibrary/cache";

    /**
     * 图片缓存文件夹
     */
    public static String IMAGEPATH="SKYunLibrary/image";

    /**
     * 默认编码  *
     */
    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


}
