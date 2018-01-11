package com.beihui.market.injection.module;

import com.beihui.market.ui.contract.DebtAddContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtAddModule {

    private DebtAddContract.View view;

    public DebtAddModule(DebtAddContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtAddContract.View providesDebtAddContractView() {
        return view;
    }
}
