package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanGroup;

import java.util.List;

public interface PagePersonalContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载个性推荐提示语
         */
        void loadProductHint();

        /**
         * 加载个性推荐分组
         */
        void loadProductGroup();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 个人推荐提示图加载完成
         *
         * @param msg 提示语
         */
        void showProductHint(List<String> msg);

        /**
         * 个性推荐分组加载完成
         *
         * @param groups 分组
         */
        void showProductGroup(List<LoanGroup> groups);

    }
}
