package com.beiwo.qnejqaz.entity;

import java.io.Serializable;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/19
 */

public class HomeData implements Serializable {
    private double totalAmount;
    private List<Bill> item;
    private String xmonth;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Bill> getItem() {
        return item;
    }

    public void setItem(List<Bill> item) {
        this.item = item;
    }

    public String getXmonth() {
        return xmonth;
    }

    public void setXmonth(String xmonth) {
        this.xmonth = xmonth;
    }
}
