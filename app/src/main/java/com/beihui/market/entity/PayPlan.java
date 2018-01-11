package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PayPlan implements Parcelable {

    private String channelId;
    private String channelName;
    private String projectName;
    private String repayType;
    private String capital;
    private String interest;
    private String term;
    private String termType;
    private String startDate;
    private String payableAmount;
    private String everyTermAmount;
    private String termAmount;
    private String termNum;
    private String rate;
    private String logo;
    private String remark;

    private List<RepayPlanBean> repayPlan;

    public List<RepayPlanBean> getRepayPlan() {
        return repayPlan;
    }

    public void setRepayPlan(List<RepayPlanBean> repayPlan) {
        this.repayPlan = repayPlan;
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

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getEveryTermAmount() {
        return everyTermAmount;
    }

    public void setEveryTermAmount(String everyTermAmount) {
        this.everyTermAmount = everyTermAmount;
    }

    public String getTermAmount() {
        return termAmount;
    }

    public void setTermAmount(String termAmount) {
        this.termAmount = termAmount;
    }

    public String getTermNum() {
        return termNum;
    }

    public void setTermNum(String termNum) {
        this.termNum = termNum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
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
        dest.writeString(this.repayType);
        dest.writeString(this.capital);
        dest.writeString(this.interest);
        dest.writeString(this.term);
        dest.writeString(this.termType);
        dest.writeString(this.startDate);
        dest.writeString(this.payableAmount);
        dest.writeString(this.everyTermAmount);
        dest.writeString(this.termAmount);
        dest.writeString(this.termNum);
        dest.writeString(this.rate);
        dest.writeString(this.logo);
        dest.writeString(this.remark);
        dest.writeTypedList(this.repayPlan);
    }

    protected PayPlan(Parcel in) {
        this.channelId = in.readString();
        this.channelName = in.readString();
        this.projectName = in.readString();
        this.repayType = in.readString();
        this.capital = in.readString();
        this.interest = in.readString();
        this.term = in.readString();
        this.termType = in.readString();
        this.startDate = in.readString();
        this.payableAmount = in.readString();
        this.everyTermAmount = in.readString();
        this.termAmount = in.readString();
        this.termNum = in.readString();
        this.rate = in.readString();
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
