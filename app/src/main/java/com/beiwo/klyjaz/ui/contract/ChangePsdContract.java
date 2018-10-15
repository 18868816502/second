package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;

public interface ChangePsdContract {

    interface Presenter extends BasePresenter {
        void updatePsd(String origin, String newPsd, String confirm);
    }

    interface View extends BaseView<Presenter> {
        void showUpdateSuccess(String msg, String account);
    }
}
