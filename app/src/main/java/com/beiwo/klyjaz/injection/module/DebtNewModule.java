package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.DebtNewContract;

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
