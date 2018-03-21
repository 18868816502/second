package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtSourceContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtSourceModule {

    private DebtSourceContract.View view;

    public DebtSourceModule(DebtSourceContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtSourceContract.View provideView() {
        return view;
    }
}
