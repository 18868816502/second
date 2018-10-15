package com.beiwo.klyjaz.base;


public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);

    void showErrorMsg(String msg);

    void showProgress();

    void dismissProgress();
}