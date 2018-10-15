package com.beiwo.klyjaz.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/20
 */

public class EventBean implements Serializable {

    /**
     * id : 760c6a4430fd4cd0817eefe2b565c474
     * userId : 4d35d9cc6dd911e7b1f0fa163e17e803
     * title : 1
     * explain : 1
     * image : eab72e192a7643b689102aeccd57d790
     * url : http://116.62.148.52/activity/page/activity-gzwx-180508.html
     * type : 1
     * priority : 9999
     * productType : null
     * status : 1
     * port : 1,2
     * localId :
     * needLogin : 0
     * uv : 11
     * pv : 61
     * gmtCreate : 1525346037000
     * gmtModify : 1532053414000
     * packageInfoId : null
     * checking : 1
     * location : 3
     * imgUrl : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Banner/1a7c37ce8d7148f4a81f58336c549459.jpg
     * userLable : null
     * unionType : null
     */

    private String id;
    private String userId;
    private String title;
    private String explain;
    private String image;
    private String imgUrl;//仅用
    private String url;//仅用
    private int type;
    private int priority;
    private Object productType;
    private int status;
    private String port;
    private String localId;
    private int needLogin;
    private int uv;
    private int pv;
    private long gmtCreate;
    private long gmtModify;
    private Object packageInfoId;
    private int checking;
    private String location;
    private Object userLable;
    private Object unionType;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public int getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(int needLogin) {
        this.needLogin = needLogin;
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

    public int getChecking() {
        return checking;
    }

    public void setChecking(int checking) {
        this.checking = checking;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Object getUserLable() {
        return userLable;
    }

    public void setUserLable(Object userLable) {
        this.userLable = userLable;
    }

    public Object getUnionType() {
        return unionType;
    }

    public void setUnionType(Object unionType) {
        this.unionType = unionType;
    }
}
