package com.beihui.market.entity;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @descripe
 * @time 2018/9/19 10:13
 */
public class SocialTopicBean {

    private String name;
    private String time;
    private String title;
    private String content;
    private String pariseNum;
    private String commentNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPariseNum() {
        return pariseNum;
    }

    public void setPariseNum(String pariseNum) {
        this.pariseNum = pariseNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }
}
