package com.toughegg.teorderpo.modle.entry.dishMenu;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;

/**
 * Created by Andy on 15/12/2.
 * 菜品税务
 */
public class ItemTax implements Serializable {
    private String id;// 获取数据的时候需要的
    private String taxId;// 提交订单的时候需要的,不是获取的json字段
    private Name name;
    private boolean enable;
    private String rate;
    private boolean isInclude;
    private boolean fixed;
    private int order;
    private String code;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getTaxId () {
        return taxId;
    }

    public void setTaxId (String taxId) {
        this.taxId = taxId;
    }

    public Name getName () {
        return name;
    }

    public void setName (Name name) {
        this.name = name;
    }

    public boolean isEnable () {
        return enable;
    }

    public void setEnable (boolean enable) {
        this.enable = enable;
    }

    public String getRate () {
        return rate;
    }

    public void setRate (String rate) {
        this.rate = rate;
    }

    public boolean isInclude () {
        return isInclude;
    }

    public void setIsInclude (boolean isInclude) {
        this.isInclude = isInclude;
    }

    public boolean isFixed () {
        return fixed;
    }

    public void setFixed (boolean fixed) {
        this.fixed = fixed;
    }

    public int getOrder () {
        return order;
    }

    public void setOrder (int order) {
        this.order = order;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    @Override
    public String toString () {
        return "ItemTax{" +
                "id='" + id + '\'' +
                ", taxId='" + taxId + '\'' +
                ", name=" + name +
                ", enable=" + enable +
                ", rate='" + rate + '\'' +
                ", isInclude=" + isInclude +
                ", fixed=" + fixed +
                ", order=" + order +
                ", code='" + code + '\'' +
                '}';
    }
}
