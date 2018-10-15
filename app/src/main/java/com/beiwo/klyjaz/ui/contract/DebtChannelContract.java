package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.DebtChannel;

import java.util.List;

/**
 * @author xhb
 * 网贷记账
 */
public interface DebtChannelContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载借款渠道
         */
        void loadDebtChannel();

        /**
         * 加载借款渠道历史
         */
        void loadDebtChannelHistory();

        /**
         * 搜索借款渠道
         *
         * @param key 关键字
         */
        void search(String key);

        /**
         * 添加借款渠道
         *
         */
        void addDebtChannel();

        /**
         * 选中渠道
         *
         * @param index   选中的位置
         * @param history 选中的是否为历史
         */
        void selectDebtChannel(int index, boolean history);

        /**
         * 选中搜索出来的渠道
         *
         * @param index 选中的位置
         */
        void selectSearchDebtChannel(int index);

        /**
         * 选中首字母搜索
         *
         * @param index    字母位置
         * @param alphabet 字母
         */
        void selectedAlphabet(int index, String alphabet);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 价款渠道加载完成
         *
         * @param list 借款渠道
         */
        void showDebtChannel(List<DebtChannel> list);

        /**
         * 借款渠道历史记录加载完成
         *
         * @param list 借款渠道历史记录
         */
        void showDebtChannelHistory(List<DebtChannel> list);

        /**
         * 搜索完成
         *
         * @param list 搜索结构渠道
         */
        void showSearchResult(List<DebtChannel> list);

        /**
         * 显示没有搜索结果
         */
        void showNoSearchResult();

        /**
         * 选中搜索到的渠道或者新建渠道
         *
         * @param channel 渠道
         */
        void showSearchChannelSelected(DebtChannel channel);

        /**
         * 列表滚动
         *
         * @param position 目标位置
         */
        void scrollToPosition(int position);
    }
}
