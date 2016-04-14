package com.toughegg.teorderpo.modle.bean;

/**
 * Created by Andy on 15/8/10.
 * 餐厅详情
 */
public class RestaurantInfo {
    private int id;
    private String restaurantId;//餐厅ID；
    private String gstTax;// 国家税
    private String serviceTax;// 服务税

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getRestaurantId () {
        return restaurantId;
    }

    public void setRestaurantId (String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getGstTax () {
        return gstTax;
    }

    public void setGstTax (String gstTax) {
        this.gstTax = gstTax;
    }

    public String getServiceTax () {
        return serviceTax;
    }

    public void setServiceTax (String serviceTax) {
        this.serviceTax = serviceTax;
    }

    @Override
    public String toString () {
        return "RestaurantInfo{" +
                "id=" + id +
                ", restaurantId='" + restaurantId + '\'' +
                ", gstTax='" + gstTax + '\'' +
                ", serviceTax='" + serviceTax + '\'' +
                '}';
    }
}
