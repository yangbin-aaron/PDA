package com.toughegg.andytools.update;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.toughegg.andytools.systemUtil.SystemTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {

    public static final String ACTION_STOP_DOWNLOAD = "com.toughegg.andytools.update.ACTION_STOP_DOWNLOAD";
    public static final String ACTION_PROGRESS = "com.toughegg.andytools.update.ACTION_PROGRESS";

    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
    private static final String TAG = "DownloadService";
    //    private NotificationManager mNotifyManager;
//    private Builder mBuilder;
    private File mApkFile;

    public static final int DOWNLOAD_CON = 1;// 下载中
    public static final int DOWNLOAD_STOP = -3;// 下载停止（或者未开始，完成）
    public static final int DOWNLOAD_FAIL = -1;// 下载失败

    private int mDownloadStatus = DOWNLOAD_STOP;// 下载状态
    private int mProgress = 0;

    public DownloadService () {
        super ("DownloadService");
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            if (intent.getAction ().equals (ACTION_STOP_DOWNLOAD)) {
                mDownloadStatus = DOWNLOAD_STOP;
            }
        }
    };

    @Override
    public void onCreate () {
        super.onCreate ();
        IntentFilter filter = new IntentFilter (ACTION_STOP_DOWNLOAD);
        registerReceiver (mReceiver, filter);
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mReceiver);
    }

    @Override
    protected void onHandleIntent (Intent intent) {

//        mNotifyManager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
//        mBuilder = new Builder (this);

//        String appName = getString (getApplicationInfo ().labelRes);
//        int icon = getApplicationInfo ().icon;

//        mBuilder.setContentTitle (appName).setSmallIcon (icon);
        String urlStr = intent.getStringExtra (Constants.APK_DOWNLOAD_URL);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL url = new URL (urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection ();

            urlConnection.setRequestMethod ("GET");
            urlConnection.setDoOutput (false);
            urlConnection.setConnectTimeout (10 * 1000);
            urlConnection.setReadTimeout (10 * 1000);
            urlConnection.setRequestProperty ("Connection", "Keep-Alive");
            urlConnection.setRequestProperty ("Charset", "UTF-8");
            urlConnection.setRequestProperty ("Accept-Encoding", "gzip, deflate");

            urlConnection.connect ();
            long bytetotal = urlConnection.getContentLength ();
            long bytesum = 0;
            int byteread = 0;
            in = urlConnection.getInputStream ();
            File dir = StorageUtils.getCacheDirectory (this);

            // 指定apk存储地址
            boolean sdCardExist = Environment.getExternalStorageState ().equals (android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (sdCardExist) {
                dir = Environment.getExternalStorageDirectory ();//获取跟目录
                File dirSDCard = new File (dir.getAbsolutePath () + "/TEPdaDownload");
                if (!dirSDCard.exists ()) {
                    dirSDCard.mkdirs ();
                }
                dir = dirSDCard;
            }

            // apk命名
//            String apkName = urlStr.substring (urlStr.lastIndexOf ("/") + 1, urlStr.length ());
            String name = intent.getStringExtra (Constants.APK_DOWNLOAD_NAME);
            String time = intent.getStringExtra (Constants.APK_DOWNLOAD_TIME);
            String version = intent.getStringExtra (Constants.APK_DOWNLOAD_VERSION);
            String apkName = name + time + version + ".apk";
            mApkFile = new File (dir, apkName);

            // 多次下载时，名字不重复
            for (int i = 1; i > 0; i++) {
                if (SystemTool.fileIsExists (mApkFile)) {// 如果存在便重命名
                    String apkNameFor = name + time + version + "(" + i + ").apk";
                    mApkFile = new File (dir, apkNameFor);
                } else {// 不存在便保留当前文件名
                    break;
                }
            }

            out = new FileOutputStream (mApkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

//            int oldProgress = 0;
            mDownloadStatus = DOWNLOAD_CON;
            sendBrodcast ();// 改变进度

            while ((byteread = in.read (buffer)) != -1 && mDownloadStatus == DOWNLOAD_CON) {
                bytesum += byteread;
                out.write (buffer, 0, byteread);

                mProgress = (int) (bytesum * 100L / bytetotal);
                // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
//                if (progress != oldProgress) {
////                    updateProgress (progress);
//                    mProgress = progress;
//                }
//                oldProgress = progress;
            }

            mProgress = 100;

            // 下载完成
//            mBuilder.setContentText (getString (R.string.download_success)).setProgress (0, 0, false);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity (this, 0, installAPKIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            mBuilder.setContentIntent (pendingIntent);
//            Notification noti = mBuilder.build ();
//            noti.flags = Notification.FLAG_AUTO_CANCEL;
//            mNotifyManager.notify (0, noti);
        } catch (Exception e) {
            mProgress = DOWNLOAD_FAIL;

            Log.e (TAG, "download apk file error", e);
        } finally {
            if (out != null) {
                try {
                    out.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            if (in != null) {
                try {
                    in.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    private void sendBrodcast () {
        new Thread () {
            @Override
            public void run () {
                super.run ();
                while (mDownloadStatus == DOWNLOAD_CON) {
                    try {
                        Thread.sleep (500);
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                    Intent intent = new Intent ();
                    intent.setAction (ACTION_PROGRESS);
                    intent.putExtra ("progress", mProgress);// 当前进度  10％
                    intent.putExtra ("apkname",mApkFile.toString ());
                    sendBroadcast (intent);

                    if (mProgress == DOWNLOAD_FAIL) {
                        mDownloadStatus = DOWNLOAD_FAIL;// 失败
                    } else if (mProgress == 100) {
                        mProgress = 101;// 为了能够显示最后100%
                    } else if (mProgress == 101) {
                        if (mDownloadStatus == DOWNLOAD_CON) {// 安装
                            Intent installAPKIntent = new Intent (Intent.ACTION_VIEW);
                            //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
                            String[] command = {"chmod", "777", mApkFile.toString ()};
                            ProcessBuilder builder = new ProcessBuilder (command);
                            try {
                                builder.start ();
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }

                            installAPKIntent.setDataAndType (Uri.fromFile (mApkFile), "application/vnd.android.package-archive");
                            Log.e (TAG, "install>>>>>>>" + mApkFile.getName () + "\n------" + mApkFile.toString ());

                            installAPKIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                            installAPKIntent.addFlags (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            installAPKIntent.addFlags (Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity (installAPKIntent);
                            mDownloadStatus = DOWNLOAD_STOP;// 完成
                        }
                    }
                }
            }
        }.start ();
    }

//    private void updateProgress (int progress) {
    //"正在下载:" + progress + "%"
//        mBuilder.setContentText (this.getString (R.string.download_progress, progress)).setProgress (100, progress, false);
//        //setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
//        PendingIntent pendingintent = PendingIntent.getActivity (this, 0, new Intent (), PendingIntent.FLAG_CANCEL_CURRENT);
//        mBuilder.setContentIntent (pendingintent);
//        mNotifyManager.notify (0, mBuilder.build ());
//    }

}
