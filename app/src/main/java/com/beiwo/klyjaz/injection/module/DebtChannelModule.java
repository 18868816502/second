package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.DebtChannelContract;

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
