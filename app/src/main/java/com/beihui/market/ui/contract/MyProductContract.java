package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.MyProduct;

import java.util.List;

public interface MyProductContract {

    interface Presenter extends BasePresenter {
        /**
         * 刷新我的借款产品
         */
        void refreshMyProduct();

        /**
         * 加载更多我的借款产品
         */
        void loadMoreMyProduct();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 成功注册的个数
         *
         * @param count 个数
         */
        void showSuccessCount(int count);

        /**
         * 我的产品加载完成
         *
         * @param list        我的产品列表
         * @param canLoadMore 是否还有更多数据可以加载
         */
        void showMyProduct(List<MyProduct.Row> list, boolean canLoadMore);

        /**
         * 暂无我的产品
         */
        void showMyProductEmpty();
    }
}
