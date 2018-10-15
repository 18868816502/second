package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.SysMsgDetailModule;
import com.beiwo.klyjaz.ui.activity.SysMsgDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SysMsgDetailModule.class)
public interface SysMsgDetailComponent {

    void inject(SysMsgDetailActivity activity);
}
