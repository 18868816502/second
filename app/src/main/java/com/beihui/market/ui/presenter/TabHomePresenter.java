package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.TabHomeContract;

import javax.inject.Inject;

public class TabHomePresenter extends BaseRxPresenter implements TabHomeContract.Presenter {

    private Api mApi;
    private TabHomeContract.View mView;

    @Inject
    TabHomePresenter(Api api, TabHomeContract.View view) {
        mApi = api;
        mView = view;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void onStart() {
    }
}
