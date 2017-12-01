package com.beihui.market.injection.component;


import com.beihui.market.injection.module.ChoiceProductModule;
import com.beihui.market.ui.activity.ChoiceProductActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ChoiceProductModule.class)
public interface ChoiceProductComponent {

    void inject(ChoiceProductActivity activity);
}
