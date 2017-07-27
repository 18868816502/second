package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.NewsContract;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsModule {

    private NewsContract.View mView;

    public NewsModule(NewsContract.View view) {
        mView = view;
    }

    @Provides
    public NewsContract.View provideNewsContractView() {
        return mView;
    }
}
