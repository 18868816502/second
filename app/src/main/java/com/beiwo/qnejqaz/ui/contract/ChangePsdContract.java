package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;

public interface ChangePsdContract {

    interface Presenter extends BasePresenter {
        void updatePsd(String origin, String newPsd, String confirm);
    }

    interface View extends BaseView<Presenter> {
        void showUpdateSuccess(String msg, String account);
    }
}
