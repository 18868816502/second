package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.SysMsgDetail;

public interface SysMsgDetailContract {

    interface Presenter extends BasePresenter {
        void queryMsgDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showSysMsgDetail(SysMsgDetail detail);
    }
}
