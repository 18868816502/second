package com.beihui.market.injection.component;


import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.ui.activity.TabMineActivity;
import com.beihui.market.ui.fragment.PersonalFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TabMineModule.class)
public interface TabMineComponent {

    void inject(TabMineActivity fragment);
    void inject(PersonalFragment fragment);
}
