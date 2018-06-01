package com.beihui.market.entity;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/1.
 * 查询网贷账单的还款记录
 */

public class DebeDetailRecord implements Serializable {

    public String discription;
    //交易时间 ."2018-06-01 00:00:00"
    public String transDate;
    //还款金额
    public Double amount;
}
