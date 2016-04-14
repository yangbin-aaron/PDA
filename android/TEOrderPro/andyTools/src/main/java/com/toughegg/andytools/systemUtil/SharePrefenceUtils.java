package com.toughegg.andytools.systemUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Created by Andy on 15/7/13.
 * SharePrefence保存操作
 */
public class SharePrefenceUtils {

    public static void write (Context context, String fileName, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit ();
        editor.putInt (k, v);
        editor.commit ();
    }

    public static void write (Context context, String fileName, String k, boolean v) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit ();
        editor.putBoolean (k, v);
        editor.commit ();
    }

    public static void write (Context context, String fileName, String k, String v) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit ();
        editor.putString (k, v);
        editor.commit ();
    }

    public static int readInt (Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getInt (k, 0);
    }

    public static int readInt (Context context, String fileName, String k, int defv) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getInt (k, defv);
    }

    public static boolean readBoolean (Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getBoolean (k, false);
    }

    public static boolean readBoolean (Context context, String fileName, String k, boolean defBool) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getBoolean (k, defBool);
    }

    public static String readString (Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getString (k, null);
    }

    public static String readString (Context context, String fileName, String k, String defV) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        return preference.getString (k, defV);
    }

    public static void remove (Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit ();
        editor.remove (k);
        editor.commit ();
    }

    public static void clean (Context cxt, String fileName) {
        SharedPreferences preference = cxt.getSharedPreferences (fileName, Context.MODE_PRIVATE);
        Editor editor = preference.edit ();
        editor.clear ();
        editor.commit ();
    }

    // 数据list转化为String
    public static String sceneList2String (List<String> SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream (
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject (SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String (Base64.encode (
                byteArrayOutputStream.toByteArray (), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close ();
        return SceneListString;
    }

    // 数据String 转化为List
    public static List<String> string2SceneList (String SceneListString)
            throws StreamCorruptedException, IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode (SceneListString.getBytes (),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream (
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream (byteArrayInputStream);
        List SceneList = (List) objectInputStream
                .readObject ();
        objectInputStream.close ();
        return SceneList;
    }
}
