package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.CreditCardDebtNewModule;
import com.beiwo.klyjaz.ui.activity.CreditCardDebtNewActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = CreditCardDebtNewModule.class)
public interface CreditCardDebtNewComponent {

    void inject(CreditCardDebtNewActivity activity);
}
