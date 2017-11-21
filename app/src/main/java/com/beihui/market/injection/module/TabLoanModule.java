package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.TabLoanContract;

import dagger.Module;
import dagger.Provides;

@Module
public class TabLoanModule {

    private TabLoanContract.View view;

    public TabLoanModule(TabLoanContract.View view) {
        this.view = view;
    }

    @Provides
    TabLoanContract.View proviceTabLoanContractView() {
        return view;
    }
}
