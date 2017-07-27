package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AnnounceAbstract;
import com.beihui.market.entity.SysMsgAbstract;

public interface MessageCenterContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showAnnounce(AnnounceAbstract announce);

        void showSysMsg(SysMsgAbstract sysMsg);

        void showNoRecommend();
    }
}
