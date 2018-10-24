package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.RecommendProductContract;

import dagger.Module;
import dagger.Provides;

@Module
public class RecommendProductModule {
    private RecommendProductContract.View view;

    public RecommendProductModule(RecommendProductContract.View view) {
        this.view = view;
    }

    @Provides
    public RecommendProductContract.View provideRecommendProductContractView() {
        return view;
    }
}