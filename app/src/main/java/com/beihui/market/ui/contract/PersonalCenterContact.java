package com.beihui.market.ui.contract;

import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

import java.util.List;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.contract
 * @class describe
 * @anthor A
 * @time 2018/9/11 17:19
 */
public interface PersonalCenterContact {

    interface Presenter extends BasePresenter {
        /**
         * 获取银行列表
         */
        void fetchPersonalData();

        /**
         * 获取个人发布的文章
         */
        void fetchPersonalArticle();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 银行列表加载完成
         *
         * @param list 银行列表
         */
        void showPersonalData(List<String> list);

    }

}
