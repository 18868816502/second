package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/23
 */

public class DetailHead implements Serializable {

    /**
     * recordId : 00a8927865e548b59c33a2e2a14e4b7b
     * userId : c40e1a92893b43a9b7acaa7a12eba1a1
     * channelName : 辜负
     * returnedAmount : 525
     * stayReturnedAmount : 1525
     * term : 36
     * returnedTerm : 1
     * remark :
     * logo : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/liabilities_channel/1d544fabbb064c67b76e40cc3599806d.png
     * status : 1  //状态 1-未还清 2-还清
     */

    private String recordId;
    private String userId;
    private String channelName;
    private double returnedAmount;
    private double stayReturnedAmount;
    private int term;
    private int returnedTerm;
    private String remark;
    private String logo;
    private int status;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public double getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(double returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public double getStayReturnedAmount() {
        return stayReturnedAmount;
    }

    public void setStayReturnedAmount(double stayReturnedAmount) {
        this.stayReturnedAmount = stayReturnedAmount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getReturnedTerm() {
        return returnedTerm;
    }

    public void setReturnedTerm(int returnedTerm) {
        this.returnedTerm = returnedTerm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
