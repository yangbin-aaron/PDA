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
    private Name name;//税名
    private boolean enable; //是否需要计算该税种
    private String rate; //税率
    private String amount;//税额;
    private boolean free;//是否免税
    private boolean fixed;//是否为固定税率(仅AWS后台用)// true表示所有店铺统一的系统设置, 不能删除, 只能更改
    private String createdStamp;
    private String code;//code是固定的 [service][gst]
    private boolean isInclude;// 是否已经包含在商品价格之中(true: 包含税, false: 附加税)
    private int order;
    private String effectOrderTypes; // 应用的订单种类 例如: 1,2,3 逗号分隔
    private boolean afterDiscount; // 折后再计算税率, 默认为true(包含税一定是先折扣再计算的)
    private String precondition; // 计算该税前要将另外一个税的金额算到总金额中去

    public String getEffectOrderTypes () {
        return effectOrderTypes;
    }

    public void setEffectOrderTypes (String effectOrderTypes) {
        this.effectOrderTypes = effectOrderTypes;
    }

    public boolean isAfterDiscount () {
        return afterDiscount;
    }

    public void setAfterDiscount (boolean afterDiscount) {
        this.afterDiscount = afterDiscount;
    }

    public String getPrecondition () {
        return precondition;
    }

    public void setPrecondition (String precondition) {
        this.precondition = precondition;
    }

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

    public String getAmount () {
        return amount;
    }

    public void setAmount (String amount) {
        this.amount = amount;
    }

    public boolean isFree () {
        return free;
    }

    public void setFree (boolean free) {
        this.free = free;
    }

    public boolean isFixed () {
        return fixed;
    }

    public void setFixed (boolean fixed) {
        this.fixed = fixed;
    }

    public String getCreatedStamp () {
        return createdStamp;
    }

    public void setCreatedStamp (String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public boolean isInclude () {
        return isInclude;
    }

    public void setIsInclude (boolean isInclude) {
        this.isInclude = isInclude;
    }

    public int getOrder () {
        return order;
    }

    public void setOrder (int order) {
        this.order = order;
    }

    @Override
    public String toString () {
        return "ItemTax{" +
                "id='" + id + '\'' +
                ", taxId='" + taxId + '\'' +
                ", name=" + name +
                ", enable=" + enable +
                ", rate='" + rate + '\'' +
                ", amount='" + amount + '\'' +
                ", free=" + free +
                ", fixed=" + fixed +
                ", createdStamp='" + createdStamp + '\'' +
                ", code='" + code + '\'' +
                ", isInclude=" + isInclude +
                ", order=" + order +
                ", effectOrderTypes='" + effectOrderTypes + '\'' +
                ", afterDiscount=" + afterDiscount +
                ", precondition='" + precondition + '\'' +
                '}';
    }
}
