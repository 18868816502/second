package com.beihui.market.social.component;

import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.social.module.SocialPublishModule;
import com.beihui.market.ui.activity.CommunityPublishActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SocialPublishModule.class)
public interface SocialPublishComponent {

    void inject(CommunityPublishActivity activity);
}
