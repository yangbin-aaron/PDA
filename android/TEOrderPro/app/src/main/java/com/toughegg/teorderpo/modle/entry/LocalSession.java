package com.toughegg.teorderpo.modle.entry;

/**
 * Created by Andy on 15/11/27.
 */
public class LocalSession {
    private String lang ;  // 语言
    private String restId;
    private String token;
    private String userId;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
