package com.toughegg.teorderpo.modle.entry.tablelist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/11/27.
 */
public class TableResultData implements Serializable {
//            "id": "5649ce9a72c6892dbcbfea3e",    //餐桌id
//            "capacity": 5,      //餐桌容量
//            "code": "T1",       //餐桌桌号
//            "customerNum": 0,   //当前入座人数
//            "enabled": true,    //餐桌可用状态
//            "maximum": 7,       //餐桌最大容量
//            "minimum": 3,       //餐桌最小入座数
//            "occupied": false,  //是否被占用
//            "order": 1,         //餐桌排序序号
//            "orderNo": "",      //此餐桌当前对应的订单id
//            "orderStatus": 1,   //订单状态
//            "payPrice": "",     //订单价格
//            "paymentStatus": 0, //订单支付状态 0表示未支付， 1表示已支付
//            "restId": "5649569e72c6892bf008df49"  //餐桌所在餐厅id

    private String id;
    private String capacity;
    private String code;
    private int customerNum;
    private boolean enable;
    private int maximum;
    private int minimum;
    private boolean occupied;
    private int order;
    private String orderNo;
    private int orderStatus;
    private String payPrice;
    private int paymentStatus;
    private String restId;
    private List<TableResultDataListItem> tableList;// 拼桌的餐桌列表

    private boolean isSelected;// 操作拼桌时要用

    public boolean isSelected () {
        return isSelected;
    }

    public void setIsSelected (boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<TableResultDataListItem> getTableList () {
        return tableList;
    }

    public void setTableList (List<TableResultDataListItem> tableList) {
        this.tableList = tableList;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getCapacity () {
        return capacity;
    }

    public void setCapacity (String capacity) {
        this.capacity = capacity;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public int getCustomerNum () {
        return customerNum;
    }

    public void setCustomerNum (int customerNum) {
        this.customerNum = customerNum;
    }

    public boolean isEnable () {
        return enable;
    }

    public void setEnable (boolean enable) {
        this.enable = enable;
    }

    public int getMaximum () {
        return maximum;
    }

    public void setMaximum (int maximum) {
        this.maximum = maximum;
    }

    public int getMinimum () {
        return minimum;
    }

    public void setMinimum (int minimum) {
        this.minimum = minimum;
    }

    public boolean isOccupied () {
        return occupied;
    }

    public void setOccupied (boolean occupied) {
        this.occupied = occupied;
    }

    public int getOrder () {
        return order;
    }

    public void setOrder (int order) {
        this.order = order;
    }

    public String getOrderNo () {
        return orderNo;
    }

    public void setOrderNo (String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus (int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayPrice () {
        return payPrice;
    }

    public void setPayPrice (String payPrice) {
        this.payPrice = payPrice;
    }

    public int getPaymentStatus () {
        return paymentStatus;
    }

    public void setPaymentStatus (int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRestId () {
        return restId;
    }

    public void setRestId (String restId) {
        this.restId = restId;
    }

    @Override
    public String toString () {
        return "TableResultData{" +
                "id='" + id + '\'' +
                ", capacity='" + capacity + '\'' +
                ", code='" + code + '\'' +
                ", customerNum=" + customerNum +
                ", enable=" + enable +
                ", maximum=" + maximum +
                ", minimum=" + minimum +
                ", occupied=" + occupied +
                ", order=" + order +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus=" + orderStatus +
                ", payPrice='" + payPrice + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", restId='" + restId + '\'' +
                ", tableList=" + tableList +
                '}';
    }
}
