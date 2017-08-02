package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.ReNews;
import com.beihui.market.entity.SysMsgAbstract;

import java.util.List;

public interface MessageCenterContract {

    interface Presenter extends BasePresenter {
        void refreshNews();
    }

    interface View extends BaseView<Presenter> {
        void showAnnounce(NoticeAbstract announce);

        void showSysMsg(SysMsgAbstract sysMsg);

        void showReNews(List<ReNews.Row> news);

        void showNoRecommend();

        void showNoMoreReNews();

    }
}
