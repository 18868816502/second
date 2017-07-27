package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.Announce;

import java.util.List;

public interface AnnounceContract {

    interface Presenter extends BasePresenter {
        void loadMore();
    }

    interface View extends BaseView<Presenter> {
        void showAnnounce(List<Announce.Row> announceList);

        void showNoAnnounce();

        void showNoMoreAnnounce();
    }
}
