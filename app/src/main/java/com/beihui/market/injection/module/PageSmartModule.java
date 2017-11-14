package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.PageSmartContract;

import dagger.Module;
import dagger.Provides;

@Module
public class PageSmartModule {

    private PageSmartContract.View mView;

    public PageSmartModule(PageSmartContract.View view) {
        mView = view;
    }

    @Provides
    public PageSmartContract.View provideTabLoanContractView() {
        return mView;
    }
}
