package com.beihui.market.jjd;


import dagger.Module;
import dagger.Provides;

@Module
public class VertifyIDModule {
    private VertifyIDContract.View mView;

    public VertifyIDModule(VertifyIDContract.View view) {
        mView = view;
    }

    @Provides
    public VertifyIDContract.View provideLoginContractView() {
        return mView;
    }
}