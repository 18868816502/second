package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class DebtChannel implements Parcelable {

    @Id
    private String id;
    @Property
    private String channelName;
    @Property
    private String channelInitials;
    @Property
    private String logo;
    @Property
    private String type;
    @Property
    private String customId; //自定义渠道id

    public String projectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelInitials() {
        return channelInitials;
    }

    public void setChannelInitials(String channelInitials) {
        this.channelInitials = channelInitials;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomId() {
        return this.customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public boolean isCustom() {
        return type.equals("custom");
    }


    public DebtChannel() {
    }

    @Generated(hash = 1212294916)
    public DebtChannel(String id, String channelName, String channelInitials, String logo, String type,
            String customId, String projectName) {
        this.id = id;
        this.channelName = channelName;
        this.channelInitials = channelInitials;
        this.logo = logo;
        this.type = type;
        this.customId = customId;
        this.projectName = projectName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.channelName);
        dest.writeString(this.channelInitials);
        dest.writeString(this.logo);
        dest.writeString(this.type);
        dest.writeString(this.customId);
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    protected DebtChannel(Parcel in) {
        this.id = in.readString();
        this.channelName = in.readString();
        this.channelInitials = in.readString();
        this.logo = in.readString();
        this.type = in.readString();
        this.customId = in.readString();
    }

    public static final Creator<DebtChannel> CREATOR = new Creator<DebtChannel>() {
        @Override
        public DebtChannel createFromParcel(Parcel source) {
            return new DebtChannel(source);
        }

        @Override
        public DebtChannel[] newArray(int size) {
            return new DebtChannel[size];
        }
    };
}
