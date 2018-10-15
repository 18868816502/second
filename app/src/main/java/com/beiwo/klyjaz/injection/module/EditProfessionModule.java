package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.EditProfessionContract;

import dagger.Module;
import dagger.Provides;

@Module
public class EditProfessionModule {

    private EditProfessionContract.View mView;

    public EditProfessionModule(EditProfessionContract.View view) {
        mView = view;
    }

    @Provides
    public EditProfessionContract.View provideEditProfessionContractView() {
        return mView;
    }
}
