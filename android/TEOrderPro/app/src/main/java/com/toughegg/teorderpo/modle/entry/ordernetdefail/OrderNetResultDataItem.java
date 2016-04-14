package com.toughegg.teorderpo.modle.entry.ordernetdefail;

import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;

import java.io.Serializable;
import java.util.List;

/**
 * Created by toughegg on 15/12/2.
 * 网络菜品
 */
public class OrderNetResultDataItem implements Serializable {

    private int id;//来自AWS的id
    private String orderItemId;//由LMS生成,从1开始,每单独立,每菜唯一
    private int action;//仅order-update时使用，1:添加; 2:更新; 3:删除
    private String menuItemId;//菜品id
    private boolean setNeedsPrint;//是否需要打印
    private String code;//菜品代码
    private boolean isHot;// 是否热销
    private String menuPrice;//菜单上的标价
    private String modifierAmount;//附加项目价格总和(加料总和)
    private String priceAdjustment;// 对菜品价格的调整(加价)
    private String basePrice;// menuPrice + modifierAmount + priceAdjustment
    private String itemPrice;// 折后价格 basePrice - discountAmount
    private Name name;// 菜品名字
    private String addTaxAmount;// 加税总额
    private String discountAmount; //折扣优惠 basePrice * itemDiscount
    private String itemDiscount; //单个菜品折扣率
    private boolean isDeleted;//是否删除（要删除菜品只能改这里）
    private int groupNumber;//点菜分组
    private int count;//数量
    private String remark;//备注
    private String updatedStamp;
    private String createdStamp;
    private boolean allowCustomDiscount;// 允许 菜品打折
    private List<OrderNetResultItemModifier> modifier;//每个菜品包含一个附加项目数组
    private List<ItemTax> itemTax;//每个菜品包含一个税数组

    private String categoryId;// 本地用
    private List<String> modifierList;// 本地用

    public List<String> getModifierList () {
        return modifierList;
    }

    public void setModifierList (List<String> modifierList) {
        this.modifierList = modifierList;
    }

    public String getCategoryId () {
        return categoryId;
    }

    public void setCategoryId (String categoryId) {
        this.categoryId = categoryId;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getOrderItemId () {
        return orderItemId;
    }

    public void setOrderItemId (String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getAction () {
        return action;
    }

    public void setAction (int action) {
        this.action = action;
    }

    public String getMenuItemId () {
        return menuItemId;
    }

    public void setMenuItemId (String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public boolean isSetNeedsPrint () {
        return setNeedsPrint;
    }

    public void setSetNeedsPrint (boolean setNeedsPrint) {
        this.setNeedsPrint = setNeedsPrint;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public boolean isHot () {
        return isHot;
    }

    public void setIsHot (boolean isHot) {
        this.isHot = isHot;
    }

    public String getMenuPrice () {
        return menuPrice;
    }

    public void setMenuPrice (String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getModifierAmount () {
        return modifierAmount;
    }

    public void setModifierAmount (String modifierAmount) {
        this.modifierAmount = modifierAmount;
    }

    public String getPriceAdjustment () {
        return priceAdjustment;
    }

    public void setPriceAdjustment (String priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getBasePrice () {
        return basePrice;
    }

    public void setBasePrice (String basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemPrice () {
        return itemPrice;
    }

    public void setItemPrice (String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Name getName () {
        return name;
    }

    public void setName (Name name) {
        this.name = name;
    }

    public String getAddTaxAmount () {
        return addTaxAmount;
    }

    public void setAddTaxAmount (String addTaxAmount) {
        this.addTaxAmount = addTaxAmount;
    }

    public String getDiscountAmount () {
        return discountAmount;
    }

    public void setDiscountAmount (String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getItemDiscount () {
        return itemDiscount;
    }

    public void setItemDiscount (String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public boolean isDeleted () {
        return isDeleted;
    }

    public void setIsDeleted (boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getGroupNumber () {
        return groupNumber;
    }

    public void setGroupNumber (int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getCount () {
        return count;
    }

    public void setCount (int count) {
        this.count = count;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
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

    public boolean isAllowCustomDiscount () {
        return allowCustomDiscount;
    }

    public void setAllowCustomDiscount (boolean allowCustomDiscount) {
        this.allowCustomDiscount = allowCustomDiscount;
    }

    public List<OrderNetResultItemModifier> getModifier () {
        return modifier;
    }

    public void setModifier (List<OrderNetResultItemModifier> modifier) {
        this.modifier = modifier;
    }

    public List<ItemTax> getItemTax () {
        return itemTax;
    }

    public void setItemTax (List<ItemTax> itemTax) {
        this.itemTax = itemTax;
    }

    @Override
    public String toString () {
        return "OrderNetResultDataItem{" +
                "id=" + id +
                ", orderItemId='" + orderItemId + '\'' +
                ", action=" + action +
                ", menuItemId='" + menuItemId + '\'' +
                ", setNeedsPrint=" + setNeedsPrint +
                ", code='" + code + '\'' +
                ", isHot=" + isHot +
                ", menuPrice='" + menuPrice + '\'' +
                ", modifierAmount='" + modifierAmount + '\'' +
                ", priceAdjustment='" + priceAdjustment + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", name=" + name +
                ", addTaxAmount='" + addTaxAmount + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", itemDiscount='" + itemDiscount + '\'' +
                ", isDeleted=" + isDeleted +
                ", groupNumber=" + groupNumber +
                ", count=" + count +
                ", remark='" + remark + '\'' +
                ", updatedStamp='" + updatedStamp + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                ", allowCustomDiscount=" + allowCustomDiscount +
                ", modifier=" + modifier +
                ", CategoryId=" + categoryId +
                ", itemTax=" + itemTax +
                '}';
    }
}