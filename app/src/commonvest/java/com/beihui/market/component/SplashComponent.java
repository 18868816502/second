package com.beiwo.klyjaz.component;


import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.activity.SplashActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface SplashComponent {

    void inject(SplashActivity activity);
}
