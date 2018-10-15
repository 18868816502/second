package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.DebtDetailContract;

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
