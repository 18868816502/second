package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

public class AccountFlowIconBean implements Serializable {
    //图标地址
    public String logo;
    //	图标名称
    public String iconName;
    //	图标类型 LCommon：通用记账 LNetLoan：网贷记账 LCustom：自定义图标
    public String type;
    // 	图标ID
    public String iconId;
    // 拼音首字母
    public String iconInitials;
    // 图标标识 getIconRemark接口入参所取的值
    public String tallyId;

    //是否为个人自定义图标 0：否 1：是
    public String isPrivate;
    // 	标签备注 用英语逗号作分隔符
    public String remark;
}