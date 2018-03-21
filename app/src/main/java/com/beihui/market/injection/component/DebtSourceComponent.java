package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtSourceModule;
import com.beihui.market.ui.activity.DebtSourceActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtSourceModule.class)
public interface DebtSourceComponent {

    void inject(DebtSourceActivity activity);
}
