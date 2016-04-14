package com.toughegg.teorderpo.modle.bean;

import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.Option;

import java.io.Serializable;
import java.util.List;

/**
 * Created by toughegg on 15/9/11.
 * 购物车类
 */
public class ShoppingCart implements Serializable {
    private int id;
    private String dishId; // 菜品id
    private String code;
    private Name name;
    private int copies;
    private String price, totalPrice;// 原价，总价
    private String remark;
    private List<Option> optionList;
    private boolean isDeleted;

    public boolean getIsDeleted () {
        return isDeleted;
    }

    public void setIsDeleted (boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString () {
        return "ShoppingCart{" +
                "id=" + id +
                ", dishId='" + dishId + '\'' +
                ", code='" + code + '\'' +
                ", name=" + name +
                ", copies=" + copies +
                ", price='" + price + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", remark='" + remark + '\'' +
                ", optionList=" + optionList +
                '}';
    }
}
