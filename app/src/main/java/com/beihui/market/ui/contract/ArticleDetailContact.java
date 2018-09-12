package com.beihui.market.ui.contract;

import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.UserArticleBean;
import com.beihui.market.entity.UserInfoBean;

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
         * 获取文章详情
         * @param userId 用户id
         */
        void fetchArticleDetailInfo(String userId);

        /**
         * 获取文章评论列表
         */
        void fetchArticleComment();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 文章详情
         *
         * @param userInfoBean 用户个人信息
         */
        void onQueryArticleDetailSucceed(UserInfoBean userInfoBean);

        /**
         * 文章评论列表
         * @param list 文章列表
         */
        void onQueryArticleCommentSucceed(List<UserArticleBean> list);


    }

}
