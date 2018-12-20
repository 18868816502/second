package com.beiwo.qnejqaz.social.contract;

import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.social.bean.CommentReplyBean;
import com.beiwo.qnejqaz.social.bean.ForumInfoBean;

import java.util.List;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.contract
 * @class describe
 * @author A
 * @time 2018/9/12 17:19
 */
public interface ForumCommentContact {

    interface Presenter extends BasePresenter {

        /**
         * 获取评论评论列表
         * @param forumId 动态id
         * @param pageNo 页码
         * @param pageSize 每页记录数
         */
        void queryCommentList(String forumId, int pageNo, int pageSize);

        /**
         * 发表评论回复
         * @param userId 用户名
         * @param commentType 1 评论 2 回复
         * @param commentContent 评论回复内容
         * @param forumId 动态id
         * @param toUserId 回复用户id
         * @param selfId 对应的评论回复id
         */
        void fetchReplyInfo(String userId, String commentType, String commentContent,
                                 String forumId, String toUserId, String selfId, String replyId, String replyContent);

        /**
         * 删除评论回复
         * @param replyId
         */
        void fetchCancelReply(String replyId);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 获取d动态评论列表成功
         * @param list
         */
        void onQueryCommentSucceed(List<CommentReplyBean> list);

        /**
         * 评论回复成功
         */
        void onReplyCommentSucceed();

        /**
         * 删除评论回复成功
         */
        void onCancelReplySucceed();

    }

}
