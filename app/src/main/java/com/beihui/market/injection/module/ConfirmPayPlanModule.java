package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ConfirmPayPlanContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfirmPayPlanModule {

    private ConfirmPayPlanContract.View view;

    public ConfirmPayPlanModule(ConfirmPayPlanContract.View view) {
        this.view = view;
    }

    @Provides
    public ConfirmPayPlanContract.View providesConfirmPayPlanContractView() {
        return view;
    }
}
