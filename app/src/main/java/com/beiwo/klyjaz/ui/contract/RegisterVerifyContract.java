package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;

public interface RegisterVerifyContract {

    interface Presenter extends BasePresenter {
        void requestVerification(String phone);

        void nextMove(String phone, String verificationCode);
    }

    interface View extends BaseView<Presenter> {
        void showVerificationSend(String msg);

        void moveToNextStep(String requestPhone);
    }
}
