package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class AdBanner implements Parcelable {
    /**
     * 广告类型，1 跳转url， 2跳转产品详情页
     */

    private String id;
    private String userId;
    private String title;
    private String explain;
    private String image;
    private String url;
    private int type;
    private int priority;
    private Object productType;
    private int status;
    private String port;
    private String localId;
    private int uv;
    private int pv;
    private long gmtCreate;
    private long gmtModify;
    private Object packageInfoId;
    private int needLogin;
    private long beginTime;
    private long endTime;
    private int showTimes;
    private int location;//1-账单首页，2-发现页
    private String imgUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getProductType() {
        return productType;
    }

    public void setProductType(Object productType) {
        this.productType = productType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Object getPackageInfoId() {
        return packageInfoId;
    }

    public void setPackageInfoId(Object packageInfoId) {
        this.packageInfoId = packageInfoId;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

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
