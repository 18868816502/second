package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.LoanProduct;

import java.util.List;

public interface RecommendProductContract {

    interface Presenter extends BasePresenter {

        void loadRecommendProduct(int amount);
    }

    interface View extends BaseView<Presenter> {

        void showRecommendProduct(List<LoanProduct.Row> list);
    }
}
