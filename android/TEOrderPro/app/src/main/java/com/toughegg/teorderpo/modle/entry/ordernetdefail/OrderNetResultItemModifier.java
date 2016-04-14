package com.toughegg.teorderpo.modle.entry.ordernetdefail;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;

/**
 * 加料
 * Created by toughegg on 15/12/2.
 */
public class OrderNetResultItemModifier implements Serializable {

//    "count": 1,
//    "modifierId": "56497f3172c68923b40333c0",
//    "modifierOptionId": "5649818272c68923b40339af",
//    "name": {
//    "en": "Add Mutton Tripe",
//    "zh": "\u52a0\u7f8a\u6742"
//    },
//    "unitPrice": "3.0"

    private int count ;
    private String modifierId;
    private String modifierOptionId;
    private Name name;
    private String unitPrice;

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
                "count=" + count +
                ", modifierId='" + modifierId + '\'' +
                ", modifierOptionId='" + modifierOptionId + '\'' +
                ", name=" + name +
                ", unitPrice='" + unitPrice + '\'' +
                '}';
    }
}
