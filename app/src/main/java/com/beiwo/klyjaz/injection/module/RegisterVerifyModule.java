package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.RegisterVerifyContract;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterVerifyModule {

    private RegisterVerifyContract.View mView;

    public RegisterVerifyModule(RegisterVerifyContract.View view) {
        mView = view;
    }

    @Provides
    public RegisterVerifyContract.View provideRegisterVerifyContractView() {
        return mView;
    }
}
