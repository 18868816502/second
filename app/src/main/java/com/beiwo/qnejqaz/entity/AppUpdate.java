package com.beiwo.qnejqaz.entity;


public class AppUpdate {
    private String id;
    private String userId;
    private int clientType;
    private String version;
    private int hasForcedUpgrade;
    private int status;
    private int appType;
    private String versionUrl;
    private String content;
    private String gmtCreate;
    private String gmtModify;

    /**
     * @version 3.1.0
     * 审核 1-资讯页，2-借贷页
     */
    public int audit;

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

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getHasForcedUpgrade() {
        return hasForcedUpgrade;
    }

    public void setHasForcedUpgrade(int hasForcedUpgrade) {
        this.hasForcedUpgrade = hasForcedUpgrade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
