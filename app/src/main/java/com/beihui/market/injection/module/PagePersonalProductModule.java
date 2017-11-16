package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.PagePersonalProductContract;

import dagger.Module;
import dagger.Provides;

@Module
public class PagePersonalProductModule {

    private PagePersonalProductContract.View view;

    public PagePersonalProductModule(PagePersonalProductContract.View view) {
        this.view = view;
    }

    @Provides
    PagePersonalProductContract.View providePagePersonalProductContractView() {
        return view;
    }
}
