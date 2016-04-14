package com.toughegg.teorderpo.modle.entry.userlogin;

/**
 * Created by Andy on 15/11/27.
 */
public class UserLoginResult {
    private int status;
    private String message;
    private String token;
    private UserLoginResultDate data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserLoginResultDate getData() {
        return data;
    }

    public void setData(UserLoginResultDate data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getString(){
        String result="status:"+status+"\n message:"+message+"\n token: "+token+"\n resultDate:"+data;
        return result;
    }
}
