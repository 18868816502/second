package com.beiwo.qnejqaz.entity;

import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/17
 */

public class PurseBalance {
    private double balance;
    private List<Amount> amountList;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Amount> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<Amount> amountList) {
        this.amountList = amountList;
    }

    public static class Amount {
        private double amount;
        private boolean isSelect;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
