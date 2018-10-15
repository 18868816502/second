package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.ChangePsdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePsdModule {

    private ChangePsdContract.View mView;

    public ChangePsdModule(ChangePsdContract.View view) {
        mView = view;
    }

    @Provides
    public ChangePsdContract.View provideChangePsdContractView() {
        return mView;
    }
}
