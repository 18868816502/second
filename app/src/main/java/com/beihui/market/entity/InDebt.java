package com.beihui.market.entity;


public class InDebt {

    private String id;
    private String logo;
    private String channelName;
    private String projectName;
    private int returnedTerm;
    private int term;
    private int termNum;
    private double termPayableAmount;
    private String termRepayDate;
    private String termId;
    private int returnDay;
    private int status;
    private int repayType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getReturnedTerm() {
        return returnedTerm;
    }

    public void setReturnedTerm(int returnedTerm) {
        this.returnedTerm = returnedTerm;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getTermNum() {
        return termNum;
    }

    public void setTermNum(int termNum) {
        this.termNum = termNum;
    }

    public double getTermPayableAmount() {
        return termPayableAmount;
    }

    public void setTermPayableAmount(double termPayableAmount) {
        this.termPayableAmount = termPayableAmount;
    }

    public String getTermRepayDate() {
        return termRepayDate;
    }

    public void setTermRepayDate(String termRepayDate) {
        this.termRepayDate = termRepayDate;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepayType() {
        return repayType;
    }

    public void setRepayType(int repayType) {
        this.repayType = repayType;
    }
}
