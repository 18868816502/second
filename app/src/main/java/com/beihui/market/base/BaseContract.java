package com.beihui.market.base;

/**
 * Contract(翻译：合同)
 * Created by Administrator on 2016/11/8.
 */

public interface BaseContract {

    interface BasePresenter<T> {
        void attachView(T view);

        void detachView();
    }

    interface BaseView {
        void showError(String err);

        void complete();
    }

}
