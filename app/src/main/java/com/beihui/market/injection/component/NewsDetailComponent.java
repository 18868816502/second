package com.beihui.market.injection.component;


import com.beihui.market.injection.module.NewsDetailModule;
import com.beihui.market.ui.activity.NewsDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = NewsDetailModule.class)
public interface NewsDetailComponent {

    void inject(NewsDetailActivity activity);
}
