package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.AnnounceContract;

import dagger.Module;
import dagger.Provides;

@Module
public class AnnounceModule {

    private AnnounceContract.View mView;

    public AnnounceModule(AnnounceContract.View view) {
        mView = view;
    }

    @Provides
    public AnnounceContract.View provideAnnounceContractView() {
        return mView;
    }
}
