package com.toughegg.teorderpo.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.toughegg.andytools.systemUtil.FileUtils;
import com.toughegg.teorderpo.TEOrderPoConstans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipException;

/**
 * Created by Andy on 15/9/4.
 */
public class DownLoadFile extends AsyncTask<String, Integer, String> {

    private String baseName;

    private Handler mHandler;
    private boolean handlerIsSend = false;// handler是否已经发消息出去

    public DownLoadFile (Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
    }

    @Override
    protected String doInBackground (String... params) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        Log.e ("aaron", ">>>>>>>>>>>>-----1");
        try {
            Log.e ("aaron", ">>>>>>>>>>>>-----2");
            URL url = new URL (params[0]);
            connection = (HttpURLConnection) url.openConnection ();
            connection.connect ();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode () != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode ()
                        + " " + connection.getResponseMessage ();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength ();

            // download the file
            baseName = params[0].substring (params[0].lastIndexOf ('/') + 1, params[0].length ());
            File f = new File (Environment.getExternalStorageDirectory ().getPath () + "/" + baseName);
            File f2 = new File (Environment.getExternalStorageDirectory ().getPath () + "/Config");

            //删除文件和文件夹
            FileUtils.deleteFolder (Environment.getExternalStorageDirectory ().getPath () + "/" + baseName);
            FileUtils.deleteFolder (Environment.getExternalStorageDirectory ().getPath () + "/Config");

            input = connection.getInputStream ();
            output = new FileOutputStream (Environment.getExternalStorageDirectory ().getPath () + "/" + baseName);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read (data)) != -1) {
                // allow canceling with back button
                if (isCancelled ()) {
                    input.close ();
                    return null;
                }
                total += count;
                if (fileLength > 0) { // only if total length is known
                    this.publishProgress ((int) (total * 100 / fileLength));
                }
                output.write (data, 0, count);
            }
            Log.e ("aaron", ">>>>>成功>>>>>>>-----3");
            handlerIsSend = true;
            mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_SUCCESS).sendToTarget ();
        } catch (Exception e) {
            Log.e ("aaron", ">>>>>失败>>>>>>>-----4");
            handlerIsSend = true;
            mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
            return e.toString ();
        } finally {
            if (handlerIsSend == false) {
                Log.e ("aaron", ">>>>>地址错误>>>>>>>-----5");
                mHandler.obtainMessage (TEOrderPoConstans.HANDLER_WHAT_POST_FAIL).sendToTarget ();
            }
            try {
                if (output != null)
                    output.close ();
                if (input != null)
                    input.close ();
            } catch (IOException ignored) {
                Log.e ("aaron", ">>>>>>>>>>>>-----6");
            }

            if (connection != null)
                connection.disconnect ();

        }
        return null;
    }

    @Override
    protected void onPostExecute (String s) {
        super.onPostExecute (s);
        Log.e ("aaron", ">>>>>>>>>>>>-----7");
        unZipFile ();
        Log.e ("aaron", ">>>>>>>>>>>>-----8");
//        Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
    }

    private void unZipFile () {
        String strFile = Environment.getExternalStorageDirectory ().getPath () + "/" + baseName;
        String strFile2 = Environment.getExternalStorageDirectory ().getPath () + "/" + baseName;
        File file1 = new File (strFile);
        File file2 = new File (strFile2);
        boolean flag = file1.renameTo (file2);
        if (flag) {
            Log.e ("zip", "--->>>" + Environment.getExternalStorageDirectory ().getPath () + "/" + baseName);
            try {
                ZipUtils.upZipFile (file2, Environment.getExternalStorageDirectory ().getPath ());
            } catch (ZipException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
    }

}
