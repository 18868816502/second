package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.DebtChannelContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author xhb
 * 网贷记账
 */
@Module
public class DebtChannelModule {

    private DebtChannelContract.View view;

    public DebtChannelModule(DebtChannelContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtChannelContract.View provideView() {
        return view;
    }
}
