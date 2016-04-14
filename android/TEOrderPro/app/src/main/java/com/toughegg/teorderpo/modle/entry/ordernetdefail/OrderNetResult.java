package com.toughegg.teorderpo.modle.entry.ordernetdefail;

/**
 * Created by toughegg on 15/12/2.
 *
 */
public class OrderNetResult {
    private int status;
    private String message;
    OrderNetResultData data;

    public int getStatus () {
        return status;
    }

    public String getMessage () {
        return message;
    }

    public OrderNetResultData getData () {
        return data;
    }

    @Override
    public String toString () {
        return "OrderNetResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}