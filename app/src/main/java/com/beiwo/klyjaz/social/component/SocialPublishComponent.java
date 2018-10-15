package com.beiwo.klyjaz.social.component;

import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.module.SocialPublishModule;
import com.beiwo.klyjaz.ui.activity.CommunityPublishActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SocialPublishModule.class)
public interface SocialPublishComponent {

    void inject(CommunityPublishActivity activity);
}
