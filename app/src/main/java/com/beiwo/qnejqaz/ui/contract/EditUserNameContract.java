package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;

public interface EditUserNameContract {

    interface Presenter extends BasePresenter {

        void updateUserName(String username);
    }


    interface View extends BaseView<Presenter> {

        void showUserName(String name);

        void showUpdateNameSuccess(String msg, String nickName);
    }

}
