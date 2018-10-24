package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.InvitationContract;

import dagger.Module;
import dagger.Provides;

@Module
public class InvitationModule {
    private InvitationContract.View mView;

    public InvitationModule(InvitationContract.View view) {
        mView = view;
    }

    @Provides
    public InvitationContract.View provideInvitationContractView() {
        return mView;
    }
}