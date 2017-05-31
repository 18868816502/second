package com.beihui.market.ui.bean;

import com.beihui.market.base.BaseBean;

import java.util.List;

/**
 * Created by C on 2017/5/27.
 */

public class Toutiao extends BaseBean {

    private List<ToutiaoData> data;

    public List<ToutiaoData> getData() {
        return data;
    }

    public void setData(List<ToutiaoData> data) {
        this.data = data;
    }
}
