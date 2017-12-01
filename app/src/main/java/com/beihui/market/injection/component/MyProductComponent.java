package com.beihui.market.injection.component;


import com.beihui.market.injection.module.MyProductModule;
import com.beihui.market.ui.activity.MyProductActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = MyProductModule.class)
public interface MyProductComponent {

    void inject(MyProductActivity activity);
}
