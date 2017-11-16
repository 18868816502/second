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
         * 删除产品收藏
         *
         * @param id 产品id
         */
        void deleteCollection(String id);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 产品收藏列表加载完成
         *
         * @param list 产品列表
         */
        void showProductCollection(List<LoanProduct.Row> list);

        /**
         * 删除收藏成功
         */
        void showDeleteCollectionSuccess(String msg);
    }
}
