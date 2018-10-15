package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.AllDebtContract;

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
