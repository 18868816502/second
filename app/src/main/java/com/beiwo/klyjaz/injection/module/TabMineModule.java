package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.TabMineContract;

import dagger.Module;
import dagger.Provides;

@Module
public class TabMineModule {
    private TabMineContract.View mView;

    public TabMineModule(TabMineContract.View view) {
        mView = view;
    }

    @Provides
    public TabMineContract.View provideTabMineContractView() {
        return mView;
    }
}