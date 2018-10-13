package com.beihui.market.jjd;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

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