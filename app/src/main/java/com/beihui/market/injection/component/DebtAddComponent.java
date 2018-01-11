package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtAddModule;
import com.beihui.market.ui.activity.AddDebtActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtAddModule.class)
public interface DebtAddComponent {

    void inject(AddDebtActivity activity);
}
