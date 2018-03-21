package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtNewContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtNewModule {

    private DebtNewContract.View view;

    public DebtNewModule(DebtNewContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtNewContract.View provideView() {
        return view;
    }
}
