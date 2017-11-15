package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface NewsDetailContract {

    interface Presenter extends BasePresenter {
        void addCollection(String id);

        void deleteCollection(String id);
    }

    interface View extends BaseView<Presenter> {
        void showCollectSuccess(String msg);
    }
}
