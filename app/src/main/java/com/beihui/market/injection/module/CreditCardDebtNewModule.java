package com.beihui.market.injection.module;

import com.beihui.market.ui.contract.CreditCardDebtNewContract;

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
