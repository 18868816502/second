package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtDetailModule;
import com.beihui.market.ui.activity.DebtDetailActivity;

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

    void inject(DebtDetailActivity activity);
}
