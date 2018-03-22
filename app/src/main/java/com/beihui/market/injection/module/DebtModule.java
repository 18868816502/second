package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabAccountContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtModule {

    private TabAccountContract.View view;

    public DebtModule(TabAccountContract.View view) {
        this.view = view;
    }

    @Provides
    public TabAccountContract.View providesDebtContractView() {
        return view;
    }
}
