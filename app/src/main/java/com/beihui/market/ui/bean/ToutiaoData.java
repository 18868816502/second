package com.beihui.market.ui.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */

public class ToutiaoData implements Serializable {
    private String httpUrl;
    private String content;

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
