package com.beiwo.klyjaz.entity;


public class DebtAbstract {

    private double currentDebt;
    private double last7DayStayStill;

    //近30待还的金额
    private double last30DayStayStill;

    //近30待还账单数量
    public double last30DayStayStillCount;
    //还款金额
    public double repayAmount;
    //待款金额
    public double unRepayAmount;

    public double getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(double currentDebt) {
        this.currentDebt = currentDebt;
    }

    public double getLast7DayStayStill() {
        return last7DayStayStill;
    }

    public void setLast7DayStayStill(double last7DayStayStill) {
        this.last7DayStayStill = last7DayStayStill;
    }

    public double getLast30DayStayStill() {
        return last30DayStayStill;
    }

    public void setLast30DayStayStill(double last30DayStayStill) {
        this.last30DayStayStill = last30DayStayStill;
    }
}
