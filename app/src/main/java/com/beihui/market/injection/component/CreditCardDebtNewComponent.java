package com.beihui.market.injection.component;


import com.beihui.market.injection.module.CreditCardDebtNewModule;
import com.beihui.market.ui.activity.CreditCardDebtNewActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = CreditCardDebtNewModule.class)
public interface CreditCardDebtNewComponent {

    void inject(CreditCardDebtNewActivity activity);
}
