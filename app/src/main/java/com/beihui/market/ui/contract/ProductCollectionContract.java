package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface ProductCollectionContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载产品收藏列表
         */
        void loadCollection();

        /**
         * 加载更过收藏列表
         */
        void loadMoreCollection();

        /**
         * 删除产品收藏
         *
         * @param index 点击位置
         */
        void deleteCollection(int index);

        /**
         * 点击收藏
         *
         * @param index 点击位置
         */
        void clickCollection(int index);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 产品收藏列表加载完成
         *
         * @param list 产品列表
         */
        void showProductCollection(List<LoanProduct.Row> list, boolean canLoadMore);

        /**
         * 删除收藏成功
         */
        void showDeleteCollectionSuccess(String msg);

        /**
         * 没有收藏
         */
        void showNoCollection();


        /**
         * 导航至产品详情页
         *
         * @param loan 收藏的产品
         */
        void navigateLoanDetail(LoanProduct.Row loan);
    }
}
