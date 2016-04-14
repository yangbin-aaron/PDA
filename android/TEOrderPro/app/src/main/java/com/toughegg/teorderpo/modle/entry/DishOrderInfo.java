package com.toughegg.teorderpo.modle.entry;

import java.io.Serializable;

/**
 * Created by Andy on 15/9/19.
 *
 */
public class DishOrderInfo implements Serializable {
    private String orderNumber;
    private int orderType;
    private String orderCreateTime;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }
}
