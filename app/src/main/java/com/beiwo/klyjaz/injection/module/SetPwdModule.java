package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.ResetPwdSetPwdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SetPwdModule {

    private ResetPwdSetPwdContract.View mView;

    public SetPwdModule(ResetPwdSetPwdContract.View view) {
        mView = view;
    }

    @Provides
    public ResetPwdSetPwdContract.View provideResetPwdSetPwdContractView() {
        return mView;
    }
}
