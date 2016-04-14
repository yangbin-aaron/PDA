package com.toughegg.teorderpo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.WelcomeActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toughegg on 15/7/6.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    // 打印log标记
    public static final String TAG = "CrashHandler";
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler ();
    // 程序的Context对象
    private Context mContext;
    // 系统默认的 UncaughtException 处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String> ();

    /**
     * 保证只有一个 CrashHandler 实例
     */
    private CrashHandler () {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static CrashHandler getInstance () {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init (Context context) {
        mContext = context;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler ();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler (this);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException (Thread thread, Throwable ex) {
        if (!handleException (ex) && mDefaultHandler != null) {// 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException (thread, ex);
        } else {
            try {
                Thread.sleep (5000);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            // 重新启动程序，注释上面的退出程序
            Intent intent = new Intent ();
            intent.setClass (mContext, WelcomeActivity.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity (intent);

            android.os.Process.killProcess (android.os.Process.myPid ());
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException (Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用 Toast 来显示异常信息提醒用户
        new Thread () {
            @Override
            public void run () {
                Looper.prepare ();
                Toast.makeText (mContext, R.string.app_error_debug, Toast.LENGTH_SHORT).show ();
                Looper.loop ();
            }
        }.start ();
        // 收集设备参数信息和错误信息
        StringBuffer sb = collectBugInfo (mContext, ex);
        // 处理日志文件
        handlerBugInfo (sb);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private StringBuffer collectBugInfo (Context context, Throwable ex) {
        // 获取设备信息－－－－－－
        try {
            PackageManager pm = context.getPackageManager ();
            PackageInfo pi = pm.getPackageInfo (context.getPackageName (), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                // 版本名称
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                // 版本号
                String versionCode = pi.versionCode + "";
                infos.put ("versionName", versionName);
                infos.put ("versionCode", versionCode);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        // 字段类
        Field[] fields = Build.class.getDeclaredFields ();
        for (Field field : fields) {
            try {
                field.setAccessible (true);
                infos.put (field.getName (), field.get (null).toString ());
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        StringBuffer sb = new StringBuffer ();
        for (Map.Entry<String, String> entry : infos.entrySet ()) {
            String key = entry.getKey ();
            String value = entry.getValue ();
            if (key.equals ("TIME")) {
                sb.append (key + "=" + SystemTool.getDataTime(new Date (), TEOrderPoConstans.FORMAT_DATE_YY_MM_DD_HH_MM_SS) + "<br/>\n");
            } else {
                sb.append (key + "=" + value + "<br/>\n");
            }
        }
        // 获取bug信息－－－－－－
        Writer writer = new StringWriter ();
        PrintWriter printWriter = new PrintWriter (writer);
        ex.printStackTrace (printWriter);
        Throwable cause = ex.getCause ();
        while (cause != null) {
            cause.printStackTrace (printWriter);
            cause = cause.getCause ();
        }
        printWriter.close ();
        String result = writer.toString ();
        sb.append (result);
        return sb;
    }

    /**
     * 处理获取到的bug日志
     *
     * @param sb
     */
    private void handlerBugInfo (final StringBuffer sb) {
        if (Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent ("sb");
            intent.putExtra("sb", sb.toString());
            Log.d ("log", "log-->>" + sb.toString ());
            mContext.sendBroadcast (intent);
            if (SystemTool.checkNet (mContext)) {
                new Thread (new Runnable () {

                    @Override
                    public void run () {
                        try {
                            EmailSender sender = new EmailSender ();
                            // 设置服务器地址和端口
                            sender.setProperties("smtp.163.com", "25");
                            // 分别设置发件人，邮件标题和文本内容
                            sender.setMessage("yangbin_aaron@163.com", "PDA_错误报告", "<html><body>" + sb.toString() + "</body></html>");
                            // 设置收件人
                            sender.setReceiver(new String[]{"yangbin_aaron@163.com"});
                            // 发送邮件
                            sender.sendEmail("smtp.163.com", "yangbin_aaron@163.com", "121965871.yb");
                        } catch (Exception e) {
                            e.printStackTrace ();
                            return;
                        }
                    }
                }).start ();
            }
        }
    }
}
