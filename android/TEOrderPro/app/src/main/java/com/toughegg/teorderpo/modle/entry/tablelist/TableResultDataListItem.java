package com.toughegg.teorderpo.modle.entry.tablelist;

import java.io.Serializable;

/**
 * Created by toughegg on 16/3/23.
 */
public class TableResultDataListItem implements Serializable {
//    id: TESchema.TEString,        //餐桌id
//    code: TESchema.TEString,      //餐桌桌号, 如"No.1","T1"等
//    orderNo: TESchema.TEString,   //此餐桌当前对应的订单id
//    payPrice: TESchema.TEString,  //订单价格
//    orderStatus: TESchema.TEInt,  //订单状态
//    paymentStatus: TESchema.TEInt //订单支付状态 0表示未支付， 1表示已支付

    private String tableId;// 所属餐桌id
    private String id;
    private String code;
    private String orderNo;
    private String payPrice;
    private int orderStatus;
    private int paymentStatus;

    public String getTableId () {
        return tableId;
    }

    public void setTableId (String tableId) {
        this.tableId = tableId;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getOrderNo () {
        return orderNo;
    }

    public void setOrderNo (String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayPrice () {
        return payPrice;
    }

    public void setPayPrice (String payPrice) {
        this.payPrice = payPrice;
    }

    public int getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus (int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPaymentStatus () {
        return paymentStatus;
    }

    public void setPaymentStatus (int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString () {
        return "TableResultDataListItem{" +
                "tableId='" + tableId + '\'' +
                ", id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", payPrice='" + payPrice + '\'' +
                ", orderStatus=" + orderStatus +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
