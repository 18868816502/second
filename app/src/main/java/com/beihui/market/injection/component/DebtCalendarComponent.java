package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtCalendarModule;
import com.beihui.market.ui.fragment.DebtCalCalendarFragment;
import com.beihui.market.ui.fragment.DebtCalChartFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtCalendarModule.class)
public interface DebtCalendarComponent {

    void inject(DebtCalCalendarFragment fragment);

    void inject(DebtCalChartFragment fragment);
}
