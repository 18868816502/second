package com.beihui.market.injection.module;

import com.beihui.market.ui.contract.RemindContract;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindModule
 * Author: jiang
 * Create on: 2018/7/18 15:24
 * Description:
 */

@Module
public class RemindModule {

    private RemindContract.View view;

    public RemindModule(RemindContract.View view) {
        this.view = view;

    }

    @Provides
    public RemindContract.View provideRemindContractView() {
        return view;
    }


}
