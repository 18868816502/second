package com.beiwo.klyjaz.social.bean;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/4
 */
public class TopicDetail implements Serializable {
    /**
     * topicId : 927d8dd3aff011e8901400163e13c505
     * topicFourmCount : 4
     * content : adfasdfasdf
     * title : adfasdf
     * imgUrl : adf
     */

    private String topicId;
    private int topicFourmCount;
    private String content;
    private String title;
    private String imgUrl;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getTopicFourmCount() {
        return topicFourmCount;
    }

    public void setTopicFourmCount(int topicFourmCount) {
        this.topicFourmCount = topicFourmCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}