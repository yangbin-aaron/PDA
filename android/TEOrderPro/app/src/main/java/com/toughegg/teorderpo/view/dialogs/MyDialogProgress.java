package com.toughegg.teorderpo.view.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toughegg.andytools.update.DownloadService;
import com.toughegg.andytools.update.UpdateChecker;
import com.toughegg.andytools.update.UpdateVersion;
import com.toughegg.teorderpo.R;

/**
 * Created by toughegg on 15/12/11.
 */
public class MyDialogProgress extends Dialog {

    private Context mContext;

    private TextView confirmTextView;
    private TextView timeTextView;
    private TextView progressTextView;
    private ProgressBar mProgressBar;

    private UpdateVersion mUpdateVersion;

    public void setUpdateVersion (UpdateVersion updateVersion) {
        mUpdateVersion = updateVersion;
    }

    private int mDownloadStatus = DownloadService.DOWNLOAD_STOP;
    private int mTimeInt = 0;

    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (DownloadService.ACTION_PROGRESS)) {
                updateProgress (intent);
            }
        }
    };

    // 更新进度条
    private void updateProgress (Intent intent) {
        int progress = intent.getIntExtra ("progress", 0);
        String apkName = intent.getStringExtra ("apkname");
        if (mTimeInt == 0) {
            if (progress == 0) {
                progress = 5;
            }
            mTimeInt = 500 / progress;// 1%所需时间 /ms   500ms发送一次广播，大约可以得出每一个进度需要的时间
        }
//        Log.e ("DownloadService", "----Total>time:" + mTimeInt);
        long timeLong = mTimeInt * (100 - progress) + 1000;// 剩余时间  +1000时间差
//        Log.e ("DownloadService", "----SY>time:" + timeLong);
        if (progress == DownloadService.DOWNLOAD_FAIL) {// 下载失败，重新下载？
            mDownloadStatus = DownloadService.DOWNLOAD_FAIL;
            confirmTextView.setText (R.string.app_re_download);
            updateProgressTime (-1);
        } else {
            if (progress > 100) {
                updateProgressTime (0);
                dismiss ();
            } else {
                progressTextView.setText (progress + "%");
                mProgressBar.setProgress (progress);
                if (progress == 100) {
                    timeLong = 0;
                    // 显示下载地址
                    Toast.makeText (mContext, apkName, Toast.LENGTH_LONG).show ();
                }
                updateProgressTime (timeLong);
            }
        }
    }

    // 更新剩余时间
    private void updateProgressTime (long timeLong) {
        if (timeLong >= 0) {
            int min = (int) (timeLong / 1000) / 60;
            int sercond = (int) (timeLong / 1000) % 60;
            String minStr = min + "";
            String sercondStr = sercond + "";
            if (min < 10) {
                minStr = "0" + min;
            }
            if (sercond < 10) {
                sercondStr = "0" + sercond;
            }
            timeTextView.setText (minStr + ":" + sercondStr + " " + mContext.getResources ().getString (R.string.app_left));
        } else {
            timeTextView.setText ("--:--");
        }
    }

    public MyDialogProgress (Context context) {
        super (context, R.style.styles_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from (mContext);
        View view = inflater.inflate (R.layout.dialog_progress_layout, null);
        setContentView (view);
        setCancelable (false);
        confirmTextView = (TextView) view.findViewById (R.id.dialog_confirm_textview);
        confirmTextView.setOnClickListener (mClickListener);

        progressTextView = (TextView) view.findViewById (R.id.dialog_progress_textview);

        timeTextView = (TextView) view.findViewById (R.id.dialog_time_textview);

        mProgressBar = (ProgressBar) view.findViewById (R.id.dialog_progressbar);

        Window dialogWindow = getWindow ();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes ();
        DisplayMetrics d = mContext.getResources ().getDisplayMetrics (); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        lp.height = d.heightPixels;
        dialogWindow.setAttributes (lp);
    }

    @Override
    protected void onStart () {
        super.onStart ();
        IntentFilter filter = new IntentFilter ();
        filter.addAction (DownloadService.ACTION_PROGRESS);
        mContext.registerReceiver (mReceiver, filter);
    }

    @Override
    protected void onStop () {
        super.onStop ();
        mContext.unregisterReceiver (mReceiver);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener () {
        @Override
        public void onClick (View v) {
            if (v == confirmTextView) {
                switch (mDownloadStatus) {
                    case DownloadService.DOWNLOAD_FAIL:
                        UpdateChecker.getInstance ().goToDownload (mContext, mUpdateVersion);
                        break;
                    case DownloadService.DOWNLOAD_CON:
                        dismiss ();
                        break;
                    case DownloadService.DOWNLOAD_STOP:// 停止更新
                        mContext.sendBroadcast (new Intent (DownloadService.ACTION_STOP_DOWNLOAD));
                        dismiss ();
                        break;
                }
                return;
            }
        }
    };

    /**
     * 返回键关闭对话框
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown (keyCode, event);
    }
}
