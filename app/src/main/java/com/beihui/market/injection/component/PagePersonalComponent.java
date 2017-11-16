package com.beihui.market.injection.component;


import com.beihui.market.injection.module.PagePersonalModule;
import com.beihui.market.ui.fragment.PagePersonalFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PagePersonalModule.class)
public interface PagePersonalComponent {

    void inject(PagePersonalFragment fragment);
}
