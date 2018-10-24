package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.TabMineModule;
import com.beiwo.klyjaz.scdk.fragment.MineFragment;
import com.beiwo.klyjaz.ui.fragment.PersonalFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TabMineModule.class)
public interface TabMineComponent {
    void inject(PersonalFragment fragment);

    void inject(MineFragment fragment);
}