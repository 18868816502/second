package com.beihui.market.component;


import com.beihui.market.module.TabHomeModule;
import com.beihui.market.ui.fragment.TabHomeFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TabHomeModule.class)
public interface TabHomeComponent {

    void inject(TabHomeFragment tabHome);
}
