package com.beiwo.qnejqaz.entity;

import java.io.Serializable;
import java.util.List;

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

public class DetailList implements Serializable {

    /**
     * total : 1
     * rows : [{"billId":"00a8927865e548b59c33a2e2a14e4b7b","termNo":1,"termPayableAmount":1525,"termRepayDate":"2018-07-17 00:00:00","status":1,"returnDay":1,"presentFlag":true}]
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable {
        /**
         * billId : 00a8927865e548b59c33a2e2a14e4b7b
         * termNo : 1
         * termPayableAmount : 1525
         * termRepayDate : 2018-07-17 00:00:00
         * status : 1
         * returnDay : 1
         * presentFlag : true
         */

        private String billId;
        private int termNo;
        private double termPayableAmount;
        private String termRepayDate;
        private int status;
        private int returnDay;
        private boolean presentFlag;
        private String id;
        private String userId;
        private String recordId;

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public int getTermNo() {
            return termNo;
        }

        public void setTermNo(int termNo) {
            this.termNo = termNo;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }

        public boolean isPresentFlag() {
            return presentFlag;
        }

        public void setPresentFlag(boolean presentFlag) {
            this.presentFlag = presentFlag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }
    }
}