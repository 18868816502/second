package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.InvitationModule;
import com.beiwo.klyjaz.ui.activity.InvitationActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = InvitationModule.class)
public interface InvitationComponent {

    void inject(InvitationActivity activity);
}
