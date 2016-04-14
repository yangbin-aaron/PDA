package com.toughegg.teorderpo.modle.entry.uploadOrder;

import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单时用，（其实它是OrderLuaDetail的一个子集，防止修改字段时，lua可能难以同步的情况新建的一个类）
 * Created by toughegg on 16/3/28.
 */
public class OrderDetail implements Serializable {
    private String addTax;
    private String address;
    private String basePrice;
    private String cancelRemark;
    private int cancelledBy;
    private String contactName;
    private String contactNum;
    private String createdStamp;
    private int customerNum;
    private String discountAmount;
    private String discountPrice;
    private String remark;
    private String restId;
    private String round;
    private String tableCode;
    private String tableId;
    private String totalGst;
    private int type;
    private String updatedStamp;
    private int operatorId;
    private String orderDiscount;
    private String orderNo;
    private String payPrice;
    private List<OrderNetResultDataItem> item;

    public String getAddTax () {
        return addTax;
    }

    public void setAddTax (String addTax) {
        this.addTax = addTax;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public String getBasePrice () {
        return basePrice;
    }

    public void setBasePrice (String basePrice) {
        this.basePrice = basePrice;
    }

    public String getCancelRemark () {
        return cancelRemark;
    }

    public void setCancelRemark (String cancelRemark) {
        this.cancelRemark = cancelRemark;
    }

    public int getCancelledBy () {
        return cancelledBy;
    }

    public void setCancelledBy (int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getContactName () {
        return contactName;
    }

    public void setContactName (String contactName) {
        this.contactName = contactName;
    }

    public String getContactNum () {
        return contactNum;
    }

    public void setContactNum (String contactNum) {
        this.contactNum = contactNum;
    }

    public String getCreatedStamp () {
        return createdStamp;
    }

    public void setCreatedStamp (String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public int getCustomerNum () {
        return customerNum;
    }

    public void setCustomerNum (int customerNum) {
        this.customerNum = customerNum;
    }

    public String getDiscountAmount () {
        return discountAmount;
    }

    public void setDiscountAmount (String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountPrice () {
        return discountPrice;
    }

    public void setDiscountPrice (String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getRestId () {
        return restId;
    }

    public void setRestId (String restId) {
        this.restId = restId;
    }

    public String getRound () {
        return round;
    }

    public void setRound (String round) {
        this.round = round;
    }

    public String getTableCode () {
        return tableCode;
    }

    public void setTableCode (String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableId () {
        return tableId;
    }

    public void setTableId (String tableId) {
        this.tableId = tableId;
    }

    public String getTotalGst () {
        return totalGst;
    }

    public void setTotalGst (String totalGst) {
        this.totalGst = totalGst;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getUpdatedStamp () {
        return updatedStamp;
    }

    public void setUpdatedStamp (String updatedStamp) {
        this.updatedStamp = updatedStamp;
    }

    public int getOperatorId () {
        return operatorId;
    }

    public void setOperatorId (int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOrderDiscount () {
        return orderDiscount;
    }

    public void setOrderDiscount (String orderDiscount) {
        this.orderDiscount = orderDiscount;
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

    public List<OrderNetResultDataItem> getItem () {
        return item;
    }

    public void setItem (List<OrderNetResultDataItem> item) {
        this.item = item;
    }

    @Override
    public String toString () {
        return "OrderDetail{" +
                "addTax='" + addTax + '\'' +
                ", address='" + address + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", cancelRemark='" + cancelRemark + '\'' +
                ", cancelledBy=" + cancelledBy +
                ", contactName='" + contactName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                ", customerNum=" + customerNum +
                ", discountAmount='" + discountAmount + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", remark='" + remark + '\'' +
                ", restId='" + restId + '\'' +
                ", round='" + round + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", tableId='" + tableId + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", type=" + type +
                ", updatedStamp='" + updatedStamp + '\'' +
                ", operatorId=" + operatorId +
                ", orderDiscount='" + orderDiscount + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", payPrice='" + payPrice + '\'' +
                ", item=" + item +
                '}';
    }
}
