package com.toughegg.teorderpo.modle.entry.dishMenu;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/1.
 * 菜品分类
 */
public class DishCategory implements Serializable {
    private String id;
    private Name name;
    private int order;
    private boolean onlyForRest;//是否只在商户端显示(手机端不显示)
    private List<DishItems> dishItemsList;

    public boolean isOnlyForRest () {
        return onlyForRest;
    }

    public void setOnlyForRest (boolean onlyForRest) {
        this.onlyForRest = onlyForRest;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public List<DishItems> getDishItemsList() {
        return dishItemsList;
    }

    public void setDishItemsList(List<DishItems> dishItemsList) {
        this.dishItemsList = dishItemsList;
    }

    @Override
    public String toString () {
        return "DishCategory{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", order=" + order +
                ", dishItemsList=" + dishItemsList +
                '}';
    }
}
