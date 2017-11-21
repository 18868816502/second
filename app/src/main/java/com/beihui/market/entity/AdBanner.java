package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class AdBanner implements Parcelable {
    /**
     * 广告类型，1 跳转url， 2跳转产品详情页
     */
    private int type;
    private String url;
    private String localId;
    private String imgUrl;
    private String id;
    private String title;
    private long beginTime;
    private long endTime;
    private int showTimes;
    private int needLogin;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getShowTimes() {
        return showTimes;
    }

    public void setShowTimes(int showTimes) {
        this.showTimes = showTimes;
    }

    public int getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(int needLogin) {
        this.needLogin = needLogin;
    }

    /**
     * 是否是跳转产品
     */
    public boolean isNative() {
        return getType() == 2;
    }

    /**
     * 是否需要登录
     */
    public boolean needLogin() {
        return needLogin == 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.url);
        dest.writeString(this.localId);
        dest.writeString(this.imgUrl);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.beginTime);
        dest.writeLong(this.endTime);
        dest.writeInt(this.showTimes);
        dest.writeInt(this.needLogin);
    }

    public AdBanner() {
    }

    protected AdBanner(Parcel in) {
        this.type = in.readInt();
        this.url = in.readString();
        this.localId = in.readString();
        this.imgUrl = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.beginTime = in.readLong();
        this.endTime = in.readLong();
        this.showTimes = in.readInt();
        this.needLogin = in.readInt();
    }

    public static final Parcelable.Creator<AdBanner> CREATOR = new Parcelable.Creator<AdBanner>() {
        @Override
        public AdBanner createFromParcel(Parcel source) {
            return new AdBanner(source);
        }

        @Override
        public AdBanner[] newArray(int size) {
            return new AdBanner[size];
        }
    };
}
