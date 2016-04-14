package com.toughegg.teorderpo.utils;

import android.content.Context;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.StringUtils;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.db.TEOrderPoDataBase;
import com.toughegg.teorderpo.modle.bean.ShoppingCart;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andy on 15/8/10.
 */
public class TENetUtils {
    /**
     * 解析restaurant token数据
     */
    private static String pasResToken (Context context, boolean mustToURLEncoded) {
        try {
            JSONObject json = new JSONObject ();
            String reustaurantId = SharePrefenceUtils.readString(context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_RESTAURANT_ID,
                    "");
            String loginrestaurantuserId = SharePrefenceUtils.readString (context, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans
                    .SP_LOGIN_RESTAURANT_USER_ID, "");
            String lan = Locale.getDefault ().getLanguage ();
            if (lan.equals ("zh")) {
                lan = "zh_cn";
            }
            if (!reustaurantId.equals ("")) {
                lan = "zh_cn";
                String orignal = reustaurantId + "" + loginrestaurantuserId + lan;
                String key;
                key = TESHA2.SHA256Encrypt (orignal, "", "ToughEgg");
                json.put (TEOrderPoConstans.RESTAURANTID, reustaurantId);
                json.put (TEOrderPoConstans.LOGINRESTAURANTUSERID, loginrestaurantuserId);
                json.put (TEOrderPoConstans.LAN, lan);
                if (mustToURLEncoded) {
                    json.put (TEOrderPoConstans.KEY, StringUtils.toURLEncoded(key));
                } else {
                    json.put (TEOrderPoConstans.KEY, key);
                }
            } else {
                reustaurantId = "0";
                String orignal = reustaurantId + "" + loginrestaurantuserId + lan;
                String key;
                key = TESHA2.SHA256Encrypt (orignal, "", "ToughEgg");
                json.put (TEOrderPoConstans.RESTAURANTID, 0);
                json.put (TEOrderPoConstans.LOGINRESTAURANTUSERID, loginrestaurantuserId);
                json.put (TEOrderPoConstans.LAN, lan);
                if (mustToURLEncoded) {
                    json.put (TEOrderPoConstans.KEY, StringUtils.toURLEncoded (key));
                } else {
                    json.put (TEOrderPoConstans.KEY, key);
                }
            }
            return json.toString ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * key  toURLEncoded  不需要
     *
     * @param context
     * @return
     */
    public static String pasResTokenTable (Context context) {
        return pasResToken (context, false);
    }

    /**
     * key  toURLEncoded  需要
     *
     * @param context
     * @return
     */
    public static String pasResToken (Context context) {
        return pasResToken (context, true);
    }

    public static String pasLoginData (String merchantId, String userId, String password) {
        try {
            JSONObject json = new JSONObject ();
            json.put ("MerchantId", merchantId);
            json.put ("UserId", userId);
            json.put ("Password", password);
            return json.toString ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * 网络http数据获取
     *
     * @param httpPost
     * @param params
     * @return
     */
    public static String executeHttpPost (HttpPost httpPost, List<NameValuePair> params) {
        if (params != null) {
            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                httpPost.setEntity (urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        HttpClient httpclient = new DefaultHttpClient ();
        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout (httpclient.getParams (), 5000);
        try {
            HttpResponse httpResponse = httpclient.execute (httpPost);
            if (httpResponse.getStatusLine ().getStatusCode () == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity ();
                BufferedReader bi = new BufferedReader (new InputStreamReader (httpEntity.getContent ()));
                String line = null;
                StringBuilder sb = new StringBuilder ();
                while ((line = bi.readLine ()) != null) {
                    sb.append (line);
                }
                bi.close ();
                if (sb.length () > 0) {
                    return sb.toString ();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * 封装json数据
     *
     * @param shoppingCarts
     * @param tableId
     * @param discount
     * @param customerNumber
     * @return
     */
    public static String pasAddOrder (List<ShoppingCart> shoppingCarts, int tableId, double discount, int customerNumber) throws JSONException {
        JSONObject json = new JSONObject ();
        json.put ("table_id", tableId);
        json.put ("submit_from", 2);// 点菜宝
        json.put ("discount", discount);
        json.put ("customer_number", customerNumber);
        JSONArray jsonMembers = new JSONArray ();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            JSONObject member = new JSONObject ();
            member.put ("menu_id", shoppingCart.getDishId ());
            member.put ("count", shoppingCart.getCopies ());
            if (!shoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
                member.put ("remark", shoppingCart.getRemark ());
            }
            if (shoppingCart.getOptionList() != null && shoppingCart.getOptionList().size () > 0) {
                JSONArray priceOptions = new JSONArray ();
                for (Option dishLabel : shoppingCart.getOptionList()) {
                    JSONObject adds = new JSONObject ();
                    adds.put ("priceOptionID", dishLabel.getId());
                    adds.put ("addPrice", dishLabel.getPrice ());
                    adds.put ("count", dishLabel.getCount ());
                    priceOptions.put (adds);
                }
                member.put ("priceOptions", priceOptions);
            }
            jsonMembers.put (member);
        }
        json.put ("menu_list", jsonMembers);
        return json.toString ();
    }

    public static String pasAddOrderTakeAeay (List<ShoppingCart> shoppingCarts, double discount) throws JSONException {
        JSONObject json = new JSONObject ();
        json.put ("discount", discount);
        json.put ("submit_from", 2);// 点菜宝
        JSONArray jsonMembers = new JSONArray ();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            JSONObject member = new JSONObject ();
            member.put ("menu_id", shoppingCart.getDishId ());
            member.put ("count", shoppingCart.getCopies ());
            if (!shoppingCart.getRemark ().equals (TEOrderPoDataBase.DB_NULL)) {
                member.put ("remark", shoppingCart.getRemark ());
            }
            if (shoppingCart.getOptionList() != null && shoppingCart.getOptionList().size () > 0) {
                JSONArray priceOptions = new JSONArray ();
                for (Option dishLabel : shoppingCart.getOptionList ()) {
                    JSONObject adds = new JSONObject ();
                    adds.put ("id", dishLabel.getId());
                    adds.put ("add_price", dishLabel.getPrice ());
                    adds.put ("count", dishLabel.getCount ());
                    priceOptions.put (adds);
                }
                member.put ("price_options", priceOptions);
            }
            jsonMembers.put (member);
        }
        json.put ("menu_list", jsonMembers);
        return json.toString ();
    }


    /**
     * 将字符串中的中文转化为拼音,并提取首字母
     *
     * @param inputString
     * @return
     */
    public static String getPingYinShort (String inputString) {


//        StringBuffer pybf = new StringBuffer();
//        char[] arr = inputString.toCharArray();
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] > 128) {
//                try {
//                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
//                    if (temp != null) {
//                        pybf.append(temp[0].charAt(0));
//                    }
//                } catch (BadHanyuPinyinOutputFormatCombination e) {
//                    e.printStackTrace();
//                }
//            } else {
//                pybf.append(arr[i]);
//            }
//        }
//        return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();


        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat ();
        format.setCaseType (HanyuPinyinCaseType.LOWERCASE);
        format.setToneType (HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType (HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim ().toCharArray ();// 把字符串转化成字符数组
        String shortString = "";
        try {
            for (int i = 0; i < input.length; i++) {
                // \\u4E00是unicode编码，判断是不是中文
                if (java.lang.Character.toString (input[i]).matches (
                        "[\\u4E00-\\u9FA5]+")) {
                    // 将汉语拼音的全拼存到temp数组
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray (
                            input[i], format);
                    // 取拼音的第一个读音
                    shortString += temp[0].substring (0, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return shortString;
    }
}