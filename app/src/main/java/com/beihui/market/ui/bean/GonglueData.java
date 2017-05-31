package com.beihui.market.ui.bean;


import com.beihui.market.base.BaseBean;

/**
 * Created by Administrator on 2017/1/18.
 */

public class GonglueData extends BaseBean {

    private String title;
    private String imageUrl;

    private String httpUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
}
