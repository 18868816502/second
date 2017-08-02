package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.News;
import com.beihui.market.entity.NoticeAbstract;

import java.util.List;

public interface TabHomeContract {

    interface Presenter extends BasePresenter {
        void checkMsg();

        void checkMyWorth();
    }

    interface View extends BaseView<Presenter> {
        void showBanner(List<AdBanner> list);

        void showBorrowingScroll(List<String> list);

        void showHotNews(List<News.Row> news);

        void showHotLoanProducts(List<LoanProduct.Row> products);

        void showAdDialog(AdBanner ad);

        void showNotice(NoticeAbstract notice);

        void navigateLogin();

        void navigateWorthTest();

        void navigateMessageCenter();
    }
}
