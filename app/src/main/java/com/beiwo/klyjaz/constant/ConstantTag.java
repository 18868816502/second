package com.beiwo.klyjaz.constant;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.constant
 * @class describe
 * @time 2018/9/18 15:44
 */
public interface ConstantTag {

    /*
    * ******************************个人中心( 整体结构：tag + 功能 +  模块 )********************************
    * */

    /**
     * 用户头像
     */
    int TAG_PERSONAL_AVATAR = 0;
    /**
     * 编辑资料
     */
    int TAG_PERSONAL_INFO_EDIT = 1;
    /**
     * 发布
     */
    int TAG_PERSONAL_PUBLISH = 2;
    /**
     * 关注
     */
    int TAG_PERSONAL_ATTENTION = 3;
    /**
     * 粉丝
     */
    int TAG_PERSONAL_FANS = 4;
    /**
     * 获赞
     */
    int TAG_PERSONAL_PARISE = 5;
    /**
     * 更多
     */
    int TAG_PERSONAL_MORE = 6;
    /**
     * 文章作者头像
     */
    int TAG_PERSONAL_ARTICLE_AVATAR = 7;
    /**
     * 文章点赞
     */
    int TAG_ARTICLE_PARISE = 8;
    /**
     * 文章评论
     */
    int TAG_ARTICLE_COMMENT = 9;

    /*
     * *****************************|文章( 整体结构：tag + 功能 +  模块 )|********************************
     * */

    /**
     * 作者关注
     */
    int TAG_ATTENTION = 0;
    /**
     * 文章点赞
     */
    int TAG_PARISE_ARTICLE = 1;
    /**
     * 文章评论
     */
    int TAG_COMMENT_ARTICLE = 2;
    /**
     * 评论点赞
     */
    int TAG_PRAISE_COMMENT = 3;
    /**
     * 评论回复
     */
    int TAG_REPLY_COMMENT = 4;
    /**
     * 子评论点赞
     */
    int TAG_CHILD_PARISE_COMMENT = 5;
    /**
     * 子评论回复
     */
    int TAG_CHILD_REPLY_COMMENT = 6;

    /**
     * 查看更多评论
     */
    int TAG_COMMENT_MORE = 7;

    /**
     * 删除评论
     */
    int TAG_COMMENT_DELETE = 8;
    /**
     * 子评论删除
     */
    int TAG_CHILD_COMMENT_DELETE = 9;
}
