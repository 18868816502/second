package com.beihui.market.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/5
 */

public class Ticket implements Serializable {

    /**
     * userId : 000058c81c5a4894b43f9409dbf3d284
     * companyName : 杭州贝沃科技有限公司1
     * taxNo : null
     * companyAddress : null
     * telephone : null
     * openBank : null
     * bankAccount : null
     * status : 1
     * invoiceId : 2
     */

    private String userId;
    private String companyName;//公司名称
    private String taxNo;//税号
    private String companyAddress;//单位地址
    private String telephone;//电话号码
    private String openBank;//开户银行
    private String bankAccount;//银行账号
    private int status;//状态 0-无效, 1-正常
    private long invoiceId;//发票Id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }
}