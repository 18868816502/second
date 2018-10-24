package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.RecommendProductModule;
import com.beiwo.klyjaz.ui.activity.RecommendProductActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RecommendProductModule.class)
public interface RecommendComponent {
    void inject(RecommendProductActivity activity);
}