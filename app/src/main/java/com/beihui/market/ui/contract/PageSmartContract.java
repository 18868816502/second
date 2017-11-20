package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanProduct;

import java.util.List;

public interface PageSmartContract {

    interface Presenter extends BasePresenter {
        /**
         * 修改金额查询条件
         *
         * @param selected 选择的金额位置
         */
        void clickAmount(int selected);

        /**
         * 修改借款期限查询条件
         *
         * @param selected 选择的日期位置
         */
        void clickDueTime(int selected);

        /**
         * 修改排序条件
         *
         * @param selected 选择的排序条件位置
         */
        void clickSort(int selected);

        /**
         * 点击产品
         *
         * @param index 点击的位置
         */
        void clickLoanProduct(int index);

        /**
         * 刷新产品列表
         */
        void refresh();

        /**
         * 加载更多产品列表
         */
        void loadMore();

        /**
         * @return 可选择的金额选项
         */
        String[] getMoneySelection();

        /**
         * @return 当前选择的金额选项
         */
        int getMoneySelected();

        /**
         * @return 可选择的借款期限选项
         */
        String[] getDueTimeSelection();

        /**
         * @return 当前选择的借款期限选项
         */
        int getDueTimeSelected();

        /**
         * @return 可选择的排序选项
         */
        String[] getSortGroup();

        /**
         * @return 当前选择的排序选项
         */
        int getSortGroupIndex();

    }

    interface View extends BaseView<Presenter> {
        /**
         * 显示当前搜索选项
         *
         * @param amount  当前选择的金额
         * @param dueTime 当前选择的借款期限
         * @param sort    当前选择的排序方式
         */
        void showFilters(String amount, String dueTime, String sort);

        /**
         * 产品列表加载完成
         *
         * @param list           产品列表
         * @param enableLoadMore 是否还能加载更多产品
         */
        void showLoanProduct(List<LoanProduct.Row> list, boolean enableLoadMore);

        void showNetError();

        void showNoLoanProduct();

        void showNoMoreLoanProduct();

        /**
         * 导航至登录
         */
        void navigateLogin();

        /**
         * 导航至产品详情
         */
        void navigateProductDetail(LoanProduct.Row loan);
    }
}
