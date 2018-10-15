package com.beiwo.klyjaz.injection.module;

import com.beiwo.klyjaz.ui.contract.CreditCardDebtNewContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreditCardDebtNewModule {

    private CreditCardDebtNewContract.View view;

    public CreditCardDebtNewModule(CreditCardDebtNewContract.View view) {
        this.view = view;
    }

    @Provides
    public CreditCardDebtNewContract.View provideView() {
        return view;
    }
}
