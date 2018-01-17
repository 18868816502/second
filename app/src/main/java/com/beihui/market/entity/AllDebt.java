package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AllDebt implements Parcelable {
    private int total;
    private double capital;
    private double interest;
    private double payableAmount;
    private List<Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public static class Row implements Parcelable {
        private double capital;
        private int termType;
        private String remark;
        private String userId;
        private double payableAmount;
        private int repayType;
        private double interest;
        private double rate;
        private String logo;
        private String channelName;
        private int term;
        private String id;
        private String projectName;
        private double returnedAmount;
        private double stayReturnedAmount;
        private String channelId;
        private String startDate;
        private int status;

        public double getCapital() {
            return capital;
        }

        public void setCapital(double capital) {
            this.capital = capital;
        }

        public int getTermType() {
            return termType;
        }

        public void setTermType(int termType) {
            this.termType = termType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
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

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.capital);
            dest.writeInt(this.termType);
            dest.writeString(this.remark);
            dest.writeString(this.userId);
            dest.writeDouble(this.payableAmount);
            dest.writeInt(this.repayType);
            dest.writeDouble(this.interest);
            dest.writeDouble(this.rate);
            dest.writeString(this.logo);
            dest.writeString(this.channelName);
            dest.writeInt(this.term);
            dest.writeString(this.id);
            dest.writeString(this.projectName);
            dest.writeDouble(this.returnedAmount);
            dest.writeDouble(this.stayReturnedAmount);
            dest.writeString(this.channelId);
            dest.writeString(this.startDate);
            dest.writeInt(this.status);
        }

        public Row() {
        }

        protected Row(Parcel in) {
            this.capital = in.readDouble();
            this.termType = in.readInt();
            this.remark = in.readString();
            this.userId = in.readString();
            this.payableAmount = in.readDouble();
            this.repayType = in.readInt();
            this.interest = in.readDouble();
            this.rate = in.readDouble();
            this.logo = in.readString();
            this.channelName = in.readString();
            this.term = in.readInt();
            this.id = in.readString();
            this.projectName = in.readString();
            this.returnedAmount = in.readDouble();
            this.stayReturnedAmount = in.readDouble();
            this.channelId = in.readString();
            this.startDate = in.readString();
            this.status = in.readInt();
        }

        public static final Parcelable.Creator<Row> CREATOR = new Parcelable.Creator<Row>() {
            @Override
            public Row createFromParcel(Parcel source) {
                return new Row(source);
            }

            @Override
            public Row[] newArray(int size) {
                return new Row[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeDouble(this.capital);
        dest.writeDouble(this.interest);
        dest.writeDouble(this.payableAmount);
        dest.writeTypedList(this.rows);
    }

    public AllDebt() {
    }

    protected AllDebt(Parcel in) {
        this.total = in.readInt();
        this.capital = in.readDouble();
        this.interest = in.readDouble();
        this.payableAmount = in.readDouble();
        this.rows = in.createTypedArrayList(Row.CREATOR);
    }

    public static final Parcelable.Creator<AllDebt> CREATOR = new Parcelable.Creator<AllDebt>() {
        @Override
        public AllDebt createFromParcel(Parcel source) {
            return new AllDebt(source);
        }

        @Override
        public AllDebt[] newArray(int size) {
            return new AllDebt[size];
        }
    };
}
