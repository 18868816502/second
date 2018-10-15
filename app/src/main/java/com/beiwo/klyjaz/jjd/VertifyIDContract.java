package com.beiwo.klyjaz.jjd;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;

/**
 * @author chenguoguo
 */
public interface VertifyIDContract {
    interface Presenter extends BasePresenter {
        /**
         * 实名认证请求
         *
         * @param idName
         * @param idNo
         */
        void fetchVertifyIDCard(String idName, String idNo);

        /**
         * 请求保存紧急联系人
         *
         * @param userContact
         * @param userRelate
         * @param mobileNum
         */
        void fetchSaveContact(String userContact, String userRelate, String mobileNum);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 认证成功回调
         */
        void onVertifyIDCardSucceed();

        /**
         * 保存联系人成功
         */
        void onSaveContactSucceed();
    }
}