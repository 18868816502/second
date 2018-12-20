package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.SysMsgDetail;
import com.beiwo.qnejqaz.social.bean.DraftEditForumBean;
import com.beiwo.qnejqaz.social.bean.ForumInfoBean;

public interface SysMsgDetailContract {

    interface Presenter extends BasePresenter {
        void queryMsgDetail(String id);

        /**
         * 获取草稿编辑信息
         * @param forumId
         */
        void fetchEditForum(String forumId);

        /**
         * 动态详情加载
         * @param userId
         * @param forumId
         * @param pageNo
         * @param pageSize
         */
        void queryForumInfo(String userId, String forumId, int pageNo, int pageSize);
    }

    interface View extends BaseView<Presenter> {
        void showSysMsgDetail(SysMsgDetail detail);

        /**
         * 获取草稿箱信息成功
         */
        void onEditForumSucceed(DraftEditForumBean forumBean);

        /**
         * 获取草稿箱信息失败
         */
        void onEditForumFailure();

        /**
         * 动态详情加载成功
         * @param forumBean
         */
        void onQueryForumInfoSucceed(ForumInfoBean forumBean);
    }
}
