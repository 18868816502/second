package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

import java.util.List;

public interface TabLoanContract {

    interface Presenter extends BasePresenter {
        /**
         * 加载产品提示语
         */
        void loadProductNotice();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 产品提示语加载完成
         *
         * @param notice 产品提示语
         */
        void showProductNotice(List<String> notice);
    }
}
