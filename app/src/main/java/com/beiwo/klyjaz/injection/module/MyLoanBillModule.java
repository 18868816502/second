package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.MyLoanBillContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MyLoanBillModule {

    private MyLoanBillContract.View view;

    public MyLoanBillModule(MyLoanBillContract.View view) {
        this.view = view;
    }

    @Provides
    public MyLoanBillContract.View provideView() {
        return view;
    }
}
