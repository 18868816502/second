package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface ChoiceProductContract {

    interface Presenter extends BasePresenter {
        /**
         * 刷新精选产品
         */
        void refreshChoiceProduct();

        /**
         * 加载更多精选产品
         */
        void loadMoreChoiceProduct();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 精选产品加载完成
         *
         * @param list        精选产品
         * @param canLoadMore 是否还有更多精选可加载
         */
        void showChoiceProduct(List<LoanProduct.Row> list, boolean canLoadMore);
    }
}
