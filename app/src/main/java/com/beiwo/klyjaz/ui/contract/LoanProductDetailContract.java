package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.LoanProductDetail;
import com.beiwo.klyjaz.entity.ThirdAuthorization;

public interface LoanProductDetailContract {

    interface Presenter extends BasePresenter {
        /**
         * 查询产品详情
         *
         * @param id 产品id
         */
        void queryDetail(String id);

        /**
         * 点击收藏或取消收藏
         */
        void clickCollection();

        /**
         * 点击我要借款
         */
        void clickLoanRequested();

        /**
         * 查询第三方产品授权信息
         */
        void queryThirdAuthorization();

        /**
         * 点击确认授权
         */
        void clickConfirmAuthorize();

    }

    interface View extends BaseView<Presenter> {
        /**
         * 产品详情加载完成
         *
         * @param detail 产品详情
         */
        void showLoanDetail(LoanProductDetail detail);

        /**
         * 更新借款按钮文案
         *
         * @param text 更新的文案
         */
        void showLoanRequestText(String text);

        /**
         * 产品已下架
         */
        void showLoanOffSell();

        /**
         * 添加收藏成功
         *
         * @param msg 提示语
         */
        void showAddCollectionSuccess(String msg);

        /**
         * 删除收藏成功
         *
         * @param msg 提示语
         */
        void showDeleteCollectionSuccess(String msg);

        /**
         * 导航至第三方借款界面
         *
         * @param title 产品名字
         * @param url   借款页面url
         */
        void navigateThirdPartLoanPage(String title, String url);

        /**
         * 跳转至联合注册授权界面.如果该产品是联合注册产品且用户尚未在本平台注册，则让用户注册该平台
         *
         * @param id 产品id
         */
        void navigateAuthorizationPage(String id);

        /**
         * 用户已在其他平台注册，导航至相关推荐页面
         *
         * @param amount 相关金额
         */
        void navigateRecommendProduct(int amount);

        /**
         * 第三方产品用户协议等资料加载完成
         *
         * @param auth 用户协议等资料
         */
        void showThirdAuthorization(ThirdAuthorization auth);

        /**
         * 返回授权结果
         *
         * @param success 是否成功
         * @param msg     提示信息
         */
        void showAuthorizeResult(boolean success, String msg);

        /**
         * 授权注册等待dialog
         */
        void updateRegisterDialogVisibility(boolean visible);


    }
}
