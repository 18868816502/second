package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.SetPwdModule;
import com.beiwo.klyjaz.ui.fragment.SetPsdFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SetPwdModule.class)
public interface SetPwdComponent {
    void inject(SetPsdFragment fragment);
}