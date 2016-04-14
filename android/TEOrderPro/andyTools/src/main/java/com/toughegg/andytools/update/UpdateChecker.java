package com.toughegg.andytools.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import com.toughegg.andytools.R;

public class UpdateChecker {

    public static UpdateChecker mUpdateChecker;

    public static UpdateChecker getInstance () {
        if (mUpdateChecker == null) {
            mUpdateChecker = new UpdateChecker ();
        }
        return mUpdateChecker;
    }


    /**
     * Show Notification
     */
    public void showNotification (Context mContext, String content, String apkUrl) {
        Notification noti;
        Intent myIntent = new Intent (mContext, DownloadService.class);
        myIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra (Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService (mContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = mContext.getApplicationInfo ().icon;
        noti = new NotificationCompat.Builder (mContext).setTicker (mContext.getString (R.string.newUpdateAvailable))
                .setContentTitle (mContext.getString (R.string.newUpdateAvailable)).setContentText (content).setSmallIcon (smallIcon)
                .setContentIntent (pendingIntent).build ();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService (Context.NOTIFICATION_SERVICE);
        notificationManager.notify (0, noti);
    }

    // 直接下载新版本；
    public void goToDownload (Context mContext, UpdateVersion updateVersion) {
        Intent intent = new Intent (mContext.getApplicationContext (), DownloadService.class);
        intent.putExtra (Constants.APK_DOWNLOAD_URL, updateVersion.getInstallUrl ());
        intent.putExtra (Constants.APK_DOWNLOAD_VERSION, updateVersion.getVersion ());
        intent.putExtra (Constants.APK_DOWNLOAD_NAME, updateVersion.getName ());
        intent.putExtra (Constants.APK_DOWNLOAD_TIME, updateVersion.getUpdated_at ());
        mContext.startService (intent);
    }


    /**
     * Check if a network available
     */
    public static boolean isNetworkAvailable (Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo ();
            if (ni != null) {
                connected = ni.isConnected ();
            }
        }
        return connected;
    }


}
