package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtModule {

    private DebtContract.View view;

    public DebtModule(DebtContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtContract.View providesDebtContractView() {
        return view;
    }
}
