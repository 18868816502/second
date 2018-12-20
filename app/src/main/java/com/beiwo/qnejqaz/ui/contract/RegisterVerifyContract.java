package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;

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
