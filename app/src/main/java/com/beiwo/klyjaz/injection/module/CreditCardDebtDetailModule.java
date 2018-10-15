package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.CreditCardDebtDetailContract;

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
