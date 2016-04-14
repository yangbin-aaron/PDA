package com.toughegg.teorderpo.modle.entry.ordernetdefail;

import java.io.Serializable;
import java.util.List;

/**
 * 网络订单详情
 * Created by toughegg on 15/12/2.
 */
public class OrderNetResultData implements Serializable {
    private int id;//来自AWS的订单id
    private String addTax;//总税额，sum(item.addTaxAmount * count)
    private String address;  //可选，订单送货地址
    private String basePrice;//原始价格，所有item的 basePrice * count 的总和
    private String cancelRemark;// 取消说明
    private int cancelledBy;//取消人id （0表示是顾客，其它表示店员)
    private String contactName;//手机App使用，订单联系人
    private String contactNum;//手机App使用，订单联系电话
    private String createdStamp;// 创建时间
    private int customerNum;//用餐人数
    private String diningDataTime;//用餐时间
    private String discountAmount;//折扣优惠金额，sum(item.discountAmount * count)
    private String discountPrice;//不含税的折后价格，sum(item.itemPrice * count)
    private boolean fromYami; //是否来自yami的订单(手机下的单)
    private boolean isRead;//餐厅是否已经读过此单(仅LMS使用)
    private int maxGroupNumber; //当前最大GroupNumber(仅LMS使用)
    private int maxOrderItemId; //当前最大OrderItemId(仅LMS使用)
    private int memberId;       //会员id
    private String menuId;      //菜单id
    private boolean merchantConfirmed;//餐厅是否已经确认接收此订单
    private int operatorId;//操作店员账号id
    private String orderDiscount;//整单折扣率
    private String orderNo; // 订单编号
    private String payPrice; //订单应付金额
    private String remark;// 订单备注
    private String restId;// 餐厅id
    private String round;//现金付款为了取整而调整的金额
    private int status;//订单状态（0新增，1取消，2已付款，3完成，4已退款）
    private boolean setNeedsUpload;//是否需要提交以生成报表, true表示尚未提交, false表示已提交
    private String tableCode;//桌号
    private String tableId;//餐桌id
    private String takeAwayNo;//打包单单号
    private String totalGst;   //商户端自用的gst数额显示
    private int type; //订单类型（1堂食，2打包，3预定）
    private String updatedStamp;//更新时间
    private String tips;// 小费
    private Payment payment;//订单的支付信息
    private Refund refund;//订单的退款信息，如果尚未退款，就保持为null
    private List<OrderNetResultDataItem> item;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
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

    public String getDiningDataTime () {
        return diningDataTime;
    }

    public void setDiningDataTime (String diningDataTime) {
        this.diningDataTime = diningDataTime;
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

    public boolean isFromYami () {
        return fromYami;
    }

    public void setFromYami (boolean fromYami) {
        this.fromYami = fromYami;
    }

    public boolean isRead () {
        return isRead;
    }

    public void setIsRead (boolean isRead) {
        this.isRead = isRead;
    }

    public int getMaxGroupNumber () {
        return maxGroupNumber;
    }

    public void setMaxGroupNumber (int maxGroupNumber) {
        this.maxGroupNumber = maxGroupNumber;
    }

    public int getMaxOrderItemId () {
        return maxOrderItemId;
    }

    public void setMaxOrderItemId (int maxOrderItemId) {
        this.maxOrderItemId = maxOrderItemId;
    }

    public int getMemberId () {
        return memberId;
    }

    public void setMemberId (int memberId) {
        this.memberId = memberId;
    }

    public String getMenuId () {
        return menuId;
    }

    public void setMenuId (String menuId) {
        this.menuId = menuId;
    }

    public boolean isMerchantConfirmed () {
        return merchantConfirmed;
    }

    public void setMerchantConfirmed (boolean merchantConfirmed) {
        this.merchantConfirmed = merchantConfirmed;
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

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public boolean isSetNeedsUpload () {
        return setNeedsUpload;
    }

    public void setSetNeedsUpload (boolean setNeedsUpload) {
        this.setNeedsUpload = setNeedsUpload;
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

    public String getTakeAwayNo () {
        return takeAwayNo;
    }

    public void setTakeAwayNo (String takeAwayNo) {
        this.takeAwayNo = takeAwayNo;
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

    public String getTips () {
        return tips;
    }

    public void setTips (String tips) {
        this.tips = tips;
    }

    public Payment getPayment () {
        return payment;
    }

    public void setPayment (Payment payment) {
        this.payment = payment;
    }

    public Refund getRefund () {
        return refund;
    }

    public void setRefund (Refund refund) {
        this.refund = refund;
    }

    public List<OrderNetResultDataItem> getItem () {
        return item;
    }

    public void setItem (List<OrderNetResultDataItem> item) {
        this.item = item;
    }

    @Override
    public String toString () {
        return "OrderNetResultData{" +
                "id=" + id +
                ", addTax='" + addTax + '\'' +
                ", address='" + address + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", cancelRemark='" + cancelRemark + '\'' +
                ", cancelledBy=" + cancelledBy +
                ", contactName='" + contactName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                ", customerNum=" + customerNum +
                ", diningDataTime='" + diningDataTime + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", fromYami=" + fromYami +
                ", isRead=" + isRead +
                ", maxGroupNumber=" + maxGroupNumber +
                ", maxOrderItemId=" + maxOrderItemId +
                ", memberId=" + memberId +
                ", menuId='" + menuId + '\'' +
                ", merchantConfirmed=" + merchantConfirmed +
                ", operatorId=" + operatorId +
                ", orderDiscount='" + orderDiscount + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", payPrice='" + payPrice + '\'' +
                ", remark='" + remark + '\'' +
                ", restId='" + restId + '\'' +
                ", round='" + round + '\'' +
                ", status=" + status +
                ", setNeedsUpload=" + setNeedsUpload +
                ", tableCode='" + tableCode + '\'' +
                ", tableId='" + tableId + '\'' +
                ", takeAwayNo=" + takeAwayNo +
                ", totalGst='" + totalGst + '\'' +
                ", type=" + type +
                ", updatedStamp='" + updatedStamp + '\'' +
                ", tips='" + tips + '\'' +
                ", payment=" + payment +
                ", refund=" + refund +
                ", item=" + item +
                '}';
    }


    // -----------------------------------Payment--------------------------------------

    /**
     * 收款类
     */
    public static class Payment implements Serializable {
        private int id;            //来自AWS的付款id
        private int operatorId;    //收款人id
        private String invoiceNo;  //唯一的收据号
        private int paymentMethod;//支付方式
        private String amount;//应收总金额(含小费)
        private String received;   //实收金额
        private boolean setNeedsPrint;  //是否需要打印
        private String change;     //找零金额
        private int status;//付款状态（0未付款，1已付款）
        private String createdStamp;
        private String updatedStamp;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public int getOperatorId () {
            return operatorId;
        }

        public void setOperatorId (int operatorId) {
            this.operatorId = operatorId;
        }

        public String getInvoiceNo () {
            return invoiceNo;
        }

        public void setInvoiceNo (String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public int getPaymentMethod () {
            return paymentMethod;
        }

        public void setPaymentMethod (int paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getAmount () {
            return amount;
        }

        public void setAmount (String amount) {
            this.amount = amount;
        }

        public String getReceived () {
            return received;
        }

        public void setReceived (String received) {
            this.received = received;
        }

        public boolean isSetNeedsPrint () {
            return setNeedsPrint;
        }

        public void setSetNeedsPrint (boolean setNeedsPrint) {
            this.setNeedsPrint = setNeedsPrint;
        }

        public String getChange () {
            return change;
        }

        public void setChange (String change) {
            this.change = change;
        }

        public int getStatus () {
            return status;
        }

        public void setStatus (int status) {
            this.status = status;
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

        @Override
        public String toString () {
            return "Payment{" +
                    "id=" + id +
                    ", operatorId=" + operatorId +
                    ", invoiceNo='" + invoiceNo + '\'' +
                    ", paymentMethod=" + paymentMethod +
                    ", amount='" + amount + '\'' +
                    ", received='" + received + '\'' +
                    ", setNeedsPrint=" + setNeedsPrint +
                    ", change='" + change + '\'' +
                    ", status=" + status +
                    ", createdStamp='" + createdStamp + '\'' +
                    ", updatedStamp='" + updatedStamp + '\'' +
                    '}';
        }
    }

    // -----------------------------------Refund--------------------------------------

    /**
     * 退款类
     */
    public static class Refund implements Serializable {
        private int id; //来自AWS的退款id
        private int operatorId;     //退款人id
        private int refundMethod;    //退款方式，和付款方式的范围相同
        private int status;//退款状态（申请退款，完成退款）
        private String amount;//退款金额
        private int refundedBy;//退款操作人（店员）
        private String refundeason;//退款原因说明
        private String updatedStamp, createdStamp;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public int getOperatorId () {
            return operatorId;
        }

        public void setOperatorId (int operatorId) {
            this.operatorId = operatorId;
        }

        public int getRefundMethod () {
            return refundMethod;
        }

        public void setRefundMethod (int refundMethod) {
            this.refundMethod = refundMethod;
        }

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

        public int getRefundedBy () {
            return refundedBy;
        }

        public void setRefundedBy (int refundedBy) {
            this.refundedBy = refundedBy;
        }

        public String getRefundeason () {
            return refundeason;
        }

        public void setRefundeason (String refundeason) {
            this.refundeason = refundeason;
        }

        public String getUpdatedStamp () {
            return updatedStamp;
        }

        public void setUpdatedStamp (String updatedStamp) {
            this.updatedStamp = updatedStamp;
        }

        public String getCreatedStamp () {
            return createdStamp;
        }

        public void setCreatedStamp (String createdStamp) {
            this.createdStamp = createdStamp;
        }

        @Override
        public String toString () {
            return "Refund{" +
                    "id=" + id +
                    ", operatorId=" + operatorId +
                    ", refundMethod=" + refundMethod +
                    ", status=" + status +
                    ", amount='" + amount + '\'' +
                    ", refundedBy='" + refundedBy + '\'' +
                    ", refundeason='" + refundeason + '\'' +
                    ", updatedStamp=" + updatedStamp +
                    ", createdStamp=" + createdStamp +
                    '}';
        }
    }
}
