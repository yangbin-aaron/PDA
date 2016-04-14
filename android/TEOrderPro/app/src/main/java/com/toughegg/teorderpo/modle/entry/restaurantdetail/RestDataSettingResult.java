package com.toughegg.teorderpo.modle.entry.restaurantdetail;

/**
 * Created by toughegg on 16/1/28.
 */
public class RestDataSettingResult {
    private String calculatePolicy;
    private String openingTime;
    private String closingTime;

    public String getCalculatePolicy () {
        return calculatePolicy;
    }

    public void setCalculatePolicy (String calculatePolicy) {
        this.calculatePolicy = calculatePolicy;
    }

    public String getOpeningTime () {
        return openingTime;
    }

    public void setOpeningTime (String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime () {
        return closingTime;
    }

    public void setClosingTime (String closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public String toString () {
        return "RestDataSettingResult{" +
                "calculatePolicy='" + calculatePolicy + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                '}';
    }
}
