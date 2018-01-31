package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.CreditCard;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;

import java.util.List;

public interface TabHomeContract {

    interface Presenter extends BasePresenter {
        /**
         * 刷新页面
         */
        void refresh();

        /**
         * 加载Banner
         */
        void loadBanner();

        /**
         * 加载借款头条
         */
        void loadHeadline();

        /**
         * 刷新热门产品
         */
        void refreshHotProducts();

        /**
         * 加载更多精选产品
         */
        void loadMoreChoiceProducts();

        /**
         * 加载全部推荐信用卡
         */
        void loadCreditCards();

        /**
         * 加载借款攻略
         */
        void loadHotNews();

        /**
         * 点击测试身价
         */
        void clickQualityTest();

        /**
         * 点击轮播图
         *
         * @param position 点击位置
         */
        void clickBanner(int position);

        /**
         * 点击热门产品
         *
         * @param position 点击位置
         */
        void clickHotProduct(int position);

        /**
         * 点击一键借款
         */
        void clickOneKeyLoan();

        /**
         * 点击精选产品
         *
         * @param position 点击位置
         */
        void clickChoiceProduct(int position);

        /**
         * 点击信用卡
         *
         * @param position 点击位置
         */
        void clickCreditCard(int position);

    }

    interface View extends BaseView<Presenter> {
        /**
         * banner加载完成
         *
         * @param list banner
         */
        void showBanner(List<AdBanner> list);

        /**
         * 头条加载完成
         *
         * @param list 头条列表
         */
        void showHeadline(List<String> list);

        /**
         * 热门产品加载完成
         *
         * @param products 热门产品
         */
        void showHotProducts(List<LoanProduct.Row> products);

        /**
         * 更新一键借款按钮状态
         *
         * @param visible 是否显示
         */
        void updateOneKeyLoanVisibility(boolean visible);

        /**
         * 精选产品加载完成
         *
         * @param products    精选产品
         * @param canLoadMore 是否还能加载更多
         */
        void showChoiceProducts(List<LoanProduct.Row> products, boolean canLoadMore);

        /**
         * 推荐信用卡列表加载完成
         *
         * @param creditCards 信用卡列表
         */
        void showCreditCards(List<CreditCard.Row> creditCards);

        /**
         * 借款攻略加载完成
         *
         * @param news 借款攻略
         */
        void showHotNews(List<HotNews> news);

        /**
         * 显示广告弹窗
         *
         * @param ad 广告
         */
        void showAdDialog(AdBanner ad);

        /**
         * 显示系统通知
         *
         * @param notice 系统通知
         */
        void showNotice(NoticeAbstract notice);

        /**
         * 请求出错
         */
        void showError();

        /**
         * 导航至登录页
         */
        void navigateLogin();

        /**
         * 导航至登录页
         *
         * @param adBanner 后续动作
         */
        void navigateLoginWithPending(AdBanner adBanner);

        /**
         * 导航至身价测试页
         */
        void navigateWorthTest();

        /**
         * 导航至产品详情页
         *
         * @param loan   产品
         * @param loanId 产品id，如果loan为null，则使用该值
         */
        void navigateProductDetail(LoanProduct.Row loan, String loanId);

        /**
         * 导航至产品授权页
         *
         * @param ids 产品id数组
         */
        void navigateThirdAuthorization(List<String> ids);

        /**
         * 导航至精选产品页
         */
        void navigateChoiceProduct();

        /**
         * 导航至Web页面
         *
         * @param title 页面标题
         * @param url   页面url
         */
        void navigateWeb(String title, String url);

        /**
         * 导航至信用卡Web页面
         *
         * @param title 页面标题
         * @param url   页面url
         */
        void navigateCreditCard(String title, String url);
    }
}
