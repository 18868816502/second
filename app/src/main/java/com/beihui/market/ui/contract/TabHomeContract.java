package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;

import java.util.List;

public interface TabHomeContract {

    interface Presenter extends BasePresenter {
        void refresh();

        void checkMyWorth();

        void refreshHotProduct();
    }

    interface View extends BaseView<Presenter> {

        void showBanner(List<AdBanner> list);

        void showBorrowingScroll(List<String> list);

        void showHotProducts(List<LoanProduct.Row> products);

        void showChoiceProducts(List<LoanProduct.Row> products);

        void showHotNews(List<HotNews> news);

        void showAdDialog(AdBanner ad);

        void showNotice(NoticeAbstract notice);

        void showError();

        void navigateLogin();

        void navigateWorthTest();

    }
}
