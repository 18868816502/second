package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.UserProfileContract;

import dagger.Module;
import dagger.Provides;

@Module
public class UserProfileModule {

    private UserProfileContract.View mView;

    public UserProfileModule(UserProfileContract.View view) {
        mView = view;
    }

    @Provides
    public UserProfileContract.View provideUsreProfileContractView() {
        return mView;
    }
}
