package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProductDetail;

public interface LoanProductDetailContract {

    interface Presenter extends BasePresenter {
        void queryDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showLoanDetail(LoanProductDetail detail);
    }
}
