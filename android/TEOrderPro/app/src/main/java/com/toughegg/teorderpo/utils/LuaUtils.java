package com.toughegg.teorderpo.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andy on 15/8/30.
 */
public class LuaUtils {

    public static final int HANDLER_LUA_SUCCESS = 0x22;// 处理lua成功

    private LuaState mLuaState;
    private Context mContext;
    private String mOrderDetailStr;

    private Handler mHandler;

    public LuaUtils() {

    }

    public LuaUtils(LuaState mLuaState, Context mContext, Handler handler) {
        this.mLuaState = mLuaState;
        this.mContext = mContext;
        this.mHandler = handler;
    }

    public void sendData(String result) {
        Log.e("hcc","----->>>>>"+result+"\n");
        setOrderDetailStr(result);
        mHandler.obtainMessage(HANDLER_LUA_SUCCESS, result).sendToTarget();
    }

    public String getOrderDetailStr() {
        return mOrderDetailStr;
    }

    public void setOrderDetailStr(String mOrderDetailStr) {
        this.mOrderDetailStr = mOrderDetailStr;
    }

    /**
     * 执行lua计算操作
     */
    public void executeLuaCalculate(String json) {
        try {

//            File externalFilesDir = mContext.getExternalFilesDir (null);
//            String path = externalFilesDir.getAbsolutePath ();
            String path = Environment.getExternalStorageDirectory().getPath() + "/Config/1";
            Log.d("path", "path-->>" + Environment.getExternalStorageDirectory().getPath());
            mLuaState.LloadFile(path + "/Logic.lua");
//            mLuaState.LdoString(getFromAssets("1/Logic.lua", mContext));
            mLuaState.pushString(path);
            mLuaState.setGlobal("TE_PWD");

            mLuaState.pushString(json);
            mLuaState.setGlobal("TE_OrderDetail");

            try {
                mLuaState.pushObjectValue(Log.class);
                mLuaState.setGlobal("Log");
            } catch (LuaException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            /**
             * 将sendData 方法设置到lua中
             */
            final JavaFunction sendData = new JavaFunction(mLuaState) {
                @Override
                public int execute() throws LuaException {
                    sendData(mLuaState.toString(-1));
                    return 0;
                }
            };
            sendData.register("TE_sendJsonText");

//            mLuaState.getGlobal("outLua");
            mLuaState.pcall(0, 0, 0);          //执行调用lua 中 printDate 方法；
        } catch (LuaException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            Log.e("ReadStream", "读取文件流失败");
            return "";
        }
    }


    //从assets 文件夹中获取文件并读取数据
    public String getFromAssets(String fileName, Context mContext) {
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
