package com.toughegg.teorderpo.print;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.teorderpo.TEOrderPoApplication;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.asynserver.ITEAsyncTaskCallBack;
import com.toughegg.teorderpo.asynserver.TEAsyncTask;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;
import org.keplerproject.luajava.LuaState;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Andy on 15/9/7.
 * netPrint功能
 */
public class NetPrintServer {
    public static NetPrintServer mNetPrintServer = null;

    public static final int HANDLER_PRINT_COM = 0x11;// 处理打印完成

    public NetPrintServer() {

    }

    public static NetPrintServer getInstance() {
        if (mNetPrintServer == null) {
            mNetPrintServer = new NetPrintServer();
        }
        return mNetPrintServer;
    }

    /**
     * 执行打印
     */
    public void executePrint(final String printTag, final String ipKey, final String ipAddress, final int netPort, final LuaState mLuaState, final Context mContext, final String printJson, final
    Handler handler) {
        final TEOrderPoApplication application = (TEOrderPoApplication) mContext.getApplicationContext();
//        final NetPrintUtils mNetPrintUtils = new NetPrintUtils(ipKey, ipAddress, netPort, mLuaState, mContext);

        new TEAsyncTask<>(new ITEAsyncTaskCallBack<Socket>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public Socket doInBackground(String... params) {
                if (application.printLog == null) {
                    application.printLog = new ArrayList<>();
                }
                HashMap<String, Object> hashMap = new HashMap();
                hashMap.put("key", ipKey);
                hashMap.put("ip", ipAddress);
                Socket socket = null;
                try {
                    socket = new Socket();
                    InetAddress serverAdd = InetAddress.getByName(ipAddress);
                    SocketAddress socAddress = new InetSocketAddress(serverAdd, netPort);
                    socket.connect(socAddress, 5000);
//                    socket = new Socket(serverAdd, netPort);
//                    socket.connect();
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(3000);
                    NetPrintUtils mNetPrintUtils = new NetPrintUtils(ipKey, ipAddress, netPort, mLuaState, mContext);
                    mNetPrintUtils.executeLuaPrint(ipKey, printJson, printTag, mContext, socket);
                    hashMap.put("status", true);// 打印成功
                    hashMap.put("select", false);// 需要重新打印
                    application.printLog.add(hashMap);
                } catch (Exception e) {
                    NetPrintUtils.closeConnect(socket);
                    hashMap.put("status", false);// 打印失败
                    hashMap.put("select", true);// 需要重新打印
                    application.printFail = true;// 记录有失败的打印
                    application.printLog.add(hashMap);
                    e.printStackTrace();
                    Log.d("log", "log-->>>>" + e.toString());
                }
                return socket;
            }

            @Override
            public void progressUpdate(Void[] value) {

            }

            @Override
            public void onPostExecute(Socket socket) {
                application.printSuccessCount--;
                Log.d("aaron", "onPostExecute-->>>>>" + application.printSuccessCount + ">>>>" + socket);
                if (application.printSuccessCount == 0) {// 最后一次执行打印，需要发送Handler
                    handler.obtainMessage(HANDLER_PRINT_COM).sendToTarget();
                }

                NetPrintUtils.closeConnect(socket);
            }
        }).executeOnExecutor(Executors.newCachedThreadPool());
    }

    /**
     * 通过解析lua 中config 去进行确认出入的打印机ip
     *
     * @param mContext
     * @param printTag
     * @return
     */
    public HashMap<String, String> hashMapIp(Context mContext, String printTag) {

        HashMap<String, String> printIpHash = new HashMap<>();
        String configJson;
        // 读取Lua Config
        String path = Environment.getExternalStorageDirectory().getPath() + "/Config/2/config.json";
        try {
            FileInputStream filInput = new FileInputStream(path);
            int length = filInput.available();
            byte[] buffer = new byte[length];
            filInput.read(buffer);
            configJson = EncodingUtils.getString(buffer, "UTF-8");////依Y.txt的编码类型选择合适的编码，如果不调整会乱码
            Log.e("key", "=================" + configJson);
            filInput.close();//关闭资源
            JSONObject jsonObj = new JSONObject(configJson);
            JSONObject printTagObj = jsonObj.optJSONObject(printTag);
            if (printTagObj.optBoolean("bar")) {
                String barIp = SharePrefenceUtils.readString(
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.BAR, TEOrderPoConstans.PRINT_DETALS_IP);
                if (barIp != null) {
                    List<String> ipBarList = SharePrefenceUtils.string2SceneList(barIp);
                    if (ipBarList.size() != 0) {
                        printIpHash.put("bar", ipBarList.get(0));
                    }
                }
            }
            if (printTagObj.optBoolean("cash")) {
                String cashIp = SharePrefenceUtils.readString(
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.CASH, TEOrderPoConstans.PRINT_DETALS_IP);
                if (cashIp != null) {
                    List<String> ipCashList = SharePrefenceUtils.string2SceneList(cashIp);
                    if (ipCashList.size() != 0) {
                        printIpHash.put("cash", ipCashList.get(0));
                    }
                }
            }
            if (printTagObj.optBoolean("kitchen")) {
                String kitchenIp = SharePrefenceUtils.readString(
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.KITCHEN, TEOrderPoConstans.PRINT_DETALS_IP);
                if (kitchenIp != null) {
                    List<String> ipKitchenList = SharePrefenceUtils.string2SceneList(kitchenIp);
                    if (ipKitchenList.size() != 0) {
                        printIpHash.put("kitchen", ipKitchenList.get(0));
                    }
                }
            }
//            Log.e("key", "res--->" + printTagObj.optBoolean("receipt"));
            if (printTagObj.optBoolean("receipt")) {
                String receiptIp = SharePrefenceUtils.readString(
                        mContext, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.RECEIPT, TEOrderPoConstans.PRINT_DETALS_IP);
                if (receiptIp != null) {
                    List<String> ipReceiptList = SharePrefenceUtils.string2SceneList(receiptIp);
                    if (ipReceiptList.size() != 0) {
                        printIpHash.put("receipt", ipReceiptList.get(0));
                    }
                }
            }

            Log.e("key", "res--->" + configJson);
            return printIpHash;
        } catch (Exception e) {
            Log.e ("key", "Exception--->" + e.toString ());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
