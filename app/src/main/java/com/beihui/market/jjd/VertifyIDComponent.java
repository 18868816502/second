package com.beihui.market.jjd;


import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.jjd.activity.VerticyIDActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = VertifyIDModule.class)
public interface VertifyIDComponent {
    void inject(VerticyIDActivity activity);
}