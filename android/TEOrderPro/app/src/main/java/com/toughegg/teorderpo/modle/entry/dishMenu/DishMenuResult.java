package com.toughegg.teorderpo.modle.entry.dishMenu;

import java.io.Serializable;

/**
 * Created by Andy on 15/12/1.
 *
 */
public class DishMenuResult implements Serializable {
    private int status;
    private String message;
    private String token;
    private DishMenuResultData data;

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

    public DishMenuResultData getData() {
        return data;
    }

    public void setData(DishMenuResultData data) {
        this.data = data;
    }

    @Override
    public String toString () {
        return "DishMenuResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
