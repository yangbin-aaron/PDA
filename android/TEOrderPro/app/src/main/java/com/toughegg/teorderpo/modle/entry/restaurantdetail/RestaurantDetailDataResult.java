package com.toughegg.teorderpo.modle.entry.restaurantdetail;

import com.toughegg.teorderpo.modle.entry.Name;

import java.io.Serializable;

/**
 * Created by toughegg on 16/1/28.
 */
public class RestaurantDetailDataResult {
    private RestDataSettingResult setting;
    private Name name;
    private Address address;

    public RestDataSettingResult getSetting () {
        return setting;
    }

    public void setSetting (RestDataSettingResult setting) {
        this.setting = setting;
    }

    public Name getName () {
        return name;
    }

    public void setName (Name name) {
        this.name = name;
    }

    public Address getAddress () {
        return address;
    }

    public void setAddress (Address address) {
        this.address = address;
    }

    @Override
    public String toString () {
        return "RestaurantDetailDataResult{" +
                "setting=" + setting +
                ", name=" + name +
                ", address=" + address +
                '}';
    }

    public static class Address implements Serializable {
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
}
