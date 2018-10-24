package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.LoanDetailModule;
import com.beiwo.klyjaz.ui.activity.LoanDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = LoanDetailModule.class)
public interface LoanDetailComponent {
    void inject(LoanDetailActivity activity);
}