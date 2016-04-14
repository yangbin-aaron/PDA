package com.toughegg.teorderpo.modle.entry.uploadOrder;

/**
 * Created by Andy on 15/12/8.
 */
public class OrderNewResult {
    private int status;
    private String message;
    private String token;
    private OrderNewData data;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OrderNewData getData() {
        return data;
    }

    public void setData(OrderNewData data) {
        this.data = data;
    }
}
