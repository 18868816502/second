package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.NewsDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsDetailModule {

    private NewsDetailContract.View view;

    public NewsDetailModule(NewsDetailContract.View view) {
        this.view = view;
    }

    @Provides
    public NewsDetailContract.View provideNewsDetailContractView() {
        return view;
    }
}
