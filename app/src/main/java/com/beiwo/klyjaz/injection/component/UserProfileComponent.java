package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.UserProfileModule;
import com.beiwo.klyjaz.ui.activity.UserProfileActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = UserProfileModule.class)
public interface UserProfileComponent {
    void inject(UserProfileActivity activity);
}