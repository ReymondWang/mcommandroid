package com.purplelight.mcommunity.entity;

/**
 * 用于画面首页的数据源信息，主要存储画面显示的图片／图片信息以及点击跳转显示的链接信息。
 * Created by wangyn on 16/4/10.
 */
public class WebBanner {

    private String image;

    private String url;

    private String label;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
