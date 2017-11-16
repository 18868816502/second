package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.PagePersonalContract;

import dagger.Module;
import dagger.Provides;

@Module
public class PagePersonalModule {

    private PagePersonalContract.View view;

    public PagePersonalModule(PagePersonalContract.View view) {
        this.view = view;
    }

    @Provides
    public PagePersonalContract.View providesPagePersonalContractView() {
        return view;
    }
}
