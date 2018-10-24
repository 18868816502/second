package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.helper.updatehelper.AppUpdateHelper;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface AppUpdateHelperComponent {
    void inject(AppUpdateHelper helper);
}