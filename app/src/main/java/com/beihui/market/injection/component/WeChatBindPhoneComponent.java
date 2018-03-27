package com.beihui.market.injection.component;


import com.beihui.market.injection.module.WeChatBindPhoneModule;
import com.beihui.market.ui.activity.WeChatBindPhoneActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = WeChatBindPhoneModule.class)
public interface WeChatBindPhoneComponent {

    void inject(WeChatBindPhoneActivity activity);
}
