package com.beihui.market.entity;


public class AdBanner {
    /**
     * 广告类型，1 跳转url， 2跳转产品详情页
     */
    private int type;
    private String url;
    private String localId;
    private String imgUrl;
    private String id;
    private String title;
    private int needLoading;
    private long beginTime;
    private long endTime;
    private int showTimes;

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

    public int getNeedLoading() {
        return needLoading;
    }

    public void setNeedLoading(int needLoading) {
        this.needLoading = needLoading;
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

    public boolean isNative() {
        return getType() == 2;
    }
}
