package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.LoanProductDetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoanDetailModule {

    private LoanProductDetailContract.View mView;

    public LoanDetailModule(LoanProductDetailContract.View view) {
        mView = view;
    }

    @Provides
    public LoanProductDetailContract.View provideLoanProductDetailContractView() {
        return mView;
    }
}
