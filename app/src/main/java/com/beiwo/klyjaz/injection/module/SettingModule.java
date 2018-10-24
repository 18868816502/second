package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.SettingContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingModule {
    private SettingContract.View mView;

    public SettingModule(SettingContract.View view) {
        mView = view;
    }

    @Provides
    public SettingContract.View provideSettingContractView() {
        return mView;
    }
}