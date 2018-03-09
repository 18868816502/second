package com.beihui.market.injection.component;


import com.beihui.market.injection.module.LoanProductModule;
import com.beihui.market.ui.activity.LoanProductActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = LoanProductModule.class)
public interface LoanProductComponent {

    void inject(LoanProductActivity activity);

}
