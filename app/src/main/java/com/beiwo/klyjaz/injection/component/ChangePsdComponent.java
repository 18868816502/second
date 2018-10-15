package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.ChangePsdModule;
import com.beiwo.klyjaz.ui.activity.ChangePsdActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ChangePsdModule.class)
public interface ChangePsdComponent {

    void inject(ChangePsdActivity activity);
}
