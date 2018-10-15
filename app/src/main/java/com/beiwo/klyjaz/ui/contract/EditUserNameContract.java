package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;

public interface EditUserNameContract {

    interface Presenter extends BasePresenter {

        void updateUserName(String username);
    }


    interface View extends BaseView<Presenter> {

        void showUserName(String name);

        void showUpdateNameSuccess(String msg, String nickName);
    }

}
