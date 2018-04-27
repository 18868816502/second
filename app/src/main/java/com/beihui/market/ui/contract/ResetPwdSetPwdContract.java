package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface ResetPwdSetPwdContract {

    interface Presenter extends BasePresenter {
        void nextMove(String phone, String verificationCode);

        void requestVerification(String phone);

        void resetPwd(String phone, String pwd);
    }

    interface View extends BaseView<Presenter> {
        void moveToNextStep(String requestPhone);

        void showVerificationSend(String msg);

        void showRestPwdSuccess(String msg);
    }

}
