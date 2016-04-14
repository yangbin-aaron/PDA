package com.toughegg.teorderpo.modle.entry;

import java.io.Serializable;

/**
 * Created by Andy on 15/12/3.
 */
public class Name implements Serializable {
    private String zh_CN;// 简体中文
    private String en_US;// 英文
    private String zh_TW;// 繁体中文

    public String getZh_TW () {
        return zh_TW;
    }

    public void setZh_TW (String zh_TW) {
        this.zh_TW = zh_TW;
    }

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
                "zh_TW='" + zh_TW + '\'' +
                ", en_US='" + en_US + '\'' +
                '}';
    }
}
