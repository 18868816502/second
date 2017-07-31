package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface TabLoanContract {

    interface Presenter extends BasePresenter {
        void filterAmount(double amount);

        void filterDueTime(int selected);

        void filterPro(int selected);

        void loadMore();

        String getFilterAmount();

        String[] getFilterDueTime();

        int getFilterDueTimeSelected();

        String[] getFilterPro();

    }

    interface View extends BaseView<Presenter> {
        void showFilters(String amount, String dueTime, String pro);

        void showLoanProduct(List<LoanProduct.Row> list);

        void showNoLoanProduct();

        void showNoMoreLoanProduct();
    }
}
