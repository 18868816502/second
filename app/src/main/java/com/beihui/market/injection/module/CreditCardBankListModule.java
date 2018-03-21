package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.CreditCardBankContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreditCardBankListModule {

    private CreditCardBankContract.View view;

    public CreditCardBankListModule(CreditCardBankContract.View view) {
        this.view = view;
    }

    @Provides
    public CreditCardBankContract.View provideView() {
        return view;
    }
}
