package com.beihui.market.entity;


public class Debt {

    private String logo;
    private String channelName;
    private String projectName;
    private double capital;
    private double interest;
    private double rate;
    private String startDate;
    private int status;
    private int term;
    private int termNum;
    private double termPayableAmount;
    private int returnDay;

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

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }

    public boolean isPaid() {
        return status == 2;
    }
}
