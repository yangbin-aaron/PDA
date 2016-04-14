package com.toughegg.teorderpo.modle.printOrder;

import java.util.List;

/**
 * Created by Andy on 16/2/22.
 */
public class PrintPriceOptionList {

    private int sortno;
    private List<String> name;
    private String price_option_id;
    private int pre_dish_count;
    private int is_internet;
    private int isSaved;
    private int dish_count;
    private int isSelected;
    private String default_price;

    public int getSortno() {
        return sortno;
    }

    public void setSortno(int sortno) {
        this.sortno = sortno;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getPrice_option_id() {
        return price_option_id;
    }

    public void setPrice_option_id(String price_option_id) {
        this.price_option_id = price_option_id;
    }

    public int getPre_dish_count() {
        return pre_dish_count;
    }

    public void setPre_dish_count(int pre_dish_count) {
        this.pre_dish_count = pre_dish_count;
    }

    public int getIs_internet() {
        return is_internet;
    }

    public void setIs_internet(int is_internet) {
        this.is_internet = is_internet;
    }

    public int getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(int isSaved) {
        this.isSaved = isSaved;
    }

    public int getDish_count() {
        return dish_count;
    }

    public void setDish_count(int dish_count) {
        this.dish_count = dish_count;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getDefault_price() {
        return default_price;
    }

    public void setDefault_price(String default_price) {
        this.default_price = default_price;
    }
}
