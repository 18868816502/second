package com.beihui.market.injection.component;


import com.beihui.market.injection.module.RecommendProductModule;
import com.beihui.market.ui.activity.RecommendProductActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RecommendProductModule.class)
public interface RecommendComponent {

    void inject(RecommendProductActivity activity);
}
