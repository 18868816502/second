package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.News;

import java.util.List;

public interface TabNewsContract {

    interface Presenter extends BasePresenter {
        void refresh();

        void loadMore();
    }

    interface View extends BaseView<Presenter> {
        void showRefreshHint(String refreshHint);

        void showNews(List<News.Row> news);

        void showNoNews();

        void showNetError();

        void showNoMoreNews();
    }
}
