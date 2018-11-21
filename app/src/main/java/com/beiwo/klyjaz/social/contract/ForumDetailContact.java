package com.beiwo.klyjaz.social.contract;

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
public interface ForumDetailContact {

    interface Presenter extends BasePresenter {

        /**
         * 动态详情加载
         * @param forumId
         * @param pageNo
         * @param pageSize
         */
        void queryForumInfo(String forumId, int pageNo, int pageSize);

        /**
         * 获取文章评论列表
         * @param forumId 动态id
         * @param pageNo 页码
         * @param pageSize 每页记录数
         */
        void queryCommentList(String forumId, int pageNo, int pageSize);

        /**
         * 提交举报信息
         * @param linkId
         * @param reportType
         * @param reportContent
         */
        void fetchSaveReport(String linkId, String reportType, String reportContent);

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

    }

}
