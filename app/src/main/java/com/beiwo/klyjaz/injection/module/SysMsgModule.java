package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.SysMsgContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SysMsgModule {

    private SysMsgContract.View mView;

    public SysMsgModule(SysMsgContract.View view) {
        mView = view;
    }

    @Provides
    public SysMsgContract.View provideSysMsgContractView() {
        return mView;
    }
}
