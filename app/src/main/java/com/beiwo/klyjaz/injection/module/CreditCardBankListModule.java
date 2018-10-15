package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.CreditCardBankContract;

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
