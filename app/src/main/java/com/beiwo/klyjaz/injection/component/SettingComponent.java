package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.SettingModule;
import com.beiwo.klyjaz.ui.activity.SettingsActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SettingModule.class)
public interface SettingComponent {

    void inject(SettingsActivity activity);
}
