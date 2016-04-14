package com.toughegg.teorderpo.modle.entry;

import java.io.Serializable;

/**
 * 只需要返回网络访问状态的数据对象
 * Created by toughegg on 15/12/12.
 */
public class APIStatusResult implements Serializable {
    private int status;
    private String message;

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
