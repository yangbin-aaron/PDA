package com.toughegg.teorderpo.modle.entry;

import java.io.Serializable;

/**
 * Created by Andy on 15/12/3.
 */
public class Name implements Serializable {
    private String zh_CN;
    private String en_US;

    public String getZh_CN () {
        return zh_CN;
    }

    public void setZh_CN (String zh_CN) {
        this.zh_CN = zh_CN;
    }

    public String getEn_US () {
        return en_US;
    }

    public void setEn_US (String en_US) {
        this.en_US = en_US;
    }

    @Override
    public String toString () {
        return "Name{" +
                "zh_CH='" + zh_CN + '\'' +
                ", en_US='" + en_US + '\'' +
                '}';
    }
}
