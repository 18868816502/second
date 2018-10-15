package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.LoginModule;
import com.beiwo.klyjaz.ui.fragment.LoginMainFragment;
import com.beiwo.klyjaz.ui.fragment.UserLoginFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = LoginModule.class)
public interface LoginComponent {

    void inject(UserLoginFragment loginFragment);


    void inject(LoginMainFragment loginFragment);

}
