package com.beiwo.klyjaz.ui.contract;

import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;

import java.util.List;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.contract
 * @class describe
 * @author A
 * @time 2018/9/12 17:19
 */
public interface ArticleDetailContact {

    interface Presenter extends BasePresenter {

        /**
         * 动态详情加载
         * @param userId
         * @param forumId
         * @param pageNo
         * @param pageSize
         */
        void queryForumInfo(String userId, String forumId, int pageNo, int pageSize);

        /**
         * 获取文章评论列表
         * @param forumId 动态id
         * @param pageNo 页码
         * @param pageSize 每页记录数
         */
        void queryCommentList(String forumId,int pageNo,int pageSize);

        /**
         * 发表评论回复
         * @param userId 用户名
         * @param commentType 1 评论 2 回复
         * @param commentContent 评论回复内容
         * @param forumId 动态id
         * @param toUserId 回复用户id
         * @param selfId 对应的评论回复id
         */
        void fetchReplyForumInfo(String userId,String commentType,String commentContent,
                                 String forumId,String toUserId,String selfId,String replyId,String replyContent);

        /**
         * 提交举报信息
         * @param userId
         * @param linkId
         * @param reportType
         * @param reportContent
         */
        void fetchSaveReport(String userId,String linkId,String reportType,String reportContent);

        /**
         * 删除动态
         * @param forumId
         */
        void fetchCancelForum(String forumId);

        /**
         * 删除评论回复
         * @param replyId
         */
        void fetchCancelReply(String replyId);

        /**
         * 社区动态评论回复点赞
         * @param praiseType
         * @param forumReplyId
         * @param userId
         */
        void fetchClickPraise(int praiseType,String forumReplyId,String userId);

        /**
         * 社区动态评论回复取消点赞
         * @param praiseType
         * @param forumReplyId
         * @param userId
         */
        void fetchCancelPraise(int praiseType,String forumReplyId,String userId);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 动态详情加载成功
         * @param forumBean
         */
        void onQueryForumInfoSucceed(ForumInfoBean forumBean);

        /**
         * 获取话题评论列表成功
         * @param list
         */
        void onQueryCommentSucceed(List<CommentReplyBean> list);

        /**
         * 评论回复成功
         */
        void onReplyCommentSucceed();

        /**
         * 举报成功
         */
        void onSaveReportSucceed();

        /**
         * 删除动态成功
         */
        void onCancelForumSucceed();

        /**
         * 删除评论回复成功
         */
        void onCancelReplySucceed();

        /**
         * 社区动态评论回复点赞成功
         */
        void onPraiseSucceed();

        /**
         * 社区动态评论回复取消点赞成功
         */
        void OnCancelPraiseSucceed();
    }

}
