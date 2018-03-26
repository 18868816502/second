package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.CreditCardDebtDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreditCardDebtDetailModule {

    private CreditCardDebtDetailContract.View view;

    public CreditCardDebtDetailModule(CreditCardDebtDetailContract.View view) {
        this.view = view;
    }

    @Provides
    public CreditCardDebtDetailContract.View provideView() {
        return view;
    }
}
