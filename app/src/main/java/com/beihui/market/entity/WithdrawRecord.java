package com.beihui.market.entity;

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

public class WithdrawRecord {
    private int total;
    private List<Rows> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public static class Rows {
        private String tradeAccount;
        private String tradeName;
        private double tradeAmount;
        private int status;// 0- 交易失败, 1-处理中, 2-交易成功
        private String gmtCreate;
        private String reason;

        public String getTradeAccount() {
            return tradeAccount;
        }

        public void setTradeAccount(String tradeAccount) {
            this.tradeAccount = tradeAccount;
        }

        public String getTradeName() {
            return tradeName;
        }

        public void setTradeName(String tradeName) {
            this.tradeName = tradeName;
        }

        public double getTradeAmount() {
            return tradeAmount;
        }

        public void setTradeAmount(double tradeAmount) {
            this.tradeAmount = tradeAmount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}