package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtModule;
import com.beihui.market.ui.fragment.TabAccountFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtModule.class)
public interface DebtComponent {

    void inject(TabAccountFragment fragment);
}
