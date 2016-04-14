package com.toughegg.teorderpo.modle.entry.uploadOrder;

import com.toughegg.teorderpo.modle.entry.Name;
import com.toughegg.teorderpo.modle.entry.dishMenu.ItemTax;
import com.toughegg.teorderpo.modle.entry.ordernetdefail.OrderNetResultItemModifier;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/3.
 */
public class Item implements Serializable {
    private int action;
    private String addTaxAmount;
    private String basePrice;
    private String categoryId;
    private String code;
    private int count;
    private String discountAmount;
    private String dishAction;
    private int groupNumber;
    private boolean isDeleted;
    private String itemDiscount;
    private String itemPrice;
    private String menuItemId;
    private String menuPrice;
    private String modifierAmount;
    private String orderItemId;
    private String priceAdjustment;
    private String remark;
    private boolean allowCustomDiscount;
    private Name name;
    private List<ItemTax> itemTax;
    private List<String> modifierList;
    private List<OrderNetResultItemModifier> modifier;
    private boolean setNeedsPrint;


    public int getAction () {
        return action;
    }

    public void setAction (int action) {
        this.action = action;
    }

    public String getAddTaxAmount () {
        return addTaxAmount;
    }

    public void setAddTaxAmount (String addTaxAmount) {
        this.addTaxAmount = addTaxAmount;
    }

    public String getBasePrice () {
        return basePrice;
    }

    public void setBasePrice (String basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategoryId () {
        return categoryId;
    }

    public void setCategoryId (String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public int getCount () {
        return count;
    }

    public void setCount (int count) {
        this.count = count;
    }

    public String getDiscountAmount () {
        return discountAmount;
    }

    public void setDiscountAmount (String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDishAction () {
        return dishAction;
    }

    public void setDishAction (String dishAction) {
        this.dishAction = dishAction;
    }

    public int getGroupNumber () {
        return groupNumber;
    }

    public void setGroupNumber (int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public boolean getIsDeleted () {
        return isDeleted;
    }

    public void setIsDeleted (boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getItemDiscount () {
        return itemDiscount;
    }

    public void setItemDiscount (String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getItemPrice () {
        return itemPrice;
    }

    public void setItemPrice (String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getMenuItemId () {
        return menuItemId;
    }

    public void setMenuItemId (String menuItemId) {
        this.menuItemId = menuItemId;
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

    public String getOrderItemId () {
        return orderItemId;
    }

    public void setOrderItemId (String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getPriceAdjustment () {
        return priceAdjustment;
    }

    public void setPriceAdjustment (String priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public boolean isAllowCustomDiscount () {
        return allowCustomDiscount;
    }

    public void setAllowCustomDiscount (boolean allowCustomDiscount) {
        this.allowCustomDiscount = allowCustomDiscount;
    }

    public Name getName () {
        return name;
    }

    public void setName (Name name) {
        this.name = name;
    }

    public List<ItemTax> getItemTax () {
        return itemTax;
    }

    public void setItemTax (List<ItemTax> itemTax) {
        this.itemTax = itemTax;
    }

    public List<String> getModifierList () {
        return modifierList;
    }

    public void setModifierList (List<String> modifierList) {
        this.modifierList = modifierList;
    }

    public List<OrderNetResultItemModifier> getModifier () {
        return modifier;
    }

    public void setModifier (List<OrderNetResultItemModifier> modifier) {
        this.modifier = modifier;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isSetNeedsPrint() {
        return setNeedsPrint;
    }

    public void setSetNeedsPrint(boolean setNeedsPrint) {
        this.setNeedsPrint = setNeedsPrint;
    }

    @Override
    public String toString() {
        return "Item{" +
                "action=" + action +
                ", addTaxAmount='" + addTaxAmount + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", code='" + code + '\'' +
                ", count=" + count +
                ", discountAmount='" + discountAmount + '\'' +
                ", dishAction='" + dishAction + '\'' +
                ", groupNumber=" + groupNumber +
                ", isDeleted=" + isDeleted +
                ", itemDiscount='" + itemDiscount + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", menuItemId='" + menuItemId + '\'' +
                ", menuPrice='" + menuPrice + '\'' +
                ", modifierAmount='" + modifierAmount + '\'' +
                ", orderItemId='" + orderItemId + '\'' +
                ", priceAdjustment='" + priceAdjustment + '\'' +
                ", remark='" + remark + '\'' +
                ", allowCustomDiscount=" + allowCustomDiscount +
                ", name=" + name +
                ", itemTax=" + itemTax +
                ", modifierList=" + modifierList +
                ", modifier=" + modifier +
                '}';
    }
}
