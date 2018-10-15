package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.CreditCardDebtDetailModule;
import com.beiwo.klyjaz.ui.activity.CreditCardDebtDetailActivity;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = CreditCardDebtDetailModule.class)
public interface CreditCardDebtDetailComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder debtId(String debtId);

        Builder appComponent(AppComponent appComponent);

        Builder creditCardDebtDetailModule(CreditCardDebtDetailModule module);

        CreditCardDebtDetailComponent build();
    }

    void inject(CreditCardDebtDetailActivity activity);
}
