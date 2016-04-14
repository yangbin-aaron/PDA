package com.toughegg.teorderpo.modle.entry.dishMenu;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;

/**
 * Created by Andy on 15/12/3.
 * 加料详情
 */
public class Option implements Serializable {
    private String id;// json字段
    private String itemModifierId;
    private Name name;// json字段
    private boolean selected;
    private String price;// json字段
    private int order;// json字段
    private int shoppingCartId;//存放数据时用
    private int count;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getItemModifierId () {
        return itemModifierId;
    }

    public void setItemModifierId (String itemModifierId) {
        this.itemModifierId = itemModifierId;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString () {
        return "Option{" +
                "id='" + id + '\'' +
                ", itemModifierId='" + itemModifierId + '\'' +
                ", name=" + name +
                ", selected=" + selected +
                ", price='" + price + '\'' +
                ", order=" + order +
                ", shoppingCartId=" + shoppingCartId +
                ", count=" + count +
                '}';
    }
}
