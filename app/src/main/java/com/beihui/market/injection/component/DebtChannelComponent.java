package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtChannelModule;
import com.beihui.market.ui.activity.DebtChannelActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtChannelModule.class)
public interface DebtChannelComponent {

    void inject(DebtChannelActivity activity);
}
