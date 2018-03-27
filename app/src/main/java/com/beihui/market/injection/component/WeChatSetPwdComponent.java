package com.beihui.market.injection.component;


import com.beihui.market.injection.module.WeChatSetPwdModule;
import com.beihui.market.ui.activity.WeChatSetPwdActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = WeChatSetPwdModule.class)
public interface WeChatSetPwdComponent {

    void inject(WeChatSetPwdActivity activity);

}
