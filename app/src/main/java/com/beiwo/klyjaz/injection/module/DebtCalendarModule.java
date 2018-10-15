package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.DebtCalendarContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DebtCalendarModule {

    private DebtCalendarContract.View view;

    public DebtCalendarModule(DebtCalendarContract.View view) {
        this.view = view;
    }

    @Provides
    public DebtCalendarContract.View providesCalendarContractView() {
        return view;
    }
}