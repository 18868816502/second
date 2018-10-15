package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.AllDebtModule;
import com.beiwo.klyjaz.ui.fragment.AllDebtFragment;

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
