package com.toughegg.teorderpo.modle.entry;

import com.toughegg.teorderpo.modle.bean.ShoppingCart;

import java.util.List;

/**
 * Created by toughegg on 15/9/25.
 */
public class OrderNetDetail {
    private int orderId;// 订单id
    private String restaurantTableFk;  //餐桌id
    private String orderNo;  //订单号
    private String totalPrice;//总价
    private String totalPriceWithoutFee;//所有菜品总价（不含其他费用）
    private String totalGst;//政府税
    private String gst;//政府税率 --添加订单时从setting表拿的数据
    private String totalServiceTax;
    private String serviceTax;//服务税率 --添加订单时从setting表拿的数据
    private String discountPrice;//折扣价格
    private String discount;//折扣率
    private int customerNumber;//用餐人数
    private String diningDateTime;//用餐时间
    private int state;//订单状态 [订位:(Reservation = 2,Seating = 3,Cooking = 4,Dining = 5,Completed = 6,Cancelled = 0),堂食:(Seating = 3,Cooking = 4,
    // Dining = 5,Completed = 6,Cancelled = 0),外卖:(Cooking = 4,sending = 5,Completed = 6,Cancelled = 0)]
    private int payType;//支付类型(Cash = 1,Online = 2,YamiCoins = 3,Nets = 4,Visa = 5,Master = 6,AMEX = 7,JCB = 8,UnionPay = 9,Diners = 10,Voucher = 11)
    private boolean isPaid;//是否付款
    private int type;//订单类型 (HallFood = 1,CarryOut = 2,Reserve = 3)
    private String contactName;//联系人
    private String contactNo;//联系电话
    private boolean isPrintedKitchen;//是否已经打印厨房
    private boolean isPrintedCashier;//是否已经打印收银
    private boolean isPrintedInvoice;//是否已经打印发票
    private String createDateTime;//添加时间
    private String updateDateTime;//修改时间
    private String remark;//备注
    private boolean isOrdered;//是否已经点菜
    private int cancelledBy;//取消人 (NA = 0,Member= 1,Restaurant = 2)
    private String receiptSubTotal;// (所有菜品的相加   总价格（菜品总价包含它所加的料），不包含单子的折扣，也不包含针对菜的折扣)的总价
    private String receiptDisc;//  ( 针对（每个）菜的折扣的总价 + 针对单子的打折的总价)
    private String receiptRound;//免去的钱数

    private List<ShoppingCart> shoppingCarts;// 菜品列表

    public int getCancelledBy () {
        return cancelledBy;
    }

    public void setCancelledBy (int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public int getOrderId () {
        return orderId;
    }

    public void setOrderId (int orderId) {
        this.orderId = orderId;
    }

    public String getRestaurantTableFk () {
        return restaurantTableFk;
    }

    public void setRestaurantTableFk (String restaurantTableFk) {
        this.restaurantTableFk = restaurantTableFk;
    }

    public String getOrderNo () {
        return orderNo;
    }

    public void setOrderNo (String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTotalPrice () {
        return totalPrice;
    }

    public void setTotalPrice (String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPriceWithoutFee () {
        return totalPriceWithoutFee;
    }

    public void setTotalPriceWithoutFee (String totalPriceWithoutFee) {
        this.totalPriceWithoutFee = totalPriceWithoutFee;
    }

    public String getTotalGst () {
        return totalGst;
    }

    public void setTotalGst (String totalGst) {
        this.totalGst = totalGst;
    }

    public String getGst () {
        return gst;
    }

    public void setGst (String gst) {
        this.gst = gst;
    }

    public String getServiceTax () {
        return serviceTax;
    }

    public void setServiceTax (String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getTotalServiceTax () {
        return totalServiceTax;
    }

    public void setTotalServiceTax (String totalServiceTax) {
        this.totalServiceTax = totalServiceTax;
    }

    public String getDiscountPrice () {
        return discountPrice;
    }

    public void setDiscountPrice (String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscount () {
        return discount;
    }

    public void setDiscount (String discount) {
        this.discount = discount;
    }

    public int getCustomerNumber () {
        return customerNumber;
    }

    public void setCustomerNumber (int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDiningDateTime () {
        return diningDateTime;
    }

    public void setDiningDateTime (String diningDateTime) {
        this.diningDateTime = diningDateTime;
    }

    public int getState () {
        return state;
    }

    public void setState (int state) {
        this.state = state;
    }

    public int getPayType () {
        return payType;
    }

    public void setPayType (int payType) {
        this.payType = payType;
    }

    public boolean getIsPaid () {
        return isPaid;
    }

    public void setIsPaid (boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getContactName () {
        return contactName;
    }

    public void setContactName (String contactName) {
        this.contactName = contactName;
    }

    public String getContactNo () {
        return contactNo;
    }

    public void setContactNo (String contactNo) {
        this.contactNo = contactNo;
    }

    public boolean isPrintedKitchen () {
        return isPrintedKitchen;
    }

    public void setIsPrintedKitchen (boolean isPrintedKitchen) {
        this.isPrintedKitchen = isPrintedKitchen;
    }

    public boolean isPrintedCashier () {
        return isPrintedCashier;
    }

    public void setIsPrintedCashier (boolean isPrintedCashier) {
        this.isPrintedCashier = isPrintedCashier;
    }

    public boolean isPrintedInvoice () {
        return isPrintedInvoice;
    }

    public void setIsPrintedInvoice (boolean isPrintedInvoice) {
        this.isPrintedInvoice = isPrintedInvoice;
    }

    public String getCreateDateTime () {
        return createDateTime;
    }

    public void setCreateDateTime (String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUpdateDateTime () {
        return updateDateTime;
    }

    public void setUpdateDateTime (String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public boolean isOrdered () {
        return isOrdered;
    }

    public void setIsOrdered (boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public String getReceiptSubTotal () {
        return receiptSubTotal;
    }

    public void setReceiptSubTotal (String receiptSubTotal) {
        this.receiptSubTotal = receiptSubTotal;
    }

    public String getReceiptDisc () {
        return receiptDisc;
    }

    public void setReceiptDisc (String receiptDisc) {
        this.receiptDisc = receiptDisc;
    }

    public String getReceiptRound () {
        return receiptRound;
    }

    public void setReceiptRound (String receiptRound) {
        this.receiptRound = receiptRound;
    }

    public List<ShoppingCart> getShoppingCarts () {
        return shoppingCarts;
    }

    public void setShoppingCarts (List<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    @Override
    public String toString () {
        return "OrderNetDetail{" +
                "orderNo='" + orderNo + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", totalGst='" + totalGst + '\'' +
                ", totalPriceWithoutFee='" + totalPriceWithoutFee + '\'' +
                ", serviceTax='" + serviceTax + '\'' +
                ", customerNumber=" + customerNumber +
                ", shoppingCarts=" + shoppingCarts +
                '}';
    }
}
