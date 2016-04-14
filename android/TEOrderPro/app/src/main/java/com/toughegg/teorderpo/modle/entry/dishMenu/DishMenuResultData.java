package com.toughegg.teorderpo.modle.entry.dishMenu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/1.
 * 菜单返回结果
 */
public class DishMenuResultData implements Serializable {
    private String id;
    private String restId;
    private String groupId;

    private List<DishCategory> itemCategory; // 菜品分类
    private List<DishItems> dishItems;  //菜品详情
    private List<DishItems> hotDishItems; //热销菜品
    private List<ItemTax> itemTax;    //税务
    private List<ItemModifier> itemModifier;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<DishCategory> getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(List<DishCategory> itemCategory) {
        this.itemCategory = itemCategory;
    }

    public List<DishItems> getDishItems() {
        return dishItems;
    }

    public void setDishItems(List<DishItems> dishItems) {
        this.dishItems = dishItems;
    }

    public List<ItemTax> getItemTax() {
        return itemTax;
    }

    public void setItemTax(List<ItemTax> itemTax) {
        this.itemTax = itemTax;
    }

    public List<ItemModifier> getItemModifier() {
        return itemModifier;
    }

    public void setItemModifier(List<ItemModifier> itemModifier) {
        this.itemModifier = itemModifier;
    }

    public List<DishItems> getHotDishItems() {
        return hotDishItems;
    }

    public void setHotDishItems(List<DishItems> hotDishItems) {
        this.hotDishItems = hotDishItems;
    }

    @Override
    public String toString () {
        return "DishMenuResultData{" +
                "id='" + id + '\'' +
                ", restId='" + restId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", itemCategory=" + itemCategory +
                ", dishItems=" + dishItems +
                ", hotDishItems=" + hotDishItems +
                ", itemTax=" + itemTax +
                ", itemModifier=" + itemModifier +
                '}';
    }
}
