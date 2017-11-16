package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface PagePersonalProductContract {

    interface Presenter extends BasePresenter {
        /**
         * 加载特定分组的产品
         */
        void loadGroupProduct();

        /**
         * 加载更多特工分组的产品
         */
        void loadMoreGroupProduct();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 个推推荐分组产品加载完成
         *
         * @param products 分组产品
         */
        void showGroupProducts(List<LoanProduct.Row> products);
    }
}
