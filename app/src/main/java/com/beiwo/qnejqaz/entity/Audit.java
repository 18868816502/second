package com.beiwo.qnejqaz.entity;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/8
 */

public class Audit {
    public int audit;//是否审核中 1-审核中, 2-审核通过
    public int appNeedLogin;//启动APP时是否需要强制登陆 0：否 1：是
    public int productNeedLogin;//点击产品时是否需要强制登陆 0：否 1：是
}