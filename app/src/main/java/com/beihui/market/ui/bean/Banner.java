package com.beihui.market.ui.bean;


import com.beihui.market.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class Banner extends BaseBean {
    private List<BannerData> data;

    public List<BannerData> getData() {
        return data;
    }

    public void setData(List<BannerData> data) {
        this.data = data;
    }
}
