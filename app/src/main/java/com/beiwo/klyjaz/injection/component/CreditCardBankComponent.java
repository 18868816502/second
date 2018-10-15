package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.CreditCardBankListModule;
import com.beiwo.klyjaz.ui.activity.CreditCardBankActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = CreditCardBankListModule.class)
public interface CreditCardBankComponent {

    void inject(CreditCardBankActivity activity);
}
