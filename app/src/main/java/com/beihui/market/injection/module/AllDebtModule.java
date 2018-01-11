package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.AllDebtContract;

import dagger.Module;
import dagger.Provides;

@Module
public class AllDebtModule {

    AllDebtContract.View view;

    public AllDebtModule(AllDebtContract.View view) {
        this.view = view;
    }

    @Provides
    public AllDebtContract.View providesAllDebtContractView() {
        return view;
    }
}
