package com.toughegg.teorderpo.modle.entry.ordernetdefail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by toughegg on 15/12/2.
 */
public class OrderNetResultData implements Serializable {
    private String id; // data ID
    private String orderNo; // 订单编号
    private int status;// 订单状态  new(0), canceled(1), sending(2), completed(3), refunded(4)
    private int type; // 订单类型  dine_in(1), take_away(2), reservation(3)
    private String addTax;
    private String address;  //可选，订单送货地址
    private String basePrice, payPrice; //未计算税费的基础价格  //计算税费等信息后的应付价格
    //如果订单被取消，则现实取消订单的店员id,
    //如果是客户端用户自行取消，则显示"0"
    private int cancelledBy;
    private String contactName;//手机App使用，订单联系人
    private String contactNum;//手机App使用，订单联系电话
    private int customerNum;//用餐人数
    private String diningDataTime, createdStamp, updatedStamp;//用餐时间//创建时间//修改时间
    private String discountAmount;
    private String discountPrice;
    //    private boolean isOrdered;// 无效字段
    private boolean isRead;//表示订单是否被读取过
    private int isReview;
    private String orderDiscount;
    private Payment payment;//订单的支付信息
    private Refund redund;//订单的退款信息，如果尚未退款，就保持为null
    private String remark;
    private String restaurantId;
    private String round;
    private String tableCode;
    private String tableId;
    private int takeAwayNo;
    //    private String totalGst,totalServiceTax;
    private List<OrderNetResultDataItem> item;
    private int orderId;

    public int getOrderId () {
        return orderId;
    }

    public void setOrderId (int orderId) {
        this.orderId = orderId;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getOrderNo () {
        return orderNo;
    }

    public void setOrderNo (String orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

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

    public String getPayPrice () {
        return payPrice;
    }

    public void setPayPrice (String payPrice) {
        this.payPrice = payPrice;
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

    public int getCustomerNum () {
        return customerNum;
    }

    public void setCustomerNum (int customerNum) {
        this.customerNum = customerNum;
    }

    public String getDiningDataTime () {
        return diningDataTime;
    }

    public void setDiningDataTime (String diningDataTime) {
        this.diningDataTime = diningDataTime;
    }

    public String getCreatedStamp () {
        return createdStamp;
    }

    public void setCreatedStamp (String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public String getUpdatedStamp () {
        return updatedStamp;
    }

    public void setUpdatedStamp (String updatedStamp) {
        this.updatedStamp = updatedStamp;
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

    public boolean isRead () {
        return isRead;
    }

    public void setIsRead (boolean isRead) {
        this.isRead = isRead;
    }

    public int getIsReview () {
        return isReview;
    }

    public void setIsReview (int isReview) {
        this.isReview = isReview;
    }

    public String getOrderDiscount () {
        return orderDiscount;
    }

    public void setOrderDiscount (String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public Payment getPayment () {
        return payment;
    }

    public void setPayment (Payment payment) {
        this.payment = payment;
    }

    public Refund getRedund () {
        return redund;
    }

    public void setRedund (Refund redund) {
        this.redund = redund;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getRestaurantId () {
        return restaurantId;
    }

    public void setRestaurantId (String restaurantId) {
        this.restaurantId = restaurantId;
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

    public int getTakeAwayNo () {
        return takeAwayNo;
    }

    public void setTakeAwayNo (int takeAwayNo) {
        this.takeAwayNo = takeAwayNo;
    }

//    public String getTotalGst () {
//        return totalGst;
//    }
//
//    public void setTotalGst (String totalGst) {
//        this.totalGst = totalGst;
//    }
//
//    public String getTotalServiceTax () {
//        return totalServiceTax;
//    }
//
//    public void setTotalServiceTax (String totalServiceTax) {
//        this.totalServiceTax = totalServiceTax;
//    }

    public List<OrderNetResultDataItem> getItem () {
        return item;
    }

    public void setItem (List<OrderNetResultDataItem> item) {
        this.item = item;
    }

    public static class Payment implements Serializable {
        private String amount;//支付金额
        private int paymentMethod;//支付方式
        private int status;//支付状态
        private String transactionNo;//交易号

        public String getAmount () {
            return amount;
        }

        public void setAmount (String amount) {
            this.amount = amount;
        }

        public int getPaymentMethod () {
            return paymentMethod;
        }

        public void setPaymentMethod (int paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public int isStatus () {
            return status;
        }

        public void setStatus (int status) {
            this.status = status;
        }

        public String getTransactionNo () {
            return transactionNo;
        }

        public void setTransactionNo (String transactionNo) {
            this.transactionNo = transactionNo;
        }

        @Override
        public String toString () {
            return "Payment{" +
                    "amount='" + amount + '\'' +
                    ", paymentMethod=" + paymentMethod +
                    ", status=" + status +
                    ", transactionNo='" + transactionNo + '\'' +
                    '}';
        }
    }

    public static class Refund implements Serializable {
        private int status;//退款状态（申请退款，完成退款）
        private String amount;//退款金额
        private String refundedBy;//退款操作人id（店员）
        private String refundeason;//退款原因说明
        private long updatedStamp, createdStamp;

        public int getStatus () {
            return status;
        }

        public void setStatus (int status) {
            this.status = status;
        }

        public String getAmount () {
            return amount;
        }

        public void setAmount (String amount) {
            this.amount = amount;
        }

        public String getRefundedBy () {
            return refundedBy;
        }

        public void setRefundedBy (String refundedBy) {
            this.refundedBy = refundedBy;
        }

        public String getRefundeason () {
            return refundeason;
        }

        public void setRefundeason (String refundeason) {
            this.refundeason = refundeason;
        }

        public long getUpdatedStamp () {
            return updatedStamp;
        }

        public void setUpdatedStamp (long updatedStamp) {
            this.updatedStamp = updatedStamp;
        }

        public long getCreatedStamp () {
            return createdStamp;
        }

        public void setCreatedStamp (long createdStamp) {
            this.createdStamp = createdStamp;
        }
    }
}
