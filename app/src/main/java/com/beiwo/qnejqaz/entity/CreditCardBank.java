package com.beiwo.qnejqaz.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class CreditCardBank implements Parcelable {

    private long id;
    private int priority;
    private String bankName;
    private int manual;
    private String gmtCreate;
    private String gmtModify;
    private String logo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getManual() {
        return manual;
    }

    public void setManual(int manual) {
        this.manual = manual;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.priority);
        dest.writeString(this.bankName);
        dest.writeInt(this.manual);
        dest.writeString(this.gmtCreate);
        dest.writeString(this.gmtModify);
        dest.writeString(this.logo);
    }

    public CreditCardBank() {
    }

    protected CreditCardBank(Parcel in) {
        this.id = in.readLong();
        this.priority = in.readInt();
        this.bankName = in.readString();
        this.manual = in.readInt();
        this.gmtCreate = in.readString();
        this.gmtModify = in.readString();
        this.logo = in.readString();
    }

    public static final Creator<CreditCardBank> CREATOR = new Creator<CreditCardBank>() {
        @Override
        public CreditCardBank createFromParcel(Parcel source) {
            return new CreditCardBank(source);
        }

        @Override
        public CreditCardBank[] newArray(int size) {
            return new CreditCardBank[size];
        }
    };
}
