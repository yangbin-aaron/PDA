package com.toughegg.teorderpo.modle.entry.uploadOrder;

import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;

import java.util.List;

/**
 * Created by Andy on 15/12/3.
 *
 */
public class OrderDetail {
    private String addTax;
    private String address;
    private String basePrice;
    private int cancelledBy;
    private String contactName;
    private String contactNum;
    private String createdStamp;
    private int customerNum;
    private String diningDataTime;
    private String discountAmount;
    private String discountPrice;
//    private boolean isOrdered;
    private int isPaid;
    private int isReview;
    private List<Item> item;
    private String orderDiscount;
    private int orderId;
    private String orderNo;
    private String payPrice;
    private OrderNetResultData.Payment payment;
    private String remark;
    private String restaurantId;
    private String round;
    private int status;
    private String tableCode;
    private String tableId;
    private String totalGst;
    private String totalServiceTax;
    private int type;
    private String updatedStamp;

    public String getAddTat() {
        return addTax;
    }

    public void setAddTat(String addTax) {
        this.addTax = addTax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public int getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

    public String getDiningDataTime() {
        return diningDataTime;
    }

    public void setDiningDataTime(String diningDataTime) {
        this.diningDataTime = diningDataTime;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getAddTax () {
        return addTax;
    }

    public void setAddTax (String addTax) {
        this.addTax = addTax;
    }

    public int getIsReview() {
        return isReview;
    }

    public void setIsReview(int isReview) {
        this.isReview = isReview;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public OrderNetResultData.Payment getPayment() {
        return payment;
    }

    public void setPayment(OrderNetResultData.Payment payment) {
        this.payment = payment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(String totalGst) {
        this.totalGst = totalGst;
    }

    public String getTotalServiceTax() {
        return totalServiceTax;
    }

    public void setTotalServiceTax(String totalServiceTax) {
        this.totalServiceTax = totalServiceTax;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdatedStamp() {
        return updatedStamp;
    }

    public void setUpdatedStamp(String updatedStamp) {
        this.updatedStamp = updatedStamp;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    @Override
    public String toString () {
        return "OrderDetail{" +
                "addTax='" + addTax + '\'' +
                ", address='" + address + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", cancelledBy=" + cancelledBy +
                ", contactName='" + contactName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                ", customerNum=" + customerNum +
                ", diningDataTime='" + diningDataTime + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", isPaid=" + isPaid +
                ", isReview=" + isReview +
                ", item=" + item +
                ", orderDiscount='" + orderDiscount + '\'' +
                ", orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", payPrice='" + payPrice + '\'' +
                ", payment=" + payment +
                ", remark='" + remark + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", round='" + round + '\'' +
                ", status=" + status +
                ", tableCode='" + tableCode + '\'' +
                ", tableId='" + tableId + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", totalServiceTax='" + totalServiceTax + '\'' +
                ", type=" + type +
                ", updatedStamp='" + updatedStamp + '\'' +
                '}';
    }
}
