package com.toughegg.teorderpo.modle.bean;

/**
 * Created by toughegg on 16/2/26.
 */
public class Language {
    private String titleLang;
    private int subLang;
    private String content;
    private boolean select;

    public Language () {
    }

    public Language (String titleLang, int subLang, String content, boolean select) {
        this.titleLang = titleLang;
        this.subLang = subLang;
        this.content = content;
        this.select = select;
    }

    public String getTitleLang () {
        return titleLang;
    }

    public void setTitleLang (String titleLang) {
        this.titleLang = titleLang;
    }

    public int getSubLang () {
        return subLang;
    }

    public void setSubLang (int subLang) {
        this.subLang = subLang;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public boolean isSelect () {
        return select;
    }

    public void setSelect (boolean select) {
        this.select = select;
    }

    @Override
    public String toString () {
        return "Language{" +
                "titleLang='" + titleLang + '\'' +
                ", subLang=" + subLang +
                ", content='" + content + '\'' +
                ", select=" + select +
                '}';
    }
}
