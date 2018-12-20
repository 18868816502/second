package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.SysMsg;
import com.beiwo.qnejqaz.social.bean.SocialMessageBean;

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
