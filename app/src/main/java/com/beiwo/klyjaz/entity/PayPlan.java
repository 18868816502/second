package com.beiwo.klyjaz.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PayPlan implements Parcelable {

    private String channelId;
    private String channelName;
    private String projectName;
    private int repayType;
    private double capital;
    private double interest;
    private int term;
    private int termType;
    private String startDate;
    private double payableAmount;
    private double everyTermAmount;
    private double termAmount;
    private int termNum;
    private double rate;
    private String logo;
    private String remark;

    private List<RepayPlanBean> repayPlan;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public double getEveryTermAmount() {
        return everyTermAmount;
    }

    public void setEveryTermAmount(double everyTermAmount) {
        this.everyTermAmount = everyTermAmount;
    }

    public double getTermAmount() {
        return termAmount;
    }

    public void setTermAmount(double termAmount) {
        this.termAmount = termAmount;
    }

    public int getTermNum() {
        return termNum;
    }

    public void setTermNum(int termNum) {
        this.termNum = termNum;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<RepayPlanBean> getRepayPlan() {
        return repayPlan;
    }

    public void setRepayPlan(List<RepayPlanBean> repayPlan) {
        this.repayPlan = repayPlan;
    }

    public static class RepayPlanBean implements Parcelable {

        private String id;
        private String recordId;
        private int termNo;
        private String termRepayDate;
        private double termPayableAmount;
        private int status;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.recordId);
            dest.writeInt(this.termNo);
            dest.writeString(this.termRepayDate);
            dest.writeDouble(this.termPayableAmount);
            dest.writeInt(this.status);
            dest.writeString(this.userId);
        }

        public RepayPlanBean() {
        }

        protected RepayPlanBean(Parcel in) {
            this.id = in.readString();
            this.recordId = in.readString();
            this.termNo = in.readInt();
            this.termRepayDate = in.readString();
            this.termPayableAmount = in.readDouble();
            this.status = in.readInt();
            this.userId = in.readString();
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

    public PayPlan() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelId);
        dest.writeString(this.channelName);
        dest.writeString(this.projectName);
        dest.writeInt(this.repayType);
        dest.writeDouble(this.capital);
        dest.writeDouble(this.interest);
        dest.writeInt(this.term);
        dest.writeInt(this.termType);
        dest.writeString(this.startDate);
        dest.writeDouble(this.payableAmount);
        dest.writeDouble(this.everyTermAmount);
        dest.writeDouble(this.termAmount);
        dest.writeInt(this.termNum);
        dest.writeDouble(this.rate);
        dest.writeString(this.logo);
        dest.writeString(this.remark);
        dest.writeTypedList(this.repayPlan);
    }

    protected PayPlan(Parcel in) {
        this.channelId = in.readString();
        this.channelName = in.readString();
        this.projectName = in.readString();
        this.repayType = in.readInt();
        this.capital = in.readDouble();
        this.interest = in.readDouble();
        this.term = in.readInt();
        this.termType = in.readInt();
        this.startDate = in.readString();
        this.payableAmount = in.readDouble();
        this.everyTermAmount = in.readDouble();
        this.termAmount = in.readDouble();
        this.termNum = in.readInt();
        this.rate = in.readDouble();
        this.logo = in.readString();
        this.remark = in.readString();
        this.repayPlan = in.createTypedArrayList(RepayPlanBean.CREATOR);
    }

    public static final Creator<PayPlan> CREATOR = new Creator<PayPlan>() {
        @Override
        public PayPlan createFromParcel(Parcel source) {
            return new PayPlan(source);
        }

        @Override
        public PayPlan[] newArray(int size) {
            return new PayPlan[size];
        }
    };
}
