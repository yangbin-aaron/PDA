package com.toughegg.teorderpo.modle.printOrder;

import java.util.List;

/**
 * Created by Andy on 16/2/22.
 */
public class PrintDishList {
    private int is_hot;
    private int sortno;
    private String remark;
    private String dish_id;
    private int is_localChange;
    private boolean is_deleted;
    private int is_discount_select;
    private String image;
    private boolean is_select_priceOption;
    private List<PrintPriceOptionList> priceOptionList;
    private String type_id;
    private String describtion_en;
    private int discount;
    private int is_internetData;
    private int add_price;
    private int is_can_take_away;
    private List<String> name;
    private String dish_code;
    private int state;
    private int restaurant_id;
    private int minCopies;
    private int is_set;
    private boolean is_change_forPrint;
    private String orderItem_id;
    private boolean is_rec;
    private String price;
    private int is_hot_sortno;
    private int copies;

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getSortno() {
        return sortno;
    }

    public void setSortno(int sortno) {
        this.sortno = sortno;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDish_id() {
        return dish_id;
    }

    public void setDish_id(String dish_id) {
        this.dish_id = dish_id;
    }

    public int getIs_localChange() {
        return is_localChange;
    }

    public void setIs_localChange(int is_localChange) {
        this.is_localChange = is_localChange;
    }

    public boolean is_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getIs_discount_select() {
        return is_discount_select;
    }

    public void setIs_discount_select(int is_discount_select) {
        this.is_discount_select = is_discount_select;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean is_select_priceOption() {
        return is_select_priceOption;
    }

    public void setIs_select_priceOption(boolean is_select_priceOption) {
        this.is_select_priceOption = is_select_priceOption;
    }

    public List<PrintPriceOptionList> getPriceOptionList() {
        return priceOptionList;
    }

    public void setPriceOptionList(List<PrintPriceOptionList> priceOptionList) {
        this.priceOptionList = priceOptionList;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getDescribtion_en() {
        return describtion_en;
    }

    public void setDescribtion_en(String describtion_en) {
        this.describtion_en = describtion_en;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getIs_internetData() {
        return is_internetData;
    }

    public void setIs_internetData(int is_internetData) {
        this.is_internetData = is_internetData;
    }

    public int getAdd_price() {
        return add_price;
    }

    public void setAdd_price(int add_price) {
        this.add_price = add_price;
    }

    public int getIs_can_take_away() {
        return is_can_take_away;
    }

    public void setIs_can_take_away(int is_can_take_away) {
        this.is_can_take_away = is_can_take_away;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getDish_code() {
        return dish_code;
    }

    public void setDish_code(String dish_code) {
        this.dish_code = dish_code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getMinCopies() {
        return minCopies;
    }

    public void setMinCopies(int minCopies) {
        this.minCopies = minCopies;
    }

    public int getIs_set() {
        return is_set;
    }

    public void setIs_set(int is_set) {
        this.is_set = is_set;
    }

    public boolean is_change_forPrint() {
        return is_change_forPrint;
    }

    public void setIs_change_forPrint(boolean is_change_forPrint) {
        this.is_change_forPrint = is_change_forPrint;
    }

    public String getOrderItem_id() {
        return orderItem_id;
    }

    public void setOrderItem_id(String orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    public boolean is_rec() {
        return is_rec;
    }

    public void setIs_rec(boolean is_rec) {
        this.is_rec = is_rec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIs_hot_sortno() {
        return is_hot_sortno;
    }

    public void setIs_hot_sortno(int is_hot_sortno) {
        this.is_hot_sortno = is_hot_sortno;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
