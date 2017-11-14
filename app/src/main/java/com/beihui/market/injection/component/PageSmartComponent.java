package com.beihui.market.injection.component;


import com.beihui.market.injection.module.PageSmartModule;
import com.beihui.market.ui.fragment.PageSmartFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PageSmartModule.class)
public interface PageSmartComponent {

    void inject(PageSmartFragment fragment);
}
