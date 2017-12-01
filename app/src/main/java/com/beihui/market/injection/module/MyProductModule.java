package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.MyProductContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MyProductModule {

    private MyProductContract.View view;

    public MyProductModule(MyProductContract.View view) {
        this.view = view;
    }

    @Provides
    public MyProductContract.View provideMyProductContractView() {
        return view;
    }
}
