package com.toughegg.teorderpo.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.utils.BitmapUtils;
import com.toughegg.teorderpo.utils.MyQRCode;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andy on 15/8/30.
 * 打印工具
 */
public class NetPrintUtils {

    public String command = ""; //打印命令字符串
    public byte[] out_bytes; //传输的命令集
    public byte[] out_bytes_Line;
    public String ipAddress;
    public String ipKey;
    public int netPort;
    private LuaState mLuaState;
    private Context mContext;

    public NetPrintUtils() {

    }

    public NetPrintUtils(String ipKey, String ipAddress, int netPort, LuaState mLuaState, Context mContext) {
        this.ipKey = ipKey;
        this.ipAddress = ipAddress;
        this.netPort = netPort;
        this.mLuaState = mLuaState;
        this.mContext = mContext;
    }


    /**
     * 关闭链接
     */
    public static void closeConnect(Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendText(LuaState mLuaState, Socket socket) {
        OutputStream mOutputStream_P;
        try {
            mOutputStream_P = socket.getOutputStream();
            command = mLuaState.toString(-1);
            out_bytes = command.getBytes("GB18030");
            mOutputStream_P.write(out_bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("key", "error --->>" + e.toString());
        }
    }

    public void sendCmd(LuaState mLuaState, Socket socket) {
        OutputStream mOutputStream_P;
        try {
            mOutputStream_P = socket.getOutputStream();
            command = mLuaState.toString(-1);
            out_bytes = command.getBytes(Charset.forName("ASCII"));
            for (byte b : out_bytes) {
                mOutputStream_P.write(b);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("key", "error --->>" + e.toString());
        }
    }

    // 打印二维码
    public void printQRCode(LuaState mLuaState, Socket socket) {
        Bitmap bitmap;
        OutputStream mOutputStream_P;
        try {
            mOutputStream_P = socket.getOutputStream();
            bitmap = BitmapUtils.binarization(witchQRCodeImage(mContext, mLuaState.toString(-1)));

            int bmWidth = bitmap.getWidth();
            int bmHeight = bitmap.getHeight();
            int prefix_Int;

        /*
         * 定义行间距。
         */

            char[] line = {0x1b, 0x64, 0x01};

            String lineStr = String.valueOf(line);
            out_bytes_Line = lineStr.getBytes(Charset.forName("ASCII"));


            char[] spaceLine = {27, 51, 0};
            String printLine = String.valueOf(spaceLine);
            out_bytes = printLine.getBytes(Charset.forName("ASCII"));
            mOutputStream_P.write(out_bytes);

            /**
             * 设置打印二维码布局
             */
            char[] modeBitmap = {27, 42, 33, (char) (bmWidth % 256), (char) (bmWidth / 256)};
            String print = String.valueOf(modeBitmap);
            out_bytes = print.getBytes(Charset.forName("ASCII"));


            byte[] data = new byte[]{0x00, 0x00, 0x00};
            for (int i = 0; i < bmHeight / 24 + 6; i++) {
                mOutputStream_P.write(out_bytes);

                for (int j = 0; j < bmWidth; j++) {
                    for (int k = 0; k < 24; k++) {
                        if (((i * 24) + k) < bmHeight) {
                            prefix_Int = bitmap.getPixel(j, (i * 24) + k);
                            if (Color.BLACK == prefix_Int) {
                                data[k / 8] += (byte) (128 >> (k % 8));
                            }
                        }
                    }
                    mOutputStream_P.write(data);
                    data[0] = 0x00;
                    data[1] = 0x00;
                    data[2] = 0x00;

                }
                mOutputStream_P.write(out_bytes_Line);
            }
        } catch (Exception e) {
            Log.e("dd", "e.printStackTrace1111-->>>" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 执行打印操作
     */

    public void executeLuaPrint(final String ipKey, final String printJson, final String printTag, final Context mContext, final Socket socket) {
        this.mContext = mContext;
        Log.e("key", "socket-->>" + socket + "--->> ipKey-->>" + ipKey + "--->>>> printJson-->>" + printJson);
        String path = Environment.getExternalStorageDirectory().getPath() + "/Config/2";
        mLuaState.LloadFile(path + "/parser.lua");
        mLuaState.pushString(path);
        mLuaState.setGlobal("TE_PWD");
        mLuaState.pushString(printJson);
        mLuaState.setGlobal("TE_OrderDetail");
        mLuaState.pushString(printTag);
        mLuaState.setGlobal("TE_Tag");
        mLuaState.pushString(ipKey);
        mLuaState.setGlobal("TE_PrinterKey");

        try {
            mLuaState.pushObjectValue(Log.class);
            mLuaState.setGlobal("Log");
        } catch (LuaException e1) {
            e1.printStackTrace();
        }

        /**
         * 将sendText 方法设置到lua中
         */
        JavaFunction sendText = new JavaFunction(mLuaState) {
            @Override
            public int execute() throws LuaException {
                sendText(mLuaState, socket);
                return 0;
            }
        };
        try {
            sendText.register("TE_sendText");
        } catch (LuaException e) {
            e.printStackTrace();
        }


        /**
         * 将sendCmd 方法设置到lua中
         */
        final JavaFunction sendCmd = new JavaFunction(mLuaState) {
            @Override
            public int execute() throws LuaException {
                sendCmd(mLuaState, socket);
                return 0;
            }
        };
        try {
            sendCmd.register("TE_sendCmd");
        } catch (LuaException e) {
            e.printStackTrace();
        }


        /**
         * 将qrCode 方法设置到lua
         */
        JavaFunction printQRCode = new JavaFunction(mLuaState) {
            @Override
            public int execute() throws LuaException {
                printQRCode(mLuaState, socket);
                return 0;
            }
        };
        try {
            printQRCode.register("TE_print2dCode");
        } catch (LuaException e) {
            e.printStackTrace();
        }

        mLuaState.pcall(0, 0, 0);          //执行调用lua 中 printDate 方法；

    }


    public Bitmap witchQRCodeImage(Context mContext, String code) {
        Bitmap bitmap;
        LayoutInflater factory = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = factory.inflate(R.layout.qrcode_text, null);
        //获得布局文件中的TextView
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);
        TextView textTime = (TextView) view.findViewById(R.id.textTime);
//        int size = SystemTool.getFontSize(mContext, 10);
//        text1.setTextSize(size);
//        text2.setTextSize(size);
//        text3.setTextSize(size);
//        textTime.setTextSize(size);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(new Date());
        textTime.setText(timeStr);
        ImageView qrImageView = (ImageView) view.findViewById(R.id.qrcode);
        qrImageView.setImageBitmap(MyQRCode.create2DCode(code));
        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        view.measure(View.MeasureSpec.makeMeasureSpec(550, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(130, View.MeasureSpec.EXACTLY));
        //这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        //获得绘图缓存中的Bitmap
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        return bitmap;
    }
}
