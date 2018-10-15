package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.ChoiceProductContract;

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
