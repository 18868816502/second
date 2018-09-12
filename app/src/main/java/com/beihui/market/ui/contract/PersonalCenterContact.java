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
 * @time 2018/9/11 17:19
 */
public interface PersonalCenterContact {

    interface Presenter extends BasePresenter {
        /**
         * 获取银行列表
         * @param userId 用户id
         */
        void fetchPersonalInfo(String userId);

        /**
         * 获取个人发布的文章
         * @param userId 用户id
         * @param pageNo 页码
         * @param pageSize 每页条数
         */
        void fetchPersonalArticle(String userId,int pageNo,int pageSize);
    }

    interface View extends BaseView<Presenter> {
        /**
         * y用户个人信息加载完成
         *
         * @param userInfoBean 用户个人信息
         */
        void onQueryUserInfoSucceed(UserInfoBean userInfoBean);

        /**
         * 用户发表文章列表
         * @param list 文章列表
         */
        void onQueryUserArticleSucceed(List<UserArticleBean> list);


    }

}
