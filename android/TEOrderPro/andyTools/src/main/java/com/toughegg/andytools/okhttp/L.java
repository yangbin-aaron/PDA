package com.toughegg.andytools.okhttp;

import android.util.Log;

/**
 * Created by Andy on 15/11/9.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

