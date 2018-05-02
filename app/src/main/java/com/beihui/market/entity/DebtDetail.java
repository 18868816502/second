package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DebtDetail implements Parcelable {

    private String id;
    private double termPayableAmount;
    private String termRepayDate;
    private double returnedAmount;
    private double stayReturnedAmount;
    private double payableAmount;
    private int repayType;
    private double capital;
    private double interest;
    private String channelId;
    private String channelName;
    private String projectName;
    private String termId;
    private int term;
    private int termType;
    private double rate;
    private String startDate;
    private String remark;
    private String logo;
    private int status;
    private int termStatus;
    private int redmineDay;
    private String firstRepayDate;

    /**
     * 距离还款日
     */
    public int returnDay;
    /**
     * 期号
     */
    public int termNum;

    private List<RepayPlanBean> repayPlan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getRepayType() {
        return repayType;
    }

    public void setRepayType(int repayType) {
        this.repayType = repayType;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
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

    public int getTermStatus() {
        return termStatus;
    }

    public void setTermStatus(int termStatus) {
        this.termStatus = termStatus;
    }

    public int getRedmineDay() {
        return redmineDay;
    }

    public void setRedmineDay(int redmineDay) {
        this.redmineDay = redmineDay;
    }

    public String getFirstRepayDate() {
        return firstRepayDate;
    }

    public void setFirstRepayDate(String firstRepayDate) {
        this.firstRepayDate = firstRepayDate;
    }

    public List<RepayPlanBean> getRepayPlan() {
        return repayPlan;
    }

    public void setRepayPlan(List<RepayPlanBean> repayPlan) {
        this.repayPlan = repayPlan;
    }

    public static class RepayPlanBean implements Parcelable {
        private String id;
        private int termNo;
        private String termRepayDate;
        private double termPayableAmount;
        private int status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeInt(this.termNo);
            dest.writeString(this.termRepayDate);
            dest.writeDouble(this.termPayableAmount);
            dest.writeInt(this.status);
        }

        public RepayPlanBean() {
        }

        protected RepayPlanBean(Parcel in) {
            this.id = in.readString();
            this.termNo = in.readInt();
            this.termRepayDate = in.readString();
            this.termPayableAmount = in.readDouble();
            this.status = in.readInt();
        }

        public static final Parcelable.Creator<RepayPlanBean> CREATOR = new Parcelable.Creator<RepayPlanBean>() {
            @Override
            public RepayPlanBean createFromParcel(Parcel source) {
                return new RepayPlanBean(source);
            }

            @Override
            public RepayPlanBean[] newArray(int size) {
                return new RepayPlanBean[size];
            }
        };
    }

    public DebtDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.termPayableAmount);
        dest.writeString(this.termRepayDate);
        dest.writeDouble(this.returnedAmount);
        dest.writeDouble(this.stayReturnedAmount);
        dest.writeDouble(this.payableAmount);
        dest.writeInt(this.repayType);
        dest.writeDouble(this.capital);
        dest.writeDouble(this.interest);
        dest.writeString(this.channelId);
        dest.writeString(this.channelName);
        dest.writeString(this.projectName);
        dest.writeString(this.termId);
        dest.writeInt(this.term);
        dest.writeInt(this.termType);
        dest.writeDouble(this.rate);
        dest.writeString(this.startDate);
        dest.writeString(this.remark);
        dest.writeString(this.logo);
        dest.writeInt(this.status);
        dest.writeInt(this.termStatus);
        dest.writeInt(this.redmineDay);
        dest.writeString(this.firstRepayDate);
        dest.writeTypedList(this.repayPlan);
    }

    protected DebtDetail(Parcel in) {
        this.id = in.readString();
        this.termPayableAmount = in.readDouble();
        this.termRepayDate = in.readString();
        this.returnedAmount = in.readDouble();
        this.stayReturnedAmount = in.readDouble();
        this.payableAmount = in.readDouble();
        this.repayType = in.readInt();
        this.capital = in.readDouble();
        this.interest = in.readDouble();
        this.channelId = in.readString();
        this.channelName = in.readString();
        this.projectName = in.readString();
        this.termId = in.readString();
        this.term = in.readInt();
        this.termType = in.readInt();
        this.rate = in.readDouble();
        this.startDate = in.readString();
        this.remark = in.readString();
        this.logo = in.readString();
        this.status = in.readInt();
        this.termStatus = in.readInt();
        this.redmineDay = in.readInt();
        this.firstRepayDate = in.readString();
        this.repayPlan = in.createTypedArrayList(RepayPlanBean.CREATOR);
    }

    public static final Creator<DebtDetail> CREATOR = new Creator<DebtDetail>() {
        @Override
        public DebtDetail createFromParcel(Parcel source) {
            return new DebtDetail(source);
        }

        @Override
        public DebtDetail[] newArray(int size) {
            return new DebtDetail[size];
        }
    };
}
