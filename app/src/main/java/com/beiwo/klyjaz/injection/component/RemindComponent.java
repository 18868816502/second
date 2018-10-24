package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.RemindModule;
import com.beiwo.klyjaz.ui.activity.RemindActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RemindModule.class)
public interface RemindComponent {
    void inject(RemindActivity activity);
}