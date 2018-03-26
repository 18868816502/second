package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class CreditCardDebtDetail implements Parcelable {

    private String id;
    private String userId;
    private int bankId;
    private String cardNums;
    private String cardUserName;
    private int gender;
    private String fullCardNo;
    private String email;
    private int cardType;
    private int creditLimit;
    private int cashLimit;
    private int billDay;
    private int dueDay;
    private int cardSource;
    private int remind;
    private String lastCollectionDate;
    private int status;
    private ShowBillBean showBill;
    private int maxFreeInterestDay;

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

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getCardNums() {
        return cardNums;
    }

    public void setCardNums(String cardNums) {
        this.cardNums = cardNums;
    }

    public String getCardUserName() {
        return cardUserName;
    }

    public void setCardUserName(String cardUserName) {
        this.cardUserName = cardUserName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFullCardNo() {
        return fullCardNo;
    }

    public void setFullCardNo(String fullCardNo) {
        this.fullCardNo = fullCardNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(int cashLimit) {
        this.cashLimit = cashLimit;
    }

    public int getBillDay() {
        return billDay;
    }

    public void setBillDay(int billDay) {
        this.billDay = billDay;
    }

    public int getDueDay() {
        return dueDay;
    }

    public void setDueDay(int dueDay) {
        this.dueDay = dueDay;
    }

    public int getCardSource() {
        return cardSource;
    }

    public void setCardSource(int cardSource) {
        this.cardSource = cardSource;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public String getLastCollectionDate() {
        return lastCollectionDate;
    }

    public void setLastCollectionDate(String lastCollectionDate) {
        this.lastCollectionDate = lastCollectionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ShowBillBean getShowBill() {
        return showBill;
    }

    public void setShowBill(ShowBillBean showBill) {
        this.showBill = showBill;
    }

    public int getMaxFreeInterestDay() {
        return maxFreeInterestDay;
    }

    public void setMaxFreeInterestDay(int maxFreeInterestDay) {
        this.maxFreeInterestDay = maxFreeInterestDay;
    }


    public static class ShowBillBean implements Parcelable {

        private String id;
        private String userId;
        private String cardId;
        private String billDate;
        private String billMonth;
        private String paymentDueDate;
        private int currencyType;
        private double lastBalance;
        private double lastPayment;
        private double interest;
        private double newBalance;
        private double minPayment;
        private int billSource;
        private int status;
        private String outBillDay;
        private String returnDay;

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

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getBillMonth() {
            return billMonth;
        }

        public void setBillMonth(String billMonth) {
            this.billMonth = billMonth;
        }

        public String getPaymentDueDate() {
            return paymentDueDate;
        }

        public void setPaymentDueDate(String paymentDueDate) {
            this.paymentDueDate = paymentDueDate;
        }

        public int getCurrencyType() {
            return currencyType;
        }

        public void setCurrencyType(int currencyType) {
            this.currencyType = currencyType;
        }

        public double getLastBalance() {
            return lastBalance;
        }

        public void setLastBalance(double lastBalance) {
            this.lastBalance = lastBalance;
        }

        public double getLastPayment() {
            return lastPayment;
        }

        public void setLastPayment(double lastPayment) {
            this.lastPayment = lastPayment;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public double getNewBalance() {
            return newBalance;
        }

        public void setNewBalance(double newBalance) {
            this.newBalance = newBalance;
        }

        public double getMinPayment() {
            return minPayment;
        }

        public void setMinPayment(double minPayment) {
            this.minPayment = minPayment;
        }

        public int getBillSource() {
            return billSource;
        }

        public void setBillSource(int billSource) {
            this.billSource = billSource;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOutBillDay() {
            return outBillDay;
        }

        public void setOutBillDay(String outBillDay) {
            this.outBillDay = outBillDay;
        }

        public String getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(String returnDay) {
            this.returnDay = returnDay;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.userId);
            dest.writeString(this.cardId);
            dest.writeString(this.billDate);
            dest.writeString(this.billMonth);
            dest.writeString(this.paymentDueDate);
            dest.writeInt(this.currencyType);
            dest.writeDouble(this.lastBalance);
            dest.writeDouble(this.lastPayment);
            dest.writeDouble(this.interest);
            dest.writeDouble(this.newBalance);
            dest.writeDouble(this.minPayment);
            dest.writeInt(this.billSource);
            dest.writeInt(this.status);
            dest.writeString(this.outBillDay);
            dest.writeString(this.returnDay);
        }

        public ShowBillBean() {
        }

        protected ShowBillBean(Parcel in) {
            this.id = in.readString();
            this.userId = in.readString();
            this.cardId = in.readString();
            this.billDate = in.readString();
            this.billMonth = in.readString();
            this.paymentDueDate = in.readString();
            this.currencyType = in.readInt();
            this.lastBalance = in.readDouble();
            this.lastPayment = in.readDouble();
            this.interest = in.readDouble();
            this.newBalance = in.readDouble();
            this.minPayment = in.readDouble();
            this.billSource = in.readInt();
            this.status = in.readInt();
            this.outBillDay = in.readString();
            this.returnDay = in.readString();
        }

        public static final Parcelable.Creator<ShowBillBean> CREATOR = new Parcelable.Creator<ShowBillBean>() {
            @Override
            public ShowBillBean createFromParcel(Parcel source) {
                return new ShowBillBean(source);
            }

            @Override
            public ShowBillBean[] newArray(int size) {
                return new ShowBillBean[size];
            }
        };
    }

    public CreditCardDebtDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeInt(this.bankId);
        dest.writeString(this.cardNums);
        dest.writeString(this.cardUserName);
        dest.writeInt(this.gender);
        dest.writeString(this.fullCardNo);
        dest.writeString(this.email);
        dest.writeInt(this.cardType);
        dest.writeInt(this.creditLimit);
        dest.writeInt(this.cashLimit);
        dest.writeInt(this.billDay);
        dest.writeInt(this.dueDay);
        dest.writeInt(this.cardSource);
        dest.writeInt(this.remind);
        dest.writeString(this.lastCollectionDate);
        dest.writeInt(this.status);
        dest.writeParcelable(this.showBill, flags);
        dest.writeInt(this.maxFreeInterestDay);
    }

    protected CreditCardDebtDetail(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.bankId = in.readInt();
        this.cardNums = in.readString();
        this.cardUserName = in.readString();
        this.gender = in.readInt();
        this.fullCardNo = in.readString();
        this.email = in.readString();
        this.cardType = in.readInt();
        this.creditLimit = in.readInt();
        this.cashLimit = in.readInt();
        this.billDay = in.readInt();
        this.dueDay = in.readInt();
        this.cardSource = in.readInt();
        this.remind = in.readInt();
        this.lastCollectionDate = in.readString();
        this.status = in.readInt();
        this.showBill = in.readParcelable(ShowBillBean.class.getClassLoader());
        this.maxFreeInterestDay = in.readInt();
    }

    public static final Creator<CreditCardDebtDetail> CREATOR = new Creator<CreditCardDebtDetail>() {
        @Override
        public CreditCardDebtDetail createFromParcel(Parcel source) {
            return new CreditCardDebtDetail(source);
        }

        @Override
        public CreditCardDebtDetail[] newArray(int size) {
            return new CreditCardDebtDetail[size];
        }
    };
}
