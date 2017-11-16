package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProductDetail;

public interface LoanProductDetailContract {

    interface Presenter extends BasePresenter {
        /**
         * 查询产品详情
         *
         * @param id 产品id
         */
        void queryDetail(String id);

        /**
         * 添加收藏
         *
         * @param id 产品id
         */
        void addCollection(String id);

        /**
         * 删除收藏
         *
         * @param id 产品id
         */
        void deleteCollection(String id);

    }

    interface View extends BaseView<Presenter> {
        /**
         * 产品详情加载完成
         *
         * @param detail 产品详情
         */
        void showLoanDetail(LoanProductDetail detail);

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

    }
}
