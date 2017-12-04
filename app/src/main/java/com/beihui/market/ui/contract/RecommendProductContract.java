package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface RecommendProductContract {

    interface Presenter extends BasePresenter {

        void loadRecommendProduct(int amount);
    }

    interface View extends BaseView<Presenter> {

        void showRecommendProduct(List<LoanProduct.Row> list);
    }
}
