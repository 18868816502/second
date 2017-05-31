package com.beihui.market.ui.bean;


import com.beihui.market.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class Gonglue extends BaseBean {
    private List<GonglueData> data;

    public List<GonglueData> getData() {
        return data;
    }

    public void setData(List<GonglueData> data) {
        this.data = data;
    }
}
