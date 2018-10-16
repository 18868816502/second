package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.SysMsg;
import com.beiwo.klyjaz.social.bean.SocialMessageBean;

import java.util.List;

public interface SysMsgContract {

    interface Presenter extends BasePresenter {
        void loadMore();
    }

    interface View extends BaseView<Presenter> {
        void showSysMsg(List<SysMsg.Row> sysMsg);

        void showNoSysMsg();

        void showNoMoreSysMsg();

        void onCountViewSucceed(SocialMessageBean msgBean);
    }
}
