package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.NewsDetailContract;

import javax.inject.Inject;

public class NewsDetailPresenter extends BaseRxPresenter implements NewsDetailContract.Presenter {

    private Api api;
    private NewsDetailContract.View view;

    @Inject
    NewsDetailPresenter(Api api, NewsDetailContract.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void addCollection(String id) {

    }

    @Override
    public void deleteCollection(String id) {

    }
}
