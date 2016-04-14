package com.toughegg.teorderpo.modle.entry.uploadOrder;

import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultData;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultDataItem;

import java.io.Serializable;
import java.util.List;

/**
 * LMS内部订单详情模板,只供计算和打印用
 * Created by toughegg on 16/3/28.
 * Aaron
 */
public class OrderLuaDetail {
    private int id;//来自AWS的订单id
    private String addTax;    //总税额，sum(item.addTaxAmount * count)
    private String address;   //联系人地址
    private String basePrice; //原始价格，所有item的 basePrice * count 的总和
    private String cancelRemark; //取消说明
    private int cancelledBy;     //取消人id （0表示是顾客，其它表示店员)
    private Cancel cancel;//取消操作信息
    private String contactName;  //联系人
    private String contactNum;   //联系电话
    private String createdStamp;   //创建时间
    private int customerNum;     //用餐人数
    private String diningDateTime; //用餐时间
    private String discountAmount; //折扣优惠金额，sum(item.discountAmount * count)
    private String discountPrice; //不含税的折后价格，sum(item.itemPrice * count)
    private boolean fromYami;   //是否来自yami的订单(手机下的单)
    private boolean isRead;    //餐厅是否已经读过此单(仅LMS使用)
    private int maxGroupNumber; //当前最大GroupNumber(仅LMS使用)
    private int maxOrderItemId; //当前最大OrderItemId(仅LMS使用)
    private int memberId;       //会员id
    private String menuId;      //菜单id
    private boolean merchantConfirmed; //餐厅是否已经确认接收此订单
    private int operatorId;       //操作店员账号id
    private String operatorName; // 操作员 名词；
    private String orderDiscount; //整单折扣率
    private String orderNo;       //订单号
    private String payPrice;      //订单应付金额
    private String remark;        //备注
    private String restId;        //餐厅id
    private String round;         //现金付款为了取整而调整的金额
    private int status; //订单状态 0 新增订单,1 已经取消, 2 已付款, 3 已经完成, 4 已经退款, 5 商家已经确认接收订单, 6 订单过期失效(目前商家拒收也会置为此状态)
    private boolean setNeedsUpload;//是否需要提交以生成报表, true表示尚未提交, false表示已提交
    private String tableCode;  //桌号
    private String tableId;    //餐桌id
    private String takeAwayNo; //打包单单号
    private String totalGst;   //商户端自用的gst数额显示
    private String totalService; // 总的Service Charge 服务税
    private String didTableCode; // 换桌旧桌号；  默认为空字符 ''
    private int type;          //订单类型（1堂食，2打包，3预定）
    private String updatedStamp; //更新时间
    private boolean setNeedsPrint;  //是否需要打印//以前使用的是    isChangeForDish;; // 是否有菜品改变
    private String tips;       //小费
    private boolean isTotalOrder;//判断是否是总单（程序里面用来使用的字段，打印需要调用）
    private String cashRounding;//判断到底要不要取整
    private String roundingRule;//取整规则 [key]:[value],[key]:[value]... key是付款尾数,value是取整动作
    private OrderNetResultData.Payment payment;//付款信息
    private OrderNetResultData.Refund mRefund;// 退款信息
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

    public Cancel getCancel () {
        return cancel;
    }

    public void setCancel (Cancel cancel) {
        this.cancel = cancel;
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

    public String getDiningDateTime () {
        return diningDateTime;
    }

    public void setDiningDateTime (String diningDateTime) {
        this.diningDateTime = diningDateTime;
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

    public String getOperatorName () {
        return operatorName;
    }

    public void setOperatorName (String operatorName) {
        this.operatorName = operatorName;
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

    public String getTotalService () {
        return totalService;
    }

    public void setTotalService (String totalService) {
        this.totalService = totalService;
    }

    public String getDidTableCode () {
        return didTableCode;
    }

    public void setDidTableCode (String didTableCode) {
        this.didTableCode = didTableCode;
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

    public boolean isSetNeedsPrint () {
        return setNeedsPrint;
    }

    public void setSetNeedsPrint (boolean setNeedsPrint) {
        this.setNeedsPrint = setNeedsPrint;
    }

    public String getTips () {
        return tips;
    }

    public void setTips (String tips) {
        this.tips = tips;
    }

    public boolean isTotalOrder () {
        return isTotalOrder;
    }

    public void setIsTotalOrder (boolean isTotalOrder) {
        this.isTotalOrder = isTotalOrder;
    }

    public String getCashRounding () {
        return cashRounding;
    }

    public void setCashRounding (String cashRounding) {
        this.cashRounding = cashRounding;
    }

    public String getRoundingRule () {
        return roundingRule;
    }

    public void setRoundingRule (String roundingRule) {
        this.roundingRule = roundingRule;
    }

    public OrderNetResultData.Payment getPayment () {
        return payment;
    }

    public void setPayment (OrderNetResultData.Payment payment) {
        this.payment = payment;
    }

    public OrderNetResultData.Refund getRefund () {
        return mRefund;
    }

    public void setRefund (OrderNetResultData.Refund refund) {
        mRefund = refund;
    }

    public List<OrderNetResultDataItem> getItem () {
        return item;
    }

    public void setItem (List<OrderNetResultDataItem> item) {
        this.item = item;
    }

    @Override
    public String toString () {
        return "OrderLuaDetail{" +
                "id=" + id +
                ", addTax='" + addTax + '\'' +
                ", address='" + address + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", cancelRemark='" + cancelRemark + '\'' +
                ", cancelledBy=" + cancelledBy +
                ", cancel=" + cancel +
                ", contactName='" + contactName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                ", customerNum=" + customerNum +
                ", diningDateTime='" + diningDateTime + '\'' +
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
                ", operatorName='" + operatorName + '\'' +
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
                ", takeAwayNo='" + takeAwayNo + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", totalService='" + totalService + '\'' +
                ", didTableCode='" + didTableCode + '\'' +
                ", type=" + type +
                ", updatedStamp='" + updatedStamp + '\'' +
                ", setNeedsPrint=" + setNeedsPrint +
                ", tips='" + tips + '\'' +
                ", isTotalOrder=" + isTotalOrder +
                ", cashRounding='" + cashRounding + '\'' +
                ", roundingRule='" + roundingRule + '\'' +
                ", payment=" + payment +
                ", mRefund=" + mRefund +
                ", item=" + item +
                '}';
    }

    // -------------------------------cancel---------------------------------
    public static class Cancel implements Serializable {
        private int id; //来自AWS的id
        private int operatorId;       //取消人id （0表示是顾客，其它表示店员)
        private String operatorName; //操作员名字
        private String cancelReason; //取消原因说明
        private String createdStamp; //取消时间

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

        public String getOperatorName () {
            return operatorName;
        }

        public void setOperatorName (String operatorName) {
            this.operatorName = operatorName;
        }

        public String getCancelReason () {
            return cancelReason;
        }

        public void setCancelReason (String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public String getCreatedStamp () {
            return createdStamp;
        }

        public void setCreatedStamp (String createdStamp) {
            this.createdStamp = createdStamp;
        }

        @Override
        public String toString () {
            return "Cancel{" +
                    "id=" + id +
                    ", operatorId=" + operatorId +
                    ", operatorName='" + operatorName + '\'' +
                    ", cancelReason='" + cancelReason + '\'' +
                    ", createdStamp='" + createdStamp + '\'' +
                    '}';
        }
    }

}
