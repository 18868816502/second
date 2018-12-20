package com.beiwo.qnejqaz.social.bean;


import java.io.Serializable;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @descripe
 * @time 2018/9/19 10:13
 */
public class SocialTopicBean implements Serializable {
    private ForumBean forumActive;
    private ForumBean topic;
    private String daynamicCount;
    private List<ForumBean> forum;
    private int loadProductNum;

    public int getLoadProductNum() {
        return loadProductNum;
    }

    public void setLoadProductNum(int loadProductNum) {
        this.loadProductNum = loadProductNum;
    }

    public ForumBean getForumActive() {
        return forumActive;
    }

    public void setForumActive(ForumBean forumActive) {
        this.forumActive = forumActive;
    }

    public ForumBean getTopic() {
        return topic;
    }

    public void setTopic(ForumBean topic) {
        this.topic = topic;
    }

    public String getDaynamicCount() {
        return daynamicCount;
    }

    public void setDaynamicCount(String daynamicCount) {
        this.daynamicCount = daynamicCount;
    }

    public List<ForumBean> getForum() {
        return forum;
    }

    public void setForum(List<ForumBean> forum) {
        this.forum = forum;
    }
}