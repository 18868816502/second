package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.LoanProductContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoanProductModule {

    private LoanProductContract.View view;

    public LoanProductModule(LoanProductContract.View view) {
        this.view = view;
    }

    @Provides
    LoanProductContract.View provideTabLoanContractView() {
        return view;
    }
}
