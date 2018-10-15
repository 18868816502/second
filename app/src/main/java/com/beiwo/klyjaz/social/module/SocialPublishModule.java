package com.beiwo.klyjaz.social.module;


import com.beiwo.klyjaz.social.contract.SocialPublishContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SocialPublishModule {

    private SocialPublishContract.View mView;

    public SocialPublishModule(SocialPublishContract.View view) {
        mView = view;
    }

    @Provides
    public SocialPublishContract.View provideResetPwdSetPwdContractView() {
        return mView;
    }
}
