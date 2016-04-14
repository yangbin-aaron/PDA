package com.toughegg.teorderpo.modle.entry.dishMenu;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/12/1.
 * 菜品详情
 */
public class DishItems implements Serializable {
    private String id;
    private List<String> categoryId;// 菜品所属分类
    private Name name;
    private String code;
    private String price;
    private boolean isRec;
    private boolean isSet;
    private boolean isHot;
    private int hotFactor;
    private boolean display;
    private boolean deleted;
    private boolean allowCustomDiscount;
    private boolean canTakeAway;
    private int order;
    private String fromDate;
    private String toDate;
    private Description description;
    private List<String> modifier;
    private String img;
    private int copies;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isRec() {
        return isRec;
    }

    public void setIsRec(boolean isRec) {
        this.isRec = isRec;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setIsSet(boolean isSet) {
        this.isSet = isSet;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public int getHotFactor() {
        return hotFactor;
    }

    public void setHotFactor(int hotFactor) {
        this.hotFactor = hotFactor;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isAllowCustomDiscount() {
        return allowCustomDiscount;
    }

    public void setAllowCustomDiscount(boolean allowCustomDiscount) {
        this.allowCustomDiscount = allowCustomDiscount;
    }

    public boolean isCanTakeAway() {
        return canTakeAway;
    }

    public void setCanTakeAway(boolean canTakeAway) {
        this.canTakeAway = canTakeAway;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<String> getModifier() {
        return modifier;
    }

    public void setModifier(List<String> modifier) {
        this.modifier = modifier;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }


    public static class Description implements Serializable {
        private String zh_CN;
        private String en_US;

        public String getZh_CN() {
            return zh_CN;
        }

        public void setZh_CN(String zh_CN) {
            this.zh_CN = zh_CN;
        }

        public String getEn_US() {
            return en_US;
        }

        public void setEn_US(String en_US) {
            this.en_US = en_US;
        }
    }

    @Override
    public String toString() {
        return "DishItems{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", name=" + name +
                ", code='" + code + '\'' +
                ", price='" + price + '\'' +
                ", isRec=" + isRec +
                ", isSet=" + isSet +
                ", isHot=" + isHot +
                ", hotFactor=" + hotFactor +
                ", display=" + display +
                ", deleted=" + deleted +
                ", allowCustomDiscount=" + allowCustomDiscount +
                ", canTakeAway=" + canTakeAway +
                ", order=" + order +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", description=" + description +
                ", modifier=" + modifier +
                ", img='" + img + '\'' +
                ", copies=" + copies +
                '}';
    }
}
