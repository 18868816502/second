package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AdBanner;

import java.util.List;

public interface TabHomeContract {

    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
        void showBanner(List<AdBanner> list);

        void showBorrowingScroll(List<String> list);

        void showAdDialog(AdBanner ad);
    }
}
