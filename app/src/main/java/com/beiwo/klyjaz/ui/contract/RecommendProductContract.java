package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.LoanProduct;

import java.util.List;

public interface RecommendProductContract {

    interface Presenter extends BasePresenter {

        void loadRecommendProduct(int amount);
    }

    interface View extends BaseView<Presenter> {

        void showRecommendProduct(List<LoanProduct.Row> list);
    }
}
