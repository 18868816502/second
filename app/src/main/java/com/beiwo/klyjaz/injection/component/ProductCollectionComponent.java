package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.ProductCollectionModule;
import com.beiwo.klyjaz.ui.fragment.PageCollectionProductFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ProductCollectionModule.class)
public interface ProductCollectionComponent {

    void inject(PageCollectionProductFragment fragment);
}
