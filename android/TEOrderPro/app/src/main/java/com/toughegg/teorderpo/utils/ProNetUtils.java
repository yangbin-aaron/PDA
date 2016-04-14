package com.toughegg.teorderpo.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by toughegg on 15/11/25.
 */
public class ProNetUtils {

    public static JSONObject pasSession (String memberId, String token) throws JSONException {
        // 获取系统语言
        String lang = Locale.getDefault ().getLanguage ();
        if (lang.equals ("zh")) {
            lang = "zh_cn";
        }
        // 创建JSONObject
        JSONObject json = new JSONObject ();
        json.put ("memberId", memberId);
        json.put ("lang", lang);
        json.put ("token", token);
        return json;
    }

    /**
     * 登录的参数集
     * @param employeeId 用户ID
     * @param password 密码
     * @param restCode 商户ID
     * @return
     * @throws JSONException
     */
    public static String pasLoginData (String employeeId, String password, String restCode)  {
        JSONObject json = new JSONObject ();
        // 创建ArgsJson
        JSONObject jsonArgs = new JSONObject ();
        try {
            jsonArgs.put ("employeeId", employeeId);
            jsonArgs.put ("password", password);
            jsonArgs.put ("restCode", restCode);
            json.put ("args", jsonArgs);
            json.put ("session", pasSession ("", ""));
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return json.toString ();
    }
}
