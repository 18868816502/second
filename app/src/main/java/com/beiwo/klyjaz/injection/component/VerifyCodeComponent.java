package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.VerifyCodeModule;
import com.beiwo.klyjaz.ui.fragment.RequireVerifyCodeFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = VerifyCodeModule.class)
public interface VerifyCodeComponent {

    void inject(RequireVerifyCodeFragment fragment);
}
