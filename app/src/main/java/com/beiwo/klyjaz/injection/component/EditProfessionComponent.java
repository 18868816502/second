package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.EditProfessionModule;
import com.beiwo.klyjaz.ui.activity.EditJobGroupActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = EditProfessionModule.class)
public interface EditProfessionComponent {

    void inject(EditJobGroupActivity activity);
}
