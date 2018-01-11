package com.beihui.market.injection.component;


import com.beihui.market.injection.module.AllDebtModule;
import com.beihui.market.ui.fragment.AllDebtFragment;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = AllDebtModule.class)
public interface AllDebtComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder status(int status);

        Builder appComponent(AppComponent appComponent);

        Builder allDebtModule(AllDebtModule module);

        AllDebtComponent build();
    }

    void inject(AllDebtFragment fragment);
}
