package com.beihui.market.entity;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/20.
 * 分组贷超产品
 */

public class GroupProductBean implements Serializable {


    /**
     * explains : 1231
     * id : 2131
     * productName : 31231231
     * logoUrl : 31231231
     */

    public String explains;
    public String id;
    public String productName;
    public String logoUrl;

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
