package com.beihui.market.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/16.
 */

public class BaseBean implements Serializable {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
