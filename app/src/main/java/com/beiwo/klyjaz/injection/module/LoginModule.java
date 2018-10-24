package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.LoginContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    private LoginContract.View mView;

    public LoginModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    public LoginContract.View provideLoginContractView() {
        return mView;
    }
}