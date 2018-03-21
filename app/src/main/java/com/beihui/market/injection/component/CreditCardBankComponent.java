package com.beihui.market.injection.component;


import com.beihui.market.injection.module.CreditCardBankListModule;
import com.beihui.market.ui.activity.CreditCardBankActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = CreditCardBankListModule.class)
public interface CreditCardBankComponent {

    void inject(CreditCardBankActivity activity);
}
