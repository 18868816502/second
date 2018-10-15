package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.EditUserNameContract;

import dagger.Module;
import dagger.Provides;

@Module
public class EditUserNameModule {

    private EditUserNameContract.View mView;

    public EditUserNameModule(EditUserNameContract.View view) {
        mView = view;
    }

    @Provides
    public EditUserNameContract.View provideEditUserNameContractView() {
        return mView;
    }
}
