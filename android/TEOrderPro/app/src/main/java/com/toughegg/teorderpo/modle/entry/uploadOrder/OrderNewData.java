package com.toughegg.teorderpo.modle.entry.uploadOrder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/8.
 */
public class OrderNewData implements Serializable {
    private String createdStamp; //create 时间
    private String diningDataTime; // dining time
    private String updatedStamp; // update time
    private String orderNo; //订单编号
    private int customerNum; //客户人数
//    private boolean isOrdered;
    private String remark;
    private String discountPrice;
    private int status;
    private String basePrice;
    private String addTax;
    private String round;
    private int type;  //就餐方式
    private List<Item> item;
    private int orderId;
    private String discountAmount;
    private String totalServiceTax;
    private String tableCode;
    private int isPaid;
    private String payPrice;
    private String totalGst;
    private String restId;
    private String takeAwayNo;
    private String tableId;


    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

//    public boolean isOrdered () {
//        return isOrdered;
//    }
//
//    public void setIsOrdered (boolean isOrdered) {
//        this.isOrdered = isOrdered;
//    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getAddTax() {
        return addTax;
    }

    public void setAddTax(String addTax) {
        this.addTax = addTax;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalServiceTax() {
        return totalServiceTax;
    }

    public void setTotalServiceTax(String totalServiceTax) {
        this.totalServiceTax = totalServiceTax;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getTakeAwayNo() {
        return takeAwayNo;
    }

    public void setTakeAwayNo(String takeAwayNo) {
        this.takeAwayNo = takeAwayNo;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getCreatedStamp () {
        return createdStamp;
    }

    public void setCreatedStamp (String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public String getDiningDataTime () {
        return diningDataTime;
    }

    public void setDiningDataTime (String diningDataTime) {
        this.diningDataTime = diningDataTime;
    }

    public String getUpdatedStamp () {
        return updatedStamp;
    }

    public void setUpdatedStamp (String updatedStamp) {
        this.updatedStamp = updatedStamp;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getOrderNo () {
        return orderNo;
    }

    public void setOrderNo (String orderNo) {
        this.orderNo = orderNo;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString () {
        return "OrderNewData{" +
                "createdStamp='" + createdStamp + '\'' +
                ", diningDataTime='" + diningDataTime + '\'' +
                ", updatedStamp='" + updatedStamp + '\'' +
                ", type=" + type +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
