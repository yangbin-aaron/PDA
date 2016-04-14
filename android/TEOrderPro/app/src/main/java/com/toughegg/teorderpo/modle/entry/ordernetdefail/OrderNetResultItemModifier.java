package com.toughegg.teorderpo.modle.entry.ordernetdefail;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;

/**
 * 加料
 * Created by toughegg on 15/12/2.
 */
public class OrderNetResultItemModifier implements Serializable {
    private int id;//来自AWS的id
    private String modifierId;
    private String modifierOptionId;
    private int count;
    private Name name;
    private String unitPrice;
    private String createdStamp;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getCreatedStamp () {
        return createdStamp;
    }

    public void setCreatedStamp (String createdStamp) {
        this.createdStamp = createdStamp;
    }

    public int getCount () {
        return count;
    }

    public void setCount (int count) {
        this.count = count;
    }

    public String getModifierId () {
        return modifierId;
    }

    public void setModifierId (String modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierOptionId () {
        return modifierOptionId;
    }

    public void setModifierOptionId (String modifierOptionId) {
        this.modifierOptionId = modifierOptionId;
    }

    public Name getName () {
        return name;
    }

    public void setName (Name name) {
        this.name = name;
    }

    public String getUnitPrice () {
        return unitPrice;
    }

    public void setUnitPrice (String unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString () {
        return "OrderNetResultItemModifier{" +
                "id=" + id +
                ", modifierId='" + modifierId + '\'' +
                ", modifierOptionId='" + modifierOptionId + '\'' +
                ", count=" + count +
                ", name=" + name +
                ", unitPrice='" + unitPrice + '\'' +
                ", createdStamp='" + createdStamp + '\'' +
                '}';
    }
}
