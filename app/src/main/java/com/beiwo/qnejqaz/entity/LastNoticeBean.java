package com.beiwo.qnejqaz.entity;

import java.io.Serializable;


public class LastNoticeBean implements Serializable {

    /**
     * id : 10fa4373a9674171b04279a825ee201e
     * title : 公告：部分产品骗取资料提醒
     * explain : 近期收到用户反馈,有部分借款产品存在骗取资料行为...
     */

    private String id;
    private String title;
    private String explain;

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

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}