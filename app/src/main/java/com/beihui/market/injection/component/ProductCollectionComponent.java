package com.beihui.market.injection.component;

import com.beihui.market.injection.module.ProductCollectionModule;
import com.beihui.market.ui.fragment.PageCollectionProductFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ProductCollectionModule.class)
public interface ProductCollectionComponent {

    void inject(PageCollectionProductFragment fragment);
}
