package com.beihui.market.ui.bean.support;

/**
 * Created by C on 2017/5/27.
 * 第一个页面调到第二个页面进行查询的条件
 */

public class ReturnMain2 {

    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    public int selectTimeIndex;
    //记录输入的钱数
    public String inputMoney;


    public ReturnMain2(int selectTimeIndex,String inputMoney){
        this.selectTimeIndex = selectTimeIndex;
        this.inputMoney = inputMoney;
    }

}
