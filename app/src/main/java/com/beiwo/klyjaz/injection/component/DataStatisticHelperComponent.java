package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.helper.DataStatisticsHelper;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface DataStatisticHelperComponent {
    void inject(DataStatisticsHelper helper);
}