package com.beiwo.qnejqaz.entity;


import java.io.Serializable;

public class TabImage implements Serializable{

    //菜单Id
    private String id;
    //适用平台1Android，2ios，同时适用时1,2
    private String applyPlatform;
    //底部栏名称
    private String name;
    //选中底部栏图标
    private String selectedImageId;
    //未选中底部栏图标
    private String unselectedImageId;
    //选中底部栏文字颜色
    private String selectedFontColor;
    //未选中底部栏文字颜色
    private String unselectedFontColor;
    //版本号
    private String version;
    //位置：1第1位，2第2位，3第3位，4第4位，5第5位，6底部栏横条
    private int position;
    //状态 0-无效, 1-保存, 2-发布
    private int status;
    //创建时间
    private String gmtCreate;
    //修改时间
    private String gmtModify;
    //选中图片
    private String selectedImage;
    //未选中图片
    private String unselectedImage;
    //马甲包Id
    private String packageId;
    //焦点 1-是 0-否
    private String focus;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyPlatform() {
        return applyPlatform;
    }

    public void setApplyPlatform(String applyPlatform) {
        this.applyPlatform = applyPlatform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedImageId() {
        return selectedImageId;
    }

    public void setSelectedImageId(String selectedImageId) {
        this.selectedImageId = selectedImageId;
    }

    public String getUnselectedImageId() {
        return unselectedImageId;
    }

    public void setUnselectedImageId(String unselectedImageId) {
        this.unselectedImageId = unselectedImageId;
    }

    public String getSelectedFontColor() {
        return selectedFontColor;
    }

    public void setSelectedFontColor(String selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }

    public String getUnselectedFontColor() {
        return unselectedFontColor;
    }

    public void setUnselectedFontColor(String unselectedFontColor) {
        this.unselectedFontColor = unselectedFontColor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getUnselectedImage() {
        return unselectedImage;
    }

    public void setUnselectedImage(String unselectedImage) {
        this.unselectedImage = unselectedImage;
    }
}
