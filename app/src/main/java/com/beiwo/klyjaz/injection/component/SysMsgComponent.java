package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.SysMsgModule;
import com.beiwo.klyjaz.ui.activity.SysMsgActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SysMsgModule.class)
public interface SysMsgComponent {

    void inject(SysMsgActivity activity);
}
