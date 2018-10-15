package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.WeChatBindPhoneModule;
import com.beiwo.klyjaz.ui.activity.WeChatBindPhoneActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = WeChatBindPhoneModule.class)
public interface WeChatBindPhoneComponent {

    void inject(WeChatBindPhoneActivity activity);
}
