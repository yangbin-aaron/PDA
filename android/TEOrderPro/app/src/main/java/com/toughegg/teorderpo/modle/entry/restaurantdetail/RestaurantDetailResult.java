package com.toughegg.teorderpo.modle.entry.restaurantdetail;

/**
 * Created by toughegg on 16/1/28.
 */
public class RestaurantDetailResult {
    private int status;
    private String message;
    private RestaurantDetailDataResult data;

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public RestaurantDetailDataResult getData () {
        return data;
    }

    public void setData (RestaurantDetailDataResult data) {
        this.data = data;
    }

    @Override
    public String toString () {
        return "RestaurantDetailResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
