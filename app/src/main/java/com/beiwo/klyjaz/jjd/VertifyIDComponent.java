package com.beiwo.klyjaz.jjd;


import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.jjd.activity.VerticyIDActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = VertifyIDModule.class)
public interface VertifyIDComponent {
    void inject(VerticyIDActivity activity);
}