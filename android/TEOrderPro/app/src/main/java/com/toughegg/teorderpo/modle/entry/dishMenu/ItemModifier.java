package com.toughegg.teorderpo.modle.entry.dishMenu;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/3.
 * 菜品加料分类
 */
public class ItemModifier implements Serializable {
    private String id;
    private Name name;
    private boolean isSingle;
    private boolean isOptional;
    private boolean onlyForRest;
    private List<Option> option;

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

    public boolean isSingle() {
        return isSingle;
    }

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setIsOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public boolean isOnlyForRest() {
        return onlyForRest;
    }

    public void setOnlyForRest(boolean onlyForRest) {
        this.onlyForRest = onlyForRest;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

    @Override
    public String toString () {
        return "ItemModifier{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", isSingle=" + isSingle +
                ", isOptional=" + isOptional +
                ", onlyForRest=" + onlyForRest +
                ", option=" + option +
                '}';
    }
}
