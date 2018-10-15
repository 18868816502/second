package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.DebtCalendarModule;
import com.beiwo.klyjaz.ui.fragment.DebtCalCalendarFragment;
import com.beiwo.klyjaz.ui.fragment.DebtCalChartFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtCalendarModule.class)
public interface DebtCalendarComponent {

    void inject(DebtCalCalendarFragment fragment);

    void inject(DebtCalChartFragment fragment);
}
