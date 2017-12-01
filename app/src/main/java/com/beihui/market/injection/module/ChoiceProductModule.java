package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ChoiceProductContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ChoiceProductModule {

    private ChoiceProductContract.View view;

    public ChoiceProductModule(ChoiceProductContract.View view) {
        this.view = view;
    }

    @Provides
    public ChoiceProductContract.View provideChoiceProductContractView() {
        return view;
    }
}
