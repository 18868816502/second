package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.DebtDetailModule;
import com.beiwo.klyjaz.ui.activity.LoanDebtDetailActivity;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtDetailModule.class)
public interface DebtDetailComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder debtId(String debtId);

        Builder appComponent(AppComponent appComponent);

        Builder debtDetailModule(DebtDetailModule module);

        DebtDetailComponent build();
    }

    void inject(LoanDebtDetailActivity activity);
}
