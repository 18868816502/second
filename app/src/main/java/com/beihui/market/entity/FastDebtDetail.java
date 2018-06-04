package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xhb
 * @version 3.0.1
 * 快速记账详情bean
 */
public class FastDebtDetail implements Parcelable {


    /**
     * id : 4ecf27a19b7b46b7af277b227ecee8cc
     * userId : 9477aa0c6de641d7adb1eb3e3600c42a
     * projectName : 快捷记账
     * repayType : 2
     * payableAmount : 20.0
     * startDate : 2018-05-10 00:00:00
     * term : 6
     * remark :
     * status : 1
     * gmtCreate : 2018-05-10 17:08:18
     * gmtModify : 2018-05-10 17:12:08
     * remind : 1
     * detailList : [{"id":"c5ad045e4efb42feab40520619bd2deb","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":1,"termRepayDate":"2018-05-10","termPayableAmount":20,"status":1,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":0,"returnDay":0},{"id":"69b25b4a37144db190002f91035b013e","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":2,"termRepayDate":"2018-06-10","termPayableAmount":20,"status":2,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":20,"returnDay":0},{"id":"f9cd6ffbda6147ea99e2b353eaa13966","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":3,"termRepayDate":"2018-07-10","termPayableAmount":20,"status":2,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":20,"returnDay":0},{"id":"86c8caff71ed49669776bb954f3833ed","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":4,"termRepayDate":"2018-08-10","termPayableAmount":20,"status":2,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":20,"returnDay":0},{"id":"6cd600667be0472980ad86d1615bdc1a","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":5,"termRepayDate":"2018-09-10","termPayableAmount":20,"status":2,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":20,"returnDay":0},{"id":"791d5920cf004d16be3cbd17c5a13c11","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":6,"termRepayDate":"2018-10-10","termPayableAmount":20,"status":2,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":20,"returnDay":0}]
     * showBill : {"id":"c5ad045e4efb42feab40520619bd2deb","userId":"9477aa0c6de641d7adb1eb3e3600c42a","recordId":"4ecf27a19b7b46b7af277b227ecee8cc","termNo":1,"termRepayDate":"2018-05-10","termPayableAmount":20,"status":1,"gmtCreate":"2018-05-10 17:08:18","gmtModify":"2018-05-10 17:08:18","repayedAmount":0,"returnDay":0}
     * returnedTerm : 5
     * stayReturnedAmount : null
     * termRepayDate : null
     * termPayableAmount : 20.0
     * firstRepayDate : 2018-05-10 00:00:00
     */

    public String id;
    public String userId;
    public String projectName;
    public int repayType;
    public double payableAmount;
    public String startDate;
    public int term;
    public String remark;
    public int status;
    public String gmtCreate;
    public String gmtModify;
    public int remind;
    public int returnedTerm;
    //	到期应还
    public double stayReturnedAmount;
    //到期还款日
    public String termRepayDate;
    public double termPayableAmount;
    public String firstRepayDate;
    public List<DetailListBean> detailList = new ArrayList<>();
    public ShowBillBean showBill;

    protected FastDebtDetail(Parcel in) {
        id = in.readString();
        userId = in.readString();
        projectName = in.readString();
        repayType = in.readInt();
        payableAmount = in.readDouble();
        startDate = in.readString();
        term = in.readInt();
        remark = in.readString();
        status = in.readInt();
        gmtCreate = in.readString();
        gmtModify = in.readString();
        remind = in.readInt();
        returnedTerm = in.readInt();
        stayReturnedAmount = in.readDouble();
        termRepayDate = in.readString();
        termPayableAmount = in.readDouble();
        firstRepayDate = in.readString();
        detailList = in.createTypedArrayList(DetailListBean.CREATOR);
        showBill = in.readParcelable(ShowBillBean.class.getClassLoader());
    }

    public static final Creator<FastDebtDetail> CREATOR = new Creator<FastDebtDetail>() {
        @Override
        public FastDebtDetail createFromParcel(Parcel in) {
            return new FastDebtDetail(in);
        }

        @Override
        public FastDebtDetail[] newArray(int size) {
            return new FastDebtDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.projectName);
        dest.writeInt(this.repayType);
        dest.writeDouble(this.payableAmount);
        dest.writeString(this.startDate);
        dest.writeInt(this.term);
        dest.writeString(this.remark);
        dest.writeInt(this.status);
        dest.writeString(this.gmtCreate);
        dest.writeString(this.gmtModify);
        dest.writeInt(this.remind);
        dest.writeInt(this.returnedTerm);
        dest.writeDouble(this.stayReturnedAmount);
        dest.writeString(this.termRepayDate);
        dest.writeDouble(this.termPayableAmount);
        dest.writeString(this.firstRepayDate);
        dest.writeTypedList(this.detailList);
        dest.writeParcelable(this.showBill, 0);
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getRepayType() {
        return repayType;
    }

    public void setRepayType(int repayType) {
        this.repayType = repayType;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public int getReturnedTerm() {
        return returnedTerm;
    }

    public void setReturnedTerm(int returnedTerm) {
        this.returnedTerm = returnedTerm;
    }

    public double getStayReturnedAmount() {
        return stayReturnedAmount;
    }

    public void setStayReturnedAmount(double stayReturnedAmount) {
        this.stayReturnedAmount = stayReturnedAmount;
    }

    public String getTermRepayDate() {
        return termRepayDate;
    }

    public void setTermRepayDate(String termRepayDate) {
        this.termRepayDate = termRepayDate;
    }

    public double getTermPayableAmount() {
        return termPayableAmount;
    }

    public void setTermPayableAmount(double termPayableAmount) {
        this.termPayableAmount = termPayableAmount;
    }

    public String getFirstRepayDate() {
        return firstRepayDate;
    }

    public void setFirstRepayDate(String firstRepayDate) {
        this.firstRepayDate = firstRepayDate;
    }

    public List<DetailListBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailListBean> detailList) {
        this.detailList = detailList;
    }

    public ShowBillBean getShowBill() {
        return showBill;
    }

    public void setShowBill(ShowBillBean showBill) {
        this.showBill = showBill;
    }

    public static class ShowBillBean implements Parcelable{
        /**
         * id : c5ad045e4efb42feab40520619bd2deb
         * userId : 9477aa0c6de641d7adb1eb3e3600c42a
         * recordId : 4ecf27a19b7b46b7af277b227ecee8cc
         * termNo : 1
         * termRepayDate : 2018-05-10
         * termPayableAmount : 20.0
         * status : 1
         * gmtCreate : 2018-05-10 17:08:18
         * gmtModify : 2018-05-10 17:08:18
         * repayedAmount : 0.0
         * returnDay : 0
         */

        public String id;
        public String userId;
        public String recordId;
        public int termNo;
        public String termRepayDate;
        public double termPayableAmount;
        public int status;
        public String gmtCreate;
        public String gmtModify;
        public double repayedAmount;
        public int returnDay;

        protected ShowBillBean(Parcel in) {
            id = in.readString();
            userId = in.readString();
            recordId = in.readString();
            termNo = in.readInt();
            termRepayDate = in.readString();
            termPayableAmount = in.readDouble();
            status = in.readInt();
            gmtCreate = in.readString();
            gmtModify = in.readString();
            repayedAmount = in.readDouble();
            returnDay = in.readInt();
        }

        public static final Creator<ShowBillBean> CREATOR = new Creator<ShowBillBean>() {
            @Override
            public ShowBillBean createFromParcel(Parcel in) {
                return new ShowBillBean(in);
            }

            @Override
            public ShowBillBean[] newArray(int size) {
                return new ShowBillBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.userId);
            dest.writeString(this.recordId);
            dest.writeInt(this.termNo);
            dest.writeString(this.termRepayDate);
            dest.writeDouble(this.termPayableAmount);
            dest.writeInt(this.status);
            dest.writeString(this.gmtCreate);
            dest.writeString(this.gmtModify);
            dest.writeDouble(this.repayedAmount);
            dest.writeInt(this.returnDay);
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

        public int getTermNo() {
            return termNo;
        }

        public void setTermNo(int termNo) {
            this.termNo = termNo;
        }

        public String getTermRepayDate() {
            return termRepayDate;
        }

        public void setTermRepayDate(String termRepayDate) {
            this.termRepayDate = termRepayDate;
        }

        public double getTermPayableAmount() {
            return termPayableAmount;
        }

        public void setTermPayableAmount(double termPayableAmount) {
            this.termPayableAmount = termPayableAmount;
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

        public String getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(String gmtModify) {
            this.gmtModify = gmtModify;
        }

        public double getRepayedAmount() {
            return repayedAmount;
        }

        public void setRepayedAmount(double repayedAmount) {
            this.repayedAmount = repayedAmount;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }
    }

    public static class DetailListBean implements Parcelable{
        /**
         * id : c5ad045e4efb42feab40520619bd2deb
         * userId : 9477aa0c6de641d7adb1eb3e3600c42a
         * recordId : 4ecf27a19b7b46b7af277b227ecee8cc
         * termNo : 1
         * termRepayDate : 2018-05-10
         * termPayableAmount : 20.0
         * status : 1
         * gmtCreate : 2018-05-10 17:08:18
         * gmtModify : 2018-05-10 17:08:18
         * repayedAmount : 0.0
         * returnDay : 0
         */
        private String id;
        private String userId;
        private String recordId;
        private int termNo;
        private String termRepayDate;
        private double termPayableAmount;
        private int status;
        private String gmtCreate;
        private String gmtModify;
        private double repayedAmount;
        private int returnDay;

        /**
         * 是否展示还款记录
         */
        public boolean isShow = false;

        protected DetailListBean(Parcel in) {
            id = in.readString();
            userId = in.readString();
            recordId = in.readString();
            termNo = in.readInt();
            termRepayDate = in.readString();
            termPayableAmount = in.readDouble();
            status = in.readInt();
            gmtCreate = in.readString();
            gmtModify = in.readString();
            repayedAmount = in.readDouble();
            returnDay = in.readInt();
        }

        public static final Creator<DetailListBean> CREATOR = new Creator<DetailListBean>() {
            @Override
            public DetailListBean createFromParcel(Parcel in) {
                return new DetailListBean(in);
            }

            @Override
            public DetailListBean[] newArray(int size) {
                return new DetailListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.userId);
            dest.writeString(this.recordId);
            dest.writeInt(this.termNo);
            dest.writeString(this.termRepayDate);
            dest.writeDouble(this.termPayableAmount);
            dest.writeInt(this.status);
            dest.writeString(this.gmtCreate);
            dest.writeString(this.gmtModify);
            dest.writeDouble(this.repayedAmount);
            dest.writeInt(this.returnDay);
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

        public int getTermNo() {
            return termNo;
        }

        public void setTermNo(int termNo) {
            this.termNo = termNo;
        }

        public String getTermRepayDate() {
            return termRepayDate;
        }

        public void setTermRepayDate(String termRepayDate) {
            this.termRepayDate = termRepayDate;
        }

        public double getTermPayableAmount() {
            return termPayableAmount;
        }

        public void setTermPayableAmount(double termPayableAmount) {
            this.termPayableAmount = termPayableAmount;
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

        public String getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(String gmtModify) {
            this.gmtModify = gmtModify;
        }

        public double getRepayedAmount() {
            return repayedAmount;
        }

        public void setRepayedAmount(double repayedAmount) {
            this.repayedAmount = repayedAmount;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }
    }
}
