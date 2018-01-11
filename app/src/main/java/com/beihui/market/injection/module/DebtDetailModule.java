package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtDetailModule {

    private DebtDetailContract.View view;

    public DebtDetailModule(DebtDetailContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtDetailContract.View providesDebtDetailContractView() {
        return view;
    }
}
