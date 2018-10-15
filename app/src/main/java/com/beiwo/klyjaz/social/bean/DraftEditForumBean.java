package com.beiwo.klyjaz.social.bean;

import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.bean
 * @descripe
 * @time 2018/10/13 14:59
 */
public class DraftEditForumBean {


    /**
     * title : 测试
     * content : 测试数据。不要关心
     * imgKey : [{"id":"546835906d08466c8d145c4f87b69634","imgUrl":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png","imgType":"2","forumTopicId":"33e39652a85f4a708c7bfd19c2fe3488","gmtCreate":1536199096000,"gmtModify":1536663193000},{"id":"742122a8417b4f1b85e59a1707031352","imgUrl":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/573ee976fa7148b2ab6ba16edfd48c94.png","imgType":"2","forumTopicId":"33e39652a85f4a708c7bfd19c2fe3488","gmtCreate":1536199096000,"gmtModify":1536663205000}]
     */

    private String title;
    private String content;
    private List<ImgKeyBean> imgKey;

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

    public List<ImgKeyBean> getImgKey() {
        return imgKey;
    }

    public void setImgKey(List<ImgKeyBean> imgKey) {
        this.imgKey = imgKey;
    }

    public static class ImgKeyBean {
        /**
         * id : 546835906d08466c8d145c4f87b69634
         * imgUrl : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png
         * imgType : 2
         * forumTopicId : 33e39652a85f4a708c7bfd19c2fe3488
         * gmtCreate : 1536199096000
         * gmtModify : 1536663193000
         */

        private String id;
        private String imgUrl;
        private String imgType;
        private String forumTopicId;
        private long gmtCreate;
        private long gmtModify;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgType() {
            return imgType;
        }

        public void setImgType(String imgType) {
            this.imgType = imgType;
        }

        public String getForumTopicId() {
            return forumTopicId;
        }

        public void setForumTopicId(String forumTopicId) {
            this.forumTopicId = forumTopicId;
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
    }
}
