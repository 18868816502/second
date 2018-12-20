package com.beiwo.qnejqaz.view.multiChildHistogram;

import java.util.List;


public class MultiGroupHistogramGroupData {

    private String groupName;
    private List<MultiGroupHistogramChildData> childDataList;

    public String getGroupName() {
        return groupName;
    }

    public List<MultiGroupHistogramChildData> getChildDataList() {
        return childDataList;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setChildDataList(List<MultiGroupHistogramChildData> childDataList) {
        this.childDataList = childDataList;
    }
}