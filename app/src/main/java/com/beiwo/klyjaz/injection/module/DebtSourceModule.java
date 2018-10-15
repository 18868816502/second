package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.DebtSourceContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author xhb
 * 添加信用卡账单
 */
@Module
public class DebtSourceModule {

    private DebtSourceContract.View view;

    public DebtSourceModule(DebtSourceContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtSourceContract.View provideView() {
        return view;
    }
}
