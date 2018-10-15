package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.SysMsgDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SysMsgDetailModule {

    private SysMsgDetailContract.View mView;

    public SysMsgDetailModule(SysMsgDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    public SysMsgDetailContract.View provideSysMsgContractView() {
        return mView;
    }
}
