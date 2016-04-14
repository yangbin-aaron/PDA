package com.toughegg.teorderpo.modle.printOrder;

import java.util.List;

/**
 * Created by Andy on 16/2/22.
 * 打印订单
 */
public class PrintOrderDetail {
    private boolean is_paid;
    private int total_price_without_fee;
    private String remark;
    private int gst;
    private int take_away_number;
    private int pay_type_local;
    private int state;
    private String contact_name;
    private int is_printed_cashier;
    private int cancelled_by;
    private int restaurant_table_fk;
    private int restaurant_id;
    private String create_date_time;
    private int is_printed_invoice;
    private String table_id;
    private String dining_date_time;
    private String address;
    private String total_gst;
    private int internetTotalPrice;
    private int type;
    private int service_tax;
    private boolean isChangeForDish;
    private int is_cancel_order;
    private int paymentCash;
    private int is_ordered;
    private int order_id;
    private String discount_price;
    private int customer_number;
    private String invoiceNo;
    private int is_end_order;
    private String username;
    private String contact_no;
    private String didTable_number;
    private String order_no;
    private List<PrintDishList> dishList;
    private String paymentChange;
    private String table_number;
    private int is_end_serving;
    private String total_price;
    private int is_readed_by_restaurant;
    private String update_date_time;
    private int pay_type;
    private int is_add_dishes;
    private String dish_total_price;
    private String restaurant_fk;
    private String discount;
    private int is_printed_kitchen;
    private int member_type;
    private String total_service_tax;
    private String round;

    public boolean is_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public int getTotal_price_without_fee() {
        return total_price_without_fee;
    }

    public void setTotal_price_without_fee(int total_price_without_fee) {
        this.total_price_without_fee = total_price_without_fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getGst() {
        return gst;
    }

    public void setGst(int gst) {
        this.gst = gst;
    }

    public int getTake_away_number() {
        return take_away_number;
    }

    public void setTake_away_number(int take_away_number) {
        this.take_away_number = take_away_number;
    }

    public int getPay_type_local() {
        return pay_type_local;
    }

    public void setPay_type_local(int pay_type_local) {
        this.pay_type_local = pay_type_local;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public int getIs_printed_cashier() {
        return is_printed_cashier;
    }

    public void setIs_printed_cashier(int is_printed_cashier) {
        this.is_printed_cashier = is_printed_cashier;
    }

    public int getCancelled_by() {
        return cancelled_by;
    }

    public void setCancelled_by(int cancelled_by) {
        this.cancelled_by = cancelled_by;
    }

    public int getRestaurant_table_fk() {
        return restaurant_table_fk;
    }

    public void setRestaurant_table_fk(int restaurant_table_fk) {
        this.restaurant_table_fk = restaurant_table_fk;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCreate_date_time() {
        return create_date_time;
    }

    public void setCreate_date_time(String create_date_time) {
        this.create_date_time = create_date_time;
    }

    public int getIs_printed_invoice() {
        return is_printed_invoice;
    }

    public void setIs_printed_invoice(int is_printed_invoice) {
        this.is_printed_invoice = is_printed_invoice;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getDining_date_time() {
        return dining_date_time;
    }

    public void setDining_date_time(String dining_date_time) {
        this.dining_date_time = dining_date_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal_gst() {
        return total_gst;
    }

    public void setTotal_gst(String total_gst) {
        this.total_gst = total_gst;
    }

    public int getInternetTotalPrice() {
        return internetTotalPrice;
    }

    public void setInternetTotalPrice(int internetTotalPrice) {
        this.internetTotalPrice = internetTotalPrice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getService_tax() {
        return service_tax;
    }

    public void setService_tax(int service_tax) {
        this.service_tax = service_tax;
    }

    public boolean isChangeForDish() {
        return isChangeForDish;
    }

    public void setIsChangeForDish(boolean isChangeForDish) {
        this.isChangeForDish = isChangeForDish;
    }

    public int getIs_cancel_order() {
        return is_cancel_order;
    }

    public void setIs_cancel_order(int is_cancel_order) {
        this.is_cancel_order = is_cancel_order;
    }

    public int getPaymentCash() {
        return paymentCash;
    }

    public void setPaymentCash(int paymentCash) {
        this.paymentCash = paymentCash;
    }

    public int getIs_ordered() {
        return is_ordered;
    }

    public void setIs_ordered(int is_ordered) {
        this.is_ordered = is_ordered;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public int getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(int customer_number) {
        this.customer_number = customer_number;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getIs_end_order() {
        return is_end_order;
    }

    public void setIs_end_order(int is_end_order) {
        this.is_end_order = is_end_order;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getDidTable_number() {
        return didTable_number;
    }

    public void setDidTable_number(String didTable_number) {
        this.didTable_number = didTable_number;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public List<PrintDishList> getDishList() {
        return dishList;
    }

    public void setDishList(List<PrintDishList> dishList) {
        this.dishList = dishList;
    }

    public String getPaymentChange() {
        return paymentChange;
    }

    public void setPaymentChange(String paymentChange) {
        this.paymentChange = paymentChange;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public int getIs_end_serving() {
        return is_end_serving;
    }

    public void setIs_end_serving(int is_end_serving) {
        this.is_end_serving = is_end_serving;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getIs_readed_by_restaurant() {
        return is_readed_by_restaurant;
    }

    public void setIs_readed_by_restaurant(int is_readed_by_restaurant) {
        this.is_readed_by_restaurant = is_readed_by_restaurant;
    }

    public String getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(String update_date_time) {
        this.update_date_time = update_date_time;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getIs_add_dishes() {
        return is_add_dishes;
    }

    public void setIs_add_dishes(int is_add_dishes) {
        this.is_add_dishes = is_add_dishes;
    }

    public String getDish_total_price() {
        return dish_total_price;
    }

    public void setDish_total_price(String dish_total_price) {
        this.dish_total_price = dish_total_price;
    }

    public String getRestaurant_fk() {
        return restaurant_fk;
    }

    public void setRestaurant_fk(String restaurant_fk) {
        this.restaurant_fk = restaurant_fk;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getIs_printed_kitchen() {
        return is_printed_kitchen;
    }

    public void setIs_printed_kitchen(int is_printed_kitchen) {
        this.is_printed_kitchen = is_printed_kitchen;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public String getTotal_service_tax() {
        return total_service_tax;
    }

    public void setTotal_service_tax(String total_service_tax) {
        this.total_service_tax = total_service_tax;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
