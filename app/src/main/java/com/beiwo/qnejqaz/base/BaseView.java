package com.beiwo.qnejqaz.base;


public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);

    void showErrorMsg(String msg);

    void showProgress();

    void dismissProgress();
}