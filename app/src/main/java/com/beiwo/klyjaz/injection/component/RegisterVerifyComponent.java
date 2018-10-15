package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.RegisterVerifyModule;
import com.beiwo.klyjaz.ui.fragment.UserRegisterVerifyCodeFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = RegisterVerifyModule.class)
public interface RegisterVerifyComponent {

    void inject(UserRegisterVerifyCodeFragment fragment);
}
