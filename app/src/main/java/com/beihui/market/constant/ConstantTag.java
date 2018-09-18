package com.beihui.market.constant;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.constant
 * @class describe
 * @time 2018/9/18 15:44
 */
public interface ConstantTag {


    /*******************************|文章( 整体结构：tag + 功能 +  模块 )|*********************************/
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
}
