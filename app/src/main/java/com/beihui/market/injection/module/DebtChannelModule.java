package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtChannelContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtChannelModule {

    private DebtChannelContract.View view;

    public DebtChannelModule(DebtChannelContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtChannelContract.View providesDebtChannelContractView() {
        return view;
    }
}
