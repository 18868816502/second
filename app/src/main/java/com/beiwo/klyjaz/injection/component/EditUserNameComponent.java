package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.EditUserNameModule;
import com.beiwo.klyjaz.ui.activity.EditNickNameActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = EditUserNameModule.class)
public interface EditUserNameComponent {

    void inject(EditNickNameActivity activity);
}
