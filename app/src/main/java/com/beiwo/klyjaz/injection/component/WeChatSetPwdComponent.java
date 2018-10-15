package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.WeChatSetPwdModule;
import com.beiwo.klyjaz.ui.activity.WeChatSetPwdActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = WeChatSetPwdModule.class)
public interface WeChatSetPwdComponent {

    void inject(WeChatSetPwdActivity activity);

}
